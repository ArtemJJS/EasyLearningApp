package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/easyLearning", "/jsp/main_page.jsp"})
public class MainPageFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("FILTER");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        try {
            new CourseService().chooseRecommendedCourses(requestContent);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
        requestContent.insertAttributes(request);
        filterChain.doFilter(request, servletResponse);
    }
}
