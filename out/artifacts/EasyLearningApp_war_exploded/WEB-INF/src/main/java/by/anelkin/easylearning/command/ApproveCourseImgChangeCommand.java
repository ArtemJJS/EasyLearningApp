package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ApproveCourseImgChangeCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/admin/approve_course_img_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {

        (new CourseService()).approveCourseImgChange(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
