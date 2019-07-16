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

    public void editAccountInfo(SessionRequestContent requestContent) throws RepositoryException, ServiceException {
        AccRepository repository = new AccRepository();
        Account updatedAccount;
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
            trimPathToPhoto(clone);
        } catch (CloneNotSupportedException e) {
            throw new ServiceException(e);
        }
        try {
            repository.update(clone);
            updatedAccount = repository.query(new SelectAccByLoginSpecification(clone.getLogin())).get(0);
        } catch (RepositoryException e) {
            // TODO: 7/12/2019 handle
            throw new RepositoryException();
        }
        // query instead of using clone because of problems with path to photo when use cloning
        requestContent.getSessionAttributes().put("user", updatedAccount);
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

    // there must be only file-name in the bd instead of full path
    private void trimPathToPhoto(Account account) {
        String pathToPhoto = account.getPathToPhoto();
        String[] parts = pathToPhoto.split(PATH_SPLITTER);
        String photoFileName = parts[parts.length - 1];
        account.setPathToPhoto(photoFileName);
    }


}
