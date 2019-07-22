package by.anelkin.easylearning.specification.mark;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;

import static by.anelkin.easylearning.entity.Mark.*;

public class SelectMarksMadeByUserSpecification implements AppSpecification<Mark>, MarkSpecification<Mark> {
    private MarkType type;
    private int accId;
    private static final String QUERY = "SELECT * FROM %s WHERE acc_id = ?";

    public SelectMarksMadeByUserSpecification(MarkType type, int accId) {
        this.type = type;
        this.accId = accId;
    }

    @Override
    public String getQuery() {
        String tableName = type.toString().toLowerCase();
        return String.format(QUERY, tableName);
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(accId)};
    }

    @Override
    public MarkType getMarkType() {
        return type;
    }
}
