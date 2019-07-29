package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.ChapterRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.LessonRepository;
import by.anelkin.easylearning.specification.chapter.SelectAllFromCourseSpecification;
import by.anelkin.easylearning.specification.chapter.SelectChapterByNameAndCourseIdSpecification;
import by.anelkin.easylearning.specification.course.*;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static by.anelkin.easylearning.entity.Course.*;

@Log4j
public class CourseService {
    private static final String PATH_RELATIVE_COURSE_IMG_FOLDER = "resources/course_img/";
    private static final String PROP_FILE_FOLDER = "file_folder";
    private static final int SEARCH_LIMIT = 4;

    private static final String ATTR_COURSES_LIST = "courses_list";
    private static final String ATTR_USER = "user";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final String ATTR_COURSE_NAME = "course_name";
    private static final String ATTR_COURSE_DESCRIPTION = "course_description";
    private static final String ATTR_COURSE_PRICE = "course_price";
    private static final String ATTR_CHAPTER_NAME = "chapter_name";
    private static final String ATTR_COURSE_ALREADY_EXISTS = "course_exists_msg";
    private static final String ATTR_FILE_EXTENSION = "file_extension";
    private static final String ATTR_SEARCH_KEY = "search_key";
    private static final String ATTR_PAGE = "page";
    private static final String ATTR_HAS_MORE_PAGES = "has_more_pages";
    private static final String ATTR_MESSAGE = "message";
    private static final String ATTR_LOCALE = "locale";
    private static final String LOCALE_SPLITTER = "_";

    private static final String RESOURCE_BUNDLE_BASE = "text_resources";
    private static final String FILE_STORAGE_BUNDLE_BASE = "file_storage";
    private static final String BUNDLE_PICTURE_APPROVED = "msg.course_picture_changed_successfully";
    private static final String BUNDLE_PICTURE_DECLINED = "msg.course_picture_change_was_declined";
    private static final String BUNDLE_COURSE_APPROVED = "msg.course_was_approved";
    private static final String BUNDLE_COURSE_FROZEN = "msg.course_was_declined";
    private static final String BUNDLE_COURSE_ALREADY_EXISTS = "msg.course_already_exists";
    private static final String BUNDLE_COURSE_SENT_TO_REVIEW = "msg.course_sent_to_review";
    private static final String DEFAULT_IMG = "default_course_avatar.png";
    private static final String PATTERN_LESSON_TITLE = "lesson_title_";
    private static final String PATTERN_LESSON_CONTENT = "lesson_content_";
    private static final String PATTERN_LESSON_DURATION = "lesson_duration_";
    private static final String EMPTY_STRING = "";
    private static final String PATH_SPLITTER = "/";


