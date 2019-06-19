package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.markspec.MarkSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static by.anelkin.easylearning.entity.Mark.*;

@Log4j
public class MarkRepository implements AppRepository<Mark> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO %s(target_id, acc_id, mark_value, mark_comment, mark_date) " +
            "VALUES (?, ?, ?, ?, ?)";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM %s WHERE mark_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE %s SET target_id = ?, acc_id = ?, mark_value = ?, mark_comment = ?, mark_date = ? WHERE mark_id = ?";

    @Override
    public boolean update(@NonNull Mark mark) {
        boolean isUpdated = false;
        String actualQuery = String.format(QUERY_UPDATE, mark.getMarkType().toString().toLowerCase());
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(actualQuery);
            statement.setInt(1, mark.getTargetId());
            statement.setInt(2, mark.getAccId());
            statement.setInt(3, mark.getMarkValue());
            statement.setString(4, mark.getComment());
            statement.setString(5, dateFormat.format(mark.getMarkDate()));
            statement.setInt(6, mark.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isUpdated = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        } finally {
            pool.returnConnection(connection);
        }
        return isUpdated;
    }

    @Override
    public boolean delete(@NonNull Mark mark) {
        boolean isDeleted = false;
        String actualQuery = String.format(QUERY_DELETE, mark.getMarkType().toString().toLowerCase());
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(actualQuery);
            statement.setInt(1, mark.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isDeleted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        } finally {
            pool.returnConnection(connection);
        }
        return isDeleted;
    }

    @Override
    public boolean insert(@NonNull Mark mark) {
        boolean isInserted;
        Connection connection = pool.getConnection();
        String actualQuery = String.format(QUERY_INSERT, mark.getMarkType().toString().toLowerCase());
        try {
            PreparedStatement statement = connection.prepareStatement(actualQuery);
            statement.setInt(1, mark.getTargetId());
            statement.setInt(2, mark.getAccId());
            statement.setInt(3, mark.getMarkValue());
            statement.setString(4, mark.getComment());
            statement.setString(5, dateFormat.format(mark.getMarkDate()));
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isInserted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        } finally {
            pool.returnConnection(connection);
        }
        return isInserted;
    }


    @Override
    public List<Mark> query(AppSpecification<Mark> specification) {
        List<Mark> markList = new ArrayList<>();
        MarkType markType = ((MarkSpecification) specification).getMarkType();
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            log.debug("Attempt to execute query: " + specification.getQuery());
            ResultSet resultSet = statement.executeQuery(specification.getQuery());
            log.debug("Query executed successfully: " + specification.getQuery());
            markList.addAll(fillMarkList(resultSet, markType));
        } catch (SQLException e) {
            throw new RuntimeException("Wrong query!!! " + e);
        } finally {
            pool.returnConnection(connection);
        }
        return markList;
    }

    private Collection<Mark> fillMarkList(ResultSet resultSet, MarkType markType) {
        List<Mark> marks = new ArrayList<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marks;
    }

}
