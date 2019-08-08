package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.dataprovider.TestDataProvider;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarksMadeByUserSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;
import static org.testng.Assert.*;

public class MarkRepositoryTest {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private MarkRepository repo = new MarkRepository();
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
        String expected = "some comment";
        Mark mark = repo.query(new SelectMarkByIdSpecification(COURSE_MARK, 1)).get(0);
        mark.setComment(expected);
        repo.update(mark);
        String actual = repo.query(new SelectMarkByIdSpecification(COURSE_MARK, 1)).get(0).getComment();
        assertEquals(actual, expected);
    }

    @Test(priority = 3)
    public void testDelete() throws RepositoryException {
        List<Mark> marks = repo.query(new SelectMarksMadeByUserSpecification(COURSE_MARK, 3));
        int expected = marks.size() - 1;
        repo.delete(marks.get(0));
        int actual = repo.query(new SelectMarksMadeByUserSpecification(COURSE_MARK, 3)).size();
        assertEquals(actual, expected);
    }

    @Test(priority = 1)
    public void testInsert() throws RepositoryException {
        List<Mark> marks = repo.query(new SelectMarksMadeByUserSpecification(COURSE_MARK, 3));
        int expected = marks.size() + 1;
        Mark mark = marks.get(0);
        mark.setTargetId(2);
        repo.insert(mark);
        int actual = repo.query(new SelectMarksMadeByUserSpecification(COURSE_MARK, 3)).size();
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "getMarkSpecifications", dataProviderClass = TestDataProvider.class)
    public void testQuery(AppSpecification<Mark> spec, List<Integer> expected) throws RepositoryException {
        List<Mark> marks = repo.query(spec);
        List<Integer> actual = marks.stream().map(Mark::getId).collect(Collectors.toList());

        assertEquals(actual, expected);
    }
}