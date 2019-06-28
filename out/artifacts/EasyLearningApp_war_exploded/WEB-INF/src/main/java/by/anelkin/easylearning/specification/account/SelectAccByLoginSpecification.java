package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAccByLoginSpecification implements AppSpecification<Account>, TempAccSpec {
    private String login;
    @Language("sql")
    private static final String QUERY = "SELECT * FROM account WHERE acc_login = ?";

    public SelectAccByLoginSpecification(String login) {
        this.login = login;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{login};
    }

}
