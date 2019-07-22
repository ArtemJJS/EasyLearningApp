package by.anelkin.easylearning.filter;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.PaymentRepository;
import by.anelkin.easylearning.service.PaymentService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
        try {
            paymentService.insertPaymentsIntoRequestAttributes(requestContent);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
        requestContent.insertAttributes(request);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
