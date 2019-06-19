package by.anelkin.easylearning.specification.account_spec;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAllAccount implements AppSpecification<Account> {
    @Language("sql")
    private static final String QUERY = "select * from account";


    @Override
    public String getQuery() {
        return QUERY;
    }
}
