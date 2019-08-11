package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.ATTR_REQUESTED_AUTHOR_LOGIN;

public class EditAuthorCommentCommand implements Command {
    private static final String REDIRECT_PATH = "/author-info/";
    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        new MarkService().editAuthorComment(requestContent);
        requestContent.setPath(REDIRECT_PATH + requestContent.getRequestParameters().get(ATTR_REQUESTED_AUTHOR_LOGIN)[0]);
        return REDIRECT;
    }
}
