package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.dataprovider.TestDataProvider;
import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.lesson.SelectAllLessonSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

@Test(priority = 1)
public class LessonRepositoryTest {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private LessonRepository repo = new LessonRepository();
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
    public void testUpdate() throws RepositoryException {
        String expected = "new lesson name";
        CourseLesson lesson = repo.query(new SelectAllLessonSpecification()).get(0);
        lesson.setName(expected);
        repo.update(lesson);
        String actual = repo.query(new SelectAllLessonSpecification()).get(0).getName();
        assertEquals(actual, expected);
    }

    @Test(priority = 2)
    public void testDelete() throws RepositoryException {
        List<CourseLesson> lessons = repo.query(new SelectAllLessonSpecification());
        int expected = lessons.size()-1;
        CourseLesson lesson = lessons.get(0);
        repo.delete(lesson);
        int actual = repo.query(new SelectAllLessonSpecification()).size();
        assertEquals(actual, expected);
    }

    @Test(priority = 2)
    public void testInsert() throws RepositoryException {
        List<CourseLesson> lessons = repo.query(new SelectAllLessonSpecification());
        int expected = lessons.size()+1;
        CourseLesson lesson = lessons.get(0);
        repo.insert(lesson);
        int actual = repo.query(new SelectAllLessonSpecification()).size();
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "getLessonSpecifications")
    public void testQuery(AppSpecification<CourseLesson> spec, List<Integer> expected) throws RepositoryException {
        List<CourseLesson> lessons = repo.query(spec);
        List<Integer> actual = lessons.stream().map(CourseLesson::getId).collect(Collectors.toList());

        assertEquals(actual, expected);
    }
}