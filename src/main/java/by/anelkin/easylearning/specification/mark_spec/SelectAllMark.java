package by.anelkin.easylearning.specification.mark_spec;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Mark.*;


public class SelectAllMark implements AppSpecification<Mark>, MarkSpecification<Mark> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM %s";

    private MarkType markType;

    public SelectAllMark(MarkType markType) {
        this.markType = markType;
    }

    @Override
    public String getQuery() {
        String tableName = markType.toString().toLowerCase();
        return String.format(QUERY, tableName);
    }

    public MarkType getMarkType() {
        return markType;
    }
}
