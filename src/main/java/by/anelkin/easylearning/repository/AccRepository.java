package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static by.anelkin.easylearning.entity.Account.AccountType.*;

@Log4j
public class AccRepository implements AppRepository<Account> {
    // TODO: 6/18/2019 добавить в запросы и методы ФОТО!!! (сейчас нету)
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE account SET acc_password = ?, acc_email = ?, acc_name = ?," +
            " acc_surname = ?, acc_birthdate = ?, acc_phone_number = ?, acc_registration_date = ?, acc_about = ?," +
            " acc_type = ? WHERE acc_login = ?";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM account WHERE acc_login = ?";
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO account(acc_login, acc_password, acc_email, acc_name, acc_surname, " +
            "acc_birthdate, acc_phone_number, acc_registration_date, acc_about, acc_type) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    @Override
    public boolean update(@NonNull Account account) {
        boolean isUpdated;
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        // TODO: 6/18/2019 Сделать нормальное шифрование пароля
        String hashedPass = String.valueOf(account.getPassword().hashCode());
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE);
            statement.setString(1, hashedPass);
            statement.setString(2, account.getEmail());
            statement.setString(3, account.getName());
            statement.setString(4, account.getSurname());
            statement.setString(5, dateFormat.format(account.getBirthDate()));
            statement.setString(6, account.getPhoneNumber());
            statement.setString(7, dateFormat.format(account.getRegistrDate()));
            statement.setString(8, account.getAbout());
            statement.setInt(9, account.getType().ordinal());
            statement.setString(10, account.getLogin());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isUpdated = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
            pool.returnConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        }
        return isUpdated;
    }

    @Override
    public boolean delete(@NonNull Account account) {
        boolean isDeleted;
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_DELETE);
            statement.setString(1, account.getLogin());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isDeleted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
            pool.returnConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        }
        return isDeleted;
    }

    @Override
    public boolean insert(@NonNull Account account) {
        boolean isInserted;
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        // TODO: 6/18/2019 Сделать нормальное шифрование пароля
        String hashedPass = String.valueOf(account.getPassword().hashCode());
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_INSERT);
            statement.setString(1, account.getLogin());
            statement.setString(2, hashedPass);
            statement.setString(3, account.getEmail());
            statement.setString(4, account.getName());
            statement.setString(5, account.getSurname());
            statement.setString(6, dateFormat.format(account.getBirthDate()));
            statement.setString(7, account.getPhoneNumber());
            statement.setString(8, dateFormat.format(account.getRegistrDate()));
            statement.setString(9, account.getAbout());
            statement.setInt(10, account.getType().ordinal());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isInserted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
            pool.returnConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        }
        return isInserted;
    }

    @Override
    public List<Account> query(@NonNull AppSpecification<Account> specification) {
        List<Account> accountList = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            log.debug("Attempt to execute query: " + specification.getQuery());
            ResultSet resultSet = statement.executeQuery(specification.getQuery());
            accountList.addAll(fillAccountList(resultSet));
            pool.returnConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        }
        return accountList;
    }

    private List<Account> fillAccountList(ResultSet resultSet) {
        List<Account> accountList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getInt("acc_id"));
                account.setLogin(resultSet.getString("acc_login"));
                account.setPassword(resultSet.getString("acc_password"));
                account.setEmail(resultSet.getString("acc_email"));
                account.setName(resultSet.getString("acc_name"));
                account.setSurname(resultSet.getString("acc_surname"));
                account.setBirthDate(resultSet.getDate("acc_birthdate"));
                account.setPhoneNumber(resultSet.getString("acc_phone_number"));
                account.setRegistrDate(resultSet.getDate("acc_registration_date"));
                account.setAbout(resultSet.getString("acc_about"));
                int typeId = resultSet.getInt("acc_type");
                switch (typeId) {
                    case 1:
                        account.setType(ADMIN);
                        break;
                    case 2:
                        account.setType(AUTHOR);
                        break;
                    case 3:
                        account.setType(USER);
                        break;
                    default:
                        throw new RuntimeException("Wrong type parameter from data base!!! User#" + account.getId());
                }
                accountList.add(account);
            }
        } catch (SQLException e) {
            log.error("Error during account creating: " + e.getMessage());
        }
        return accountList;
    }
}
