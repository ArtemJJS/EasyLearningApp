package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;
import lombok.extern.log4j.Log4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
@WebFilter(urlPatterns = "/admin/course-approve")
public class CourseApprovalPageFilter implements Filter {
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
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, request.getLocale());
            log.error(e);
            response.sendError(ERROR_500, rb.getString(BUNDLE_ETERNAL_SERVER_ERROR));
            return;
        }
        filterChain.doFilter(request, servletResponse);
    }
}
