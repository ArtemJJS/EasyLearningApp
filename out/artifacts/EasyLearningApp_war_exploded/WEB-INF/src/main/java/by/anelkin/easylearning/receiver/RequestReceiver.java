package by.anelkin.easylearning.receiver;

import by.anelkin.easylearning.command.Command;
import by.anelkin.easylearning.command.CommandFactory;
import by.anelkin.easylearning.exception.ServiceException;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.*;


public class RequestReceiver {
    private Command command;
    private SessionRequestContent requestContent;

    public RequestReceiver(CommandType commandType, SessionRequestContent requestContent) {
        CommandFactory factory = new CommandFactory();
        this.command = factory.getCommand(commandType);
        this.requestContent = requestContent;
    }

    public ResponseType executeCommand() throws ServiceException {
       return command.execute(requestContent);
    }
}
