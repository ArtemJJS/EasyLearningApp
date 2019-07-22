package by.anelkin.easylearning.command;


import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;


import static by.anelkin.easylearning.receiver.SessionRequestContent.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class SignUpNewUserCommand implements Command {
    // TODO: 7/12/2019 путь с htt должен быть относительным, как минимум на счет порта позаботиться
    private static final String CORRECT_RIGISTRATION_PATH = "http://localhost:8080/easyLearning/easyLearning";
    private static final String WRONG_REGISTRATION_REDIRECT_PATH = "/jsp/sign_up.jsp";


    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
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