    public void addCourseImgToReview(SessionRequestContent requestContent) throws ServiceException {
        int courseId = Integer.parseInt(String.valueOf(requestContent.getRequestAttributes().get(ATTR_COURSE_ID)));
        CourseRepository repository = new CourseRepository();
        try {
            Course course = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            course.setUpdatePhotoPath(course.getId() + (String) requestContent.getRequestAttributes().get(ATTR_FILE_EXTENSION));
            repository.update(course);
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
    }

    public void approveCourseImgChange(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        String fileStorage = ResourceBundle.getBundle(FILE_STORAGE_BUNDLE_BASE).getString(PROP_FILE_FOLDER);
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_PICTURE_APPROVED);
        CourseRepository repository = new CourseRepository();
        String courseName = requestContent.getRequestParameters().get(ATTR_COURSE_NAME)[0];
        try {
            Course course = repository.query(new SelectCourseByNameSpecification(courseName)).get(0);
            String imgToApprovePath = course.getUpdatePhotoPath();
            String currImgPath = fileStorage + course.getPathToPicture();
            String fileName = imgToApprovePath.substring(imgToApprovePath.lastIndexOf(PATH_SPLITTER) + 1);
            if (!currImgPath.contains("default_course_avatar")) {
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
        } catch (RepositoryException | NullPointerException | IOException e) {
            throw new ServiceException(e);
        }
    }

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
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message + course.getId());
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST
                    , repository.query(new SelectCourseUpdateImgSpecification()));
        } catch (RepositoryException | NullPointerException | IOException e) {
            throw new ServiceException(e);
        }
    }

    public void initCoursePage(SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> reqAttrs = requestContent.getRequestAttributes();
        CourseRepository repository = new CourseRepository();
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get("course-id")[0]);
        log.debug("receiving course from base, id: " + courseId);
        List<Mark> marks = (new MarkService()).takeMarksOfCourse(courseId);
        List<Course> courses = null;
        try {
            courses = repository.query(new SelectCourseByIdSpecification(courseId));
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
        if (courses.size() != 1) {
            // fixme: 7/12/2019 service exception
            throw new ServiceException("Course wasn't found");
        }
        // fixme: 7/12/2019 вынести в текстовые константы
        reqAttrs.put("currentCourseMarks", marks);
        reqAttrs.put("requestedCourse", courses.get(0));
        reqAttrs.put("currentCourseContent", takeChaptersAndLessons(courseId));
        reqAttrs.put("author_of_course", (new AccountService()).takeAuthorOfCourse(courseId));
    }

    // TODO: 7/20/2019 может объединить этот и след методы????
    public void initCourseApprovalPage(SessionRequestContent requestContent) throws ServiceException {
        CourseRepository repository = new CourseRepository();
        try {
            List<Course> courses = repository.query(new SelectByStateSpecification(CourseState.NOT_APPROVED));
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST, courses);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    // TODO: 7/21/2019 метод замещает несколько разных? после проверки попробовать совместить
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

    public void initCourseImgApprovalPage(SessionRequestContent requestContent) throws ServiceException {
        CourseRepository repository = new CourseRepository();
        try {
            List<Course> courses = repository.query(new SelectCourseUpdateImgSpecification());
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST, courses);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void approveCourse(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_APPROVED);
        // TODO: 7/18/2019 защиту если придет не инт
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
        CourseRepository repository = new CourseRepository();
        try {
            Course currCourse = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            currCourse.setState(CourseState.APPROVED);
            repository.update(currCourse);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message + currCourse.getId());
            initCourseApprovalPage(requestContent);
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
    }


    public void freezeCourse(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_FROZEN);
        // TODO: 7/18/2019 защиту если придет не инт
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
        CourseRepository repository = new CourseRepository();
        try {
            Course currCourse = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            currCourse.setState(CourseState.FREEZING);
            repository.update(currCourse);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message + currCourse.getId());
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
    }

    public void addCourseToReview(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = takeLocaleFromSession(requestContent);
        Map<String, String[]> params = requestContent.getRequestParameters();
        CourseRepository courseRepo = new CourseRepository();
        String courseName = params.get(ATTR_COURSE_NAME)[0];
        String referer = requestContent.getRequestReferer();
        boolean isCourseNew = referer.contains("author/add-new-course") || referer.contains("author/add-course");
        List<Course> courses;
        try {
            courses = courseRepo.query(new SelectCourseByNameSpecification(courseName));
            if (courses.size() > 0 && isCourseNew) {
                // TODO: 7/18/2019 попробовать сохранить данные при наличии такого названия курса в базе? при форварде?
                String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_ALREADY_EXISTS);
                requestContent.getRequestAttributes().put(ATTR_COURSE_ALREADY_EXISTS, message);
                return;
            }
            Course course;
            course = isCourseNew ? new Course() : courses.get(0);
            Account currAccount = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
            course.setAuthorId(currAccount.getId());
            course.setName(courseName);
            course.setDescription(params.get(ATTR_COURSE_DESCRIPTION)[0]);
            course.setPrice(new BigDecimal(params.get(ATTR_COURSE_PRICE)[0]));
            course.setCreationDate(new Date(System.currentTimeMillis()));
            course.setPathToPicture(DEFAULT_IMG);
            course.setUpdatePhotoPath(EMPTY_STRING);
            course.setState(CourseState.NOT_APPROVED);
            if (isCourseNew) {
                courseRepo.insert(course);
            } else {
                courseRepo.update(course);
            }
            int courseId = courseRepo.query(new SelectCourseByNameSpecification(courseName))
                    .get(0).getId();

            String[] chapterNames = insertChaptersIfNotExists(courseId, params);
            insertLessons(courseId, params, chapterNames);
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_COURSE_SENT_TO_REVIEW);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    private String[] insertChaptersIfNotExists(int courseId, Map<String, String[]> params) throws RepositoryException {
        ChapterRepository chapterRepo = new ChapterRepository();
        String[] chapterNames = params.get(ATTR_CHAPTER_NAME);

        for (String chapterName : chapterNames) {
            if (chapterRepo.query(new SelectChapterByNameAndCourseIdSpecification(chapterName, courseId)).size() == 0) {
                CourseChapter chapter = new CourseChapter();
                chapter.setCourseId(courseId);
                chapter.setName(chapterName);
                chapterRepo.insert(chapter);
            }
        }
        return chapterNames;
    }

    private void insertLessons(int courseId, Map<String, String[]> params, String[] chapterNames) throws RepositoryException {
        ChapterRepository chapterRepo = new ChapterRepository();
        LessonRepository lessonRepo = new LessonRepository();
        for (int i = 0; i < chapterNames.length; i++) {
            String[] lessonNames = params.get(PATTERN_LESSON_TITLE + (i + 1));
            String[] lessonContents = params.get(PATTERN_LESSON_CONTENT + (i + 1));
            String[] lessonDurations = params.get(PATTERN_LESSON_DURATION + (i + 1));
            if (lessonNames == null || lessonContents == null || lessonDurations == null) {
                return;
            }
            int chapterId = chapterRepo.query(new SelectChapterByNameAndCourseIdSpecification(chapterNames[i], courseId))
                    .get(0).getId();
            for (int j = 0; j < lessonNames.length; j++) {
                CourseLesson lesson = new CourseLesson();
                String name = lessonNames[j];
                String content = lessonContents[j];
                String duration = lessonDurations[j];
                //if any field is empty - skip this iteration
                if (!name.isEmpty() && !content.isEmpty() && !duration.isEmpty()) {
                    lesson.setChapterId(chapterId);
                    lesson.setName(name);
                    lesson.setPathToContent(content);
                    lesson.setDuration(Long.parseLong(duration));
                    lesson.setCreationDate(new Date(System.currentTimeMillis()));
                    lessonRepo.insert(lesson);
                }
            }
        }
    }

    private Map<CourseChapter, List<CourseLesson>> takeChaptersAndLessons(int courseId) throws ServiceException {
        ChapterRepository chapterRepository = new ChapterRepository();
        LessonRepository lessonRepository = new LessonRepository();
        Map<CourseChapter, List<CourseLesson>> courseContent = new HashMap<>();
        try {
            List<CourseChapter> chapters = chapterRepository.query(new SelectAllFromCourseSpecification(courseId));
            for (CourseChapter chapter : chapters) {
                List<CourseLesson> lessons = lessonRepository.query(new SelectByChapterIdSpecification(chapter.getId()));
                courseContent.put(chapter, lessons);
            }
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
        return courseContent;
    }

    // FIXME: 7/29/2019 общий для нескольких репозиториев, куда вынести его? сделать общий интерфейс и туда???
    public Locale takeLocaleFromSession(SessionRequestContent requestContent) {
        Locale locale;
        String[] localeParts = requestContent.getSessionAttributes().get(ATTR_LOCALE).toString().split(LOCALE_SPLITTER);
        locale = new Locale(localeParts[0], localeParts[1]);
        return locale;
    }

}
