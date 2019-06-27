package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Account.*;

public class SelectAccByTypeSpecification implements AppSpecification<Account>, TempAccSpec {
    private AccountType type;
    @Language("sql")
    private static final String QUERY = "select * from account where acc_type = ?";

    public SelectAccByTypeSpecification(AccountType type) {
        this.type = type;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    public String[] getStatementParameters() {
        return new String[]{String.valueOf(type.ordinal())};
    }
}
