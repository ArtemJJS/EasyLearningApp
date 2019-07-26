package by.anelkin.easylearning.command;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;

import java.util.concurrent.TimeUnit;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class EditUserInfoCommand implements Command {
    // FIXME: 7/16/2019 поправить адрес админу
    private static final String REDIRECT_ADMIN = "/account";
    private static final String REDIRECT_AUTHOR = "/account";
    private static final String REDIRECT_USER = "/account";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new AccountService()).editAccountInfo(requestContent);
        AccountType role = (AccountType) requestContent.getSessionAttributes().get("role");
        // TODO: 7/16/2019 а если тут гость??? может ли такое быть?
        switch (role){
            case USER:
                requestContent.setPath(REDIRECT_USER);
                break;
            case AUTHOR:
                requestContent.setPath(REDIRECT_AUTHOR);
                break;
            case ADMIN:
                requestContent.setPath(REDIRECT_ADMIN);
        }
        return REDIRECT;
    }
}
