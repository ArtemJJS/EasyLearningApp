package by.anelkin.easylearning.specification.accountspec;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectRegistrDateAfter implements AppSpecification<Account> {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date date;
    @Language("sql")
    private static final String QUERY = "select * from account where acc_registration_date > '%s'";

    public SelectRegistrDateAfter(Date date) {
        this.date = date;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, dateFormat.format(date));
    }
}
