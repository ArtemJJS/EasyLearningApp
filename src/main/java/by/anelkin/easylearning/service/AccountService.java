package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.entity.RestorePassRequest;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.repository.RestorePassRequestRepository;
import by.anelkin.easylearning.specification.account.SelectAccByChangePassUuidSpecification;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import by.anelkin.easylearning.specification.account.SelectAccToPhotoApproveSpecification;
import by.anelkin.easylearning.specification.account.SelectAuthorOfCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectByAuthorIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseRecommendedSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import by.anelkin.easylearning.validator.FormValidator;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.entity.Mark.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
public class AccountService {
    private static final int AMOUNT_COURSES_RECOMMENDED = 6;
    private static final String PATH_RELATIVE_TO_CHANGE_FORGOTTEN_PASS_PAGE = "/change-forgotten-pass?&uuid=";
    private static final String DEFAULT_ACC_AVATAR = "default_acc_avatar";
    private static final String CURRENT_ENCRYPTING = "SHA-256";

    private static final String PROP_FILE_FOLDER = "file_folder";
    private static final String PROP_EMAIL_SUBJECT = "email_subject";
    private static final String PROP_EMAIL_ADDRESS = "mail.user.name";
    private static final String PROP_EMAIL_MESSAGE = "email_message";
    private static final String PROP_EMAIL_PASSWORD = "mail.user.password";
    private static final String RESOURCE_BUNDLE_BASE = "text_resources";
    private static final String FILE_STORAGE_BUNDLE_BASE = "file_storage";
    private static final String MAIL_INFO_PROPERTIES = "mail_info.properties";
    private static final String BUNDLE_INCORRECT_DATA = "msg.incorrect_data";
    private static final String BUNDLE_INCORRECT_AGE = "msg.incorrect_date_of_birth";
    private static final String BUNDLE_CHANGE_AVATAR_REQUEST = "msg.change_avatar_request_was_sent";
    private static final String BUNDLE_INCORRECT_REPEATED_PASS_OR_PATTERN = "msg.incorrect_repeated_password_or_pattern";
    private static final String BUNDLE_LOGIN_NOT_EXISTS = "msg.login_not_exists";
    private static final String BUNDLE_EMAIL_SENT = "msg.email_sent";

    private static final String PREVIOUS_OPERATION_MSG = "previous_operation_message";
    private static final String PWD_CHANGED_SUCCESSFULLY_MSG = "You password has been successfully changed!!!";
    private static final String PWD_NOT_CHANGED_MSG = "You password wasn't changed! Check inserted data!";
    private static final String MSG_AVATAR_APPROVED = "Avatar changed successfully to account: ";
    private static final String MSG_AVATAR_DECLINED = "Avatar change was declined to account: ";


