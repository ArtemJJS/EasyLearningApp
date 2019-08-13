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
/**
 * Provides initialisation of course image approval page
 *
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
@WebFilter(urlPatterns = "/admin/approve-course-image")
public class ApproveCourseImgPageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        try {
            (new CourseService()).initCourseImgApprovalPage(requestContent);
            requestContent.insertAttributes(request);
        } catch (ServiceException e) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, request.getLocale());
            log.error(e);
            request.setAttribute(ATTR_MESSAGE,rb.getString(BUNDLE_ETERNAL_SERVER_ERROR) );
            response.sendError(ERROR_500);
            return;
        }
        filterChain.doFilter(request, servletResponse);
    }
}
