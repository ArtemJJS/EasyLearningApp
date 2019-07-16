package by.anelkin.easylearning.specification.payment;

import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectPaymentByAccountIdSpecification implements AppSpecification<Payment> {
    private int currAccId;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM user_payment WHERE acc_id = ?";

    public SelectPaymentByAccountIdSpecification(int currAccId) {
        this.currAccId = currAccId;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(currAccId)};
    }
}
