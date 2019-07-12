package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectByCourseIdSpecification implements AppSpecification<Account> {
    private int courseId;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM account WHERE acc_id " +
            "IN (SELECT author_id FROM author_of_course WHERE course_id = ?)";


    public SelectByCourseIdSpecification(int courseId) {
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
