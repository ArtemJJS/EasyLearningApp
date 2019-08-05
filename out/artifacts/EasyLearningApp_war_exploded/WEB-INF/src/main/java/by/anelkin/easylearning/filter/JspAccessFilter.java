package by.anelkin.easylearning.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static by.anelkin.easylearning.entity.Account.AccountType.GUEST;

@WebFilter(urlPatterns = "*.jsp")
public class JspAccessFilter implements Filter {
    public static final String ATTR_JSP_PERMITTED = "jsp_permitted";
    private static final String URI_SPLITTER = "/";
    private static final String JSP_EXTENSION = ".jsp";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // getSession() prevents "java.lang.IllegalStateException: Cannot create a session after the response has been committed",
        // which appears if *.jsp is the first request (still has no session)
        if (request.getSession().isNew()) {
            request.getSession().setAttribute("role", GUEST);
            request.getSession().setAttribute("locale", new Locale("en", "US"));
        }
        String uri = request.getRequestURI();
        if (uri.contains(JSP_EXTENSION) && request.getAttribute(ATTR_JSP_PERMITTED) == null) {
            response.sendRedirect(request.getContextPath() + URI_SPLITTER);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
