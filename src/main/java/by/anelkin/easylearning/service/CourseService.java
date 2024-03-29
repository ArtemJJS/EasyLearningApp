package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.*;
import by.anelkin.easylearning.specification.account.SelectAccPurchasedCourseSpecification;
import by.anelkin.easylearning.specification.chapter.SelectAllFromCourseSpecification;
import by.anelkin.easylearning.specification.chapter.SelectChapterByNameAndCourseIdSpecification;
import by.anelkin.easylearning.specification.course.*;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import by.anelkin.easylearning.validator.FormValidator;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Course.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

/**
 * Presents logic to execute operations, required mostly {@link Course} data
 * from data store. Used to operation that can change course state inside
 * of the data store or required course data.
 * Common repository - {@link CourseRepository}
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
public class CourseService {
    private static final int SEARCH_LIMIT = 4;
    private static final int AMOUNT_COURSES_RECOMMENDED = 6;
    private static final String PATH_ADD_NEW_COURSE = "author/add-new-course";
    private static final String PATH_ADD_COURSE = "author/add-course";

    /**
     * select courses that are not available to see learning page to current user
     * this courses will be shoen as recommended courses to this account
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public void chooseRecommendedCourses(SessionRequestContent requestContent) throws ServiceException {
        CourseRepository repo = new CourseRepository();
        AccountType role = (AccountType) requestContent.getSessionAttributes().get(ATTR_ROLE);
        List<Course> courses;
        try {
            if (role == AccountType.GUEST) {
                courses = repo.query(new SelectCourseRecommendedGuestSpecification(AMOUNT_COURSES_RECOMMENDED));
            } else {
                Account account = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
                courses = repo.query(new SelectCourseRecommendedSpecification(AMOUNT_COURSES_RECOMMENDED, account.getId()));
            }
            requestContent.getRequestAttributes().put(ATTR_RECOMMENDED_COURSES, courses);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


    /**
     * updates course path to picture on review
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public void addCourseImgToReview(SessionRequestContent requestContent) throws ServiceException {
        int courseId = Integer.parseInt(String.valueOf(requestContent.getRequestAttributes().get(ATTR_COURSE_ID)));
        CourseRepository repository = new CourseRepository();
        try {
            Course course = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            course.setUpdatePhotoPath(course.getId() + (String) requestContent.getRequestAttributes().get(ATTR_FILE_EXTENSION));
            repository.update(course);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * replace current course image file with file on review, updates course's path to image
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}, IOException
     */
    public void approveCourseImgChange(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        String fileStorage = ResourceBundle.getBundle(FILE_STORAGE_BUNDLE_BASE).getString(PROP_FILE_FOLDER);
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_PICTURE_APPROVED);
        CourseRepository repository = new CourseRepository();
        try {
            String courseName = requestContent.getRequestParameters().get(ATTR_COURSE_NAME)[0];
            Course course = repository.query(new SelectCourseByNameSpecification(courseName)).get(0);
            String imgToApprovePath = course.getUpdatePhotoPath();
            String currImgPath = fileStorage + course.getPathToPicture();
            String fileName = imgToApprovePath.substring(imgToApprovePath.lastIndexOf(PATH_SPLITTER) + 1);
            if (!currImgPath.contains(DEFAULT_COURSE_AVATAR)) {
                Files.deleteIfExists(Paths.get(currImgPath));
            }
            File file = new File(fileStorage + imgToApprovePath);
            file.renameTo(new File(fileStorage + PATH_RELATIVE_COURSE_IMG_FOLDER + PATH_SPLITTER + fileName));

            course.setUpdatePhotoPath(EMPTY_STRING);
            course.setPathToPicture(imgToApprovePath); //it is getting correct value inside repo
            repository.update(course);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message + course.getId());
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST
                    , repository.query(new SelectCourseUpdateImgSpecification()));
        } catch (IOException | RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * delete current course image on review file, course's image not changed
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}, IOException
     */
    public void declineCourseImgChange(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        String fileStorage = ResourceBundle.getBundle(FILE_STORAGE_BUNDLE_BASE).getString(PROP_FILE_FOLDER);
        String message = (ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale)).getString(BUNDLE_PICTURE_DECLINED);
        CourseRepository repository = new CourseRepository();
        String courseName = requestContent.getRequestParameters().get(ATTR_COURSE_NAME)[0];
        try {
            Course course = repository.query(new SelectCourseByNameSpecification(courseName)).get(0);
            Files.deleteIfExists(Paths.get(fileStorage + course.getUpdatePhotoPath()));
            course.setUpdatePhotoPath(EMPTY_STRING);
            repository.update(course);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message + course.getId());
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST
                    , repository.query(new SelectCourseUpdateImgSpecification()));
        } catch (IOException | RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * places linked with the course info into request attributes
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException} or course doesn't exists
     */
    public void initCoursePage(SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> reqAttrs = requestContent.getRequestAttributes();
        CourseRepository repository = new CourseRepository();
        try {
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID_DEFIS)[0]);
        List<Mark> marks = (new MarkService()).takeMarksOfCourse(courseId);
        List<Mark> marksWithComment = marks.stream().filter(mark -> !mark.getComment().equals(EMPTY_STRING)).collect(Collectors.toList());
            int courseMarkCount = new MarkRepository().query(new SelectMarkByTargetIdSpecification(Mark.MarkType.COURSE_MARK, courseId)).size();
            int purchaseCount = new AccRepository().query(new SelectAccPurchasedCourseSpecification(courseId)).size();
            List<Course> courses;
            courses = repository.query(new SelectCourseByIdSpecification(courseId));
            if (courses.size() != 1) {
                log.error("Course wasn't found, id: " + courseId);
                throw new ServiceException("Course wasn't found");
            }
            reqAttrs.put(ATTR_COURSE_MARK_COUNT, courseMarkCount);
            reqAttrs.put(ATTR_COURSE_PURCHASE_COUNT, purchaseCount);
            reqAttrs.put(ATTR_CURR_COURSE_MARKS, marksWithComment);
            reqAttrs.put(ATTR_REQUESTED_COURSE, courses.get(0));
            reqAttrs.put(ATTR_CURR_COURSE_CONTENT, takeChaptersAndLessons(courseId));
            reqAttrs.put(ATTR_AUTHOR_OF_COURSE, (new AccountService()).takeAuthorOfCourse(courseId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * select courses "ON REVIEW" and places it to request attributes
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public void initCourseApprovalPage(SessionRequestContent requestContent) throws ServiceException {
        CourseRepository repository = new CourseRepository();
        try {
            List<Course> courses = repository.query(new SelectByStateSpecification(CourseState.NOT_APPROVED));
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST, courses);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * select courses using {@link CourseRepository} that are corresponding to search string
     * Pagination exists. Pagination attribute will be placed to request attributes if
     * there are more courses corresponding to pattern in db, but they are over current page
     * showing courses limit
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public void searchCourses(SessionRequestContent requestContent) throws ServiceException {
        Map<String, String[]> reqParam = requestContent.getRequestParameters();
        CourseRepository repository = new CourseRepository();
        String searchKey = reqParam.get(ATTR_SEARCH_KEY)[0];
        String[] reqParamPageNumbers = reqParam.get(ATTR_PAGE);
        String searchPageNumber = reqParamPageNumbers == null ? null : reqParamPageNumbers[0];
        int currPageNumber;
        if (searchPageNumber == null || searchPageNumber.isEmpty()) {
            currPageNumber = 0;
        } else {
            int tempPageNumber = Integer.parseInt(searchPageNumber);
            currPageNumber = tempPageNumber < 0 ? 0 : tempPageNumber;
        }
        int offset = currPageNumber * SEARCH_LIMIT;
        try {
            List<Course> courses = repository.query(new SelectCourseSearchSpecification(searchKey, SEARCH_LIMIT + 1, offset));
            if (courses.size() > SEARCH_LIMIT) {
                requestContent.getRequestAttributes().put(ATTR_HAS_MORE_PAGES, "true");
                courses.remove(courses.size() - 1);
            }
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST, courses);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * select courses with images on review and places it to request attributes
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public void initCourseImgApprovalPage(SessionRequestContent requestContent) throws ServiceException {
        CourseRepository repository = new CourseRepository();
        try {
            List<Course> courses = repository.query(new SelectCourseUpdateImgSpecification());
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST, courses);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * change course state to "APPROVED"
     *@see CourseState
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}, IndexOutOfBoundsException
     */
    public void approveCourse(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_APPROVED);
        try {
            int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
            CourseRepository repository = new CourseRepository();
            Course currCourse = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            currCourse.setState(CourseState.APPROVED);
            repository.update(currCourse);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message + currCourse.getId());
            initCourseApprovalPage(requestContent);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


    /**
     * change course state to "FROZEN"
     * @see CourseState
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public void freezeCourse(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_FROZEN);
        try {
            int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
            CourseRepository repository = new CourseRepository();
            Course currCourse = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            currCourse.setState(CourseState.FREEZING);
            repository.update(currCourse);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message + currCourse.getId());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Used to create new courses and edit existing courses. Create {@link Course} and init fields,
     * then insert it into db using{@link CourseRepository}
     *
     *invokes {@link CourseService#insertChaptersIfNotExists(int, Map)}
     *and {@link CourseService#insertLessons(int, Map, String[])}
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @return true if operation proceeded, false if course exists already or data invalid
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public boolean addCourseToReview(SessionRequestContent requestContent) throws ServiceException {
        FormValidator validator = new FormValidator();
        Locale locale = takeLocaleFromSession(requestContent);
        Map<String, String[]> params = requestContent.getRequestParameters();
        boolean isPriceValid = validator.validatePrice(params.get(ATTR_COURSE_PRICE)[0]);
        boolean isCourseNameValid = validator.validateCourseName(params.get(ATTR_COURSE_NAME)[0]);
        boolean isDescriptionValid = validator.validateCourseDescription(params.get(ATTR_COURSE_DESCRIPTION)[0]);
        if (!isCourseNameValid || !isPriceValid || !isDescriptionValid) {
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_INCORRECT_DATA);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
            return false;
        }
        CourseRepository courseRepo = new CourseRepository();
        String courseName = params.get(ATTR_COURSE_NAME)[0];
        String referer = requestContent.getRequestReferer();
        boolean isCourseNew = referer.contains(PATH_ADD_NEW_COURSE) || referer.contains(PATH_ADD_COURSE);
        List<Course> courses;
        try {
            courses = courseRepo.query(new SelectCourseByNameSpecification(courseName));
            if (courses.size() > 0 && isCourseNew) {
                String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_ALREADY_EXISTS);
                requestContent.getRequestAttributes().put(ATTR_COURSE_ALREADY_EXISTS, message);
                return false;
            }
            Course course;
            course = isCourseNew ? new Course() : courses.get(0);
            Account currAccount = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
            course.setAuthorId(currAccount.getId());
            course.setName(courseName);
            course.setDescription(params.get(ATTR_COURSE_DESCRIPTION)[0]);
            course.setPrice(new BigDecimal(params.get(ATTR_COURSE_PRICE)[0]));
            course.setCreationDate(new Date(System.currentTimeMillis()));
            course.setUpdatePhotoPath(EMPTY_STRING);
            course.setState(CourseState.NOT_APPROVED);
            if (isCourseNew) {
                course.setPathToPicture(DEFAULT_IMG);
            }
            course.setDescription((new AccountService()).escapeQuotes(course.getDescription()));
            boolean isOperationProceeded = isCourseNew ? courseRepo.insert(course) : courseRepo.update(course);
            int courseId = courseRepo.query(new SelectCourseByNameSpecification(courseName))
                    .get(0).getId();

            String[] chapterNames = insertChaptersIfNotExists(courseId, params);
            insertLessons(courseId, params, chapterNames);
            new AccountService().refreshSessionAttributeAvailableCourses(requestContent, currAccount);
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_SENT_TO_REVIEW);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    /**
     * Method inserts chapters from request parameters into db
     *
     * @param courseId - id of course to place chapters in
     * @param params request parameters
     * @return array of chapter's names from request parameters
     * @throws RepositoryException from {@link ChapterRepository}
     */
    private String[] insertChaptersIfNotExists(int courseId, Map<String, String[]> params) throws RepositoryException {
        FormValidator validator = new FormValidator();
        ChapterRepository chapterRepo = new ChapterRepository();
        List<CourseChapter> existingChapters = chapterRepo.query(new SelectAllFromCourseSpecification(courseId));
        List<String> existingChaptersNames = existingChapters.stream().map(CourseChapter::getName).collect(Collectors.toList());
        String[] chapterNames = params.get(ATTR_CHAPTER_NAME);
        for (String chapterName : chapterNames) {
            boolean isChapterNameValid = validator.validateChapterName(chapterName);
            if (isChapterNameValid && !existingChaptersNames.contains(chapterName)) {
                CourseChapter chapter = new CourseChapter();
                chapter.setCourseId(courseId);
                chapter.setName(chapterName);
                chapterRepo.insert(chapter);
            }
        }
        return chapterNames;
    }

    /**
     * @param courseId - id of course to place lessons in
     * @param params request parameters
     * @param chapterNames - chapter names from current request, returned by {@link CourseService#insertChaptersIfNotExists(int, Map)}
     * @throws RepositoryException from {@link ChapterRepository}
     */
    private void insertLessons(int courseId, Map<String, String[]> params, String[] chapterNames) throws RepositoryException {
        FormValidator validator = new FormValidator();
        ChapterRepository chapterRepo = new ChapterRepository();
        LessonRepository lessonRepo = new LessonRepository();
        for (int i = 0; i < chapterNames.length; i++) {
            String[] lessonNames = params.get(PATTERN_LESSON_TITLE + (i + 1));
            String[] lessonContents = params.get(PATTERN_LESSON_CONTENT + (i + 1));
            String[] lessonDurations = params.get(PATTERN_LESSON_DURATION + (i + 1));
            if (lessonNames != null || lessonContents != null || lessonDurations != null) {
                int chapterId = chapterRepo.query(new SelectChapterByNameAndCourseIdSpecification(chapterNames[i], courseId))
                        .get(0).getId();
                for (int j = 0; j < lessonNames.length; j++) {
                    CourseLesson lesson = new CourseLesson();
                    String name = lessonNames[j];
                    String content = lessonContents[j];
                    String duration = lessonDurations[j];
                    boolean isLessonNameValid = validator.validateLessonName(name);
                    boolean isLessonDurationValid = validator.validateLessonDuration(duration);
                    //if any field is empty - skip this iteration
                    if (!name.isEmpty() && !content.isEmpty() && !duration.isEmpty()
                            && isLessonDurationValid && isLessonNameValid) {
                        lesson.setChapterId(chapterId);
                        lesson.setName(name);
                        lesson.setPathToContent(new AccountService().escapeQuotes(content));
                        lesson.setDuration(Long.parseLong(duration));
                        lesson.setCreationDate(new Date(System.currentTimeMillis()));
                        lessonRepo.insert(lesson);
                    }
                }
            }
        }
    }

    /**
     * @param courseId current course id
     * @return map with {@link CourseChapter} as keys and corresponded to particular chapter list of {@link CourseLesson}
     * @throws ServiceException if faced {@link RepositoryException}
     */
    private Map<CourseChapter, List<CourseLesson>> takeChaptersAndLessons(int courseId) throws ServiceException {
        ChapterRepository chapterRepository = new ChapterRepository();
        LessonRepository lessonRepository = new LessonRepository();
        Map<CourseChapter, List<CourseLesson>> courseContent = new LinkedHashMap<>();
        try {
            List<CourseChapter> chapters = chapterRepository.query(new SelectAllFromCourseSpecification(courseId));
            chapters.sort(Comparator.comparing(CourseChapter::getId));
            for (CourseChapter chapter : chapters) {
                List<CourseLesson> lessons = lessonRepository.query(new SelectByChapterIdSpecification(chapter.getId()));
                lessons.sort(Comparator.comparing(CourseLesson::getId));
                courseContent.put(chapter, lessons);
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return courseContent;
    }

    Locale takeLocaleFromSession(SessionRequestContent requestContent) {
        Locale locale;
        String[] localeParts = requestContent.getSessionAttributes().get(ATTR_LOCALE).toString().split(LOCALE_SPLITTER);
        locale = new Locale(localeParts[0], localeParts[1]);
        return locale;
    }

}
