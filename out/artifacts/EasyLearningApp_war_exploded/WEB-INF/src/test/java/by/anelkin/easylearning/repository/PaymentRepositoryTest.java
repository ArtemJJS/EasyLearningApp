package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.dataprovider.TestDataProvider;
import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.payment.SelectAllPaymentSpecification;
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

public class PaymentRepositoryTest {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private PaymentRepository repo = new PaymentRepository();
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
        String expected = "new comment";
        Payment payment = repo.query(new SelectAllPaymentSpecification()).get(0);
        payment.setDescription(expected);
        repo.update(payment);
        String actual = repo.query(new SelectAllPaymentSpecification()).get(0).getDescription();
        assertEquals(actual, expected);

    }

    @Test(priority = 2)
    public void testDelete() throws RepositoryException {
        List<Payment> payments = repo.query(new SelectAllPaymentSpecification());
        int expected = payments.size() - 1;
        repo.delete(payments.get(0));
        int actual = repo.query(new SelectAllPaymentSpecification()).size();
        assertEquals(actual, expected);
    }

    @Test(priority = 2)
    public void testInsert() throws RepositoryException {
        List<Payment> payments = repo.query(new SelectAllPaymentSpecification());
        int expected = payments.size() + 1;
        Payment payment = payments.get(0);
        repo.insert(payment);
        int actual = repo.query(new SelectAllPaymentSpecification()).size();
        assertEquals(actual, expected);
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "getPaymentSpecifications")
    public void testQuery(AppSpecification<Payment> spec, List<Integer> expected) throws RepositoryException {
        List<Payment> accounts = repo.query(spec);
        List<Integer> actual = accounts.stream().map(Payment::getId).collect(Collectors.toList());

        assertEquals(actual, expected);
    }
}