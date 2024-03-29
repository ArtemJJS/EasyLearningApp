package by.anelkin.easylearning.filter;


import by.anelkin.easylearning.entity.Course;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Account.AccountType.GUEST;
import static by.anelkin.easylearning.util.GlobalConstant.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;
/**
 * Provides protection from direct access to resources
 * according to {@link by.anelkin.easylearning.entity.Account} role
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@WebFilter(urlPatterns = {"/admin/*", "/author/*", "/user/*", "/account/*", "/course/learn",
                            "/operation-result"})
public class UrlAccessFilter implements Filter {
    private static final String URI_USER = "/user/";
    private static final String URI_OPERATION_RESULT = "/operation-result";
    private static final String URI_AUTHOR = "/author/";
    private static final String URI_ACCOUNT = "/account";
    private static final String URI_LEARNING_PAGE = "/course/learn";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getSession().isNew()) {
            request.getSession().setAttribute(ATTR_ROLE, GUEST);
            request.getSession().setAttribute(ATTR_LOCALE, Locale.US);
        }
        String uri = request.getRequestURI();
        AccountType role = (AccountType) request.getSession().getAttribute(ATTR_ROLE);

        boolean accessGrantedToLearningPage = uri.contains(URI_LEARNING_PAGE) && isAccessGrantedToLearningPage(request);
        switch (role) {
            case USER:
                if (!uri.contains(URI_USER) && !uri.contains(URI_ACCOUNT) && !uri.contains(URI_OPERATION_RESULT) && !accessGrantedToLearningPage) {
                    response.sendRedirect(request.getContextPath() + PATH_SPLITTER);
                }
                break;
            case AUTHOR:
                if (!uri.contains(URI_AUTHOR) && !uri.contains(URI_ACCOUNT) && !uri.contains(URI_OPERATION_RESULT) && !accessGrantedToLearningPage) {
                    response.sendRedirect(request.getContextPath() + PATH_SPLITTER);
                }
                break;
            case ADMIN:
                if (uri.contains(URI_AUTHOR) && uri.contains(URI_USER)) {
                    response.sendRedirect(request.getContextPath() + PATH_SPLITTER);
                }
                break;
            default:
                response.sendRedirect(request.getContextPath() + PATH_SPLITTER);
                return;
        }
        filterChain.doFilter(request, servletResponse);
    }

    private boolean isAccessGrantedToLearningPage(HttpServletRequest request) {
        AccountType role = AccountType.valueOf(request.getSession().getAttribute(ATTR_ROLE).toString().toUpperCase());
        int courseId = Integer.parseInt(request.getParameter(ATTR_COURSE_ID_DEFIS));
        boolean isAccessGranted = false;
        switch (role) {
            case GUEST:
                break;
            case ADMIN:
                isAccessGranted = true;
                break;
            default:
                List<Course> availableCourses = (List<Course>) request.getSession().getAttribute(ATTR_AVAILABLE_COURSES);
                List<Course> filteredCourses = availableCourses.stream().filter(course -> course.getId() == courseId).collect(Collectors.toList());
                isAccessGranted = filteredCourses.size() > 0;
        }
        return isAccessGranted;
    }

}
