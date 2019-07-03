package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.mark.MarkSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static by.anelkin.easylearning.entity.Mark.*;

@Log4j
public class MarkRepository implements AppRepository<Mark> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String QUERY_INSERT = "INSERT INTO %s(target_id, acc_id, mark_value, mark_comment, mark_date) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM %s WHERE mark_id = ?";
    private static final String QUERY_UPDATE = "UPDATE %s SET target_id = ?, acc_id = ?, mark_value = ?, mark_comment = ?, mark_date = ? WHERE mark_id = ?";

    @Override
    public boolean update(@NonNull Mark mark) throws RepositoryException {
        String actualQuery = String.format(QUERY_UPDATE, mark.getMarkType().toString().toLowerCase());
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(actualQuery)) {
            String[] params = {String.valueOf(mark.getTargetId()), String.valueOf(mark.getAccId()), String.valueOf(mark.getMarkValue()),
                    mark.getComment(), dateFormat.format(mark.getMarkDate()), String.valueOf(mark.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull Mark mark) throws RepositoryException {
        String actualQuery = String.format(QUERY_DELETE, mark.getMarkType().toString().toLowerCase());
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(actualQuery)) {
            String[] params = {String.valueOf(mark.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull Mark mark) throws RepositoryException {
        String actualQuery = String.format(QUERY_INSERT, mark.getMarkType().toString().toLowerCase());
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(actualQuery)) {
            String[] params = {String.valueOf(mark.getTargetId()), String.valueOf(mark.getAccId()),
                    String.valueOf(mark.getMarkValue()), mark.getComment(), dateFormat.format(mark.getMarkDate())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }


    @Override
    public List<Mark> query(AppSpecification<Mark> specification) throws RepositoryException {
        List<Mark> markList;
        MarkType markType = ((MarkSpecification) specification).getMarkType();
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            log.debug("Attempt to execute query: " + specification.getQuery());
            try (ResultSet resultSet = statement.executeQuery(specification.getQuery())) {
                log.debug("Query executed successfully: " + specification.getQuery());
                markList = fillMarkList(resultSet, markType);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return markList;
    }

    private List<Mark> fillMarkList(ResultSet resultSet, MarkType markType) throws SQLException {
        List<Mark> marks = new ArrayList<>();
        while (resultSet.next()) {
            Mark mark = new Mark(markType);
            mark.setId(resultSet.getInt("mark_id"));
            mark.setTargetId(resultSet.getInt("target_id"));
            mark.setAccId(resultSet.getInt("acc_id"));
            mark.setMarkValue(resultSet.getInt("mark_value"));
            mark.setComment(resultSet.getString("mark_comment"));
            mark.setMarkDate(resultSet.getDate("mark_date"));
            marks.add(mark);
        }
        return marks;
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
