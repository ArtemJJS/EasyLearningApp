package by.anelkin.easylearning.specification.course;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectCourseUpdateImgSpecification implements AppSpecification<Course> {
    @Language("sql")
    private static final String QUERY = "select * from course where update_img_path NOT IN ('' AND NULL)";

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[0];
    }
}
