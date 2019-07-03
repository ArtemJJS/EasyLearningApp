package by.anelkin.easylearning.command.factory;

import by.anelkin.easylearning.command.Command;
import by.anelkin.easylearning.command.LoginCommand;
import by.anelkin.easylearning.command.SignUpCommand;
import by.anelkin.easylearning.command.SignUpNewUserCommand;
import lombok.NonNull;

public class CommandFactory {
    public enum CommandType {
        LOGIN,
        SIGN_UP,
        SIGN_UP_NEW_USER
    }

    public Command getCommand(@NonNull CommandType type) {
        Command command = null;
        switch (type) {
            case LOGIN:
                command = new LoginCommand();
                break;
            case SIGN_UP:
                command = new SignUpCommand();
                break;
            case SIGN_UP_NEW_USER:
                command = new SignUpNewUserCommand();
        }
        return command;
    }
}
