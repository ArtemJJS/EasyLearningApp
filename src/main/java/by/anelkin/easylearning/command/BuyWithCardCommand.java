package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.PaymentService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

public class BuyWithCardCommand implements Command {
    private static final String SUCCESSFUL_OPERATION_REDIRECT = "http://localhost:8080/easyLearning/course?course-id=";

    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        (new PaymentService()).processPurchaseByCard(requestContent);
        int courseId = Integer.parseInt(requestContent.getRequestParameters().get("course_id")[0]);
        requestContent.setPath(SUCCESSFUL_OPERATION_REDIRECT + courseId);
        return REDIRECT;
    }
}
