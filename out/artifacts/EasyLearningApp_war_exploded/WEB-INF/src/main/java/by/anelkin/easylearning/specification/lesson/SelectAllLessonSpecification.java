package by.anelkin.easylearning.specification.lesson;

import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllLessonSpecification implements AppSpecification<CourseLesson> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course_lesson";
    @Override
    public String getQuery() {
        return QUERY;
    }
}
