package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exeption.RepositoryException;
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
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE account SET acc_password = ?, acc_email = ?, acc_name = ?," +
            " acc_surname = ?, acc_birthdate = ?, acc_phone_number = ?, acc_registration_date = ?, acc_about = ?," +
            " acc_photo_path = ?, acc_type = ? WHERE acc_login = ?";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM account WHERE acc_login = ?";
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO account(acc_login, acc_password, acc_email, acc_name, acc_surname, " +
            "acc_birthdate, acc_phone_number, acc_registration_date, acc_about, acc_photo_path, acc_type) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public boolean update(@NonNull Account account) throws RepositoryException {
        // TODO: 6/18/2019 Сделать нормальное шифрование пароля
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] params = {account.getPassword(), account.getEmail(), account.getName(), account.getSurname(),
                    dateFormat.format(account.getBirthDate()), account.getPhoneNumber(), dateFormat.format(account.getRegistrDate()),
                    account.getAbout(), account.getPathToPhoto(), String.valueOf(account.getType().ordinal()), account.getLogin()};
            setStatementParameters(statement, params);
            executeAndLog(statement);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull Account account) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            String[] params = {account.getLogin()};
            setStatementParameters(statement, params);
            executeAndLog(statement);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull Account account) throws RepositoryException {
        // TODO: 6/18/2019 Сделать нормальное шифрование пароля
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            String[] params = {account.getLogin(), account.getPassword(), account.getEmail(), account.getName(), account.getSurname(),
                    dateFormat.format(account.getBirthDate()), account.getPhoneNumber(), dateFormat.format(account.getRegistrDate()),
                    account.getAbout(), account.getPathToPhoto(), String.valueOf(account.getType().ordinal())};
            setStatementParameters(statement, params);
            executeAndLog(statement);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public List<Account> query(@NonNull AppSpecification<Account> specification) throws RepositoryException {
        List<Account> accountList;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
            setStatementParameters(statement, params);
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            try (ResultSet resultSet = statement.executeQuery()) {
                accountList = new ArrayList<>(fillAccountList(resultSet));
                log.debug("Query completed:" + statement.toString().split(":")[1]);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return accountList;
    }

    private List<Account> fillAccountList(ResultSet resultSet) throws SQLException {
        List<Account> accountList = new ArrayList<>();
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
            account.setPathToPhoto(resultSet.getString("acc_photo_path"));

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
        return accountList;
    }

    private void setStatementParameters(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
    }

    private void executeAndLog(PreparedStatement statement) throws SQLException {
        log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
        statement.execute();
        log.debug("Query completed:" + statement.toString().split(":")[1]);
    }
}
