package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class LogOutCommand implements Command {
    private static final String ATTR_DESTROY_SESSION = "destroy_session";

    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        requestContent.getSessionAttributes().put(ATTR_DESTROY_SESSION, true);
        requestContent.setPath(requestContent.getRequestReferer());
        requestContent.setResponseType(REDIRECT);
        return REDIRECT;
    }
}

