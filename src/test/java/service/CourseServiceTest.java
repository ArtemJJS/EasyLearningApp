package service;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.service.CourseService;
import by.anelkin.easylearning.service.MarkService;
import by.anelkin.easylearning.specification.account.SelectAccByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectAllCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static by.anelkin.easylearning.entity.Course.*;
import static org.testng.Assert.*;

public class CourseServiceTest {
    private static final String ATTR_COURSES_LIST = "courses_list";
    private static final String ATTR_USER = "user";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final String ATTR_COURSE_NAME = "course_name";
    private static final String ATTR_COURSE_DESCRIPTION = "course_description";
    private static final String ATTR_COURSE_PRICE = "course_price";
    private static final String ATTR_CHAPTER_NAME = "chapter_name";
    private static final String ATTR_COURSE_ALREADY_EXISTS = "course_exists_msg";
    private static final String ATTR_FILE_EXTENSION = "file_extension";
    private static final String ATTR_SEARCH_KEY = "search_key";
    private static final String ATTR_PAGE = "page";
    private static final String ATTR_HAS_MORE_PAGES = "has_more_pages";
    private static final String ATTR_MESSAGE = "message";
    private static final String ATTR_LOCALE = "locale";
    private static final String LOCALE_SPLITTER = "_";
    @Language("sql")
    private static final String CREATE_TABLES = "call createTables()";
    @Language("sql")
    private static final String DROP_TABLES = "call dropTables()";

    private SessionRequestContent requestContent;
    private HashMap<String, Object> sessionAttrs;
    private Map<String, String[]> reqParams;
    HashMap<String, Object> reqAttrs;

    private CourseRepository courseRepo = new CourseRepository();
    private AccRepository accRepo = new AccRepository();
    private MarkRepository markRepo = new MarkRepository();

    private CourseService courseService = new CourseService();
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
    public void CourseService_AddCourseImgToReview_ShouldUpdateTargetAccountCorrectly() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_COURSE_ID, "1");
            reqAttrs.put(ATTR_FILE_EXTENSION, ".png");

            courseService.addCourseImgToReview(requestContent);

            String expected = "1.png";
            String actual;
            String query = "select update_img_path from course where course_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("update_img_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void CourseService_ApproveCourseImgChange_ShouldUpdateTargetAccPathToPhoto() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_COURSE_ID, "1");
            reqAttrs.put(ATTR_FILE_EXTENSION, ".png");
            courseService.addCourseImgToReview(requestContent);

            reqParams.put(ATTR_COURSE_NAME, new String[]{"Updated from main course"});
            courseService.approveCourseImgChange(requestContent);

            String expected = "1.png";
            String actual;
            String query = "select course_picture from course where course_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("course_picture");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void CourseService_ApproveCourseImgChange_ShouldClearUpdateImgPath() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_COURSE_ID, "1");
            reqAttrs.put(ATTR_FILE_EXTENSION, ".png");
            courseService.addCourseImgToReview(requestContent);

            reqParams.put(ATTR_COURSE_NAME, new String[]{"Updated from main course"});
            courseService.approveCourseImgChange(requestContent);

            String expected = "";
            String actual;
            String query = "select update_img_path from course where course_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("update_img_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void CourseService_DeclineCourseImgChange_ShouldNotChangeAccPathToImg() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_COURSE_ID, "1");
            reqAttrs.put(ATTR_FILE_EXTENSION, ".png");
            courseService.addCourseImgToReview(requestContent);

            reqParams.put(ATTR_COURSE_NAME, new String[]{"Updated from main course"});
            courseService.declineCourseImgChange(requestContent);

            String expected = "default_course_avatar.png";
            String actual;
            String query = "select course_picture from course where course_id = 1";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("course_picture");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void CourseService_DeclineCourseImgChange_ShouldClearAccUpdatePhotoPath() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_COURSE_ID, "2");
            reqAttrs.put(ATTR_FILE_EXTENSION, ".png");
            courseService.addCourseImgToReview(requestContent);

            reqParams.put(ATTR_COURSE_NAME, new String[]{"Second Course"});
            courseService.declineCourseImgChange(requestContent);

            String expected = "";
            String actual;
            String query = "select update_img_path from course where course_id = 2";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = resultSet.getString("update_img_path");
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void CourseService_InitCoursePage_ShouldInitAttributesCorrectly() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put("course-id", new String[]{"1"});

            courseService.initCoursePage(requestContent);

            List<Mark> currentCourseMarks = (List<Mark>) reqAttrs.get("currentCourseMarks");
            Course requestedCourse = (Course) reqAttrs.get("requestedCourse");
            Map<CourseChapter, List<CourseLesson>> currentCourseContent = (Map<CourseChapter, List<CourseLesson>>) reqAttrs.get("currentCourseContent");
            Account author_of_course = (Account) reqAttrs.get("author_of_course");

            boolean actual = currentCourseMarks.size() == 0
                    && requestedCourse.equals(courseRepo.query(new SelectCourseByIdSpecification(1)).get(0))
                    && author_of_course.equals(accRepo.query(new SelectAccByIdSpecification(2)).get(0))
                    && currentCourseContent.size() == 2;

            assertTrue(actual);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void CourseService_InitCourseApprovalPage() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));

            courseService.initCourseApprovalPage(requestContent);

            List<Course> courses = (List<Course>) reqAttrs.get(ATTR_COURSES_LIST);
            boolean actual = courses.get(0).equals(courseRepo.query(new SelectCourseByIdSpecification(3)).get(0));

            assertTrue(actual);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void CourseService_SearchCourses_ShouldCorrectlySearchCourses() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_SEARCH_KEY, new String[]{""});
            reqParams.put(ATTR_PAGE, new String[]{"0"});

            courseService.searchCourses(requestContent);

            List<Course> expected = courseRepo.query(new SelectCourseByIdSpecification(1));
            List<Course> actual = (List<Course>) reqAttrs.get(ATTR_COURSES_LIST);

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }



    @Test
    public void CourseService_InitCourseImgApprovalPage_ShouldCorrectlyInitCoursesList() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqAttrs.put(ATTR_COURSE_ID, "3");
            reqAttrs.put(ATTR_FILE_EXTENSION, ".png");
            courseService.addCourseImgToReview(requestContent);

            courseService.initCourseImgApprovalPage(requestContent);

            List<Course> expected = courseRepo.query(new SelectCourseByIdSpecification(3));
            List<Course> actual = (List<Course>) reqAttrs.get(ATTR_COURSES_LIST);

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void CourseService_ApproveCourse_ShouldUpdateCourseStatus() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});

            courseService.approveCourse(requestContent);

            CourseState expected = CourseState.APPROVED;
            CourseState actual;
            String query = "select state from course where course_id = 3";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = CourseState.values()[resultSet.getInt("state")];
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }



    @Test
    public void CourseService_FreezeCourse_ShouldUpdateCorrectlyCourseState() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});

            courseService.freezeCourse(requestContent);

            CourseState expected = CourseState.FREEZING;
            CourseState actual;
            String query = "select state from course where course_id = 3";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.next();
                actual = CourseState.values()[resultSet.getInt("state")];
            }

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

//    @Test
//    public void CourseService_AddCourseToReview() throws SQLException, RepositoryException, ServiceException {
//
//    }
}