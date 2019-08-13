package by.anelkin.easylearning.receiver;

import by.anelkin.easylearning.command.Command;
import by.anelkin.easylearning.command.CommandFactory;
import by.anelkin.easylearning.exception.ServiceException;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.*;

/**
 * class to link {@link Command} with {@link javax.servlet.http.HttpServlet}
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
public class RequestReceiver {
    private Command command;
    private SessionRequestContent requestContent;

    public RequestReceiver(CommandType commandType, SessionRequestContent requestContent) {
        CommandFactory factory = new CommandFactory();
        this.command = factory.getCommand(commandType);
        this.requestContent = requestContent;
    }

    /**
     * @return {@link ResponseType}
     * @throws ServiceException if it came from {@link Command}
     */
    public ResponseType executeCommand() throws ServiceException {
       return command.execute(requestContent);
    }
}
