package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.CourseService;
import by.anelkin.easylearning.specification.course.SelectCourseSearchSpecification;

import static by.anelkin.easylearning.command.CommandFactory.CommandType.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class SearchCourseCommand implements Command {
    private static final String FORWARD_PATH = "/jsp/global/search_result_page.jsp";
    private static final String ATTR_SEARCH_KEY = "search_key";


    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        (new CourseService()).searchCourses(requestContent);
        requestContent.setPath(FORWARD_PATH);
        return FORWARD;
    }
}
