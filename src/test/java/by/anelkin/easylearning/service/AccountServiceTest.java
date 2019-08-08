package by.anelkin.easylearning.service;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.specification.account.SelectAccByIdSpecification;
import by.anelkin.easylearning.specification.account.SelectAllAccountSpecification;
import by.anelkin.easylearning.specification.course.SelectByAuthorIdSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.testng.Assert.*;

public class AccountServiceTest {
    private static final String ATTR_USER = "user";
    private static final String ATTR_ROLE = "role";
    private static final String ATTR_PWD = "password";
    private static final String ATTR_UPDATED_PWD = "updated_password";
    private static final String ATTR_REPEATED_PWD = "repeated_password";
    private static final String ATTR_LOGIN = "login";
    private static final String ATTR_BIRTHDATE = "birthdate";
    private static final String ATTR_REQUESTED_AUTHOR_LOGIN = "requested_author_login";
    private static final String ATTR_REQUESTED_AUTHOR = "requested_author";
    private static final String ATTR_AUTHOR_COURSE_LIST = "author_course_list";
    private static final String ATTR_FILE_EXTENSION = "file_extension";
    private static final String ATTR_ACCS_TO_AVATAR_APPROVE = "acc_avatar_approve_list";
    private static final String ATTR_UUID = "uuid";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_SURNAME = "surname";
    private static final String ATTR_EMAIL = "email";
    private static final String ATTR_PHONENUMBER = "phonenumber";
    private static final String ATTR_ABOUT = "about";

    @Language("sql")
    private static final String CREATE_TABLES = "call createTables()";
    @Language("sql")
    private static final String DROP_TABLES = "call dropTables()";

    private SessionRequestContent requestContent;
    private HashMap<String, Object> sessionAttrs;
    private Map<String, String[]> reqParams;
    private HashMap<String, Object> reqAttrs;

    private CourseRepository courseRepo = new CourseRepository();
    private AccRepository accRepo = new AccRepository();

    private AccountService accService = new AccountService();
    private ConnectionPool pool = ConnectionPool.getInstance();


//    @AfterClass
//    public void tearDownTestClass(){
//        pool.closePool();
//    }

    @BeforeMethod
    public void setUp() {
        requestContent = new SessionRequestContent();
        sessionAttrs = requestContent.getSessionAttributes();
        reqParams = requestContent.getRequestParameters();
        reqAttrs = requestContent.getRequestAttributes();

        sessionAttrs.put("locale", Locale.US);
    }

