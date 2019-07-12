package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.specification.mark.SelectByIdWithWriterInfoSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;

import java.util.List;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;

public class MarkService {
    public List<Mark> takeMarksOfCourse(int courseId) throws RepositoryException {
        MarkRepository repo = new MarkRepository();
        return repo.query(new SelectByIdWithWriterInfoSpecification(COURSE_MARK, courseId));
    }
}
