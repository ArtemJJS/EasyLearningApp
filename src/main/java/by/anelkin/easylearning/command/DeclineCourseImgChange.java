package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class DeclineCourseImgChange implements Command {
    private static final String REDIRECT_PATH = "/admin/approve-course-image";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new CourseService()).declineCourseImgChange(requestContent);
        requestContent.setPath(REDIRECT_PATH);
        return REDIRECT;
    }
}
