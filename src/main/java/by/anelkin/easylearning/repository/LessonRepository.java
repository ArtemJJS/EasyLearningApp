package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
public class LessonRepository implements AppRepository<CourseLesson> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    @Language("sql")
    private static final String QUERY_INSERT = "{call insertLesson(?, ?, ?, ?, ? )}";
    @Language("sql")
    private static final String QUERY_DELETE = "{call deleteLesson(?)}";
    @Language("sql")
    private static final String QUERY_UPDATE = "{call updateLesson(?, ?, ?, ?)}";

    @Override
    public boolean update(@NonNull CourseLesson lesson) throws RepositoryException {
        Connection connection = pool.takeConnection();
        CallableStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareCall(QUERY_UPDATE);
            String[] params = {lesson.getName(), lesson.getPathToContent(),
                    String.valueOf(lesson.getDuration()), String.valueOf(lesson.getId())};
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
    public boolean delete(@NonNull CourseLesson lesson) throws RepositoryException {
        Connection connection = pool.takeConnection();
        CallableStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareCall(QUERY_DELETE);
            String[] params = {String.valueOf(lesson.getId())};
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
    public boolean insert(@NonNull CourseLesson lesson) throws RepositoryException {
        Connection connection = pool.takeConnection();
        CallableStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareCall(QUERY_INSERT);
            String[] params = {String.valueOf(lesson.getChapterId()), dateFormat.format(lesson.getCreationDate()),
                    lesson.getName(), lesson.getPathToContent(), String.valueOf(lesson.getDuration())};
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
    public List<CourseLesson> query(@NonNull AppSpecification<CourseLesson> specification) throws RepositoryException {
        List<CourseLesson> lessonList;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                lessonList = fillLessonList(resultSet);
            }
        } catch (SQLException e) {
            ;
            throw new RepositoryException(e);
        }
        return lessonList;
    }

    private List<CourseLesson> fillLessonList(ResultSet resultSet) throws SQLException {
        List<CourseLesson> lessonList = new ArrayList<>();
        while (resultSet.next()) {
            CourseLesson lesson = new CourseLesson();
            lesson.setId(resultSet.getInt(LESSON_ID));
            lesson.setChapterId(resultSet.getInt(COURSE_CHAPTER_ID));
            lesson.setName(resultSet.getString(LESSON_NAME));
            lesson.setCreationDate(resultSet.getDate(LESSON_CREATION_DATE));
            lesson.setDuration(resultSet.getLong(LESSON_DURATION));
            lesson.setPathToContent(resultSet.getString(LESSON_CONTENT_ADDRESS));
            lessonList.add(lesson);
        }
        return lessonList;
    }

    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Executing query:" + statement.toString().split(COLON_SYMBOL)[1]);
        statement.execute();
    }
}
