package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.service.AccountService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@WebFilter(urlPatterns = "/author-info/*")
public class AuthorPageFilter implements Filter {
    // TODO: 7/12/2019 правильно ли вынести как поле в соответствии с жизненным циклом?
    private AccountService accountService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        accountService = new AccountService();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String login = accountService.pickTargetNameFromUri(request.getRequestURI());
        request.setAttribute("requested_author_login", login);
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        try {
            accountService.initAuthorPage(requestContent);
            requestContent.insertAttributes(request);
        } catch (RepositoryException | ServiceException e) {
            // TODO: 7/12/2019 на страницу ошибки
            throw new ServletException(e);
        }

        filterChain.doFilter(request, servletResponse);
    }
}
