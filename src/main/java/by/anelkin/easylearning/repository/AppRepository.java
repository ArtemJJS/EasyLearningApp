package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.entity.AppEntity;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;

import java.util.List;

public interface AppRepository<T extends AppEntity> {
    boolean update(@NonNull T entity);
    boolean delete(@NonNull T entity);
    boolean insert(@NonNull T entity);
    List<T> query(@NonNull AppSpecification<T> specification);
}
