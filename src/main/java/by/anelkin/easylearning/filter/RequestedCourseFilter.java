package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@WebFilter(urlPatterns = "/course")
public class RequestedCourseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        CourseService courseService = new CourseService();
        try {
            courseService.initCoursePage(requestContent);
            requestContent.insertAttributes(request);
        } catch (RepositoryException e) {
            // TODO: 7/8/2019 на страницу ошибки, курс не найден
            throw new ServletException(e);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
