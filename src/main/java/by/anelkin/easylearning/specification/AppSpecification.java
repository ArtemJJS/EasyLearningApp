package by.anelkin.easylearning.specification;

public interface AppSpecification<T> {
    String getQuery();
    String[] getStatementParameters();
}
