package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.entity.AppEntity;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;

import java.util.List;

public interface AppRepository<T extends AppEntity> {
    boolean update(@NonNull T entity) throws RepositoryException;
    boolean delete(@NonNull T entity) throws RepositoryException;
    boolean insert(@NonNull T entity) throws RepositoryException;
    List<T> query(@NonNull AppSpecification<T> specification) throws RepositoryException;
}
