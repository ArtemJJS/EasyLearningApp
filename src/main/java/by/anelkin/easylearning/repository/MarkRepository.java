package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import by.anelkin.easylearning.specification.mark.MarkSpecification;
import by.anelkin.easylearning.specification.mark.SelectByTargetIdWithWriterInfoSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.anelkin.easylearning.entity.Mark.*;
import static by.anelkin.easylearning.entity.Mark.MarkType.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
public class MarkRepository implements AppRepository<Mark> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static final String PATH_TO_PICTURE = "/resources/account_avatar/";
    private static final String QUERY_INSERT_COURSE_MARK = "{call InsertCourseMark(?, ?, ?, ?, ?)}";
    private static final String QUERY_INSERT_AUTHOR_MARK = "{call InsertAuthorMark(?, ?, ?, ?, ?)}";
    private static final String QUERY_UPDATE_COURSE_MARK = "{call updateCourseMark(?, ?, ?, ?, ?, ?)}";
    private static final String QUERY_UPDATE_AUTHOR_MARK = "{call updateAuthorMark(?, ?, ?, ?, ?, ?)}";
    private static final String QUERY_DELETE_COURSE_MARK = "{call deleteCourseMark(?)}";
    private static final String QUERY_DELETE_AUTHOR_MARK = "{call deleteAuthorMark(?)}";


    @Override
    public boolean update(@NonNull Mark mark) throws RepositoryException {
        String actualQuery = mark.getMarkType() == AUTHOR_MARK ? QUERY_UPDATE_AUTHOR_MARK : QUERY_UPDATE_COURSE_MARK;
        Connection connection = pool.takeConnection();
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(actualQuery);
            String[] params = {String.valueOf(mark.getTargetId()), String.valueOf(mark.getAccId()), String.valueOf(mark.getMarkValue()),
                    mark.getComment(), String.valueOf(mark.getMarkDate()), String.valueOf(mark.getId())};
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
    public boolean delete(@NonNull Mark mark) throws RepositoryException {
        String actualQuery = mark.getMarkType() == AUTHOR_MARK ? QUERY_DELETE_AUTHOR_MARK : QUERY_DELETE_COURSE_MARK;
        Connection connection = pool.takeConnection();
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(actualQuery);
            String[] params = {String.valueOf(mark.getId())};
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
    public boolean insert(@NonNull Mark mark) throws RepositoryException {
        String actualQuery = mark.getMarkType() == AUTHOR_MARK ? QUERY_INSERT_AUTHOR_MARK : QUERY_INSERT_COURSE_MARK;
        Connection connection = pool.takeConnection();
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareCall(actualQuery);
            String[] params1 = {String.valueOf(mark.getTargetId()), String.valueOf(mark.getAccId()),
                    String.valueOf(mark.getMarkValue()), mark.getComment(), String.valueOf(mark.getMarkDate())};
            setParametersAndExecute(statement, params1);
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
    public List<Mark> query(AppSpecification<Mark> specification) throws RepositoryException {
        List<Mark> markList;
        MarkType markType = ((MarkSpecification) specification).getMarkType();
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                markList = fillMarkList(resultSet, markType, specification);
            }
        } catch (SQLException e) {
            ;
            throw new RepositoryException(e);
        }
        return markList;
    }

    private List<Mark> fillMarkList(ResultSet resultSet, MarkType markType, AppSpecification<Mark> specification) throws SQLException {
        List<Mark> marks = new ArrayList<>();
        while (resultSet.next()) {
            Mark mark = new Mark(markType);
            mark.setId(resultSet.getInt(MARK_ID));
            mark.setTargetId(resultSet.getInt(MARK_TARGET_ID));
            mark.setAccId(resultSet.getInt(ACC_ID));
            mark.setMarkValue(resultSet.getInt(MARK_VALUE));
            mark.setComment(resultSet.getString(MARK_COMMENT));
            mark.setMarkDate(resultSet.getLong(MARK_DATE));
            // data from account table (for queries with join)
            if (specification instanceof SelectByTargetIdWithWriterInfoSpecification) {
                mark.setAccLogin(resultSet.getString(ACC_LOGIN));
                mark.setAccPathToPhoto(PATH_TO_PICTURE + resultSet.getString(ACC_PHOTO_PATH));
            }
            marks.add(mark);
        }
        return marks;
    }

    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Executing query:" + statement.toString().split(COLON_SYMBOL)[1]);
        statement.execute();
    }

}
