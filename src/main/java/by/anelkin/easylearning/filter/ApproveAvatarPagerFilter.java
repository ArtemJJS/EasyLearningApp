package by.anelkin.easylearning.filter;


import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;
import lombok.extern.log4j.Log4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Log4j
@WebFilter(urlPatterns = "/admin/approve-acc-avatar")
public class ApproveAvatarPagerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        try {
            (new AccountService()).initApproveAccAvatarPage(requestContent);
            requestContent.insertAttributes(request);
            log.debug("Filter " + this.getClass().getSimpleName() + " completed work.");
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, servletResponse);
    }
}
