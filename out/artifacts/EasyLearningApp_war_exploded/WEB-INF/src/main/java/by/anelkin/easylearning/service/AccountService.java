package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import by.anelkin.easylearning.specification.account.SelectAccToPhotoApproveSpecification;
import by.anelkin.easylearning.specification.account.SelectAuthorOfCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectByAuthorIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Account.AccountType.GUEST;
import static by.anelkin.easylearning.entity.Mark.*;

public class AccountService {
    // FIXME: 7/20/2019 на относительный путь
    private static final String ACC_AVATAR_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/";
    private static final String ACC_AVATAR_LOCATION_TEMP = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/";
    private static final String URI_SPACE_REPRESENT = "%20";
    private static final String PATH_SPLITTER = "/";
    private static final String SESSION_ATTR_USER = "user";
    private static final String SESSION_ATTR_ROLE = "role";
    private static final String REQUEST_PARAM_PWD = "password";
    private static final String REQUEST_PARAM_UPDATED_PWD = "updated_password";
    private static final String REQUEST_PARAM_REPEATED_PWD = "repeated_password";
    private static final String PREVIOUS_OPERATION_MSG = "previous_operation_message";
    private static final String PWD_CHANGED_SUCCESSFULLY_MSG = "You password has been successfully changed!!!";
    private static final String PWD_NOT_CHANGED_MSG = "You password wasn't changed! Check inserted data!";
    private static final String ATTR_OPERATION_RESULT = "operation_result";
    private static final String ATTR_AVAILABLE_COURSES = "coursesAvailable";
    private static final String ATTR_LOGIN = "login";
    private static final String ATTR_WRONG_LOGIN_MSG = "wrong-login";
    private static final String ATTR_REQUESTED_AUTHOR_LOGIN = "requested_author_login";
    private static final String ATTR_REQUESTED_AUTHOR = "requested_author";
    private static final String ATTR_AUTHOR_COURSE_LIST = "author_course_list";
    private static final String ATTR_FILE_EXTENSION = "file_extension";
    private static final String ATTR_ACCS_TO_AVATAR_APPROVE = "acc_avatar_approve_list";
    private static final String ATTR_IS_AUTHOR_MARKED_ALREADY = "is_author_marked_already";
    private static final String ATTR_DESTROY_SESSION = "destroy_session";
    private static final String EMPTY_STRING = "";


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public boolean login(@NonNull SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> reqAttrs = requestContent.getSessionAttributes();
        AccRepository repository = new AccRepository();
        List<Account> accounts = null;
        try {
            accounts = repository.query(new SelectAccByLoginSpecification(requestContent.getRequestParameters().get("login")[0]));
            String[] expectedPassword = requestContent.getRequestParameters().get(REQUEST_PARAM_PWD);
            if (accounts.size() != 1 || !accounts.get(0).getPassword().equals(expectedPassword[0])) {
                return false;
            }
            Account account = accounts.get(0);
            AccountType role = account.getType();
            CourseRepository courseRepository = new CourseRepository();

            List<Course> courses = new ArrayList<>();
            switch (role) {
                case USER:
                    courses = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId()));
                    break;
                case AUTHOR:
                    courses = courseRepository.query(new SelectByAuthorIdSpecification(account.getId()));
                    break;
            }

