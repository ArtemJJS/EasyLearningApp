package by.anelkin.easylearning.command;

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
        CHANGE_COURSE_IMG,
        APPROVE_COURSE_IMG_CHANGE,
        DECLINE_COURSE_IMG_CHANGE,
        SEARCH_COURSE,
        BUY_WITH_CARD,
        BUY_FROM_BALANCE,
        MARK_COURSE

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
            case APPROVE_COURSE_IMG_CHANGE:
                command = new ApproveCourseImgChangeCommand();
                break;
            case DECLINE_COURSE_IMG_CHANGE:
                command = new DeclineCourseImgChange();
                break;
            case SEARCH_COURSE:
                command = new SearchCourseCommand();
                break;
            case BUY_WITH_CARD:
                command = new BuyWithCardCommand();
                break;
            case BUY_FROM_BALANCE:
                command = new BuyFromBalanceCommand();
                break;
            case MARK_COURSE:
                command = new MarkCourseCommand();
                break;

        }
        return command;
    }
}
