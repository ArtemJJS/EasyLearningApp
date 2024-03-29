package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.PaymentRepository;
import by.anelkin.easylearning.service.PaymentService;
import lombok.extern.log4j.Log4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

import static by.anelkin.easylearning.util.GlobalConstant.*;

/**
 * Provides initialisation of {@link Payment} for current account
 *
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
@WebFilter(urlPatterns = "/account/payments")
public class AccountPaymentsFilter implements Filter {
    private PaymentService paymentService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        paymentService = new PaymentService();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, request.getLocale());
        try {
            paymentService.insertPaymentsIntoRequestAttributes(requestContent);
        } catch (ServiceException e) {
            log.error(e);
            request.setAttribute(ATTR_MESSAGE,rb.getString(BUNDLE_ETERNAL_SERVER_ERROR) );
            response.sendError(ERROR_500);
            return;
        }
        requestContent.insertAttributes(request);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
