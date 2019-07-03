package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public boolean update(@NonNull Payment payment) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] params = {String.valueOf(payment.getAccountId()), String.valueOf(payment.getCourseId()), String.valueOf(payment.getPaymentCode()),
                    String.valueOf(payment.getAmount()), dateFormat.format(payment.getPaymentDate()), String.valueOf(payment.getCurrencyId()),
                    payment.getDescription(), String.valueOf(payment.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull Payment payment) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            String[] params = {String.valueOf(payment.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull Payment payment) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            String[] params = {String.valueOf(payment.getAccountId()), String.valueOf(payment.getCourseId()), String.valueOf(payment.getPaymentCode()),
                    String.valueOf(payment.getAmount()), dateFormat.format(payment.getPaymentDate()), String.valueOf(payment.getCurrencyId()),
                    payment.getDescription()};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public List<Payment> query(@NonNull AppSpecification<Payment> specification) throws RepositoryException {
        List<Payment> paymentList;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            log.debug("Attempt to execute query:" + specification.getQuery());
            try (ResultSet resultSet = statement.executeQuery(specification.getQuery())) {
                log.debug("Query completed:" + specification.getQuery());
                paymentList = fillPaymentList(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return paymentList;
    }

    private List<Payment> fillPaymentList(ResultSet resultSet) {
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

    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
        statement.execute();
        log.debug("Query completed:" + statement.toString().split(":")[1]);
    }
}
