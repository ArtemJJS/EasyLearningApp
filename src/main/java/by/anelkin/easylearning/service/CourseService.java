package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.ChapterRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.LessonRepository;
import by.anelkin.easylearning.specification.chapter.SelectAllFromCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectByStateSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
public class CourseService {
    private static final String ATTR_NEED_APPROVAL = "courses_need_approval";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final byte STATE_APPROVED = 1;
    private static final byte STATE_FREEZE = -1;



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
            List<Course> courses = repository.query(new SelectByStateSpecification(false));
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
            currCourse.setState(STATE_APPROVED);
            repository.update(currCourse);
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
            currCourse.setState(STATE_FREEZE);
            repository.update(currCourse);
        } catch (RepositoryException e) {
            // FIXME: 7/18/2019
            throw new ServiceException(e);
        }
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
