package by.anelkin.easylearning.command;

import by.anelkin.easylearning.command.*;
import lombok.NonNull;

public class CommandFactory {
    public enum CommandType {
        LOGIN,
        SIGN_UP,
        SIGN_UP_NEW_USER,
        LOG_OUT

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
                break;
            case LOG_OUT:
                command = new LogOutCommand();
                break;

        }
        return command;
    }
}
