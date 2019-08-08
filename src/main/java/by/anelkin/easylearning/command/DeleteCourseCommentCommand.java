package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class DeleteCourseCommentCommand implements Command {
    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        new MarkService().deleteCourseMarkComment(requestContent);
        requestContent.setPath(requestContent.getRequestReferer());
        return REDIRECT;
    }
}
