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
import by.anelkin.easylearning.specification.course.SelectByStateSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByNameSpecification;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.anelkin.easylearning.entity.Course.*;

@Log4j
public class CourseService {
    private static final String ATTR_NEED_APPROVAL = "courses_need_approval";
    private static final String ATTR_USER = "user";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final String ATTR_COURSE_NAME = "course_name";
    private static final String ATTR_COURSE_DESCRIPTION = "course_description";
    private static final String ATTR_COURSE_PRICE = "course_price";
    private static final String ATTR_CHAPTER_NAME = "chapter_name";
    private static final String ATTR_COURSE_ALREADY_EXISTS = "course_exists_msg";
    private static final String MSG_COURSE_ALREADY_EXISTS = "Course with name specified already exists! Try another one!";
    private static final String DEFAULT_IMG = "default_course_avatar.png";
    private static final String PATTERN_LESSON_TITLE = "lesson_title_";
    private static final String PATTERN_LESSON_CONTENT = "lesson_content_";
    private static final String PATTERN_LESSON_DURATION = "lesson_duration_";


    public void initCoursePage(SessionRequestContent requestContent) throws RepositoryException {
        CourseRepository repository = new CourseRepository();
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get("course-id")[0]);
        log.debug("receiving course from base, id: " + courseId);
        List<Mark> marks = (new MarkService()).takeMarksOfCourse(courseId);
        List<Course> courses = repository.query(new SelectCourseByIdSpecification(courseId));
        if (courses.size() != 1) {
            // fixme: 7/12/2019 service exception
            throw new RepositoryException("Course wasn't found");
        }
        // fixme: 7/12/2019 вынести в текстовые константы
        requestContent.getRequestAttributes().put("currentCourseMarks", marks);
        requestContent.getRequestAttributes().put("requestedCourse", courses.get(0));
        requestContent.getRequestAttributes().put("currentCourseContent", takeChaptersAndLessons(courseId));
        requestContent.getRequestAttributes().put("author_of_course", (new AccountService()).takeAuthorOfCourse(courseId));
    }

    public void initCourseApprovalPage(SessionRequestContent requestContent) throws ServiceException {
        CourseRepository repository = new CourseRepository();
        try {
            List<Course> courses = repository.query(new SelectByStateSpecification(CourseState.NOT_APPROVED));
            requestContent.getRequestAttributes().put(ATTR_NEED_APPROVAL, courses);
        } catch (RepositoryException e) {
            // FIXME: 7/18/2019
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
        } catch (RepositoryException e) {
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
        } catch (RepositoryException e) {
            // FIXME: 7/18/2019
            throw new ServiceException(e);
        }
    }


    // TODO: 7/18/2019 если fail то поудалять все что добавилось?
    // TODO: 7/18/2019 подумать может получится разнести хотя бы на два метода?
    public boolean addCourseToReview(SessionRequestContent requestContent) throws RepositoryException {
        Map<String, String[]> params = requestContent.getRequestParameters();
        CourseRepository courseRepo = new CourseRepository();
        String courseName = params.get(ATTR_COURSE_NAME)[0];
        if (courseRepo.query(new SelectCourseByNameSpecification(courseName)).size() > 0) {
            // TODO: 7/18/2019 попробовать сохранить данные при наличии такого названия курса в базе? при форварде?
            requestContent.getRequestAttributes().put(ATTR_COURSE_ALREADY_EXISTS, MSG_COURSE_ALREADY_EXISTS);
            return false;
        }
        Course course = new Course();
        Account currAccount = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        course.setAuthorId(currAccount.getId());
        course.setName(courseName);
        course.setDescription(params.get(ATTR_COURSE_DESCRIPTION)[0]);
        course.setPrice(new BigDecimal(params.get(ATTR_COURSE_PRICE)[0]));
        course.setCreationDate(new Date(System.currentTimeMillis()));
        course.setPathToPicture(DEFAULT_IMG);
        course.setState(CourseState.NOT_APPROVED);
        courseRepo.insert(course);
        course = courseRepo.query(new SelectCourseByNameSpecification(courseName)).get(0);

        ChapterRepository chapterRepo = new ChapterRepository();
        String[] chapterNames = params.get(ATTR_CHAPTER_NAME);
        int courseId = courseRepo.query(new SelectCourseByNameSpecification(courseName))
                .get(0).getId();

        for (String chapterName : chapterNames) {
            CourseChapter chapter = new CourseChapter();
            chapter.setCourseId(courseId);
            chapter.setName(chapterName);
            chapterRepo.insert(chapter);
        }

        LessonRepository lessonRepo = new LessonRepository();
        for (int i = 0; i < chapterNames.length; i++) {
            String[] lessonNames = params.get(PATTERN_LESSON_TITLE + (i + 1));
            String[] lessonContents = params.get(PATTERN_LESSON_CONTENT + (i + 1));
            String[] lessonDurations = params.get(PATTERN_LESSON_DURATION + (i + 1));
            int chapterId = chapterRepo.query(new SelectChapterByNameAndCourseIdSpecification(chapterNames[i], course.getId()))
                    .get(0).getId();
            // TODO: 7/18/2019 проверить на пустых полях
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
        return true;
    }


    // returns map of chapters and lessons of current course, then it have to be setted to request attribute
    private Map<CourseChapter, List<CourseLesson>> takeChaptersAndLessons(int courseId) throws RepositoryException {
        ChapterRepository chapterRepository = new ChapterRepository();
        LessonRepository lessonRepository = new LessonRepository();
        Map<CourseChapter, List<CourseLesson>> courseContent = new HashMap<>();
        List<CourseChapter> chapters = chapterRepository.query(new SelectAllFromCourseSpecification(courseId));
        for (CourseChapter chapter : chapters) {
            List<CourseLesson> lessons = lessonRepository.query(new SelectByChapterIdSpecification(chapter.getId()));
            courseContent.put(chapter, lessons);
        }
        return courseContent;
    }


}
