package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseSearchSpecification implements AppSpecification<Course> {
    private String keyPhrase;
    private static final String SQL_PATTERN_SPECIFICATOR = "%";
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course WHERE LOWER(course_name) LIKE LOWER(?)";

    public SelectCourseSearchSpecification(String keyPhrase) {
        this.keyPhrase = keyPhrase;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        String correctedKeyPhrase = SQL_PATTERN_SPECIFICATOR + keyPhrase + SQL_PATTERN_SPECIFICATOR;
        return new String[]{correctedKeyPhrase};
    }
}
