package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.MarkService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class GoEditCourseCommentCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/user/edit_course_comment_page.jsp";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        new MarkService().takeCourseComment(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
