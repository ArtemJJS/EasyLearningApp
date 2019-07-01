package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exeption.RepositoryException;

import javax.servlet.http.HttpServletRequest;

import static by.anelkin.easylearning.command.CommandFactory.*;


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
