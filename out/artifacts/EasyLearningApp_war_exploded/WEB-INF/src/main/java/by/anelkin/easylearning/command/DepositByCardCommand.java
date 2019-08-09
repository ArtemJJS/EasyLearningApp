package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.PaymentService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class DepositByCardCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/user/deposit_page.jsp";
    private static final String REDIRECT_PATH = "/operation-result?operation=deposit";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        boolean isSuccessful = (new PaymentService()).processDepositByCard(requestContent);
        if (isSuccessful){
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
        }
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
