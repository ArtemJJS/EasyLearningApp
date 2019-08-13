package by.anelkin.easylearning.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

import static by.anelkin.easylearning.util.GlobalConstant.ENCODING_UTF_8;
/**
 * Provides correct charset for all requests
 *
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@WebFilter(urlPatterns = "/*")
public class CharsetFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(ENCODING_UTF_8);
        servletResponse.setContentType("text/html; charset=UTF-8");
        servletResponse.setCharacterEncoding(ENCODING_UTF_8);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
