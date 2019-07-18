package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ApproveCourseCommand implements Command {
    private static final String OPERATION_SUCCESSFUL_REDIRECT = "http://localhost:8080/easyLearning/admin/course-approval";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        (new CourseService()).approveCourse(requestContent);
        requestContent.setPath(OPERATION_SUCCESSFUL_REDIRECT);
        return REDIRECT;
    }
}
