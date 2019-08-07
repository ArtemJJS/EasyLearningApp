package service;

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
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;
import static org.testng.Assert.*;


public class MarkServiceTest {
    private static final String ATTR_USER = "user";
    private static final String ATTR_COMMENT = "comment";
    private static final String ATTR_TARGET_ID = "target_id";
    private static final String ATTR_MARK_VALUE = "mark_value";
    private static final String ATTR_AVAILABLE_COURSES = "coursesAvailable";
    private static final String ATTR_MARKED_COURSES_IDS = "marked_courses_ids";
    @Language("sql")
    private static final String CREATE_TABLES = "call createTables()";
    @Language("sql")
    private static final String DROP_TABLES = "call dropTables()";

    private SessionRequestContent requestContent;
    private HashMap<String, Object> sessionAttrs;
    private Map<String, String[]> reqParams;

    private CourseRepository courseRepo = new CourseRepository();
    private AccRepository accRepo = new AccRepository();
    private MarkRepository markRepo = new MarkRepository();

    private MarkService markService = new MarkService();
    private ConnectionPool pool = ConnectionPool.getInstance();


//    @AfterClass
//    public void tearDownTestClass(){
//        pool.closePool();
//    }

    @BeforeMethod
    public void setUp(){
        requestContent = new SessionRequestContent();
        sessionAttrs = requestContent.getSessionAttributes();
        reqParams = requestContent.getRequestParameters();

        sessionAttrs.put("locale", Locale.US);
    }


    @Test
    public void MarkService_MarkAuthor_ShouldAddMarkIntoDb() throws SQLException, RepositoryException, ServiceException {
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
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void MarkService_MarkAuthor_ShouldUpdateAuthorAvgMark() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Course> coursesAvailable = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3));
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_TARGET_ID, new String[]{"2"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good author"});

            markService.markAuthor(requestContent);

            double expected = 4d;
            Account accountAfter = accRepo.query(new SelectAccByIdSpecification(2)).get(0);
            double actual = accountAfter.getAvgMark();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test(expectedExceptions = ServiceException.class)
    public void MarkService_MarkAuthor_ShouldThrowExceptionIfOperationDenied() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            // empty list here (course not purchased):
            List<Course> coursesAvailable = new ArrayList<>();
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_TARGET_ID, new String[]{"2"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good author"});

            markService.markAuthor(requestContent);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void MarkService_MarkCourse_ShouldAddMarkIntoDb() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Mark> marksBefore = markRepo.query(new SelectMarkByTargetIdSpecification(COURSE_MARK, 1));
            int expected = marksBefore.size() + 1;

            List<Course> coursesAvailable = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3));
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            sessionAttrs.put(ATTR_MARKED_COURSES_IDS, new ArrayList<Integer>());
            reqParams.put(ATTR_TARGET_ID, new String[]{"1"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good course"});

            markService.markCourse(requestContent);

            List<Mark> marksAfter = markRepo.query(new SelectMarkByTargetIdSpecification(COURSE_MARK, 1));
            int actual = marksAfter.size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void MarkService_MarkCourse_ShouldUpdateCourseAvgMark() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Course> coursesAvailable = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3));
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            sessionAttrs.put(ATTR_MARKED_COURSES_IDS, new ArrayList<Integer>());
            reqParams.put(ATTR_TARGET_ID, new String[]{"1"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good course"});

            markService.markCourse(requestContent);

            double expected = 4d;
            Course course = courseRepo.query(new SelectCourseByIdSpecification(1)).get(0);
            double actual = course.getAvgMark();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test(expectedExceptions = ServiceException.class)
    public void MarkService_MarkCourse_ShouldThrowExceptionIfAccessDenied() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            //empty attribute(marked course is not purchased):
            List<Course> coursesAvailable = new ArrayList<>();
            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_AVAILABLE_COURSES, coursesAvailable);
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            sessionAttrs.put(ATTR_MARKED_COURSES_IDS, new ArrayList<Integer>());
            reqParams.put(ATTR_TARGET_ID, new String[]{"1"});
            reqParams.put(ATTR_MARK_VALUE, new String[]{"4"});
            reqParams.put(ATTR_COMMENT, new String[]{"good course"});

            markService.markCourse(requestContent);
        } finally {
            statement.close();
            connection.close();
        }
    }
}