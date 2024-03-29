package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class DeclineCourseApprovalCommand implements Command {
    private static final String REDIRECT_PATH = "/admin/course-approve";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new CourseService()).freezeCourse(requestContent);
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
    }
}
