package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCoursesPurchasedByUserSpecification implements AppSpecification<Course> {
    private int userId;
    @Language("sql")
    private static final String QUERY = "select * from user_purchased_course as t1 LEFT JOIN course as t2 ON t1.course_id = t2.course_id where t1.user_id = ?";

    public SelectCoursesPurchasedByUserSpecification(int userId) {
        this.userId = userId;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(userId)};
    }
}
