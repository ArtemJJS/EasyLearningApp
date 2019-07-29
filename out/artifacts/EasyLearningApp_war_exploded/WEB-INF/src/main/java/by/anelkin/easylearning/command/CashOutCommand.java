package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.PaymentService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class CashOutCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/author/cash_out_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new PaymentService()).processCashOutFromBalance(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
