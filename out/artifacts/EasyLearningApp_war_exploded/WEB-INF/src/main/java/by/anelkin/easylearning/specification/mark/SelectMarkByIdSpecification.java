package by.anelkin.easylearning.specification.mark;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Mark.*;

public class SelectMarkByIdSpecification implements AppSpecification<Mark>, MarkSpecification<Mark> {
    private MarkType markType;
    private int markId;
    private static final String QUERY ="SELECT * FROM %s WHERE mark_id = ?";

    public SelectMarkByIdSpecification(MarkType markType, int markId) {
        this.markType = markType;
        this.markId = markId;
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
        return new String[]{String.valueOf(markId)};
    }
}
