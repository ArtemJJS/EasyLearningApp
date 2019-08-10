package by.anelkin.easylearning.specification.payment;

import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectPaymentPaginationByAccIdSpecification implements AppSpecification<Payment> {
    private int currAccId;
    private int limit;
    private int offset;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM user_payment WHERE acc_id = ? LIMIT %d OFFSET %d";

    public SelectPaymentPaginationByAccIdSpecification(int currAccId, int limit, int offset) {
        this.currAccId = currAccId;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, limit, offset);
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(currAccId)};
    }
}
