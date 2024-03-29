package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;

public interface Command {
    SessionRequestContent.ResponseType execute(SessionRequestContent requestContent) throws ServiceException;
}
