package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.*;

public class AddCourseToReviewCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/author/add_course_page.jsp";
    private static final String REDIRECT_PATH = "/operation-result?operation=add-course-to-review";

    @Override
    public ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        boolean isSuccessful = (new CourseService()).addCourseToReview(requestContent);
        if (isSuccessful) {
            requestContent.setPath(REDIRECT_PATH);
            return ResponseType.REDIRECT;
        }
        requestContent.setPath(FORWARD_PATH);
        return ResponseType.FORWARD;
    }
}
