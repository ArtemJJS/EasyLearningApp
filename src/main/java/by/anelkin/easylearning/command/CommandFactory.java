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
        CHANGE_PASSWORD,
        CASH_OUT,
        APPROVE_COURSE,
        DECLINE_COURSE_APPROVAL,
        ADD_COURSE_TO_REVIEW,
        APPROVE_AVATAR_CHANGE,
        DECLINE_AVATAR_CHANGE,
        CHANGE_ACC_IMG,
        CHANGE_COURSE_IMG

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
            case CASH_OUT:
                command = new CashOutCommand();
                break;
            case APPROVE_COURSE:
                command = new ApproveCourseCommand();
                break;
            case DECLINE_COURSE_APPROVAL:
                command = new DeclineCourseApprovalCommand();
                break;
            case ADD_COURSE_TO_REVIEW:
                command = new AddCourseToReviewCommand();
                break;
            case APPROVE_AVATAR_CHANGE:
                command = new ApproveAccAvatarCommand();
                break;
            case DECLINE_AVATAR_CHANGE:
                command = new DeclineAccAvatarCommand();
                break;
            case CHANGE_ACC_IMG:
                command = new ChangeAccImgCommand();
                break;
            case CHANGE_COURSE_IMG:
                command = new ChangeCourseImgCommand();
                break;

        }
        return command;
    }
}
