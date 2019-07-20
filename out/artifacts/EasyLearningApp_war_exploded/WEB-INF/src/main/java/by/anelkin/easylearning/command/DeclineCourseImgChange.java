package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;

public class DeclineCourseImgChange implements Command {
    @Override
    public SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        return null;
    }
}
