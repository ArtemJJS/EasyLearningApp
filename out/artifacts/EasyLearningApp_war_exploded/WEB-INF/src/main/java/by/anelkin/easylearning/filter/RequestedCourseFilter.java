package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.ChapterRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.LessonRepository;
import by.anelkin.easylearning.specification.chapter.SelectAllFromCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import lombok.extern.log4j.Log4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@WebFilter(urlPatterns = "/course")
public class RequestedCourseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // TODO: 7/6/2019 отсюда логику в сервис?
        CourseRepository repository = new CourseRepository();
        int id = Integer.valueOf(request.getParameter("course-id"));
        log.debug("receiving course from base, id: " + id);
        try {
            List<Course> courses = repository.query(new SelectCourseByIdSpecification(id));
            if (courses.size() != 1) throw new RepositoryException("Course wasn't found");
            request.setAttribute("requestedCourse", courses.get(0));
            request.setAttribute("currentCourseContent", takeChaptersAndLessons(id));
        } catch (RepositoryException e) {
            // TODO: 7/6/2019 на страницу ошибки, курс не найден
            throw new ServletException(e);
        }
        filterChain.doFilter(request, servletResponse);
    }

    // returns map of chapters and lessons of current course, then it have to be setted to request attribute
    private Map<CourseChapter, List<CourseLesson>> takeChaptersAndLessons(int courseId) throws RepositoryException {
        ChapterRepository chapterRepository = new ChapterRepository();
        LessonRepository lessonRepository = new LessonRepository();
        Map<CourseChapter, List<CourseLesson>> courseContent = new HashMap<>();
        List<CourseChapter> chapters = chapterRepository.query(new SelectAllFromCourseSpecification(courseId));
        for (CourseChapter chapter : chapters){
            List<CourseLesson> lessons = lessonRepository.query(new SelectByChapterIdSpecification(chapter.getId()));
            courseContent.put(chapter, lessons);
        }
        return courseContent;
    }
}
