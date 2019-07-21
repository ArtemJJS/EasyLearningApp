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

    private static final String ATTR_PREVIOUS_COMMAND = "previous_command";

    private static final String TEST_FILTER_REDIRECT = "http://localhost:8080/easyLearning/search";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException {
        // using specification here allow to merge several methods into one
        String searchKey = requestContent.getRequestParameters().get(ATTR_SEARCH_KEY)[0];
        (new CourseService()).initRequestBySpecification(requestContent, new SelectCourseSearchSpecification(searchKey));
        requestContent.setPath(FORWARD_PATH);
        requestContent.getRequestAttributes().put(ATTR_PREVIOUS_COMMAND, SEARCH_COURSE);
        return FORWARD;
//        requestContent.setPath(TEST_FILTER_REDIRECT);
//        return REDIRECT;

    }
}
