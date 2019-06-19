package by.anelkin.easylearning.specification.markspec;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;

public interface MarkSpecification<T> extends AppSpecification<T> {
    Mark.MarkType getMarkType();
}
