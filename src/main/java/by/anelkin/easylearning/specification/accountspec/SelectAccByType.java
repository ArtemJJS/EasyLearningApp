package by.anelkin.easylearning.specification.accountspec;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Account.*;

public class SelectAccByType implements AppSpecification<Account> {
    private AccountType type;
    @Language("sql")
    private static final String QUERY = "select * from account where acc_type = '%d'";

    public SelectAccByType(AccountType type) {
        this.type = type;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY, type.ordinal());
    }
}
