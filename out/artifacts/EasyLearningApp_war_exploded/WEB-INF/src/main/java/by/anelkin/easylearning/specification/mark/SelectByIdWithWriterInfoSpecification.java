package by.anelkin.easylearning.specification.mark;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;

import static by.anelkin.easylearning.entity.Mark.*;

public class SelectByIdWithWriterInfoSpecification implements AppSpecification<Mark>, MarkSpecification<Mark> {
    private MarkType markType;
    private int targetId;
    private static final String QUERY ="select t1.*, t2.acc_login, t2.acc_photo_path from %s as t1 " +
            "left join account as t2 on t1.acc_id = t2.acc_id where t1.target_id = ?";

    public SelectByIdWithWriterInfoSpecification(MarkType markType, int targetId) {
        this.markType = markType;
        this.targetId = targetId;
    }

    @Override
    public MarkType getMarkType() {
        return markType;
    }

    @Override
    public String getQuery() {
        String tableName = markType.toString().toLowerCase();
        return String.format(QUERY, tableName);
    }

    @Override
    public String[] getStatementParameters() {
        return new String[]{String.valueOf(targetId)};
    }
}