            reqAttrs.put(SESSION_ATTR_USER, account);
            reqAttrs.put(ATTR_AVAILABLE_COURSES, courses);
            reqAttrs.put(SESSION_ATTR_ROLE, account.getType());
            (new MarkService()).insertMarkedCourseIdsIntoSession(requestContent);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }

        return true;
    }

    public boolean signUp(@NonNull SessionRequestContent requestContent) throws ServiceException {
        AccRepository repository = new AccRepository();
        String login = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        try {
            if (repository.query(new SelectAccByLoginSpecification(login)).size() != 0) {
                requestContent.getRequestAttributes().put(ATTR_WRONG_LOGIN_MSG, "true");
                return false;
            }
            Account account = new Account();
            initAccount(account, requestContent.getRequestParameters());
            repository.insert(account);
            // TODO: 7/6/2019  проверку на замену роли из браузера
            requestContent.getSessionAttributes().put(SESSION_ATTR_USER, account);
            requestContent.getSessionAttributes().put(SESSION_ATTR_ROLE, account.getType());
            // fixme: 7/5/2019 повторяющийся код:
            CourseRepository courseRepository = new CourseRepository();
            List<Course> courses = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId()));
            requestContent.getSessionAttributes().put(ATTR_AVAILABLE_COURSES, courses);
            (new MarkService()).insertMarkedCourseIdsIntoSession(requestContent);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    // TODO: 7/12/2019 переделать нормально!
    public void logOut(@NonNull SessionRequestContent requestContent) {
        HashMap<String, Object> sessionAttributes = requestContent.getSessionAttributes();
        sessionAttributes.put(ATTR_DESTROY_SESSION, true);
    }

    public void changeAccountPassword(SessionRequestContent requestContent) throws ServiceException {
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        String currPassword = reqParams.get(REQUEST_PARAM_PWD)[0];
        String updatedPassword = reqParams.get(REQUEST_PARAM_UPDATED_PWD)[0];
        String repeatedPassword = reqParams.get(REQUEST_PARAM_REPEATED_PWD)[0];
        AccRepository repository = new AccRepository();
        Account clone;
        try {
            clone = ((Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER)).clone();
        } catch (CloneNotSupportedException e) {
            throw new ServiceException(e);
        }
        if (clone.getPassword().equals(currPassword) && updatedPassword.equals(repeatedPassword)) {
            clone.setPassword(updatedPassword);
            try {
                repository.update(clone);
                requestContent.getSessionAttributes().put(SESSION_ATTR_USER, clone);
                requestContent.getRequestAttributes().put(PREVIOUS_OPERATION_MSG, PWD_CHANGED_SUCCESSFULLY_MSG);
                requestContent.getRequestAttributes().put(ATTR_OPERATION_RESULT, true);
            } catch (RepositoryException e) {
                // FIXME: 7/17/2019
                throw new RuntimeException(e);
            }
        } else {
            requestContent.getRequestAttributes().put(PREVIOUS_OPERATION_MSG, PWD_NOT_CHANGED_MSG);
            requestContent.getRequestAttributes().put(ATTR_OPERATION_RESULT, false);

        }
    }

    public void initApproveAccAvatarPage(SessionRequestContent requestContent) throws ServiceException {
        AccRepository repository = new AccRepository();
        try {
            List<Account> accounts = repository.query(new SelectAccToPhotoApproveSpecification());
            requestContent.getRequestAttributes().put(ATTR_ACCS_TO_AVATAR_APPROVE, accounts);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void addAccAvatarToReview(SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> sessionAttrs = requestContent.getSessionAttributes();
        Account account = (Account) sessionAttrs.get(SESSION_ATTR_USER);
        String fileExtension = (String) requestContent.getRequestAttributes().get(ATTR_FILE_EXTENSION);
        account.setUpdatePhotoPath(account.getId() + fileExtension);
        AccRepository repository = new AccRepository();
        try {
            repository.update(account);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void approveAccAvatar(SessionRequestContent requestContent) throws ServiceException {
        AccRepository repository = new AccRepository();
        String currLogin = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        Account currAccount;
        try {
            currAccount = repository.query(new SelectAccByLoginSpecification(currLogin)).get(0);
            String fileName = currAccount.getUpdatePhotoPath();

            try {
                // FIXME: 7/20/2019 по идее удалит дефолтный автар из папки если он стоит????
                File file = new File(ACC_AVATAR_LOCATION_TEMP + currAccount.getUpdatePhotoPath());
                Files.deleteIfExists(Paths.get(ACC_AVATAR_LOCATION + currAccount.getPathToPhoto()));
                file.renameTo(new File(ACC_AVATAR_LOCATION + currAccount.getPathToPhoto()));
                Files.deleteIfExists(Paths.get(ACC_AVATAR_LOCATION_TEMP + currAccount.getUpdatePhotoPath()));
            } catch (IOException e) {
                throw new ServiceException(e);
            }

            currAccount.setPathToPhoto(fileName);
            currAccount.setUpdatePhotoPath(EMPTY_STRING);
            repository.update(currAccount);
            requestContent.getRequestAttributes().put(ATTR_ACCS_TO_AVATAR_APPROVE
                    , repository.query(new SelectAccToPhotoApproveSpecification()));
        } catch (RepositoryException e) {
            // FIXME: 7/17/2019
            throw new ServiceException(e);
        }
    }

    public void declineAccAvatar(SessionRequestContent requestContent) throws ServiceException {
        AccRepository repository = new AccRepository();
        String currLogin = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        try {
            Account currAcc = repository.query(new SelectAccByLoginSpecification(currLogin)).get(0);
            Files.deleteIfExists(Paths.get(ACC_AVATAR_LOCATION_TEMP + currAcc.getUpdatePhotoPath()));
            currAcc.setUpdatePhotoPath("");
            repository.update(currAcc);
            requestContent.getRequestAttributes().put(ATTR_ACCS_TO_AVATAR_APPROVE
                    , repository.query(new SelectAccToPhotoApproveSpecification()));
        } catch (RepositoryException | IOException e) {
            throw new ServiceException(e);
        }
    }


    public void editAccountInfo(SessionRequestContent requestContent) throws ServiceException {
        AccRepository repository = new AccRepository();
        Account clone;
        try {
            clone = ((Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER)).clone();
            Map<String, String[]> requestParams = requestContent.getRequestParameters();
            clone.setLogin(requestParams.get("login")[0]);
            clone.setName(requestParams.get("name")[0]);
            clone.setSurname(requestParams.get("surname")[0]);
            clone.setEmail(requestParams.get("email")[0]);
            clone.setPhoneNumber(requestParams.get("phonenumber")[0]);
            clone.setAbout(requestParams.get("about")[0]);
            repository.update(clone);
            requestContent.getSessionAttributes().put(SESSION_ATTR_USER, clone);
        } catch (CloneNotSupportedException | RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    void refreshSessionAttributeUser(SessionRequestContent requestContent, Account account) throws ServiceException {
        AccRepository repository = new AccRepository();
        try {
            Account refreshedAcc = repository.query(new SelectAccByLoginSpecification(account.getLogin())).get(0);
            requestContent.getSessionAttributes().put(SESSION_ATTR_USER, refreshedAcc);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    void refreshSessionAttributeAvailableCourses(SessionRequestContent requestContent, Account account) throws ServiceException {
        CourseRepository repository = new CourseRepository();
        try {
            List<Course> courses = repository.query(new SelectCoursesPurchasedByUserSpecification(account.getId()));
            requestContent.getSessionAttributes().put(ATTR_AVAILABLE_COURSES, courses);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    Account takeAuthorOfCourse(int courseId) throws ServiceException {
        AccRepository repository = new AccRepository();
        List<Account> accounts = null;
        try {
            accounts = repository.query(new SelectAuthorOfCourseSpecification(courseId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        if (accounts.size() != 1) {
            throw new ServiceException("Account doesn't exists.");
        }
        return accounts.get(0);
    }

    public String pickTargetNameFromUri(String requestURI) {
        String[] parts = requestURI.split(PATH_SPLITTER);
        return parts[parts.length - 1].replaceAll(URI_SPACE_REPRESENT, " ");
    }

    public void initAuthorPage(@NonNull SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> reqAttrs = requestContent.getRequestAttributes();
        AccRepository repository = new AccRepository();
        MarkRepository markRepository = new MarkRepository();
        // TODO: 7/12/2019 подумать надо ли Optional
        String login = (String) reqAttrs.get(ATTR_REQUESTED_AUTHOR_LOGIN);
        List<Account> accounts = null;
        try {
            accounts = repository.query(new SelectAccByLoginSpecification(login));
            Account author = accounts.get(0);
            CourseRepository courseRepository = new CourseRepository();
            List<Course> courses = courseRepository.query(new SelectByAuthorIdSpecification(author.getId()));
            reqAttrs.put(ATTR_REQUESTED_AUTHOR, author);
            reqAttrs.put(ATTR_AUTHOR_COURSE_LIST, courses);

            List<Mark> accMarksForThisAuthor = markRepository.query(new SelectMarkByTargetIdSpecification(MarkType.AUTHOR_MARK, author.getId()));
            if (accMarksForThisAuthor.size() != 0){
                reqAttrs.put(ATTR_IS_AUTHOR_MARKED_ALREADY, true);
            }
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
    }


    // TODO: 7/12/2019 надо ли проверку на ноль??? nonnull?
    private void initAccount(Account account, Map<String, String[]> requestParams) {
        account.setLogin(requestParams.get("login")[0]);
        account.setPassword(requestParams.get("password")[0]);
        account.setName(requestParams.get("name")[0]);
        account.setSurname(requestParams.get("surname")[0]);
        account.setEmail(requestParams.get("email")[0]);
        account.setPhoneNumber(requestParams.get("phonenumber")[0]);
        account.setAbout(requestParams.get("about")[0]);
        // FIXME: 7/16/2019 работа с фото при регистрации
        try {
            account.setBirthDate(dateFormat.parse(requestParams.get("birthdate")[0]));
            account.setRegistrDate(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        account.setType(AccountType.valueOf(requestParams.get(SESSION_ATTR_ROLE)[0].toUpperCase()));
    }

}