    @Test
    public void AccountService_ChangeForgottenPass_ShouldChangeAccPwd() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            Account accBefore = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            String previousPwd = accBefore.getPassword();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_UUID, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"12345"});

            accService.changeForgottenPass(requestContent);

            String updatedPwd;
            String query = "select acc_password from testdb.account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                updatedPwd = resultSet.getString("acc_password");
            }

            boolean isPwdSame = previousPwd.equals(updatedPwd);

            assertFalse(isPwdSame);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void AccountService_ChangeForgottenPass_ShouldDoNothingIfUuidIncorrect() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            Account accBefore = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            String previousPwd = accBefore.getPassword();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_UUID, new String[]{"wrong uuid"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"12345"});

            accService.changeForgottenPass(requestContent);

            String updatedPwd;
            String query = "select acc_password from testdb.account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                updatedPwd = resultSet.getString("acc_password");
            }

            boolean isPwdSame = previousPwd.equals(updatedPwd);

            assertTrue(isPwdSame);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void AccountService_ChangeForgottenPass_ShouldDoNothingIfRepeatedPwdIncorrect() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            Account accBefore = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            String previousPwd = accBefore.getPassword();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_UUID, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"wrong password"});

            accService.changeForgottenPass(requestContent);

            String updatedPwd;
            String query = "select acc_password from testdb.account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                updatedPwd = resultSet.getString("acc_password");
            }

            boolean isPwdSame = previousPwd.equals(updatedPwd);

            assertTrue(isPwdSame);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_Login_ShouldTakeCorrectAccount() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            reqParams.put(ATTR_UUID, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"12345"});
            accService.changeForgottenPass(requestContent);

            reqParams.put(ATTR_PWD, new String[]{"12345"});
            reqParams.put(ATTR_LOGIN, new String[]{"admin"});

            accService.login(requestContent);

            Account expected = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            Account actual = (Account) sessionAttrs.get(ATTR_USER);

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_Login_ShouldNotTakeAccIfPwdIncorrect() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            reqParams.put(ATTR_UUID, new String[]{"wrong uuid"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"12345"});
            accService.changeForgottenPass(requestContent);

            reqParams.put(ATTR_PWD, new String[]{"wrong pass"});
            reqParams.put(ATTR_LOGIN, new String[]{"admin"});

            accService.login(requestContent);

            Account expected = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            Account actual = (Account) sessionAttrs.get(ATTR_USER);

            assertNotEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void AccountService_SignUp_ShouldInsertNewAcc() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = accRepo.query(new SelectAllAccountSpecification()).size() + 1;

            reqParams.put(ATTR_ROLE, new String[]{"author"});
            reqParams.put(ATTR_PWD, new String[]{"12345"});
            reqParams.put(ATTR_LOGIN, new String[]{"created"});
            reqParams.put(ATTR_BIRTHDATE, new String[]{"2000-01-01"});
            reqParams.put(ATTR_NAME, new String[]{"tommy"});
            reqParams.put(ATTR_SURNAME, new String[]{"jerry"});
            reqParams.put(ATTR_EMAIL, new String[]{"123123@tra.ta"});
            reqParams.put(ATTR_PHONENUMBER, new String[]{"+12345678"});
            reqParams.put(ATTR_ABOUT, new String[]{""});

            accService.signUp(requestContent);

            int actual = accRepo.query(new SelectAllAccountSpecification()).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_SignUp_ShouldNotInsertNewAccIfLoginExists() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = accRepo.query(new SelectAllAccountSpecification()).size() + 1;

            reqParams.put(ATTR_ROLE, new String[]{"author"});
            reqParams.put(ATTR_PWD, new String[]{"12345"});
            reqParams.put(ATTR_LOGIN, new String[]{"admin"});
            reqParams.put(ATTR_BIRTHDATE, new String[]{"2000-01-01"});
            reqParams.put(ATTR_NAME, new String[]{"tommy"});
            reqParams.put(ATTR_SURNAME, new String[]{"jerry"});
            reqParams.put(ATTR_EMAIL, new String[]{"123123@tra.ta"});
            reqParams.put(ATTR_PHONENUMBER, new String[]{"+12345678"});
            reqParams.put(ATTR_ABOUT, new String[]{""});

            accService.signUp(requestContent);

            int actual = accRepo.query(new SelectAllAccountSpecification()).size();

            assertNotEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void AccountService_ChangeAccountPassword_ShouldChangeAccPwd() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            Account accBefore = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            String previousPwd = accBefore.getPassword();

            reqParams.put(ATTR_UUID, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"12345"});
            accService.changeForgottenPass(requestContent);


            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_PWD, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"new pass"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"new pass"});

            accService.changeForgottenPass(requestContent);

            String updatedPwd;
            String query = "select acc_password from testdb.account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                updatedPwd = resultSet.getString("acc_password");
            }

            boolean isPwdSame = previousPwd.equals(updatedPwd);

            assertFalse(isPwdSame);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void AccountService_ChangeAccountPassword_ShouldNotChangeAccPwdIfCurrentPwdIncorrect() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            reqParams.put(ATTR_UUID, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"12345"});
            accService.changeForgottenPass(requestContent);

            Account accBefore = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            String previousPwd = accBefore.getPassword();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_PWD, new String[]{"incorrect current pass"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"new pass"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"new pass"});

            accService.changeForgottenPass(requestContent);

            String updatedPwd;
            String query = "select acc_password from testdb.account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                updatedPwd = resultSet.getString("acc_password");
            }

            boolean isPwdSame = previousPwd.equals(updatedPwd);

            assertTrue(isPwdSame);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_ChangeAccountPassword_ShouldNotChangeAccPwdIfRepeatedPwdIncorrect() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            reqParams.put(ATTR_UUID, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"12345"});
            accService.changeForgottenPass(requestContent);

            Account accBefore = accRepo.query(new SelectAccByIdSpecification(1)).get(0);
            String previousPwd = accBefore.getPassword();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_PWD, new String[]{"12345"});
            reqParams.put(ATTR_UPDATED_PWD, new String[]{"12345"});
            reqParams.put(ATTR_REPEATED_PWD, new String[]{"wrong pass"});

            accService.changeForgottenPass(requestContent);

            String updatedPwd;
            String query = "select acc_password from testdb.account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                updatedPwd = resultSet.getString("acc_password");
            }

            boolean isPwdSame = previousPwd.equals(updatedPwd);

            assertTrue(isPwdSame);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_InitApproveAccAvatarPage_ShouldCorrectlyInitAccountList() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));

            accService.initApproveAccAvatarPage(requestContent);

            List<Account> expected = accRepo.query(new SelectAccByIdSpecification(1));
            List<Account> actual = (List<Account>) reqAttrs.get(ATTR_ACCS_TO_AVATAR_APPROVE);

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_AddAccAvatarToReview_ShouldCorrectlyUpdateAccUpdatePhotoPath() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_FILE_EXTENSION, ".png");

            accService.addAccAvatarToReview(requestContent);

            String expected = "3.png";
            String actual;
            @Language("sql") String query = "select update_photo_path from account where acc_id = 3";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("update_photo_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }

    }

    @Test
    public void AccountService_ApproveAccAvatar_ShouldUpdateAccPathToPhotoCorrectly() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_LOGIN, new String[]{"admin"});
            accService.approveAccAvatar(requestContent);

            String expected = "1.png";
            String actual;
            @Language("sql") String query = "select acc_photo_path from account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("acc_photo_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_ApproveAccAvatar_ShouldClearAccUpdatePhotoPath() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_LOGIN, new String[]{"admin"});
            accService.approveAccAvatar(requestContent);

            String expected = "";
            String actual;
            @Language("sql") String query = "select update_photo_path from account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("update_photo_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_DeclineAccAvatar_ShouldNotChangeAccPathToPhoto() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            String expected;
            @Language("sql") String query = "select acc_photo_path from account where acc_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                expected = resultSet.getString("acc_photo_path");
            }

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_LOGIN, new String[]{"admin"});

            accService.declineAccAvatar(requestContent);

            String actual;
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("acc_photo_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_DeclineAccAvatar_ShouldClearAccUpdatePhotoPath() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_LOGIN, new String[]{"admin"});

            accService.declineAccAvatar(requestContent);

            String expected = "";
            @Language("sql") String query = "select update_photo_path from account where acc_id = 1";
            String actual;
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("update_photo_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_EditAccountInfo_ShouldUpdateAccData() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_NAME, new String[]{"tommy"});
            reqParams.put(ATTR_SURNAME, new String[]{"jerry"});
            reqParams.put(ATTR_EMAIL, new String[]{"123123@tra.ta"});
            reqParams.put(ATTR_PHONENUMBER, new String[]{"+12345678"});
            reqParams.put(ATTR_ABOUT, new String[]{""});

            accService.editAccountInfo(requestContent);

            String expected = "tommy";
            @Language("sql") String query = "select acc_name from account where acc_id = 1";
            String actual;
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("acc_name");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_PickTargetNameFromUri_ShouldReturnCorrectString() throws SQLException, RepositoryException, ServiceException {
        String expected = "admin admin";

        String uri = "/easyLearning/some-path/admin%20admin";
        String actual = accService.pickTargetNameFromUri(uri);

        assertEquals(actual, expected);
    }

    @Test
    public void AccountService_InitAuthorPage_ShouldInitAuthorCorrectly() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_REQUESTED_AUTHOR_LOGIN, "Author");

            accService.initAuthorPage(requestContent);

            Account expected = accRepo.query(new SelectAccByIdSpecification(2)).get(0);
            Account actual = (Account) reqAttrs.get(ATTR_REQUESTED_AUTHOR);

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void AccountService_InitAuthorPage_ShouldInitAuthorCoursesCorrectly() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(1));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_REQUESTED_AUTHOR_LOGIN, "Author");

            accService.initAuthorPage(requestContent);

            List<Course> expected = courseRepo.query(new SelectByAuthorIdSpecification(2));
            List<Course> actual = (List<Course>) reqAttrs.get(ATTR_AUTHOR_COURSE_LIST);

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }
}