package by.anelkin.easylearning.specification.mark;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Mark.*;

public class SelectMarkByTargetIdSpecification implements AppSpecification<Mark>, MarkSpecification<Mark> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM %s WHERE target_id = %d";
    private MarkType markType;
    private int targetId;

    public SelectMarkByTargetIdSpecification(MarkType markType, int targetId) {
        this.markType = markType;
        this.targetId = targetId;
    }

    @Override
    public String getQuery() {
        String tableName = markType.toString().toLowerCase();
        return String.format(QUERY, tableName, targetId);
    }

    public MarkType getMarkType() {
        return markType;
    }
}
