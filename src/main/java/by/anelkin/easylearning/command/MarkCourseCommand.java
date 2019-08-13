package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;
public class MarkCourseCommand implements Command {
    private static final String SUCCESSFUL_OPERATION_REDIRECT = "/course?course-id=";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new MarkService()).markCourse(requestContent);
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_TARGET_ID)[0]);
        requestContent.setPath(SUCCESSFUL_OPERATION_REDIRECT + courseId);
        return REDIRECT;
    }
}
