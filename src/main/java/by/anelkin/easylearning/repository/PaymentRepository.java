package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j
public class PaymentRepository implements AppRepository<Payment> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO user_payment(acc_id, course_id, payment_code, payment_amount, payment_date, currency_id, payment_description)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM user_payment WHERE payment_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE user_payment SET  acc_id = ?, " +
            "course_id = ?, payment_code = ?, payment_amount = ?, payment_date = ?, currency_id = ?, payment_description = ?" +
            " WHERE payment_id = ?";


    @Override
    public boolean update(@NonNull Payment payment) {
        int amountUpdated = 0;
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE);
            statement.setInt(1, payment.getAccountId());
            statement.setInt(2, payment.getCourseId());
            statement.setInt(3, payment.getPaymentCode());
            statement.setBigDecimal(4, payment.getAmount());
            statement.setString(5, dateFormat.format(payment.getPaymentDate()));
            statement.setInt(6, payment.getCurrencyId());
            statement.setString(7, payment.getDescription());
            statement.setInt(8, payment.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            amountUpdated = statement.executeUpdate();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return amountUpdated == 0;
    }

    @Override
    public boolean delete(@NonNull Payment payment) {
        boolean isDeleted = false;
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_DELETE);
            statement.setInt(1, payment.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isDeleted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return isDeleted;
    }

    @Override
    public boolean insert(@NonNull Payment payment) {
        boolean isInserted = false;
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_INSERT);
            statement.setInt(1, payment.getAccountId());
            statement.setInt(2, payment.getCourseId());
            statement.setInt(3, payment.getPaymentCode());
            statement.setBigDecimal(4, payment.getAmount());
            statement.setString(5, dateFormat.format(payment.getPaymentDate()));
            statement.setInt(6, payment.getCurrencyId());
            statement.setString(7, payment.getDescription());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isInserted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return isInserted;
    }

    @Override
    public List<Payment> query(@NonNull AppSpecification<Payment> specification) {
        List<Payment> paymentList = new ArrayList<>();
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            log.debug("Attempt to execute query:" + specification.getQuery());
            ResultSet resultSet = statement.executeQuery(specification.getQuery());
            log.debug("Query completed:" + specification.getQuery());
            paymentList.addAll(fillPaymentList(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return paymentList;
    }

    private Collection<Payment> fillPaymentList(ResultSet resultSet) {
        List<Payment> paymentList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Payment payment = new Payment();
                payment.setId(resultSet.getInt("payment_id"));
                payment.setAccountId(resultSet.getInt("acc_id"));
                payment.setCourseId(resultSet.getInt("course_id"));
                payment.setPaymentCode(resultSet.getInt("payment_code"));
                payment.setAmount(resultSet.getBigDecimal("payment_amount"));
                payment.setPaymentDate(resultSet.getDate("payment_date"));
                payment.setCurrencyId(resultSet.getInt("currency_id"));
                payment.setDescription(resultSet.getString("payment_description"));
                paymentList.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentList;
    }
}
