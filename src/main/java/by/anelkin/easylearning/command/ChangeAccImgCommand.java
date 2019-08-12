package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ChangeAccImgCommand implements Command {
    private static final String REDIRECT_PATH = "/operation-result?operation=send_avatar_to_review";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new AccountService()).addAccAvatarToReview(requestContent);
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
    }
}
