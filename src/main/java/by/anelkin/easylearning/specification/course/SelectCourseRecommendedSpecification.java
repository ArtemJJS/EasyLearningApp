package by.anelkin.easylearning.specification.course;


import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseRecommendedSpecification implements AppSpecification<Course> {
    private int limit;
    private int accId;
    @Language("sql")
private static final String QUERY = "select * from course where state = 2 and course_id " +
            "not in (select course_id from user_purchased_course where user_id = ?) order by rand() limit %d";

    public SelectCourseRecommendedSpecification(int limit, int accId) {
        this.limit = limit;
        this.accId = accId;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, limit);
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(accId)};
    }
}
