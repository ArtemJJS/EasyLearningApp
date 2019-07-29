package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;


import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class EditUserInfoCommand implements Command {
    private static final String REDIRECT_PATH = "/account?command-result=";


    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        boolean isOperationSuccessful = (new AccountService()).editAccountInfo(requestContent);
        if (isOperationSuccessful) {
            requestContent.setPath(REDIRECT_PATH + true);
        } else {
            requestContent.setPath(REDIRECT_PATH + false);
        }
        return REDIRECT;
    }
}
