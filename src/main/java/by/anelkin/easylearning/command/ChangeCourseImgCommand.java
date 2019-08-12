package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ChangeCourseImgCommand implements Command{
    private static final String REDIRECT_PATH = "/operation-result?operation=send_avatar_to_review";
//    private static final String ATTR_COURSE_ID = "course_id";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws  ServiceException {
        (new CourseService()).addCourseImgToReview(requestContent);
//        String courseId = requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0];
//        requestContent.setPath(REDIRECT_PATH + courseId);
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
    }
}
