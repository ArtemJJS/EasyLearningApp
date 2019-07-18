package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Course.*;

public class SelectByStateSpecification implements AppSpecification<Course> {
    private CourseState state;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course WHERE state = ?";

    public SelectByStateSpecification(CourseState state) {
        this.state = state;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(state.ordinal())};
    }
}
