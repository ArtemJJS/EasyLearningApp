package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType;
import lombok.NonNull;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.ATTR_IS_NEED_COOKIE;

public class LoginCommand implements Command {
    private static final String CORRECT_PATH = "/";
    private static final String WRONG_LOGIN_PATH = "/jsp/start_page.jsp";

    @Override
    public ResponseType execute(@NonNull SessionRequestContent requestContent) throws ServiceException {
        boolean isLoginCorrect = (new AccountService()).login(requestContent);
        if (!isLoginCorrect){
            requestContent.getRequestAttributes().put("wrong-login", "true");
            requestContent.setPath(WRONG_LOGIN_PATH);
            return FORWARD;
        }else {
            requestContent.setPath(CORRECT_PATH);
            requestContent.getRequestAttributes().put(ATTR_IS_NEED_COOKIE, true);
            return REDIRECT;
        }
    }
}
