package by.anelkin.easylearning.specification.lesson;

import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectByChapterIdSpecification implements AppSpecification<CourseLesson> {
    private int chapterId;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM course_lesson WHERE course_chapter_id = ?";

    public SelectByChapterIdSpecification(int chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(chapterId)};
    }
}
