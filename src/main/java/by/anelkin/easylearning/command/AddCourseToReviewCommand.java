package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.*;

public class AddCourseToReviewCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/author/add_course_page.jsp";

    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new CourseService()).addCourseToReview(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return ResponseType.FORWARD;
    }
}
