package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import by.anelkin.easylearning.specification.account.SelectByCourseIdSpecification;
import by.anelkin.easylearning.specification.course.SelectByAuthorIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import lombok.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static by.anelkin.easylearning.entity.Account.AccountType.GUEST;

public class AccountService {
    private static final String URI_SPACE_REPRESENT = "%20";
    private static final String PATH_SPLITTER = "/";
    private static final String SESSION_ATTR_USER = "user";
    private static final String REQUEST_PARAM_PREVIOUS_PWD = "password";
    private static final String REQUEST_PARAM_UPDATED_PWD = "updated_password";
    private static final String PREVIOUS_OPERATION_MSG = "previous_operation_message";
    private static final String PWD_CHANGED_SUCCESSFULLY_MSG = "You password has been successfully changed!!!";
    private static final String PWD_NOT_CHANGED_MSG = "You password wasn't changed! Password is not correct!";
    private static final String ATTR_OPERATION_RESULT = "operation_result";
    private static final String ATTR_FILE_NAME = "file_name";


    // TODO: 7/12/2019 надо ли из методов вынести в поле переменные класса? например репозитории?
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public boolean login(@NonNull SessionRequestContent requestContent) throws RepositoryException {
        AccRepository repository = new AccRepository();
        List<Account> accounts = repository.query(new SelectAccByLoginSpecification(requestContent.getRequestParameters().get("login")[0]));
        String[] expectedPassword = requestContent.getRequestParameters().get("password");
        if (accounts.size() != 1 || !accounts.get(0).getPassword().equals(expectedPassword[0])) {
            return false;
        }
        Account account = accounts.get(0);
        CourseRepository courseRepository = new CourseRepository();
        List<Course> courses = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId()));

        requestContent.getSessionAttributes().put("user", account);
        requestContent.getSessionAttributes().put("coursesAvailable", courses);
        requestContent.getSessionAttributes().put("role", account.getType());
        return true;
    }

    public boolean signUp(@NonNull SessionRequestContent requestContent) throws RepositoryException {
        AccRepository repository = new AccRepository();
        String login = requestContent.getRequestParameters().get("login")[0];
        if (repository.query(new SelectAccByLoginSpecification(login)).size() != 0) {
            requestContent.getRequestAttributes().put("wrong-login", "true");
            return false;
        }
        Account account = new Account();
        initAccount(account, requestContent.getRequestParameters());
        repository.insert(account);
        // TODO: 7/6/2019  проверку на замену роли из браузера
        requestContent.getSessionAttributes().put("user", account);
        requestContent.getSessionAttributes().put("role", account.getType());
        // fixme: 7/5/2019 повторяющийся код:
        CourseRepository courseRepository = new CourseRepository();
        List<Course> courses = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId()));
        // TODO: 7/6/2019  сделать размещение во времеменном атрибуте чтобы потом чистить?
        requestContent.getSessionAttributes().put("coursesAvailable", courses);
        return true;
    }

    // TODO: 7/12/2019 переделать нормально!
    public void logOut(@NonNull SessionRequestContent requestContent) {
        HashMap<String, Object> sessionAttributes = requestContent.getSessionAttributes();
        sessionAttributes.remove("user");
        sessionAttributes.remove("coursesAvailable");
        sessionAttributes.put("role", GUEST);
    }

    public void changeAccountPassword(SessionRequestContent requestContent) throws ServiceException {
        String currPassword = requestContent.getRequestParameters().get(REQUEST_PARAM_PREVIOUS_PWD)[0];
        AccRepository repository = new AccRepository();
        Account clone = null;
        try {
            clone = ((Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER)).clone();
        } catch (CloneNotSupportedException e) {
            throw new ServiceException(e);
        }
        if (clone.getPassword().equals(currPassword)) {
            String updatedPassword = requestContent.getRequestParameters().get(REQUEST_PARAM_UPDATED_PWD)[0];
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

    public void updateAccImage(SessionRequestContent requestContent) throws ServiceException {
        Map<String, Object> sessionAttrs = requestContent.getSessionAttributes();
        Account currAccount = (Account) sessionAttrs.get(SESSION_ATTR_USER);
        String fileName = (String) requestContent.getRequestAttributes().get(ATTR_FILE_NAME);
        currAccount.setPathToPhoto(fileName);

        AccRepository repository = new AccRepository();
        try {
            repository.update(currAccount);
            currAccount = repository.query(new SelectAccByLoginSpecification(currAccount.getLogin())).get(0);
            sessionAttrs.put(SESSION_ATTR_USER, currAccount);
        } catch (RepositoryException e) {
            // FIXME: 7/17/2019
            throw new ServiceException(e);
        }
    }

    public void editAccountInfo(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        AccRepository repository = new AccRepository();
        Account clone = null;
        try {
            clone = ((Account) requestContent.getSessionAttributes().get("user")).clone();
            Map<String, String[]> requestParams = requestContent.getRequestParameters();
            clone.setLogin(requestParams.get("login")[0]);
            clone.setName(requestParams.get("name")[0]);
            clone.setSurname(requestParams.get("surname")[0]);
            clone.setEmail(requestParams.get("email")[0]);
            clone.setPhoneNumber(requestParams.get("phonenumber")[0]);
            clone.setAbout(requestParams.get("about")[0]);
            repository.update(clone);
            requestContent.getSessionAttributes().put("user", clone);
        } catch (CloneNotSupportedException e) {
            throw new ServiceException(e);
        } catch (RepositoryException e) {
            // TODO: 7/12/2019 handle
            throw new RepositoryException();
        }
    }

    // TODO: 7/12/2019 исключения обработать нормально
    public Account takeAuthorOfCourse(int courseId) throws RepositoryException {
        AccRepository repository = new AccRepository();
        List<Account> accounts = repository.query(new SelectByCourseIdSpecification(courseId));
        if (accounts.size() != 1) {
            // TODO: 7/12/2019 handle
            throw new RepositoryException();
        }
        return accounts.get(0);
    }

    public String pickTargetNameFromUri(String requestURI) {
        String[] parts = requestURI.split(PATH_SPLITTER);
        return parts[parts.length - 1].replaceAll(URI_SPACE_REPRESENT, " ");
    }

    public void initAuthorPage(@NonNull SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        AccRepository repository = new AccRepository();
        // TODO: 7/12/2019 подумать надо ли Optional
        String login = (String) requestContent.getRequestAttributes().get("requested_author_login");
        List<Account> accounts = repository.query(new SelectAccByLoginSpecification(login));
        if (accounts.size() != 1) {
            throw new ServiceException("Author with login " + login + " is not exists!");
        }
        Account author = accounts.get(0);
        CourseRepository courseRepository = new CourseRepository();
        List<Course> courses = courseRepository.query(new SelectByAuthorIdSpecification(author.getId()));

        requestContent.getRequestAttributes().put("requested_author", author);
        requestContent.getRequestAttributes().put("author_course_list", courses);
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
        account.setType(Account.AccountType.valueOf(requestParams.get("role")[0].toUpperCase()));
    }

}
