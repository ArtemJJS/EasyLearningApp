package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Course.*;

public class SelectByStateAndAuthorIdSpecification implements AppSpecification<Course> {
    private int authorId;
    private CourseState state;
    @Language("sql")
    private static final String QUERY = "select * from course where state = ? and course_id IN (select course_id from author_of_course where author_id = ?)";

    public SelectByStateAndAuthorIdSpecification(int authorId, CourseState state) {
        this.authorId = authorId;
        this.state = state;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(state.ordinal()), String.valueOf(authorId)};
    }
}