    public void changeForgottenPass(SessionRequestContent requestContent) throws ServiceException {
        FormValidator val = new FormValidator();
        Locale locale = new CourseService().takeLocaleFromSession(requestContent);
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale);
        HashMap<String, Object> reqAttrs = requestContent.getRequestAttributes();
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        AccRepository repository = new AccRepository();
        RestorePassRequestRepository restorePassRequestRepo = new RestorePassRequestRepository();
        try {
            String uuid = reqParams.get(ATTR_UUID)[0];
            List<Account> accounts = repository.query(new SelectAccByChangePassUuidSpecification(uuid));
            if (accounts.size() == 0) {
                reqAttrs.put(ATTR_MESSAGE, rb.getString(BUNDLE_INCORRECT_DATA));
                return;
            }
            Account account = accounts.get(0);
            String updatedPass = reqParams.get(ATTR_UPDATED_PWD)[0];
            String repeatedPass = reqParams.get(ATTR_REPEATED_PWD)[0];
            if (!updatedPass.equals(repeatedPass) || !val.validatePassword(updatedPass)) {
                reqAttrs.put(ATTR_MESSAGE, rb.getString(BUNDLE_INCORRECT_REPEATED_PASS_OR_PATTERN));
                return;
            }

            MessageDigest messageDigest = MessageDigest.getInstance(CURRENT_ENCRYPTING);
            String salt = account.getPassSalt();
            String updatedSaltedPass = updatedPass + salt;
            String updatedHashedPass = new String(messageDigest.digest(updatedSaltedPass.getBytes()));
            account.setPassword(updatedHashedPass);
            repository.update(account);
            reqAttrs.put(ATTR_MESSAGE, PWD_CHANGED_SUCCESSFULLY_MSG);
            restorePassRequestRepo.delete(new RestorePassRequest(account.getId(), uuid));
        } catch (RepositoryException | IndexOutOfBoundsException | NoSuchAlgorithmException e) {
            log.error(e);
            throw new ServiceException(e);
        }
    }

    public void restorePassword(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = new CourseService().takeLocaleFromSession(requestContent);
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale);
        AccRepository repo = new AccRepository();
        HashMap<String, Object> reqAttrs = requestContent.getRequestAttributes();
        String login = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        try {
            List<Account> accounts = repo.query(new SelectAccByLoginSpecification(login));
            if (accounts.size() == 0) {
                reqAttrs.put(ATTR_MESSAGE, rb.getString(BUNDLE_LOGIN_NOT_EXISTS));
                return;
            }
            Account account = accounts.get(0);
            String email = account.getEmail();
            String uuid = UUID.randomUUID().toString();
            sendConfirmationEmail(email, requestContent.getRequestFullReferer(), uuid);
            new RestorePassRequestRepository().insert(new RestorePassRequest(account.getId(), uuid));
            reqAttrs.put(ATTR_MESSAGE, rb.getString(BUNDLE_EMAIL_SENT));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        } catch (IOException | MessagingException e) {
            log.error(e);
            throw new ServiceException("Error while sending confirmation email... Try again later.");
        }
    }

    public boolean login(@NonNull SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> sessionAttrs = requestContent.getSessionAttributes();
        AccRepository repository = new AccRepository();
        List<Account> accounts = null;
        try {
            accounts = repository.query(new SelectAccByLoginSpecification(requestContent.getRequestParameters().get(ATTR_LOGIN)[0]));
            if (accounts.size() != 1) {
                return false;
            }
            String expectedPassword = requestContent.getRequestParameters().get(ATTR_PWD)[0];
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(CURRENT_ENCRYPTING);
                String saltedPass = expectedPassword + accounts.get(0).getPassSalt();
                String hashedPass = new String(messageDigest.digest(saltedPass.getBytes()));
                String realPassword = accounts.get(0).getPassword();
                if (!hashedPass.equals(realPassword)) {
                    return false;
                }
            } catch (NoSuchAlgorithmException e) {
                log.error(e);
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
            List<Course> recommendedCourses = courseRepository.query(new SelectCourseRecommendedSpecification(AMOUNT_COURSES_RECOMMENDED, account.getId()));
            sessionAttrs.put(ATTR_USER, account);
            sessionAttrs.put(ATTR_AVAILABLE_COURSES, courses);
            sessionAttrs.put(ATTR_ROLE, account.getType());
            sessionAttrs.put(ATTR_RECOMMENDED_COURSES, recommendedCourses);
            (new MarkService()).insertMarkedCourseIdsIntoSession(requestContent);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }


    public boolean signUp(@NonNull SessionRequestContent requestContent) throws ServiceException {
        Locale locale = (new CourseService()).takeLocaleFromSession(requestContent);
        FormValidator validator = new FormValidator();
        AccRepository repository = new AccRepository();
        HashMap<String, Object> sessionAttrs = requestContent.getSessionAttributes();
        String login = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        try {
            if (repository.query(new SelectAccByLoginSpecification(login)).size() != 0) {
                requestContent.getRequestAttributes().put(ATTR_WRONG_LOGIN_MSG, "true");
                return false;
            }
            Account account = new Account();
            initAccount(account, requestContent.getRequestParameters());
            String pass = requestContent.getRequestParameters().get(ATTR_PWD)[0];
            String birthdate = requestContent.getRequestParameters().get(ATTR_BIRTHDATE)[0];
            if (!validator.validateBirthDate(birthdate)) {
                String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_INCORRECT_AGE);
                requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
                return false;
            }
            if (!validateAccFields(account, pass, birthdate)) {
                String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_INCORRECT_DATA);
                requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
                return false;
            }
            repository.insert(account);
            account = repository.query(new SelectAccByLoginSpecification(account.getLogin())).get(0);
            sessionAttrs.put(ATTR_USER, account);
            sessionAttrs.put(ATTR_ROLE, account.getType());
            CourseRepository courseRepository = new CourseRepository();
            List<Course> courses = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId()));
            sessionAttrs.put(ATTR_AVAILABLE_COURSES, courses);
            List<Course> recommendedCourses = courseRepository.query(new SelectCourseRecommendedSpecification(AMOUNT_COURSES_RECOMMENDED, account.getId()));
            sessionAttrs.put(ATTR_RECOMMENDED_COURSES, recommendedCourses);
            (new MarkService()).insertMarkedCourseIdsIntoSession(requestContent);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }


    public void changeAccountPassword(SessionRequestContent requestContent) throws ServiceException {
        FormValidator validator = new FormValidator();
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        HashMap<String, Object> reqAttrs = requestContent.getRequestAttributes();
        AccRepository repository = new AccRepository();
        String currPassword = reqParams.get(ATTR_PWD)[0];
        String updatedPassword = reqParams.get(ATTR_UPDATED_PWD)[0];
        String repeatedPassword = reqParams.get(ATTR_REPEATED_PWD)[0];
        boolean isUpdatedPwdCorrect = validator.validatePassword(updatedPassword);
        Account clone;
        String hashedPass;
        try {
            clone = ((Account) requestContent.getSessionAttributes().get(ATTR_USER)).clone();
            MessageDigest messageDigest = MessageDigest.getInstance(CURRENT_ENCRYPTING);
            String saltedPass = currPassword + clone.getPassSalt();
            hashedPass = new String(messageDigest.digest(saltedPass.getBytes()));

            if (clone.getPassword().equals(hashedPass) && updatedPassword.equals(repeatedPassword)
                    && isUpdatedPwdCorrect) {
                clone.setPassSalt(generateSaltForPassword());
                String updatedSaltedPass = updatedPassword + clone.getPassSalt();
                String updatedHashedPass = new String(messageDigest.digest(updatedSaltedPass.getBytes()));
                clone.setPassword(updatedHashedPass);
                repository.update(clone);

                requestContent.getSessionAttributes().put(ATTR_USER, clone);
                reqAttrs.put(PREVIOUS_OPERATION_MSG, PWD_CHANGED_SUCCESSFULLY_MSG);
                reqAttrs.put(ATTR_OPERATION_RESULT, true);
            } else {
                reqAttrs.put(PREVIOUS_OPERATION_MSG, PWD_NOT_CHANGED_MSG);
                reqAttrs.put(ATTR_OPERATION_RESULT, false);
            }
        } catch (RepositoryException | NoSuchAlgorithmException | CloneNotSupportedException e) {
            log.error(e);
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
        Account account = (Account) sessionAttrs.get(ATTR_USER);
        String fileExtension = (String) requestContent.getRequestAttributes().get(ATTR_FILE_EXTENSION);
        account.setUpdatePhotoPath(account.getId() + fileExtension);
        AccRepository repository = new AccRepository();
        try {
            repository.update(account);
            Locale locale = (new CourseService()).takeLocaleFromSession(requestContent);
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_CHANGE_AVATAR_REQUEST);
            requestContent.getRequestAttributes().put(PREVIOUS_OPERATION_MSG, message);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void approveAccAvatar(SessionRequestContent requestContent) throws ServiceException {
        String fileStorage = ResourceBundle.getBundle(FILE_STORAGE_BUNDLE_BASE).getString(PROP_FILE_FOLDER);
        AccRepository repository = new AccRepository();
        String currLogin = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        Account currAccount;
        try {
            currAccount = repository.query(new SelectAccByLoginSpecification(currLogin)).get(0);
            String fileName = currAccount.getUpdatePhotoPath();

            try {
                File file = new File(fileStorage + currAccount.getUpdatePhotoPath());
                String previousAvatarPath = fileStorage + currAccount.getPathToPhoto();
                if (!previousAvatarPath.contains(DEFAULT_ACC_AVATAR)) {
                    Files.deleteIfExists(Paths.get(fileStorage + currAccount.getPathToPhoto()));
                }
                file.renameTo(new File(fileStorage + "resources/account_avatar"
                        + currAccount.getUpdatePhotoPath().substring(currAccount.getUpdatePhotoPath().lastIndexOf(PATH_SPLITTER))));
                Files.deleteIfExists(Paths.get(fileStorage + currAccount.getUpdatePhotoPath()));
            } catch (IOException e) {
                log.error(e);
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
        String fileStorage = ResourceBundle.getBundle(FILE_STORAGE_BUNDLE_BASE).getString(PROP_FILE_FOLDER);
        AccRepository repository = new AccRepository();
        String currLogin = requestContent.getRequestParameters().get(ATTR_LOGIN)[0];
        try {
            Account currAcc = repository.query(new SelectAccByLoginSpecification(currLogin)).get(0);
            Files.deleteIfExists(Paths.get(fileStorage + currAcc.getUpdatePhotoPath()));
            currAcc.setUpdatePhotoPath("");
            repository.update(currAcc);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, MSG_AVATAR_DECLINED + currAcc.getLogin());
            requestContent.getRequestAttributes().put(ATTR_ACCS_TO_AVATAR_APPROVE
                    , repository.query(new SelectAccToPhotoApproveSpecification()));
        } catch (RepositoryException | IOException e) {
            log.error(e);
            throw new ServiceException(e);
        }
    }


    public boolean editAccountInfo(SessionRequestContent requestContent) throws ServiceException {
        FormValidator val = new FormValidator();
        Map<String, String[]> requestParams = requestContent.getRequestParameters();
        AccRepository repository = new AccRepository();
        Account clone;
        try {
            clone = ((Account) requestContent.getSessionAttributes().get(ATTR_USER)).clone();
            clone.setName(requestParams.get(ATTR_NAME)[0]);
            clone.setSurname(requestParams.get(ATTR_SURNAME)[0]);
            clone.setEmail(requestParams.get(ATTR_EMAIL)[0]);
            clone.setPhoneNumber(requestParams.get(ATTR_PHONENUMBER)[0]);
            clone.setAbout(requestParams.get(ATTR_ABOUT)[0]);
            if (val.validateLogin(clone.getLogin()) && val.validateName(clone.getName()) && val.validateSurName(clone.getSurname())
                    && val.validateEmail(clone.getEmail()) && val.validateAccAboutLength(clone.getAbout())
                    && val.validatePhone(clone.getPhoneNumber())) {
                clone.setAbout(escapeQuotes(clone.getAbout()));
                repository.update(clone);
                requestContent.getSessionAttributes().put(ATTR_USER, clone);
                return true;
            }
        } catch (CloneNotSupportedException | RepositoryException | NullPointerException e) {
            log.error(e);
            throw new ServiceException(e);
        }
        return false;
    }

    void refreshSessionAttributeUser(SessionRequestContent requestContent, Account account) throws ServiceException {
        AccRepository repository = new AccRepository();
        try {
            Account refreshedAcc = repository.query(new SelectAccByLoginSpecification(account.getLogin())).get(0);
            requestContent.getSessionAttributes().put(ATTR_USER, refreshedAcc);
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
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }catch (NullPointerException e){
            log.error(e);
            throw new ServiceException(e);
        }
    }

    private void initAccount(Account account, Map<String, String[]> requestParams) throws ServiceException {
        account.setLogin(requestParams.get(ATTR_LOGIN)[0]);
        account.setName(requestParams.get(ATTR_NAME)[0]);
        account.setSurname(requestParams.get(ATTR_SURNAME)[0]);
        account.setEmail(requestParams.get(ATTR_EMAIL)[0]);
        account.setPhoneNumber(requestParams.get(ATTR_PHONENUMBER)[0]);
        account.setAbout(requestParams.get(ATTR_ABOUT)[0]);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(CURRENT_ENCRYPTING);
            account.setPassSalt(generateSaltForPassword());
            String saltedPass = requestParams.get(ATTR_PWD)[0] + account.getPassSalt();
            String hashedPass = new String(messageDigest.digest(saltedPass.getBytes()));
            account.setPassword(hashedPass);
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
            throw new ServiceException(e);
        }


        account.setPathToPhoto("/resources/account_avatar/default_acc_avatar.png");
        account.setUpdatePhotoPath(EMPTY_STRING);
        try {
            account.setBirthDate(dateFormat.parse(requestParams.get(ATTR_BIRTHDATE)[0]));
            account.setRegistrDate(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            log.error(e);
            throw new ServiceException(e);
        }

        String role = requestParams.get(ATTR_ROLE)[0].toLowerCase();
        if (role.equals("студент") || role.equals("usager")) {
            role = "user";
        }
        if (role.equals("автор") || role.equals("auteur")) {
            role = "author";
        }
        try {
            account.setType(AccountType.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.error(e);
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
            acc.setAbout(escapeQuotes(acc.getAbout()));
        }
        boolean isPhoneCorrect = acc.getPhoneNumber() == null || val.validatePhone(acc.getPhoneNumber());
        return isAboutCorrect && isPhoneCorrect;
    }

    String escapeQuotes(String text) {
        String correctText = null;
        if (text != null) {
            correctText = text.replace("<", "&lt;");
            correctText = correctText.replace(">", "&gt;");
        }
        return correctText;
    }

    private void sendConfirmationEmail(String emailTo, String referer, String uuid) throws IOException, MessagingException {
        String changePassLink = referer.substring(0, referer.lastIndexOf(PATH_SPLITTER));
        changePassLink += PATH_RELATIVE_TO_CHANGE_FORGOTTEN_PASS_PAGE + uuid;

        Properties properties = new Properties();
        properties.load(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(MAIL_INFO_PROPERTIES)));

        Session mailSession = Session.getDefaultInstance(properties);
        Message message = new MimeMessage(mailSession);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        message.setSubject(properties.getProperty(PROP_EMAIL_SUBJECT));
        message.setText(properties.getProperty(PROP_EMAIL_MESSAGE) + changePassLink);
        Transport transport = mailSession.getTransport();
        transport.connect(properties.getProperty(PROP_EMAIL_ADDRESS), properties.getProperty(PROP_EMAIL_PASSWORD));
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

}
