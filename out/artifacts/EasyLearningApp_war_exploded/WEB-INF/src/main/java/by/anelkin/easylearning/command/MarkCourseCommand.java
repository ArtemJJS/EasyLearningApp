package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class MarkCourseCommand implements Command {
    private static final String SUCCESSFUL_OPERATION_REDIRECT = "/course?course-id=";
    private static final String ATTR_COURSE_ID = "course_id";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new MarkService()).markCourse(requestContent);
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
        requestContent.setPath(SUCCESSFUL_OPERATION_REDIRECT + courseId);
        return REDIRECT;
    }
}
