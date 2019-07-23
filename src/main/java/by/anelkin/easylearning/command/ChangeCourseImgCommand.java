package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class ChangeCourseImgCommand implements Command{
    private static final String FORWARD_PATH = "/jsp/global/change_picture.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws  ServiceException {
        (new CourseService()).addCourseImgToReview(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
