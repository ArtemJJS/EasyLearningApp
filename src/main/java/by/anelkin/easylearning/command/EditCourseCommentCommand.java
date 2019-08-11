package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.ATTR_COURSE_ID_DEFIS;

public class EditCourseCommentCommand implements Command {
    private static final String REDIRECT_PATH = "/course?course-id=";
    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        new MarkService().editCourseComment(requestContent);
        requestContent.setPath(REDIRECT_PATH + requestContent.getRequestParameters().get(ATTR_COURSE_ID_DEFIS)[0]);
        return REDIRECT;
    }
}
