package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType;
import lombok.NonNull;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class LoginCommand implements Command {
    private static final String CORRECT_PATH = "http://localhost:8080/easyLearning/easyLearning";
    private static final String WRONG_LOGIN_PATH = "/jsp/start_page.jsp";

    @Override
    public ResponseType execute(@NonNull SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        boolean isLoginCorrect = (new AccountService()).login(requestContent);
        if (!isLoginCorrect){
            requestContent.getRequestAttributes().put("wrong-login", "true");
            requestContent.setPath(WRONG_LOGIN_PATH);
            requestContent.setResponseType(FORWARD);
            return FORWARD;
        }else {
            requestContent.setResponseType(REDIRECT);
            requestContent.setPath(CORRECT_PATH);
            return REDIRECT;
        }

//        return requestContent;
    }
}
