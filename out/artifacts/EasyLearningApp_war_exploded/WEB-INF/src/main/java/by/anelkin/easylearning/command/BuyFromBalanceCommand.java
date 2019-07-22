package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.PaymentService;

import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.REDIRECT;

public class BuyFromBalanceCommand implements Command {
    private static final String SUCCESSFUL_OPERATION_REDIRECT = "http://localhost:8080/easyLearning/course?course-id=";
    private static final String FAIL_OPERATION_REDIRECT = "http://localhost:8080/easyLearning/user/buy-course?result=fail&course-id=";
    private static final String ATTR_COURSE_ID = "course_id";


    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        boolean isOperationSuccessful = (new PaymentService()).processPurchaseFromBalance(requestContent);
            int courseId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_COURSE_ID)[0]);
        if (isOperationSuccessful) {
            requestContent.setPath(SUCCESSFUL_OPERATION_REDIRECT + courseId);
        }else {
            requestContent.setPath(FAIL_OPERATION_REDIRECT + courseId);
        }
            return REDIRECT;
    }
}
