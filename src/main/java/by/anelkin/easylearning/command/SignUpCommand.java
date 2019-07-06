package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;

import javax.servlet.http.HttpServletRequest;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class SignUpCommand implements Command {
    private static final String REDIRECT_PATH = "http://localhost:8080/easyLearning/sign-up";

//    @Override
//    public String execute(HttpServletRequest request) {
//        return REDIRECT_PATH;
//    }

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException {
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
    }
}
