package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ChangePasswordCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/global/change_password_page.jsp";
//    private static final String CHANGE_SUCCESSFUL_REDIRECT = "http://localhost:8080/easyLearning/account";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
       (new AccountService()).changeAccountPassword(requestContent);
//        if (isPasswordChanged){
//            requestContent.setPath(CHANGE_SUCCESSFUL_REDIRECT);
//            return REDIRECT;
//        }
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
