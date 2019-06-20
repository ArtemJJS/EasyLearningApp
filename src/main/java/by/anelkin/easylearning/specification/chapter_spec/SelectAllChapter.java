package by.anelkin.easylearning.specification.chapter_spec;

import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllChapter implements AppSpecification<CourseChapter> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course_chapter";

    @Override
    public String getQuery() {
        return QUERY;
    }
}
