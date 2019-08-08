package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.dataprovider.TestDataProvider;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.course.SelectAllCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static by.anelkin.easylearning.entity.Course.*;
import static org.testng.Assert.*;



public class CourseRepositoryTest {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private CourseRepository repo = new CourseRepository();
    @Language("sql")
    private static final String CREATE_TABLES = "call createTables()";
    @Language("sql")
    private static final String DROP_TABLES = "call dropTables()";
    private Connection connection;
    private Statement statement;

    @BeforeClass
    public void setUpTestClass() throws SQLException {
        connection = pool.takeConnection();
        statement = connection.createStatement();

        statement.execute(DROP_TABLES);
        statement.execute(CREATE_TABLES);
    }

    @AfterClass
    public void tierDownTestClass() throws SQLException {
        statement.close();
        connection.close();
    }

    @Test(priority = 2)
    public void CourseRepository_Update() throws RepositoryException {
        String expected = "some new name";

        Course course = repo.query(new SelectCourseByIdSpecification(1)).get(0);
        course.setName(expected);
        repo.update(course);
        course = repo.query(new SelectCourseByIdSpecification(1)).get(0);
        String actual = course.getName();

        assertEquals(actual, expected);
    }

    @Test(priority = 2)
    public void CourseRepository_Delete_ShouldSetStatusFrozen() throws RepositoryException {
        CourseState expected = CourseState.FREEZING;
        Course course = repo.query(new SelectCourseByIdSpecification(2)).get(0);
        repo.delete(course);
        CourseState actual = repo.query(new SelectCourseByIdSpecification(2)).get(0).getState();

        assertEquals(actual, expected);
    }

    @Test(priority = 2)
    public void CourseRepository_Insert() throws RepositoryException {
        List<Course> courses = repo.query(new SelectAllCourseSpecification());
        int expected = courses.size() + 1;
        Course course = courses.get(0);
        course.setName("new name");
        repo.insert(course);
        int actual = repo.query(new SelectAllCourseSpecification()).size();

        assertEquals(actual, expected);
    }

    @Test(dataProvider = "getCourseSpecifications", dataProviderClass = TestDataProvider.class, priority = 1)
    public void CourseRepository_Query(AppSpecification<Course> spec, List<Integer> expected) throws RepositoryException{
        List<Course> accounts = repo.query(spec);
        List<Integer> actual = accounts.stream().map(Course::getId).collect(Collectors.toList());

        assertEquals(actual, expected);

    }
}