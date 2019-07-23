package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class MarkAuthorCommand implements Command {
    private static final String ATTR_AUTHOR_LOGIN = "author_login";
    private static final String SUCCESSFUL_OPERATION_REDIRECT = "http://localhost:8080/easyLearning/author-info/";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new MarkService()).markAuthor(requestContent);
        requestContent.setPath(SUCCESSFUL_OPERATION_REDIRECT + requestContent.getRequestAttributes().get(ATTR_AUTHOR_LOGIN));
        return REDIRECT;
    }
}
