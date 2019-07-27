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
import by.anelkin.easylearning.validator.FormValidator;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Mark.*;

public class AccountService {
    // FIXME: 7/20/2019 на относительный путь
    private static final String CURRENT_ENCRYPTING = "SHA-256";

    private static final String ACC_AVATAR_LOCATION = "C:/temp/";
//    private static final String ACC_AVATAR_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/";
    private static final String ACC_AVATAR_LOCATION_TEMP = "C:/temp/";
//    private static final String ACC_AVATAR_LOCATION_TEMP = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/";
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
    private static final String ATTR_MESSAGE = "message";
    private static final String MSG_AVATAR_APPROVED= "Avatar changed successfully to account: ";
    private static final String MSG_AVATAR_DECLINED= "Avatar change was declined to account: ";
    private static final String EMPTY_STRING = "";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public boolean login(@NonNull SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> reqAttrs = requestContent.getSessionAttributes();
        AccRepository repository = new AccRepository();
        List<Account> accounts = null;
        try {
            accounts = repository.query(new SelectAccByLoginSpecification(requestContent.getRequestParameters().get("login")[0]));
            if (accounts.size() != 1) {
                return false;
            }
            String expectedPassword = requestContent.getRequestParameters().get(REQUEST_PARAM_PWD)[0];
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(CURRENT_ENCRYPTING);
                String saltedPass = expectedPassword + accounts.get(0).getPassSalt();
                String hashedPass = new String(messageDigest.digest(saltedPass.getBytes()));
                String realPassword = accounts.get(0).getPassword();
                if (!hashedPass.equals(realPassword)) {
                    return false;
                }
            } catch (NoSuchAlgorithmException e) {
                throw new ServiceException(e);
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
        FormValidator validator = new FormValidator();
        String login = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        try {
            if (repository.query(new SelectAccByLoginSpecification(login)).size() != 0) {
                requestContent.getRequestAttributes().put(ATTR_WRONG_LOGIN_MSG, "true");
                return false;
            }
            Account account = new Account();
            initAccount(account, requestContent.getRequestParameters());
            String pass = requestContent.getRequestParameters().get(REQUEST_PARAM_PWD)[0];
            String birthdate = requestContent.getRequestParameters().get("birthdate")[0];
            if (!validateAccFields(account, pass, birthdate)) {
                // TODO: 7/26/2019 сообщение на страницу
                requestContent.getRequestAttributes().put(ATTR_WRONG_LOGIN_MSG, "true");
                return false;
            }
            repository.insert(account);
            account = repository.query(new SelectAccByLoginSpecification(account.getLogin())).get(0);
            requestContent.getSessionAttributes().put(SESSION_ATTR_USER, account);
            requestContent.getSessionAttributes().put(SESSION_ATTR_ROLE, account.getType());
            CourseRepository courseRepository = new CourseRepository();
            List<Course> courses = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId()));
            requestContent.getSessionAttributes().put(ATTR_AVAILABLE_COURSES, courses);
            (new MarkService()).insertMarkedCourseIdsIntoSession(requestContent);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }


