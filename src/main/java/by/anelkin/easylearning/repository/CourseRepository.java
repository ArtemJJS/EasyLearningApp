package by.anelkin.easylearning.repository;

import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.specification.AppSpecification;
import lombok.NonNull;

import java.util.List;

public class CourseRepository implements AppRepository<Course> {
    @Override
    public boolean update(@NonNull Course course) {
        return false;
    }

    @Override
    public boolean delete(@NonNull Course course) {
        return false;
    }

    @Override
    public boolean insert(@NonNull Course course) {
        return false;
    }

    @Override
    public List<Course> query(@NonNull AppSpecification<Course> specification) {
        return null;
    }
}
