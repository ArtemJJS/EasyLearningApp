package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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

    public boolean update(@NonNull CourseLesson lesson) {
        int amountUpdated = 0;
        Connection connection = pool.getConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE);
            statement.setInt(1, lesson.getChapterId());
            statement.setString(2, lesson.getName());

            String contentAddress = lesson.getContent() == null ? null : lesson.getContent().getAbsolutePath();
            statement.setString(3, contentAddress);

            statement.setString(4, dateFormat.format(lesson.getCreationDate()));
            statement.setLong(5, lesson.getLength());
            statement.setInt(6, lesson.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            amountUpdated = statement.executeUpdate();
            log.debug("Query completed:" + statement.toString().split(":")[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return amountUpdated == 0;
    }

    @Override
    public boolean delete(@NonNull CourseLesson lesson) {
        boolean isDeleted = false;
        Connection connection = pool.getConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(QUERY_DELETE);
            statement.setInt(1, lesson.getId());
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
    public boolean insert(@NonNull CourseLesson lesson) {
        boolean isInserted = false;
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_INSERT);
            statement.setInt(1, lesson.getChapterId());
            statement.setString(2, lesson.getName());

            String contentAddress = lesson.getContent() == null ? null : lesson.getContent().getAbsolutePath();
            statement.setString(3, contentAddress);

            statement.setString(4, dateFormat.format(lesson.getCreationDate()));
            statement.setLong(5, lesson.getLength());
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
    public List<CourseLesson> query(@NonNull AppSpecification<CourseLesson> specification) {
        List<CourseLesson> lessonList = new ArrayList<>();
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            log.debug("Attempt to execute query:" + specification.getQuery());
            ResultSet resultSet = statement.executeQuery(specification.getQuery());
            log.debug("Query completed:" + specification.getQuery());
            lessonList.addAll(fillLessonList(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return lessonList;
    }

    private Collection<CourseLesson> fillLessonList(ResultSet resultSet) {
        List<CourseLesson> lessonList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                CourseLesson lesson = new CourseLesson();
                lesson.setId(resultSet.getInt("lesson_id"));
                lesson.setChapterId(resultSet.getInt("course_chapter_id"));
                lesson.setName(resultSet.getString("lesson_name"));
                lesson.setCreationDate(resultSet.getDate("lesson_creation_date"));
                lesson.setLength(resultSet.getLong("lesson_length"));
                String contentAdress = resultSet.getString("lesson_content_address");
                if (contentAdress != null) {
                    lesson.setContent(new File(contentAdress));
                }
                lessonList.add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessonList;
    }
}
