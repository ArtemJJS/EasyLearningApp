package by.anelkin.easylearning.command;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Account.AccountType.*;

public class CommandFactory {
    public enum CommandType {
        LOGIN (GUEST),
        SIGN_UP_NEW_USER (GUEST),
        LOG_OUT (ADMIN, AUTHOR, USER, GUEST),
        EDIT_USER_INFO (ADMIN, AUTHOR, USER),
        DEPOSIT_BY_CARD (USER),
        CHANGE_PASSWORD (ADMIN, AUTHOR, USER),
        CASH_OUT (AUTHOR),
        APPROVE_COURSE (ADMIN),
        DECLINE_COURSE_APPROVAL (ADMIN),
        ADD_COURSE_TO_REVIEW (AUTHOR),
        APPROVE_AVATAR_CHANGE (ADMIN),
        DECLINE_AVATAR_CHANGE (ADMIN),
        CHANGE_ACC_IMG (ADMIN, AUTHOR, USER),
        CHANGE_COURSE_IMG (AUTHOR),
        APPROVE_COURSE_IMG_CHANGE (ADMIN),
        DECLINE_COURSE_IMG_CHANGE (ADMIN),
        SEARCH_COURSE (ADMIN, AUTHOR, USER, GUEST),
        BUY_WITH_CARD (USER),
        BUY_FROM_BALANCE (USER),
        MARK_COURSE (USER),
        MARK_AUTHOR (USER),
        CHANGE_LANG (ADMIN, AUTHOR, USER, GUEST),
        RESTORE_PASSWORD(GUEST),
//        GO_CHANGE_PASS(GUEST),
        CHANGE_FORGOTTEN_PASSWORD(GUEST);




        private List<AccountType> accessTypes = new ArrayList<>();
        CommandType(@NonNull AccountType... types) {
            accessTypes.addAll(Arrays.asList(types));
        }

        public List<AccountType> getAccessTypes() {
            return accessTypes;
        }
    }

    public Command getCommand(@NonNull CommandType type) {
        Command command = null;
        switch (type) {
            case LOGIN:
                command = new LoginCommand();
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
            case MARK_AUTHOR:
                command = new MarkAuthorCommand();
                break;
            case CHANGE_LANG:
                command = new ChangeLangCommand();
                break;
            case RESTORE_PASSWORD:
                command = new RestorePasswordCommand();
                break;
            case CHANGE_FORGOTTEN_PASSWORD:
                command = new ChangeForgottenPassCommand();
                break;


        }
        return command;
    }
}
