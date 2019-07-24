package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ChangeLangCommand implements Command {
    private static final String ATTR_LOCALE = "locale";
    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        String locale = requestContent.getRequestParameters().get(ATTR_LOCALE)[0];
        requestContent.getSessionAttributes().put(ATTR_LOCALE, locale);
        requestContent.setPath(requestContent.getRequestReferer());
        return REDIRECT;
    }
}
