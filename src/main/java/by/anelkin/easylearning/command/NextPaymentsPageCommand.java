package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.ATTR_PAGE;

public class NextPaymentsPageCommand implements Command {
    private static final String REDIRECT_PATH = "/account/payments?page=";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        requestContent.setPath(REDIRECT_PATH + requestContent.getRequestParameters().get(ATTR_PAGE)[0]);
        return REDIRECT;
    }
}
