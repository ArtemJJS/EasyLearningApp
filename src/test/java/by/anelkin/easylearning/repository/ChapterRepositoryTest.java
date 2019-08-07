package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.dataprovider.TestDataProvider;
import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.chapter.SelectAllChapterSpecification;
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

public class ChapterRepositoryTest {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private ChapterRepository repo = new ChapterRepository();
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
        String expected = "some new name";

        CourseChapter chapter = repo.query(new SelectAllChapterSpecification()).get(0);
        chapter.setName(expected);
        repo.update(chapter);
        String actual = repo.query(new SelectAllChapterSpecification()).get(0).getName();

        assertEquals(actual, expected);
    }

    @Test(priority = 2)
    public void testDelete() throws RepositoryException {
        List<CourseChapter> chapters = repo.query(new SelectAllChapterSpecification());
        int expected = chapters.size() - 1;
        CourseChapter chapter = chapters.get(0);
        repo.delete(chapter);
        int actual = repo.query(new SelectAllChapterSpecification()).size();

        assertEquals(actual, expected);
    }

    @Test(priority = 2)
    public void testInsert() throws RepositoryException {
        List<CourseChapter> chapters = repo.query(new SelectAllChapterSpecification());
        int expected = chapters.size() + 1;
        CourseChapter chapter = chapters.get(0);
        repo.insert(chapter);
        int actual = repo.query(new SelectAllChapterSpecification()).size();

        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "getChapterSpecifications", priority = 1)
    public void testQuery(AppSpecification<CourseChapter> spec, List<Integer> expected) throws RepositoryException  {
        List<CourseChapter> accounts = repo.query(spec);
        List<Integer> actual = accounts.stream().map(CourseChapter::getId).collect(Collectors.toList());

        assertEquals(actual, expected);
    }
}