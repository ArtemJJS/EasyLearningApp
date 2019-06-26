package by.anelkin.easylearning.specification.mark_spec;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.specification.AppSpecification;

public interface MarkSpecification<T> extends AppSpecification<T> {
    Mark.MarkType getMarkType();
}
