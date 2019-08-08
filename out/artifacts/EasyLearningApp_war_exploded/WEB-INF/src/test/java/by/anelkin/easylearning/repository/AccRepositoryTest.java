package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.dataprovider.TestDataProvider;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.account.SelectAccByIdSpecification;
import by.anelkin.easylearning.specification.account.SelectAllAccountSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class AccRepositoryTest {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private AccRepository repo = new AccRepository();
    @Language("sql")
    private static final String CREATE_TABLES = "call createTables()";
    @Language("sql")
    private static final String DROP_TABLES = "call dropTables()";
    private Connection connection;
    private Statement statement;

    @BeforeMethod
    public void setUpTestClass() throws SQLException {
        connection = pool.takeConnection();
        statement = connection.createStatement();

        statement.execute(DROP_TABLES);
        statement.execute(CREATE_TABLES);
    }

    @AfterMethod
    public void tierDownTestClass() throws SQLException {
        statement.close();
        connection.close();
    }


    @Test(priority = 5)
    public void AccRepository_Update_ShouldUpdateCorrectly() throws RepositoryException {
        String expected = "some new name";

        Account account = repo.query(new SelectAccByIdSpecification(1)).get(0);
        account.setName(expected);
        repo.update(account);
        account = repo.query(new SelectAccByIdSpecification(1)).get(0);
        String actual = account.getName();

        assertEquals(actual, expected);
    }

    @Test(priority = 7)
    public void AccRepository_Delete_ShouldDeleteAcc() throws RepositoryException{
        int expected = repo.query(new SelectAllAccountSpecification()).size() - 1;
        Account account = repo.query(new SelectAccByIdSpecification(3)).get(0);
        repo.delete(account);
        int actual = repo.query(new SelectAllAccountSpecification()).size();

        assertEquals(actual, expected);
    }

    @Test(priority = 4)
    public void AccRepository_Insert_ShouldInsertAcc() throws RepositoryException {
        int expected = repo.query(new SelectAllAccountSpecification()).size() + 1;
        Account account = repo.query(new SelectAccByIdSpecification(1)).get(0);
        account.setLogin("new login");
        repo.insert(account);
        int actual = repo.query(new SelectAllAccountSpecification()).size();

        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "getAccountSpecifications", priority = 1)
    public void AccRepository_Query_ShouldReturnCorrectResult(AppSpecification<Account> spec, List<Integer> expected) throws RepositoryException, SQLException {
            List<Account> accounts = repo.query(spec);
            List<Integer> actual = accounts.stream().map(Account::getId).collect(Collectors.toList());

            assertEquals(actual, expected);
    }
}