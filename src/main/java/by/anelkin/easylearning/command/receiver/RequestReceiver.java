package by.anelkin.easylearning.command.receiver;

import by.anelkin.easylearning.command.Command;
import by.anelkin.easylearning.command.factory.CommandFactory;
import by.anelkin.easylearning.exception.RepositoryException;

import javax.servlet.http.HttpServletRequest;

import static by.anelkin.easylearning.command.factory.CommandFactory.*;


public class RequestReceiver {
    private Command command;
    private HttpServletRequest request;

    public RequestReceiver(CommandType commandType, HttpServletRequest request) {
        CommandFactory factory = new CommandFactory();
        this.command = factory.getCommand(commandType);
        this.request = request;
    }

    public String executeCommand() throws RepositoryException {
        return command.execute(request);
    }
}
