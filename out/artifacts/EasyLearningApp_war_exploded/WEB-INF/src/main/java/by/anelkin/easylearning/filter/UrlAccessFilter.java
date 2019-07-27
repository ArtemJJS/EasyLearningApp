package by.anelkin.easylearning.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Account.AccountType.GUEST;

@WebFilter(urlPatterns = {"/admin/*", "/author/*", "/user/*", "/account/*"})
public class UrlAccessFilter implements Filter {
    private static final String URI_USER = "/user/";
    private static final String URI_ADMIN = "/admin/";
    private static final String URI_AUTHOR = "/author/";
    private static final String URI_ACCOUNT = "/account";
    private static final String URL_SPLITTER = "/";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getSession().isNew()) {
            request.getSession().setAttribute("role", GUEST);
            request.getSession().setAttribute("locale", Locale.US);
        }
        String uri = request.getRequestURI();
        AccountType role = (AccountType) request.getSession().getAttribute("role");
        switch (role) {
            case USER:
                if (!uri.contains(URI_USER) && !uri.contains(URI_ACCOUNT)) {
                    response.sendRedirect(request.getContextPath() + URL_SPLITTER);
                }
                break;
            case AUTHOR:
                if (!uri.contains(URI_AUTHOR) && !uri.contains(URI_ACCOUNT)) {
                    response.sendRedirect(request.getContextPath() + URL_SPLITTER);
                }
                break;
            case ADMIN:
                if (!uri.contains(URI_ADMIN) && !uri.contains(URI_ACCOUNT)) {
                    response.sendRedirect(request.getContextPath() + URL_SPLITTER);
                }
                break;
            default:
                response.sendRedirect(request.getContextPath() + URL_SPLITTER);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
