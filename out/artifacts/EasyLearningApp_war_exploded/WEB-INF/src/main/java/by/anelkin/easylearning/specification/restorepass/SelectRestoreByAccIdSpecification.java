package by.anelkin.easylearning.specification.restorepass;

import by.anelkin.easylearning.entity.RestorePassRequest;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

public class SelectRestoreByAccIdSpecification implements AppSpecification<RestorePassRequest> {
    private int accId;
    @Language("sql")
    private static final String QUERY = "select * from restore_pass_requests where acc_id = ?";

    public SelectRestoreByAccIdSpecification(int accId) {
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
