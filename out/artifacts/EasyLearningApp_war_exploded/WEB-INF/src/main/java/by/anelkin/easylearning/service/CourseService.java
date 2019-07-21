package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.ChapterRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.LessonRepository;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.chapter.SelectAllFromCourseSpecification;
import by.anelkin.easylearning.specification.chapter.SelectChapterByNameAndCourseIdSpecification;
import by.anelkin.easylearning.specification.course.*;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import com.mysql.cj.jdbc.exceptions.NotUpdatable;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Provider;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.anelkin.easylearning.entity.Course.*;

@Log4j
public class CourseService {
    private static final String COURSE_IMG_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web";
    private static final String COURSE_IMG_LOCATION_TEMP = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web";
    private static final String COURSE_DEFAULT_IMG_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/course_img/default_course_avatar.png";
    private static final String COURSE_IMG_FOLDER = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/course_img/";
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

    private static final String PREVIOUS_OPERATION_MSG = "previous_operation_message";
    private static final String MSG_COURSE_ALREADY_EXISTS = "Course with name specified already exists! Try another one!";
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
        CourseRepository repository = new CourseRepository();
        String courseName = requestContent.getRequestParameters().get(ATTR_COURSE_NAME)[0];
        try {
            Course course = repository.query(new SelectCourseByNameSpecification(courseName)).get(0);
            String imgToApprovePath = course.getUpdatePhotoPath();
            String currImgPath = COURSE_IMG_LOCATION + course.getPathToPicture();
            String fileName = imgToApprovePath.substring(imgToApprovePath.lastIndexOf(PATH_SPLITTER) + 1);
            if (!currImgPath.equals(COURSE_DEFAULT_IMG_LOCATION)) {
                Files.deleteIfExists(Paths.get(currImgPath));
            }
            File file = new File(COURSE_IMG_LOCATION_TEMP + imgToApprovePath);
            file.renameTo(new File(COURSE_IMG_FOLDER + "/" + fileName));
            Files.deleteIfExists(Paths.get(COURSE_IMG_LOCATION_TEMP + imgToApprovePath));

            course.setUpdatePhotoPath(EMPTY_STRING);
            course.setPathToPicture(imgToApprovePath); //it is getting correct value inside repo
            repository.update(course);
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST
                    , repository.query(new SelectCourseUpdateImgSpecification()));
        } catch (RepositoryException | NullPointerException | IOException e) {
            throw new ServiceException(e);
        }
    }

    public void declineCourseImgChange(SessionRequestContent requestContent) throws ServiceException {
        CourseRepository repository = new CourseRepository();
        String courseName = requestContent.getRequestParameters().get(ATTR_COURSE_NAME)[0];
        try {
            Course course = repository.query(new SelectCourseByNameSpecification(courseName)).get(0);
            Files.deleteIfExists(Paths.get(COURSE_IMG_LOCATION_TEMP + course.getUpdatePhotoPath()));
            course.setUpdatePhotoPath(EMPTY_STRING);
            requestContent.getRequestAttributes().put(ATTR_COURSES_LIST
                    , repository.query(new SelectCourseUpdateImgSpecification()));
        } catch (RepositoryException | NullPointerException | IOException e) {
            throw new ServiceException(e);
        }
    }

    public void initCoursePage(SessionRequestContent requestContent) throws ServiceException {
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
        requestContent.getRequestAttributes().put("currentCourseMarks", marks);
        requestContent.getRequestAttributes().put("requestedCourse", courses.get(0));
        requestContent.getRequestAttributes().put("currentCourseContent", takeChaptersAndLessons(courseId));
        requestContent.getRequestAttributes().put("author_of_course", (new AccountService()).takeAuthorOfCourse(courseId));
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
        // TODO: 7/18/2019 защиту если придет не инт
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
        CourseRepository repository = new CourseRepository();
        try {
            Course currCourse = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            currCourse.setState(CourseState.APPROVED);
            repository.update(currCourse);
//if I will change command result to forward, this will init correct courses list to approve:
//          initCourseApprovalPage(requestContent);
        } catch (RepositoryException | NullPointerException e) {
            // FIXME: 7/18/2019
            throw new ServiceException(e);
        }
    }


    public void freezeCourse(SessionRequestContent requestContent) throws ServiceException {
        // TODO: 7/18/2019 защиту если придет не инт
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
        CourseRepository repository = new CourseRepository();
        try {
            Course currCourse = repository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            currCourse.setState(CourseState.FREEZING);
            repository.update(currCourse);
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
    }

    // TODO: 7/18/2019 если fail то поудалять все что добавилось?
    // TODO: 7/18/2019 подумать может получится разнести хотя бы на два метода?
    public boolean addCourseToReview(SessionRequestContent requestContent) throws RepositoryException {
        Map<String, String[]> params = requestContent.getRequestParameters();
        CourseRepository courseRepo = new CourseRepository();
        String courseName = params.get(ATTR_COURSE_NAME)[0];
        // FIXME: 7/21/2019 при повторной отправке после форварда адрес
        //  отличается(там адрес сервлета) и бьет ошибку
        boolean isCourseNew = requestContent.getRequestReferer().endsWith("author/add-new-course");
        List<Course> courses = courseRepo.query(new SelectCourseByNameSpecification(courseName));
        if (courses.size() > 0 && isCourseNew) {
            // TODO: 7/18/2019 попробовать сохранить данные при наличии такого названия курса в базе? при форварде?
            requestContent.getRequestAttributes().put(ATTR_COURSE_ALREADY_EXISTS, MSG_COURSE_ALREADY_EXISTS);
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

        return true;
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

    // returns map of chapters and lessons of current course, then it have to be setted to request attribute
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


}
