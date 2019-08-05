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
@WebFilter(urlPatterns = "/admin/course-approve")
public class CourseApprovalPageFilter implements Filter {
    private static final String ATTR_ROLE = "role";
    private CourseService courseService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        courseService = new CourseService();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        try {
            courseService.initCourseApprovalPage(requestContent);
            requestContent.insertAttributes(request);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
