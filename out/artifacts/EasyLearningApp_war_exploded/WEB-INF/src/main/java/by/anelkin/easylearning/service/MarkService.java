package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.specification.mark.SelectByIdWithWriterInfoSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;

import java.util.List;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;

public class MarkService {
    public List<Mark> takeMarksOfCourse(int courseId) throws ServiceException {
        MarkRepository repo = new MarkRepository();
        List<Mark> marks;
        try {
            marks = repo.query(new SelectByIdWithWriterInfoSpecification(COURSE_MARK, courseId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return marks;
    }
}
