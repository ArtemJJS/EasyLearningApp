package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectByStateAndAuthorIdSpecification implements AppSpecification<Course> {
    private int authorId;
    private int state;
    @Language("sql")
    private static final String QUERY = "select * from course where state = ? and course_id IN (select course_id from author_of_course where author_id = ?)";

    public SelectByStateAndAuthorIdSpecification(int authorId, boolean state) {
        this.authorId = authorId;
        this.state = state ? 1 : 0;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(state), String.valueOf(authorId)};
    }
}
