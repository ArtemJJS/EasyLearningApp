package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class DeclineCourseImgChange implements Command {
    private static final String FORWARD_PATH = "/jsp/admin/approve_course_img_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        (new CourseService()).declineCourseImgChange(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
