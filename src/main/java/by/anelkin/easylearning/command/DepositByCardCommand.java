package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.PaymentService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class DepositByCardCommand implements Command {
    private static final String REDIRECT_PATH = "http://localhost:8080/easyLearning/account";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new PaymentService()).processDepositByCard(requestContent);
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
    }
}
