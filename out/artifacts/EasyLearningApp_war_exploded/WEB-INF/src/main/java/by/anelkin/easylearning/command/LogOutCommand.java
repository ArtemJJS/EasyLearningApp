package by.anelkin.easylearning.command;

import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class LogOutCommand implements Command {
    private static final String CORRECT_LOGOUT_PATH = "http://localhost:8080/easyLearning/account";

    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws RepositoryException {
        (new AccountService()).logOut(requestContent);
        requestContent.setPath(CORRECT_LOGOUT_PATH);
        requestContent.setResponseType(REDIRECT);
        return REDIRECT;
    }
}
