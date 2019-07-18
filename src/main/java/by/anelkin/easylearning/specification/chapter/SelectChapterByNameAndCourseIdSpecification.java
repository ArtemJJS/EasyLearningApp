package by.anelkin.easylearning.specification.chapter;

import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectChapterByNameAndCourseIdSpecification implements AppSpecification<CourseChapter> {
    private int courseId;
    private String chapterName;
    @Language("sql")
    private static final String QUERY = "SELECT * from course_chapter WHERE course_id = ? AND chapter_name = ?";

    public SelectChapterByNameAndCourseIdSpecification(String chapterName, int courseId) {
        this.courseId = courseId;
        this.chapterName = chapterName;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(courseId), chapterName};
    }
}
