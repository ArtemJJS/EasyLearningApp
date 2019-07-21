package by.anelkin.easylearning.filter;


import by.anelkin.easylearning.command.Command;
import by.anelkin.easylearning.command.CommandFactory;
import by.anelkin.easylearning.command.SearchCourseCommand;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;


@WebFilter(urlPatterns = "/search")
public class SearchPageFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        //command here to avoid creation of specification here
        Command command = new SearchCourseCommand();
        try {
            command.execute(requestContent);
            requestContent.insertAttributes(request);
        } catch (RepositoryException | ServiceException e) {
            throw new ServletException(e);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
