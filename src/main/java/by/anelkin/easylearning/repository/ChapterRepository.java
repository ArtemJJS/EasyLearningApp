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

import static by.anelkin.easylearning.util.GlobalConstant.*;

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
            ;
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
            ;
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
            ;
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
                statement.setString(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                chapterList = new ArrayList<>(fillChapterList(resultSet));
            }
        } catch (SQLException e) {
            ;
            throw new RepositoryException(e);
        }
        return chapterList;
    }

    private Collection<CourseChapter> fillChapterList(ResultSet resultSet) throws SQLException {
        List<CourseChapter> chapterList = new ArrayList<>();
        while (resultSet.next()) {
            CourseChapter chapter = new CourseChapter();
            chapter.setId(resultSet.getInt(COURSE_CHAPTER_ID));
            chapter.setCourseId(resultSet.getInt(COURSE_ID));
            chapter.setName(resultSet.getString(CHAPTER_NAME));
            chapter.setLessonAmount(resultSet.getInt(CHAPTER_LESSON_AMOUNT));
            chapter.setDuration(resultSet.getLong(CHAPTER_DURATION));
            chapterList.add(chapter);
        }
        return chapterList;
    }

    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Executing query:" + statement.toString().split(COLON_SYMBOL)[1]);
        statement.execute();
    }
}
