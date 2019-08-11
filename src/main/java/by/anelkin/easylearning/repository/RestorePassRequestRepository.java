package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.RestorePassRequest;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
public class RestorePassRequestRepository implements AppRepository<RestorePassRequest> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    @Language("sql")
    private static final String QUERY_DELETE = "delete from restore_pass_requests where acc_id = ?";
    @Language("sql")
    private static final String QUERY_INSERT = "insert into restore_pass_requests(acc_id, uuid) VALUES(?, ?)";

    @Override
    public boolean update(@NonNull RestorePassRequest restorePassRequest) throws RepositoryException {
        return false;
    }

    @Override
    public boolean delete(@NonNull RestorePassRequest restorePassRequest) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            String[] params = {String.valueOf(restorePassRequest.getAccId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            ;
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull RestorePassRequest restorePassRequest) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            String[] params = {String.valueOf(restorePassRequest.getAccId()), restorePassRequest.getUuid()};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            ;
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public List<RestorePassRequest> query(@NonNull AppSpecification<RestorePassRequest> specification) throws RepositoryException {
        List<RestorePassRequest> restorePassRequests;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                restorePassRequests = new ArrayList<>(fillRestorePassRequestList(resultSet));
            }
        } catch (SQLException e) {
            ;
            throw new RepositoryException(e);
        }
        return restorePassRequests;
    }

    private List<RestorePassRequest> fillRestorePassRequestList(ResultSet resultSet) throws SQLException {
        List<RestorePassRequest> list = new ArrayList<>();
        while (resultSet.next()) {
            int accId = resultSet.getInt(ACC_ID);
            String uuid = resultSet.getString(RESTORE_PASS_UUID);
            RestorePassRequest restorePassRequest = new RestorePassRequest(accId, uuid);
            list.add(restorePassRequest);
        }
        return list;
    }


    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Executing query:" + statement.toString().split(COLON_SYMBOL)[1]);
        statement.execute();
    }

}
