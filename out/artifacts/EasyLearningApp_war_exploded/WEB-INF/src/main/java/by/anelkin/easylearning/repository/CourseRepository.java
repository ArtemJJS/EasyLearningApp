package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.exeption.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class CourseRepository implements AppRepository<Course> {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ConnectionPool pool = ConnectionPool.getInstance();
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO course(course_name, course_description, course_creation_date, course_picture, course_price) " +
            "VALUES (?, ?, ?, ?, ?)";
    @Language("sql")
    private static final String QUERY_DELETE = "DELETE FROM course WHERE course_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE course SET course_name = ?, course_description = ?, " +
            "course_creation_date = ?, course_picture = ?, course_price = ? WHERE course_id = ?";

    @Override
    public boolean update(@NonNull Course course) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] params = {course.getName(), course.getDescription(), dateFormat.format(course.getCreationDate()),
                    course.getPathToPicture(), course.getPrice().toString(), String.valueOf(course.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull Course course) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            String[] params = {String.valueOf(course.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull Course course) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            String[] params = {course.getName(), course.getDescription(), dateFormat.format(course.getCreationDate()),
                    course.getPathToPicture(), course.getPrice().toString()};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
           throw new RepositoryException(e);
        }
        return true;
    }

    @Override
    public List<Course> query(@NonNull AppSpecification<Course> specification) throws RepositoryException {
        List<Course> courseList;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())){
            String[] params = specification.getStatementParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setString(i+1, params[i]);
            }
            log.debug("Attempt to execute query: " + specification.getQuery());
            try(ResultSet resultSet = statement.executeQuery(specification.getQuery())) {
                log.debug("Query successfully executed: " + specification.getQuery());
                courseList = fillCourseList(resultSet);
            }
        } catch (SQLException e) {
           throw new RepositoryException(e);
        }
        return courseList;
    }

    private List<Course> fillCourseList(ResultSet resultSet) throws SQLException {
        List<Course> courseList = new ArrayList<>();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setName(resultSet.getString("course_name"));
                course.setDescription(resultSet.getString("course_description"));
                course.setCreationDate(resultSet.getDate("course_creation_date"));
                course.setPathToPicture(resultSet.getString("course_picture"));
                course.setPrice(new BigDecimal(resultSet.getString("course_price")));
                courseList.add(course);
            }
        return courseList;
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
