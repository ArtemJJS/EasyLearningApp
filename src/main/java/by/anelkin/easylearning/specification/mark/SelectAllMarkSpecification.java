package by.anelkin.easylearning.specification.mark;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;
import org.intellij.lang.annotations.Language;

import static by.anelkin.easylearning.entity.Mark.*;


public class SelectAllMarkSpecification implements AppSpecification<Mark>, MarkSpecification<Mark> {
    @Language("sql")
    private static final String QUERY = "SELECT * FROM %s";
    private MarkType markType;

    public SelectAllMarkSpecification(MarkType markType) {
        this.markType = markType;
    }

    @Override
    public String getQuery() {
        String tableName = markType.toString().toLowerCase();
        return String.format(QUERY, tableName);
    }

    @Override
    public String[] getStatementParameters() {
        return new String[0];
    }

    public MarkType getMarkType() {
        return markType;
    }
}
