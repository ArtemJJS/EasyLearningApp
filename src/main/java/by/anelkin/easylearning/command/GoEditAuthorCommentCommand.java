package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class GoEditAuthorCommentCommand implements Command{
    private static final String FORWARD_PATH = "/jsp/user/edit_author_comment_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        new MarkService().takeAuthorComment(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
