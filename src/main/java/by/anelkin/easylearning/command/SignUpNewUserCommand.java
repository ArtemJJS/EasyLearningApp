package by.anelkin.easylearning.command;


import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.receiver.SessionRequestContent;

import static by.anelkin.easylearning.receiver.SessionRequestContent.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.ATTR_IS_NEED_COOKIE;


public class SignUpNewUserCommand implements Command {
    private static final String CORRECT_REGISTRATION_PATH = "/";
    private static final String WRONG_REGISTRATION_REDIRECT_PATH = "/jsp/sign_up.jsp";


    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        boolean isRegistered = (new AccountService()).signUp(requestContent);
        if (isRegistered) {
            requestContent.setPath(CORRECT_REGISTRATION_PATH);
            requestContent.getRequestAttributes().put(ATTR_IS_NEED_COOKIE, true);
            return REDIRECT;
        } else {
            requestContent.setPath(WRONG_REGISTRATION_REDIRECT_PATH);
            return FORWARD;
        }
    }

}
