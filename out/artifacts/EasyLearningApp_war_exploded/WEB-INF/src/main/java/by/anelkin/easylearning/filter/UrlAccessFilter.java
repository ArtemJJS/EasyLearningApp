package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.entity.Account;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Account.AccountType.GUEST;

@WebFilter(urlPatterns = {"/admin/*", "/author/*", "/user/*"})
public class UrlAccessFilter implements Filter {
    private static final String URI_USER = "/user/";
    private static final String URI_ADMIN = "/admin/";
    private static final String URI_AUTHOR = "/author/";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getSession().isNew()) {
            request.getSession().setAttribute("role", GUEST);
            request.getSession().setAttribute("locale", new Locale("en", "US"));
        }
        String uri = request.getRequestURI();
        AccountType role = (AccountType) request.getSession().getAttribute("role");
        switch (role) {
            case USER:
                if (!uri.contains(URI_USER)) {
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;
            case AUTHOR:
                if (!uri.contains(URI_AUTHOR)) {
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;
            case ADMIN:
                if (!uri.contains(URI_ADMIN)) {
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
        }
        filterChain.doFilter(request, servletResponse);
    }
}
