package by.anelkin.easylearning.specification.account_spec;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.specification.AppSpecification;

public interface TempAccSpec extends AppSpecification<Account> {
    // TODO: 6/26/2019 удалить и вынести метод в AppSpecification когда доделаю спецификации остальные
    public String[] getStatementParameters();
}
