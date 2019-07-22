package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.specification.mark.SelectByIdWithWriterInfoSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarksMadeByUserSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;

public class MarkService {
    private static final String ATTR_USER = "user";
    private static final String ATTR_COMMENT = "comment";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final String ATTR_MARK_VALUE = "mark_value";

    private static final String ATTR_MARKED_COURSES_IDS = "marked_courses_ids";


    public void markCourse(SessionRequestContent requestContent) throws ServiceException {
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        MarkRepository repository = new MarkRepository();
        Account account = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        Mark mark = new Mark(COURSE_MARK);
        mark.setTargetId(Integer.parseInt(reqParams.get(ATTR_COURSE_ID)[0]));
        mark.setAccId(account.getId());
        mark.setComment(reqParams.get(ATTR_COMMENT)[0]);
        mark.setMarkDate(System.currentTimeMillis());
        mark.setMarkValue(Integer.parseInt(reqParams.get(ATTR_MARK_VALUE)[0]));
        try {
            repository.insert(mark);
            insertMarkedCourseIdsIntoSession(requestContent);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }

    }

    List<Mark> takeMarksOfCourse(int courseId) throws ServiceException {
        MarkRepository repo = new MarkRepository();
        List<Mark> marks;
        try {
            marks = repo.query(new SelectByIdWithWriterInfoSpecification(COURSE_MARK, courseId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return marks;
    }

    void insertMarkedCourseIdsIntoSession(SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> sessionAttrs = requestContent.getSessionAttributes();
        Account account = (Account) sessionAttrs.get(ATTR_USER);
        MarkRepository repository = new MarkRepository();
        try {
            List<Mark> marks = repository.query(new SelectMarksMadeByUserSpecification(COURSE_MARK, account.getId()));
            List<Integer> markedIds = new ArrayList<>();
            marks.forEach(mark -> markedIds.add(mark.getTargetId()));
            sessionAttrs.put(ATTR_MARKED_COURSES_IDS, markedIds);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
