package by.anelkin.easylearning.command;

import by.anelkin.easylearning.command.*;
import lombok.NonNull;

public class CommandFactory {
    public enum CommandType {
        LOGIN,
        SIGN_UP,
        SIGN_UP_NEW_USER,
        LOG_OUT,
        EDIT_USER_INFO,
        DEPOSIT_BY_CARD,
        CHANGE_PASSWORD

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
            case EDIT_USER_INFO:
                command = new EditUserInfoCommand();
                break;
            case DEPOSIT_BY_CARD:
                command = new DepositByCardCommand();
                break;
            case CHANGE_PASSWORD:
                command = new ChangePasswordCommand();
                break;

        }
        return command;
    }
}
