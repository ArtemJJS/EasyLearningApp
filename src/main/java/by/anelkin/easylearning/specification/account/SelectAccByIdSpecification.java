package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAccByIdSpecification implements AppSpecification<Account> {
    private int accId;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM account WHERE acc_id = ?";

    public SelectAccByIdSpecification(int accId) {
        this.accId = accId;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(accId)};
    }
}
