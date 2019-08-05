package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class RestorePasswordCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/global/restore_pass_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        new AccountService().restorePassword(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
