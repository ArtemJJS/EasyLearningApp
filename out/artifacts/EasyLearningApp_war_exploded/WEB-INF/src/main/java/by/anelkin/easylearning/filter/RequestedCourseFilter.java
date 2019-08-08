package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;
import lombok.extern.log4j.Log4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Log4j
@WebFilter(urlPatterns = {"/course", "/course/learn", "/author/edit-course", "/user/buy-course", "/user/mark-course"})
public class RequestedCourseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Filter RequestedCourseFilter works.");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        CourseService courseService = new CourseService();
        try {
            courseService.initCoursePage(requestContent);
            requestContent.insertAttributes(request);
        } catch (ServiceException e) {
            log.error(e);
            throw new ServletException(e);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
