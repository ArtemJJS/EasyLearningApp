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
import static by.anelkin.easylearning.entity.Mark.MarkType.*;

@Log4j
public class MarkRepository implements AppRepository<Mark> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String QUERY_INSERT_COURSE_MARK = "{call InsertCourseMark(?, ?, ?, ?, ?)}";
    private static final String QUERY_INSERT_AUTHOR_MARK = "{call InsertAuthorMark(?, ?, ?, ?, ?)}";
    private static final String QUERY_UPDATE_COURSE_MARK = "{call updateCourseMark(?, ?, ?, ?, ?, ?)}";
    private static final String QUERY_UPDATE_AUTHOR_MARK = "{call updateAuthorMark(?, ?, ?, ?, ?, ?)}";
    private static final String QUERY_DELETE_COURSE_MARK = "{call deleteCourseMark(?)}";
    private static final String QUERY_DELETE_AUTHOR_MARK = "{call deleteAuthorMark(?)}";



    @Override
    public boolean update(@NonNull Mark mark) throws RepositoryException {
        String actualQuery = mark.getMarkType() == AUTHOR_MARK ? QUERY_UPDATE_AUTHOR_MARK : QUERY_UPDATE_COURSE_MARK;
        try (Connection connection = pool.takeConnection();
             // FIXME: 7/15/2019 переделать на callableStatement?
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
        String actualQuery = mark.getMarkType() == AUTHOR_MARK ? QUERY_DELETE_AUTHOR_MARK : QUERY_DELETE_COURSE_MARK;
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
        String actualQuery = mark.getMarkType() == AUTHOR_MARK ? QUERY_INSERT_AUTHOR_MARK : QUERY_INSERT_COURSE_MARK;
        try (Connection connection = pool.takeConnection();
             CallableStatement statement = connection.prepareCall(actualQuery)){
            // TODO: 7/8/2019 спросить про два запроса
            String[] params1 = {String.valueOf(mark.getTargetId()), String.valueOf(mark.getAccId()),
                    String.valueOf(mark.getMarkValue()), mark.getComment(), dateFormat.format(mark.getMarkDate())};
            setParametersAndExecute(statement, params1);
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
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            try (ResultSet resultSet = statement.executeQuery()) {
                log.debug("Query completed:" + statement.toString().split(":")[1]);
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
            // data from account table (for queries with join)
            mark.setAccLogin(resultSet.getString("acc_login"));
            mark.setAccPathToPhoto(resultSet.getString("acc_photo_path"));
            // TODO: 7/9/2019 перенести проверку фото в другое место (перед тем как попадает в базу)
            if (mark.getAccPathToPhoto() == null){
                mark.setAccPathToPhoto("resources/account_avatar/default_acc_avatar.png");
            }
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
