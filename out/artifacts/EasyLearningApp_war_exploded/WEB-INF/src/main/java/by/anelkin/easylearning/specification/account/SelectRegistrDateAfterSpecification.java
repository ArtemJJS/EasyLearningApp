package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectRegistrDateAfterSpecification implements AppSpecification<Account>, TempAccSpec{
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date date;
    @Language("sql")
    private static final String QUERY = "select * from account where acc_registration_date > ?";

    public SelectRegistrDateAfterSpecification(Date date) {
        this.date = date;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{dateFormat.format(date)};
    }
}
