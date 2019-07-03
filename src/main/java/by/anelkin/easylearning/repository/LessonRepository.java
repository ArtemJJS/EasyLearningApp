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
    private static final String QUERY_INSERT = "INSERT INTO course_lesson(course_chapter_id, lesson_name, " +
            "lesson_content_address, lesson_creation_date, lesson_length) VALUES (?, ?, ?, ?, ?)";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM course_lesson WHERE lesson_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE course_lesson SET course_chapter_id = ?, lesson_name = ?, " +
            "lesson_content_address = ?, lesson_creation_date = ?, lesson_length = ? WHERE lesson_id = ?";

    @Override
    public boolean update(@NonNull CourseLesson lesson) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)){
            String[] params = {String.valueOf(lesson.getChapterId()), lesson.getName(), lesson.getPathToContent(),
                    dateFormat.format(lesson.getCreationDate()), String.valueOf(lesson.getLength()), String.valueOf(lesson.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull CourseLesson lesson) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
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
             PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            String[] params = {String.valueOf(lesson.getChapterId()), lesson.getName(), lesson.getPathToContent(),
                    dateFormat.format(lesson.getCreationDate()), String.valueOf(lesson.getLength())};
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
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            log.debug("Attempt to execute query:" + specification.getQuery());
            try (ResultSet resultSet = statement.executeQuery(specification.getQuery())) {
                log.debug("Query completed:" + specification.getQuery());
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
            lesson.setLength(resultSet.getLong("lesson_length"));
            lesson.setPathToContent(resultSet.getString("lesson_content_address"));
            lessonList.add(lesson);
        }
        return lessonList;
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
