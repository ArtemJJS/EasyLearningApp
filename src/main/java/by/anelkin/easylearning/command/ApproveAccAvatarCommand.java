package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ApproveAccAvatarCommand implements Command {
    private static final String REDIRECT_PATH = "/admin/approve-account-avatar";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new AccountService()).approveAccAvatar(requestContent);
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
    }
}
