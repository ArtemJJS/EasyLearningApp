package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.*;

public class AddCourseToReviewCommand implements Command {
    private static final String FAIL_OPERATION_FORWARD = "jsp/author/add_course_page.jsp";
    private static final String SUCCESSFUL_OPERATION_REDIRECT = "http://localhost:8080/easyLearning/account";

    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        boolean isExecuted = (new CourseService()).addCourseToReview(requestContent);
        if (isExecuted) {
            requestContent.setPath(SUCCESSFUL_OPERATION_REDIRECT);
            return ResponseType.REDIRECT;
        }
        requestContent.setPath(FAIL_OPERATION_FORWARD);
        return ResponseType.FORWARD;
    }
}
