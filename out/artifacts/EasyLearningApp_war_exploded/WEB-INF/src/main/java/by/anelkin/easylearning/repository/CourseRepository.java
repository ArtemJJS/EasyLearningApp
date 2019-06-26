package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class CourseRepository implements AppRepository<Course> {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ConnectionPool pool = ConnectionPool.getInstance();
    private static final String AVATAR_PATH = "src/main/resources/course_avatar/%d.png";
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO course(course_name, course_description, course_creation_date, course_picture) " +
            "VALUES (?, ?, ?, ?)";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM course WHERE course_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE course SET course_name = ?, course_description = ?, " +
            "course_creation_date = ?, course_picture = ? WHERE course_id = ?";

    @Override
    public boolean update(@NonNull Course course) {
        int isUpdated = 0;
        Connection connection = pool.takeConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE);
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setString(3, dateFormat.format(course.getCreationDate()));
            statement.setInt(5, course.getId());
            if (course.getPicture() != null) {
                try (FileInputStream fis = new FileInputStream(course.getPicture())) {
                    statement.setBinaryStream(4, fis);
                    log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
                    statement.executeUpdate();
                    log.debug("Query completed:" + statement.toString().split(":")[1]);
                } catch (IOException e) {
                    log.error("Problems with uploading course picture into the base: " + e);
                }
            } else {
                statement.setBlob(4, (Blob) null);
                log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
                isUpdated = statement.executeUpdate();
                log.debug("Query completed:" + statement.toString().split(":")[1]);
            }
        } catch (SQLException e) {
            log.error("Error while course deleting: " + e);
        } finally {
            pool.returnConnection(connection);
        }

        return isUpdated != 0;
    }

    @Override
    public boolean delete(@NonNull Course course) {
        boolean isDeleted = false;
        Connection connection = pool.takeConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_DELETE);
            statement.setInt(1, course.getId());
            log.debug("Attempt to execute query:" + statement.toString().split(":")[1]);
            isDeleted = statement.execute();
            log.debug("Query completed:" + statement.toString().split(":")[1]);

            String pathToPicture = String.format(AVATAR_PATH, course.getId());
            Files.delete(Paths.get(pathToPicture));

        } catch (SQLException e) {
            log.error("Error while course deleting: " + e);
        } catch (IOException e) {
            log.info("Unable to remove course picture file: " + e);
        } finally {
            pool.returnConnection(connection);
        }
        return isDeleted;
    }

    @Override
    public boolean insert(@NonNull Course course) {
        boolean isInserted = false;
        Connection connection = pool.takeConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_INSERT);
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setString(3, dateFormat.format(course.getCreationDate()));
            if (course.getPicture() != null) {
                try (FileInputStream fis = new FileInputStream(course.getPicture())) {
                    statement.setBinaryStream(4, fis);
                    statement.executeUpdate();
                } catch (IOException e) {
                    log.error("Problems with uploading course picture into the base: " + e);
                }
            } else {
                statement.setBlob(4, (Blob) null);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return isInserted;
    }

    @Override
    public List<Course> query(@NonNull AppSpecification<Course> specification) {
        List<Course> courseList = new ArrayList<>();
        Connection connection = pool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            log.debug("Attempt to execute query: " + specification.getQuery());
            ResultSet resultSet = statement.executeQuery(specification.getQuery());
            log.debug("Query successfully executed: " + specification.getQuery());
            courseList.addAll(fillCourseList(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to execute query: " + e);
        } finally {
            pool.returnConnection(connection);
        }

        return courseList;
    }

    private List<Course> fillCourseList(ResultSet resultSet) {
        List<Course> courseList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setName(resultSet.getString("course_name"));
                course.setDescription(resultSet.getString("course_description"));
                course.setCreationDate(resultSet.getDate("course_creation_date"));

                Blob image = resultSet.getBlob("course_picture");
                if (image != null) {
                    String currAvatarPath = String.format(AVATAR_PATH, course.getId());
                    Files.write(Paths.get(currAvatarPath), image.getBytes(1, (int) image.length()));
                    course.setPicture(new File(currAvatarPath));
                }

                courseList.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Problems while init course image!!! " + e);
        }
        return courseList;
    }
}
