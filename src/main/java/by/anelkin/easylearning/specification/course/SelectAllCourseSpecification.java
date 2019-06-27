package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllCourseSpecification implements AppSpecification<Course> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course";

    @Override
    public String getQuery() {
        return QUERY;
    }
}
