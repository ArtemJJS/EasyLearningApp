package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ApproveAccAvatarCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/admin/approve_acc_avatar_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        (new AccountService()).approveAccAvatar(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}