package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Payment.PaymentCode.*;

public class SelectAccPurchasedCourseSpecification implements AppSpecification<Account> {
    private int courseId;
    @Language("sql")
    private static final String QUERY = "select * from account where acc_id " +
            "in (select distinct acc_id from user_payment where course_id = ? and payment_code in (%d, %d))";

    public SelectAccPurchasedCourseSpecification(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, BUY_COURSE_FROM_BALANCE.getCode(), BUY_COURSE_WITH_CARD.getCode());
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(courseId)};
    }
}
