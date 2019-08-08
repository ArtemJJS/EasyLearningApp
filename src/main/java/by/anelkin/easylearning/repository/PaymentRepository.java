package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.anelkin.easylearning.entity.Payment.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
public class PaymentRepository implements AppRepository<Payment> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM user_payment WHERE payment_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE user_payment SET  acc_id = ?, " +
            "course_id = ?, payment_code = ?, payment_amount = ?, payment_date = ?, currency_id = ?, payment_description = ?" +
            " WHERE payment_id = ?";
    @Language("sql")
    private static final String QUERY_INSERT_AND_UPDATE_BALANCE = "{CALL insertPaymentAndUpdateBalance(?, ?, ?, ?, ?, ?, ?)}";
    @Language("sql")
    private static final String QUERY_INSERT_BUY_FROM_BALANCE = "{call insertPurchaseCourseFromBalance(?, ?, ?, ?, ?, ?, ?)}";
    //without balance updating:
    @Language("sql")
    private static final String QUERY_INSERT_BUY_WITH_CARD = "{CALL insertPurchaseCourseByCard(?, ?, ?, ?, ?, ?, ?)}";

    @Override
    public boolean update(@NonNull Payment payment) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] params = {String.valueOf(payment.getAccountId()), String.valueOf(payment.getCourseId()), String.valueOf(payment.getPaymentCode()),
                    String.valueOf(payment.getAmount()), String.valueOf(payment.getPaymentDate()), String.valueOf(payment.getCurrencyId()),
                    payment.getDescription(), String.valueOf(payment.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            log.error(e);
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
            log.error(e);
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull Payment payment) throws RepositoryException {
        String curr_query;
        PaymentCode paymentCode = PaymentCode.getPaymentCodeInstanceByCode(payment.getPaymentCode());
        if (paymentCode == null) {
            log.error("Payment code is not correct!");
            throw new RepositoryException("Payment code is not correct!");
        }
        switch (paymentCode) {
            case BUY_COURSE_WITH_CARD:
                curr_query = QUERY_INSERT_BUY_WITH_CARD;
                break;
            case BUY_COURSE_FROM_BALANCE:
                curr_query = QUERY_INSERT_BUY_FROM_BALANCE;
                break;
            default:
                curr_query = QUERY_INSERT_AND_UPDATE_BALANCE;
        }
        Connection connection = pool.takeConnection();
        CallableStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareCall(curr_query);
            String[] params = {String.valueOf(payment.getAccountId()), String.valueOf(payment.getCourseId()), String.valueOf(payment.getPaymentCode()),
                    String.valueOf(payment.getAmount()), String.valueOf(payment.getPaymentDate()), String.valueOf(payment.getCurrencyId()),
                    payment.getDescription()};
            setParametersAndExecute(statement, params);
            connection.commit();
        } catch (SQLException e) {
            log.error(e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error(ex);
            }
            throw new RepositoryException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
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
            log.debug("Executing query:" + statement.toString().split(COLON_SYMBOL)[1]);
            try (ResultSet resultSet = statement.executeQuery()) {
                paymentList = fillPaymentList(resultSet);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new RepositoryException(e);
        }
        return paymentList;
    }

    private List<Payment> fillPaymentList(ResultSet resultSet) throws SQLException {
        List<Payment> paymentList = new ArrayList<>();
        while (resultSet.next()) {
            Payment payment = new Payment();
            payment.setId(resultSet.getInt(PAYMENT_ID));
            payment.setAccountId(resultSet.getInt(ACC_ID));
            payment.setCourseId(resultSet.getInt(COURSE_ID));
            payment.setPaymentCode(resultSet.getInt(PAYMENT_CODE));
            payment.setAmount(resultSet.getBigDecimal(PAYMENT_AMOUNT));
            payment.setPaymentDate(resultSet.getLong(PAYMENT_DATE));
            payment.setCurrencyId(resultSet.getInt(PAYMENT_CURRENCY_ID));
            payment.setDescription(resultSet.getString(PAYMENT_DESCRIPTION));
            paymentList.add(payment);
        }
        return paymentList;
    }

    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Executing query:" + statement.toString().split(COLON_SYMBOL)[1]);
        statement.execute();
    }
}
