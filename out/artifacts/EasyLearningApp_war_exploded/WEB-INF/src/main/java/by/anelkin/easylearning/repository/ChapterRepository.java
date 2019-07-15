package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j
public class ChapterRepository implements AppRepository<CourseChapter> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO course_chapter(course_id, chapter_name) VALUES (?, ?)";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM course_chapter WHERE course_chapter_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE course_chapter SET course_id = ?, chapter_name = ?" +
            "WHERE course_chapter_id = ?";

    @Override
    public boolean update(@NonNull CourseChapter chapter) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] params = {String.valueOf(chapter.getCourseId()), chapter.getName(), String.valueOf(chapter.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull CourseChapter chapter) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            String[] params = {String.valueOf(chapter.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull CourseChapter chapter) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            String[] params = {String.valueOf(chapter.getCourseId()), chapter.getName()};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public List<CourseChapter> query(@NonNull AppSpecification<CourseChapter> specification) throws RepositoryException {
        List<CourseChapter> chapterList;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i+1, params[i]);
            }
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            try (ResultSet resultSet = statement.executeQuery()) {
                chapterList = new ArrayList<>(fillChapterList(resultSet));
                log.debug("Query completed:" + statement.toString().split(":")[1]);
            }
        } catch (SQLException e) {
           throw new RepositoryException(e);
        }
        return chapterList;
    }

    private Collection<CourseChapter> fillChapterList(ResultSet resultSet) {
        List<CourseChapter> chapterList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                CourseChapter chapter = new CourseChapter();
                chapter.setId(resultSet.getInt("course_chapter_id"));
                chapter.setCourseId(resultSet.getInt("course_id"));
                chapter.setName(resultSet.getString("chapter_name"));
                chapter.setLessonAmount(resultSet.getInt("chapter_lesson_amount"));
                chapter.setDuration(resultSet.getLong("chapter_duration"));
                chapterList.add(chapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chapterList;
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
