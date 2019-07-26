package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.PaymentService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class CashOutCommand implements Command {
    private static final String SUCCESS_OPERATION_REDIRECT = "/account";
    private static final String FAIL_OPERATION_FORWARD = "/jsp/author/cash_out_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        boolean isCashOutProceed = (new PaymentService()).processCashOutFromBalance(requestContent);
        if (isCashOutProceed){
            requestContent.setPath(SUCCESS_OPERATION_REDIRECT);
            return REDIRECT;
        }
        requestContent.setPath(FAIL_OPERATION_FORWARD);
        return FORWARD;
    }
}
