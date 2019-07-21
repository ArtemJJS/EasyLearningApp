package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseByNameSpecification implements AppSpecification<Course> {
    private String courseName;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course WHERE LOWER(course_name) LIKE LOWER(?)";


    public SelectCourseByNameSpecification(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{courseName};
    }
}
