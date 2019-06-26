package by.anelkin.easylearning.specification.lesson_spec;

import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllLesson implements AppSpecification<CourseLesson> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course_lesson";
    @Override
    public String getQuery() {
        return QUERY;
    }
}
