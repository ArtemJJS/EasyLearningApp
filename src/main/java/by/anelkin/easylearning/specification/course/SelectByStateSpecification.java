package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectByStateSpecification implements AppSpecification<Course> {
    private int state;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course WHERE state = ?";

    public SelectByStateSpecification(boolean isApproved) {
        this.state= isApproved? 1 : 0;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(state)};
    }
}
