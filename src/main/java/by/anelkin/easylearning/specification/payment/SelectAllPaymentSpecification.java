package by.anelkin.easylearning.specification.payment;

import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllPaymentSpecification implements AppSpecification<Payment> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM user_payment";

    @Override
    public String getQuery() {
        return QUERY;
    }
}
