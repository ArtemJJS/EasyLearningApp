package by.anelkin.easylearning.specification.chapter;

import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllFromCourseSpecification implements AppSpecification<CourseChapter> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course_chapter WHERE course_id = ?";
    private int course_id;

    public SelectAllFromCourseSpecification(int course_id) {
        this.course_id = course_id;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(course_id)};
    }
}
