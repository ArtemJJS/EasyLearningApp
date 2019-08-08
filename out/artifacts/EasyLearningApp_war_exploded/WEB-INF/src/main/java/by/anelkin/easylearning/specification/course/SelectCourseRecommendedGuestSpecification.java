package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseRecommendedGuestSpecification implements AppSpecification<Course> {
    private int limit;
    @Language("sql")
    private static final String QUERY = "select * from course order by RAND() limit %d";

    public SelectCourseRecommendedGuestSpecification(int limit) {
        this.limit = limit;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, limit);
    }

    @Override
    public String[] getStatementParameters() {
        return new String[0];
    }
}
