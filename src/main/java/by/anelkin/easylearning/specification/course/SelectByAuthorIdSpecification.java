package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectByAuthorIdSpecification implements AppSpecification<Course> {
    private int authorId;
    @Language("sql")
    private static final String QUERY = "select * from course where course_author_id = ?";

    public SelectByAuthorIdSpecification(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(authorId)};
    }
}
