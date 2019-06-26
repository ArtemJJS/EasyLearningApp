package by.anelkin.easylearning.specification.course_spec;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseById implements AppSpecification<Course> {
    private int id;
    @Language("sql")
    private static final String QUERY = "SELECT * from course WHERE course_id = %d";

    public SelectCourseById(int id) {
        this.id = id;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, id);
    }
}
