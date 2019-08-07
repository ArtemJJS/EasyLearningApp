package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.dataprovider.TestDataProvider;
import by.anelkin.easylearning.entity.RestorePassRequest;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.restorepass.SelectRestoreByAccIdSpecification;
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

public class RestorePassRequestRepositoryTest {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private RestorePassRequestRepository repo = new RestorePassRequestRepository();
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
    public void RestorePassRequestRepository_Update_ShouldDoNothing() throws RepositoryException {
        String expected = "some text";
        RestorePassRequest passRequest = repo.query(new SelectRestoreByAccIdSpecification(1)).get(0);
        passRequest.setUuid(expected);
        repo.update(passRequest);
        String actual = repo.query(new SelectRestoreByAccIdSpecification(1)).get(0).getUuid();
        assertNotEquals(actual, expected);
    }

    @Test(priority = 3)
    public void RestorePassRequestRepository_Delete() throws RepositoryException {
        List<RestorePassRequest> list = repo.query(new SelectRestoreByAccIdSpecification(1));
        int expected = list.size() - 1;
        repo.delete(list.get(0));
        int actual = repo.query(new SelectRestoreByAccIdSpecification(1)).size();

        assertEquals(actual, expected);
    }

    @Test(priority = 1)
    public void RestorePassRequestRepository_Insert() throws RepositoryException {
        List<RestorePassRequest> list = repo.query(new SelectRestoreByAccIdSpecification(1));
        int expected = list.size() + 1;
        repo.insert(list.get(0));
        int actual = repo.query(new SelectRestoreByAccIdSpecification(1)).size();

        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "getRestorePassRequestSpecifications")
    public void RestorePassRequestRepository_Query(AppSpecification<RestorePassRequest> spec, List<Integer> expected) throws RepositoryException {
        List<RestorePassRequest> accounts = repo.query(spec);
        List<Integer> actual = accounts.stream().map(RestorePassRequest::getAccId).collect(Collectors.toList());

        assertEquals(actual, expected);

    }
}