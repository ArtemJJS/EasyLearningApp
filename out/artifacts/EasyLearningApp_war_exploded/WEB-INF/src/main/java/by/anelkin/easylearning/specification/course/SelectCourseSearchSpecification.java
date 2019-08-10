package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseSearchSpecification implements AppSpecification<Course>{
    private String keyPhrase;
    private int limit;
    private int offset;
    private static final String SQL_PATTERN_SPECIFICATOR = "%";
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course WHERE LOWER(course_name) LIKE LOWER(?) and state in (2) " +
            "LIMIT %d OFFSET %d";

    public SelectCourseSearchSpecification(String keyPhrase, int limit, int offset) {
        this.keyPhrase = keyPhrase;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, limit, offset);
    }

    @Override
    public String[] getStatementParameters() {
        String correctedKeyPhrase = SQL_PATTERN_SPECIFICATOR + keyPhrase + SQL_PATTERN_SPECIFICATOR;
        return new String[]{correctedKeyPhrase};
    }
}
