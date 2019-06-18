package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.entity.AuthorMark;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;

import java.util.List;

public class AuthorMarkRepository implements AppRepository<AuthorMark>{
    @Override
    public boolean update(@NonNull AuthorMark entity) {
        return false;
    }

    @Override
    public boolean delete(@NonNull AuthorMark entity) {
        return false;
    }

    @Override
    public boolean insert(@NonNull AuthorMark entity) {
        return false;
    }

    @Override
    public List<AuthorMark> query(AppSpecification<AuthorMark> specification) {

        return null;
    }
}
