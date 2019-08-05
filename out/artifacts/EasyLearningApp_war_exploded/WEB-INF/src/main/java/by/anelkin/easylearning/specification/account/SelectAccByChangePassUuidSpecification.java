package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAccByChangePassUuidSpecification implements AppSpecification<Account> {
    private String uuid;
    @Language("sql")
    private static final String QUERY = "select * from account where acc_id = (select acc_id from restore_pass_requests where uuid = ?)";

    public SelectAccByChangePassUuidSpecification(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{uuid};
    }
}
