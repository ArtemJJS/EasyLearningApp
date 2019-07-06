package by.anelkin.easylearning.command;


import by.anelkin.easylearning.command.service.AccountService;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;


import static by.anelkin.easylearning.receiver.SessionRequestContent.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class SignUpNewUserCommand implements Command {
    private static final String CORRECT_RIGISTRATION_PATH = "http://localhost:8080/easyLearning/account";
    private static final String WRONG_REGISTRATION_REDIRECT_PATH = "/jsp/sign_up.jsp";


    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws RepositoryException {
        boolean isRegistered = (new AccountService()).signUp(requestContent);
        if (isRegistered) {
            requestContent.setPath(CORRECT_RIGISTRATION_PATH);
            return REDIRECT;
        } else {
            requestContent.setPath(WRONG_REGISTRATION_REDIRECT_PATH);
            return FORWARD;
        }
    }

}
