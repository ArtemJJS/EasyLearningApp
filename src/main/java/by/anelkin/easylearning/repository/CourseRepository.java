package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.intellij.lang.annotations.Language;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static by.anelkin.easylearning.entity.Course.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;
/**
 * Class to process operations with data base and {@link Course} entity
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
public class CourseRepository implements AppRepository<Course> {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String PATH_TO_PICTURE = "/resources/course_img/";
    private static final String PATH_TO_PICTURE_UPDATE = "/resources/course_img_update/";
    private static final String PATH_SPLITTER = "/";
    private ConnectionPool pool = ConnectionPool.getInstance();
    @Language("sql")
    private static final String QUERY_INSERT = "INSERT INTO course(course_author_id ,course_name, course_description, course_creation_date, course_picture, course_price, state, update_img_path) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    @Language("sql")
    private static final String QUERY_DELETE = "UPDATE course SET state = 0 WHERE course_id = ?";
    @Language("sql")
    private static final String QUERY_UPDATE = "UPDATE course SET course_name = ?, course_description = ?, " +
            "course_creation_date = ?, course_picture = ?, course_price = ?, state = ?, update_img_path = ? WHERE course_id = ?";

    /**
     * updates course in the db
     *
     * @param course - updated {@link Course}
     * @return - true if successful
     * @throws RepositoryException when faced{@link SQLException}
     */
    @Override
    public boolean update(@NonNull Course course) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            String[] pathToImg = course.getPathToPicture().split(PATH_SPLITTER);
            String[] pathToImgUpdate = course.getUpdatePhotoPath().split(PATH_SPLITTER);
            String[] params = {course.getName(), course.getDescription(),
                    dateFormat.format(course.getCreationDate()), pathToImg[pathToImg.length - 1],
                    course.getPrice().toString(), String.valueOf(course.getState().ordinal()),
                    pathToImgUpdate[pathToImgUpdate.length - 1], String.valueOf(course.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    /**
     * delete course in the db
     *
     * @param course - delete {@link Course}
     * @return - true if successful
     * @throws RepositoryException when faced{@link SQLException}
     */
    @Override
    public boolean delete(@NonNull Course course) throws RepositoryException {
        //set state = 0 (freeze) instead of deleting
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            String[] params = {String.valueOf(course.getId())};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }
    /**
     * inserts course in the db
     *
     * @param course - inserted {@link Course}
     * @return - true if successful
     * @throws RepositoryException when faced{@link SQLException}
     */
    @Override
    public boolean insert(@NonNull Course course) throws RepositoryException {
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            String[] pathToImg = course.getPathToPicture().split(PATH_SPLITTER);
            String[] pathToImgUpdate = course.getUpdatePhotoPath().split(PATH_SPLITTER);
            String[] params = {String.valueOf(course.getAuthorId()), course.getName(), course.getDescription(), dateFormat.format(course.getCreationDate()),
                    pathToImg[pathToImg.length - 1], course.getPrice().toString(),
                    String.valueOf(course.getState().ordinal()), pathToImgUpdate[pathToImgUpdate.length - 1]};
            setParametersAndExecute(statement, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return true;
    }

    /**
     * query {@link Course} which satisfy the requirements of particular {@link AppSpecification}
     *
     * @param specification query specification
     * @return - list of {@link Course}
     * @throws RepositoryException when faced{@link SQLException}
     */
    @Override
    public List<Course> query(@NonNull AppSpecification<Course> specification) throws RepositoryException {
        List<Course> courseList;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(specification.getQuery())) {
            String[] params = specification.getStatementParameters();
                for (int i = 0; i < params.length; i++) {
                    statement.setString(i + 1, params[i]);
                }
            try (ResultSet resultSet = statement.executeQuery()) {
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
            course.setId(resultSet.getInt(COURSE_ID));
            course.setName(resultSet.getString(COURSE_NAME));
            course.setAuthorId(resultSet.getInt(COURSE_AUTHOR_ID));
            course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
            course.setCreationDate(resultSet.getDate(COURSE_CREATION_DATE));
            course.setPathToPicture(PATH_TO_PICTURE + resultSet.getString(COURSE_PICTURE));
            course.setPrice(new BigDecimal(resultSet.getString(COURSE_PRICE)));
            course.setLessonAmount(resultSet.getInt(COURSE_LESSON_AMOUNT));
            course.setDuration(resultSet.getLong(COURSE_DURATION));
            course.setState(CourseState.values()[resultSet.getInt(COURSE_STATE)]);
            String courseAvgMark = resultSet.getString(COURSE_AVG_MARK);

            String updatePhotoFileName = resultSet.getString(COURSE_UPDATE_IMG_PATH);
            if (updatePhotoFileName == null || updatePhotoFileName.isEmpty()) {
                course.setUpdatePhotoPath(EMPTY_STRING);
            } else {
                course.setUpdatePhotoPath(PATH_TO_PICTURE_UPDATE + updatePhotoFileName);
            }

            if (courseAvgMark != null) {
                course.setAvgMark(Double.parseDouble(courseAvgMark));
            }
            courseList.add(course);
        }
        return courseList;
    }

    private void setParametersAndExecute(PreparedStatement statement, String[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        log.debug("Executing query:" + statement.toString().split(COLON_SYMBOL)[1]);
        statement.execute();
    }
}