    public void changeAccountPassword(SessionRequestContent requestContent) throws ServiceException {
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        HashMap<String, Object> reqAttrs = requestContent.getRequestAttributes();
        AccRepository repository = new AccRepository();
        String currPassword = reqParams.get(REQUEST_PARAM_PWD)[0];
        String updatedPassword = reqParams.get(REQUEST_PARAM_UPDATED_PWD)[0];
        String repeatedPassword = reqParams.get(REQUEST_PARAM_REPEATED_PWD)[0];
        Account clone;

        String hashedPass;
        try {
            clone = ((Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER)).clone();
            MessageDigest messageDigest = MessageDigest.getInstance(CURRENT_ENCRYPTING);
            String saltedPass = currPassword + clone.getPassSalt();
            hashedPass = new String(messageDigest.digest(saltedPass.getBytes()));

            if (clone.getPassword().equals(hashedPass) && updatedPassword.equals(repeatedPassword)) {
                clone.setPassSalt(generateSaltForPassword());
                String updatedSaltedPass = updatedPassword + clone.getPassSalt();
                String updatedHashedPass = new String(messageDigest.digest(updatedSaltedPass.getBytes()));
                clone.setPassword(updatedHashedPass);
                repository.update(clone);

                requestContent.getSessionAttributes().put(SESSION_ATTR_USER, clone);
                reqAttrs.put(PREVIOUS_OPERATION_MSG, PWD_CHANGED_SUCCESSFULLY_MSG);
                reqAttrs.put(ATTR_OPERATION_RESULT, true);
            } else {
                reqAttrs.put(PREVIOUS_OPERATION_MSG, PWD_NOT_CHANGED_MSG);
                reqAttrs.put(ATTR_OPERATION_RESULT, false);
            }
        } catch (RepositoryException | NoSuchAlgorithmException | CloneNotSupportedException e) {
            throw new ServiceException(e);
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
                File file = new File(ACC_AVATAR_LOCATION_TEMP + currAccount.getUpdatePhotoPath());
                String previousAvatarPath = ACC_AVATAR_LOCATION + currAccount.getPathToPhoto();
                if (!previousAvatarPath.contains("default_acc_avatar")) {
                    Files.deleteIfExists(Paths.get(ACC_AVATAR_LOCATION + currAccount.getPathToPhoto()));
                }
                file.renameTo(new File(ACC_AVATAR_LOCATION + "resources/account_avatar"
                        + currAccount.getUpdatePhotoPath().substring(currAccount.getUpdatePhotoPath().lastIndexOf("/"))));
                Files.deleteIfExists(Paths.get(ACC_AVATAR_LOCATION_TEMP + currAccount.getUpdatePhotoPath()));
            } catch (IOException e) {
                throw new ServiceException(e);
            }

            currAccount.setPathToPhoto(fileName);
            currAccount.setUpdatePhotoPath(EMPTY_STRING);
            repository.update(currAccount);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, MSG_AVATAR_APPROVED + currAccount.getLogin());
            requestContent.getRequestAttributes().put(ATTR_ACCS_TO_AVATAR_APPROVE
                    , repository.query(new SelectAccToPhotoApproveSpecification()));
        } catch (RepositoryException e) {
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
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, MSG_AVATAR_DECLINED + currAcc.getLogin());
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
            if (accMarksForThisAuthor.size() != 0) {
                reqAttrs.put(ATTR_IS_AUTHOR_MARKED_ALREADY, true);
            }
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
    }


    // TODO: 7/12/2019 надо ли проверку на ноль??? nonnull?
    private void initAccount(Account account, Map<String, String[]> requestParams) throws ServiceException {
        account.setLogin(requestParams.get("login")[0]);
        account.setName(requestParams.get("name")[0]);
        account.setSurname(requestParams.get("surname")[0]);
        account.setEmail(requestParams.get("email")[0]);
        account.setPhoneNumber(requestParams.get("phonenumber")[0]);
        account.setAbout(requestParams.get("about")[0]);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(CURRENT_ENCRYPTING);
            account.setPassSalt(generateSaltForPassword());
            String saltedPass = requestParams.get("password")[0] + account.getPassSalt();
            String hashedPass = new String(messageDigest.digest(saltedPass.getBytes()));
            account.setPassword(hashedPass);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }

        account.setPathToPhoto("/resources/account_avatar/default_acc_avatar.png");
        account.setUpdatePhotoPath(EMPTY_STRING);
        try {
            account.setBirthDate(dateFormat.parse(requestParams.get("birthdate")[0]));
            account.setRegistrDate(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // FIXME: 7/26/2019 в отдельный метод
        String role = requestParams.get(SESSION_ATTR_ROLE)[0].toLowerCase();
        // FIXME: 7/26/2019 добавить французские
        if (role.equals("студент")) {
            role = "user";
        }
        if (role.equals("автор")) {
            role = "author";
        }
        try {
            account.setType(AccountType.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ServiceException(e);
        }
    }

    private String generateSaltForPassword() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int i = 0;
        while (i++ < 10) {
            char ch = (char) (random.nextInt(74) + 48);
            sb.append(ch);
        }
        return sb.toString();
    }

    private boolean validateAccFields(Account acc, String pass, String birthdate) {
        FormValidator val = new FormValidator();
        boolean isFieldsCorrect = val.validateLogin(acc.getLogin()) && val.validateName(acc.getName()) && val.validateSurName(acc.getSurname())
                && val.validateEmail(acc.getEmail()) && val.validateBirthDate(birthdate) && val.validatePassword(pass);
        if (!isFieldsCorrect) {
            return false;
        }
        boolean isAboutCorrect = acc.getAbout() == null || val.validateAccAboutLength(acc.getAbout());
        if (isAboutCorrect) {
            acc.setAbout(esqapeQuotes(acc.getAbout()));
        }
        boolean isPhoneCorrect = acc.getPhoneNumber() == null || val.validatePhone(acc.getPhoneNumber());
        return isAboutCorrect && isPhoneCorrect;
    }

    // TODO: 7/27/2019 общий метод, может в utils вынести в отдельный пакет?
    public String esqapeQuotes(String text) {
        String correctText = null;
        if (text != null) {
            correctText = text.replace("<", "&lt;");
            correctText = correctText.replace(">", "&gt;");
        }
        return correctText;
    }

}
