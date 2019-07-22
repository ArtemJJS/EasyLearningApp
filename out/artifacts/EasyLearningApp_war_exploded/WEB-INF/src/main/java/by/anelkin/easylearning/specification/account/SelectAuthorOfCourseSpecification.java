package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAuthorOfCourseSpecification implements AppSpecification<Account> {
    private int courseId;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM account " +
            "WHERE acc_id = (SELECT course_author_id FROM course WHERE course_id = ?)";


    public SelectAuthorOfCourseSpecification(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(courseId)};
    }
}
