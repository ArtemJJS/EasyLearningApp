package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseByIdSpecification implements AppSpecification<Course> {
    private int id;
    @Language("sql")
    private static final String QUERY = "SELECT * from course WHERE course_id = ?";

    public SelectCourseByIdSpecification(int id) {
        this.id = id;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(id)};
    }
}
