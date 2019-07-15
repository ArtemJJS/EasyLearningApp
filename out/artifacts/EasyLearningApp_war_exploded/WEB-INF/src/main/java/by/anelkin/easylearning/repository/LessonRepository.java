package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class LessonRepository implements AppRepository<CourseLesson> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Language("sql")
    private static final String QUERY_INSERT = "{call insertLesson(?, ?, ?, ?, ? )}";
    @Language("sql")
    private static final String QUERY_DELETE = "{call deleteLesson(?)}";
    @Language("sql")
    private static final String QUERY_UPDATE = "{call updateLesson(?, ?, ?, ?)}";

    @Override
    public boolean update(@NonNull CourseLesson lesson) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             CallableStatement statement = connection.prepareCall(QUERY_UPDATE)) {
            String[] params = {lesson.getName(), lesson.getPathToContent(),
                    String.valueOf(lesson.getDuration()), String.valueOf(lesson.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        // FIXME: 7/16/2019 плохо что возврат true без возможности возврата false
        return true;
    }

    // TODO: 7/16/2019 может быть сделать в параметрах просто инт id ???
    @Override
    public boolean delete(@NonNull CourseLesson lesson) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             CallableStatement statement = connection.prepareCall(QUERY_DELETE)) {
            String[] params = {String.valueOf(lesson.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull CourseLesson lesson) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             CallableStatement statement = connection.prepareCall(QUERY_INSERT)) {
            String[] params = {String.valueOf(lesson.getChapterId()), dateFormat.format(lesson.getCreationDate()),
                    lesson.getName(), lesson.getPathToContent(), String.valueOf(lesson.getDuration())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
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
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            try (ResultSet resultSet = statement.executeQuery()) {
                log.debug("Query completed:" + statement.toString().split(":")[1]);
                lessonList = fillLessonList(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return lessonList;
    }

    private List<CourseLesson> fillLessonList(ResultSet resultSet) throws SQLException {
        List<CourseLesson> lessonList = new ArrayList<>();
        while (resultSet.next()) {
            CourseLesson lesson = new CourseLesson();
            lesson.setId(resultSet.getInt("lesson_id"));
            lesson.setChapterId(resultSet.getInt("course_chapter_id"));
            lesson.setName(resultSet.getString("lesson_name"));
            lesson.setCreationDate(resultSet.getDate("lesson_creation_date"));
            lesson.setDuration(resultSet.getLong("lesson_duration"));
            lesson.setPathToContent(resultSet.getString("lesson_content_address"));
            lessonList.add(lesson);
        }
        return lessonList;
    }

    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Attempt to execute query:" + statement.toString());
        statement.execute();
        log.debug("Query completed:" + statement.toString());
    }
}
