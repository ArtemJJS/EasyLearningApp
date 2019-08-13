package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;

import java.util.Arrays;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

public class ChangeLangCommand implements Command {
    private static final String[] patternsWithIncorrectRedirect = new String[]{"/user/deposit",
            "/author/cash-out", "/author/add-course", "/account/change-pass", "/account/change-image"};
    private static final String REDIRECT_TO_ACCOUNT = "/account";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        String locale = requestContent.getRequestParameters().get(ATTR_LOCALE)[0];
        requestContent.getSessionAttributes().put(ATTR_LOCALE, locale);
        if (isGoodReferer(requestContent.getRequestReferer())) {
            requestContent.setPath(requestContent.getRequestReferer());
        } else {
            requestContent.setPath(REDIRECT_TO_ACCOUNT);
        }
        return REDIRECT;
    }

    //if we are on page with servlet pattern(after forward), redirecting back to it after change lang
    //will be cause of error(no command = nullPointer), so I'll redirect to account page
    private boolean isGoodReferer(String referer) {
        Object[] matches = Arrays.stream(patternsWithIncorrectRedirect).filter(referer::contains).toArray();
        return matches.length == 0;
    }
}
