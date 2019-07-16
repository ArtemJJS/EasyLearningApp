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
    private static final String REDIRECT_ADMIN = "http://localhost:8080/easyLearning/user";
    private static final String REDIRECT_AUTHOR = "http://localhost:8080/easyLearning/author";
    private static final String REDIRECT_USER = "http://localhost:8080/easyLearning/user";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        (new AccountService()).editAccountInfo(requestContent);
        AccountType role = (AccountType) requestContent.getSessionAttributes().get("role");

//
//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//

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
