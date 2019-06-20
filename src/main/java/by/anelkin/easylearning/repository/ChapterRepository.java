package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.text.SimpleDateFormat;
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
    private static final String QUERY_UPDATE = "UPDATE course_chapter SET course_id = ?, chapter_name = ? " +
            "WHERE course_chapter_id = ?";

    @Override
    public boolean update(@NonNull CourseChapter chapter) {
        int amnountUpdated = 0;
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE);
            statement.setInt(1, chapter.getCourseId());
            statement.setString(2, chapter.getName());
            statement.setInt(3, chapter.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            amnountUpdated = statement.executeUpdate();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return amnountUpdated != 0;
    }

    @Override
    public boolean delete(@NonNull CourseChapter chapter) {
        boolean isDeleted = false;
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_DELETE);
            statement.setInt(1, chapter.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isDeleted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return isDeleted;
    }

    @Override
    public boolean insert(@NonNull CourseChapter chapter) {
        boolean isInserted = false;
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_INSERT);
            statement.setInt(1, chapter.getCourseId());
            statement.setString(2, chapter.getName());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isInserted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return isInserted;
    }

    @Override
    public List<CourseChapter> query(@NonNull AppSpecification<CourseChapter> specification) {
        List<CourseChapter> chapterList = new ArrayList<>();
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            log.debug("Attempt to execute query:" + specification.getQuery());
            ResultSet resultSet = statement.executeQuery(specification.getQuery());
            log.debug("Query completed:" + specification.getQuery());
            chapterList.addAll(fillChapterList(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
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
                chapterList.add(chapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chapterList;
    }
}
