package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.RepositoryException;
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
    private static final String PATH_TO_AVATAR = "resources/account_avatar/";
    private static final String PATH_TO_AVATAR_UPDATE = "resources/account_avatar_update/";
    private static final String PATH_SPLITTER = "/";
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE account SET acc_password = ?, acc_email = ?, acc_name = ?," +
            " acc_surname = ?, acc_birthdate = ?, acc_phone_number = ?, acc_registration_date = ?, acc_about = ?," +
            " acc_photo_path = ?, acc_type = ?, update_photo_path = ?, acc_pass_salt = ? WHERE acc_login = ?";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM account WHERE acc_login = ?";
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO account(acc_login, acc_password, acc_email, acc_name, acc_surname, " +
            "acc_birthdate, acc_phone_number, acc_registration_date, acc_about, acc_photo_path, acc_type, update_photo_path, acc_pass_salt) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public boolean update(@NonNull Account account) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] pathToPhotoParts = account.getPathToPhoto().split(PATH_SPLITTER);
            String[] pathUpdatePhotoParts = account.getUpdatePhotoPath().split("/");
            String[] params = {account.getPassword(), account.getEmail(), account.getName(), account.getSurname(),
                    dateFormat.format(account.getBirthDate()), account.getPhoneNumber(), dateFormat.format(account.getRegistrDate()),
                    account.getAbout(), pathToPhotoParts[pathToPhotoParts.length - 1], String.valueOf(account.getType().ordinal()),
                    pathUpdatePhotoParts[pathUpdatePhotoParts.length - 1], account.getPassSalt(), account.getLogin()};
            setParametersAndExecute(statement, params);
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
            setParametersAndExecute(statement, params);
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
            String[] pathToPhotoParts = account.getPathToPhoto().split("/");
            String[] pathUpdatePhotoParts = account.getUpdatePhotoPath().split("/");
            String[] params = {account.getLogin(), account.getPassword(), account.getEmail(), account.getName(), account.getSurname(),
                    dateFormat.format(account.getBirthDate()), account.getPhoneNumber(), dateFormat.format(account.getRegistrDate()),
                    account.getAbout(), pathToPhotoParts[pathToPhotoParts.length - 1], String.valueOf(account.getType().ordinal()),
                    pathUpdatePhotoParts[pathUpdatePhotoParts.length - 1], account.getPassSalt()};
            setParametersAndExecute(statement, params);
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
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
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
            account.setBalance(resultSet.getBigDecimal("acc_balance"));
            account.setPassSalt(resultSet.getString("acc_pass_salt"));

            String updatePhotoFileName = resultSet.getString("update_photo_path");
            if (updatePhotoFileName == null || updatePhotoFileName.isEmpty()) {
                account.setUpdatePhotoPath("");
            } else {
                account.setUpdatePhotoPath(PATH_TO_AVATAR_UPDATE + updatePhotoFileName);
            }

            String avatarFileName = resultSet.getString("acc_photo_path");
            account.setPathToPhoto(PATH_TO_AVATAR + avatarFileName);

            account.setAvgMark(resultSet.getDouble("avg_mark"));

            // do it with int because need default to handle possible exception
            int typeId = resultSet.getInt("acc_type");
            switch (typeId) {
                case 0:
                    account.setType(GUEST);
                    break;
                case 1:
                    account.setType(USER);
                    break;
                case 2:
                    account.setType(AUTHOR);
                    break;
                case 3:
                    account.setType(ADMIN);
                    break;
                default:
                    throw new SQLException("Wrong type parameter from data base!!! " +
                            "User#" + account.getId() + ". Type: " + typeId);
            }
            accountList.add(account);
        }
        return accountList;
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
