package by.anelkin.easylearning.specification.account;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectAccToPhotoApproveSpecification implements AppSpecification<Account> {
    @Language("sql")
    private static final String QUERY = "select * from account where update_photo_path NOT IN ('' AND NULL)";


    @Override
    public String getQuery() {
        return QUERY;
    }

    @Override
    public String[] getStatementParameters() {
        return new String[0];
    }
}
