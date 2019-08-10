package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.PaymentService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class CashOutCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/author/cash_out_page.jsp";
    private static final String REDIRECT_PATH = "/operation-result?operation=cash-out";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        boolean isSuccessful = (new PaymentService()).processCashOutFromBalance(requestContent);
        System.out.println(isSuccessful);
        if (isSuccessful) {
            requestContent.setPath(REDIRECT_PATH);
            System.out.println(REDIRECT_PATH);
            return REDIRECT;
        }
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
