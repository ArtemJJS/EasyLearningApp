package MarkService;


import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.service.MarkService;
import by.anelkin.easylearning.specification.account.SelectAccByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.Assert;
import org.testng.annotations.*;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;
import static org.testng.Assert.*;


public class MarkServiceTest {
    private static final String ATTR_USER = "user";
    private static final String ATTR_ROLE = "role";
    private static final String ATTR_COMMENT = "comment";
    private static final String ATTR_TARGET_ID = "target_id";
    private static final String ATTR_MARK_VALUE = "mark_value";
    private static final String ATTR_AVAILABLE_COURSES = "coursesAvailable";
    private static final String ATTR_AUTHOR_LOGIN = "author_login";
    private static final String ATTR_MARKED_COURSES_IDS = "marked_courses_ids";
    @Language("sql")
    private static final String CREATE_TABLES = "call createTables()";
    @Language("sql")
    private static final String DROP_TABLES = "call dropTables()";

    private SessionRequestContent requestContent;
    HashMap<String, Object> sessionAttrs;
    Map<String, String[]> reqParams;
    HashMap<String, Object> reqAttrs;

    private CourseRepository courseRepo = new CourseRepository();
    private AccRepository accRepo = new AccRepository();
    private MarkRepository markRepo = new MarkRepository();

    private MarkService markService = new MarkService();
    private ConnectionPool pool = ConnectionPool.getInstance();


    @AfterClass
    public void tearDownTestClass() throws SQLException {
        pool.closePool();
    }

    @BeforeMethod
    public void setUp() throws SQLException {

        requestContent = new SessionRequestContent();
        sessionAttrs = requestContent.getSessionAttributes();
        reqParams = requestContent.getRequestParameters();
        reqAttrs = requestContent.getRequestAttributes();

        sessionAttrs.put("locale", Locale.US);
    }


    @Test
    public void testMarkAuthorShouldAddMarkIntoDb() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            // FIXME: 8/7/2019 ВОПРОС: почему если dropTables тут то все работает, а если в finally то нет
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Mark> marksBefore = markRepo.query(new SelectMarkByTargetIdSpecification(AUTHOR_MARK, 2));
            int expected = marksBefore.size() + 1;

            List<Course> coursesAvailable = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3));
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));

            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));

            reqParams.put(ATTR_TARGET_ID, new String[]{"2"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good author"});

            markService.markAuthor(requestContent);

            List<Mark> marksAfter = markRepo.query(new SelectMarkByTargetIdSpecification(AUTHOR_MARK, 2));
            int actual = marksAfter.size();

            assertEquals(actual, expected);
            System.out.println("actual: " + actual);
            System.out.println("expected: " + expected);
        } finally {
//            statement.execute(DROP_TABLES);
            statement.close();
            connection.close();
        }
    }

    @Test
    public void testMarkAuthorShouldUpdateAuthorAvgMark() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            double expected = 4d;

            List<Course> coursesAvailable = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3));
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));

            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_TARGET_ID, new String[]{"2"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good author"});

            markService.markAuthor(requestContent);

            Account accountAfter = accRepo.query(new SelectAccByIdSpecification(2)).get(0);
            double actual = accountAfter.getAvgMark();

            assertEquals(actual, expected);
            System.out.println("actual: " + actual);
            System.out.println("expected: " + expected);
        } finally {
//            statement.execute(DROP_TABLES);
            statement.close();
            connection.close();
        }
    }

    @Test
    public void testMarkAuthorShouldDoNothingIfNoPurchasedCoursesFromAuthor() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            // FIXME: 8/7/2019 ВОПРОС: почему если dropTables тут то все работает, а если в finally то нет
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Mark> marksBefore = markRepo.query(new SelectMarkByTargetIdSpecification(AUTHOR_MARK, 2));
            int expected = marksBefore.size();

            // empty list here:
            List<Course> coursesAvailable = new ArrayList<>();
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));

            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));

            reqParams.put(ATTR_TARGET_ID, new String[]{"2"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good author"});

            markService.markAuthor(requestContent);

            List<Mark> marksAfter = markRepo.query(new SelectMarkByTargetIdSpecification(AUTHOR_MARK, 2));
            int actual = marksAfter.size();

            assertEquals(actual, expected);
            System.out.println("actual: " + actual);
            System.out.println("expected: " + expected);
        } finally {
//            statement.execute(DROP_TABLES);
            statement.close();
            connection.close();
        }
    }


    @Test
    public void testMarkCourse() {
    }

    @Test
    public void testTakeMarksOfCourse() {
    }

    @Test
    public void testInsertMarkedCourseIdsIntoSession() {
    }
}