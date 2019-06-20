package by.anelkin.easylearning.specification.chapter_spec;

import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllFromCourse implements AppSpecification<CourseChapter> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course_chapter WHERE course_id = %d";
    private int course_id;

    public SelectAllFromCourse(int course_id) {
        this.course_id = course_id;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, course_id);
    }
}
