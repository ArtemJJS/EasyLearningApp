package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.specification.account.SelectAccByIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectByIdWithWriterInfoSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarksMadeByUserSpecification;
import by.anelkin.easylearning.validator.FormValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;

public class MarkService {
    private static final String ATTR_USER = "user";
    private static final String ATTR_COMMENT = "comment";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final String ATTR_AUTHOR_ID = "author_id";
    private static final String ATTR_MARK_VALUE = "mark_value";
    private static final String ATTR_AVAILABLE_COURSES = "coursesAvailable";
    private static final String ATTR_AUTHOR_LOGIN = "author_login";

    private static final String ATTR_MARKED_COURSES_IDS = "marked_courses_ids";


    public void markAuthor(SessionRequestContent requestContent) throws ServiceException {
        MarkRepository repository = new MarkRepository();
        AccRepository accRepository = new AccRepository();
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        Account currAcc = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        int authorId = Integer.parseInt(reqParams.get("author_id")[0]);
        try {
            Account author = accRepository.query(new SelectAccByIdSpecification(authorId)).get(0);
            //security check:
            List<Course> availableCourses = (List<Course>) requestContent.getSessionAttributes().get(ATTR_AVAILABLE_COURSES);
            List<Course> purchasedCoursesOfCurrentAuthor = availableCourses.stream().filter(course -> course.getAuthorId() == authorId).collect(Collectors.toList());
            List<Mark> allMarksOfAuthor = repository.query(new SelectMarkByTargetIdSpecification(AUTHOR_MARK, authorId));
            List<Mark> marksOfThisAuthorByCurrAcc = allMarksOfAuthor.stream().filter(mark -> mark.getAccId() == currAcc.getId()).collect(Collectors.toList());
            if (purchasedCoursesOfCurrentAuthor.size() == 0 || marksOfThisAuthorByCurrAcc.size() > 0) {
                throw new ServiceException("You have no access to rate current author!");
            }

            Mark mark = new Mark(AUTHOR_MARK);
            mark.setTargetId(Integer.parseInt(reqParams.get(ATTR_AUTHOR_ID)[0]));
            mark.setAccId(currAcc.getId());
            mark.setComment(reqParams.get(ATTR_COMMENT)[0]);
            mark.setMarkDate(System.currentTimeMillis());
            mark.setMarkValue(Integer.parseInt(reqParams.get(ATTR_MARK_VALUE)[0]));
            repository.insert(mark);
            requestContent.getRequestAttributes().put(ATTR_AUTHOR_LOGIN, author.getLogin());
        } catch (RepositoryException | NullPointerException e) {
            throw new ServiceException(e);
        }
    }

    public void markCourse(SessionRequestContent requestContent) throws ServiceException {
        FormValidator val = new FormValidator();
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        MarkRepository repository = new MarkRepository();
        Account account = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        Mark mark = new Mark(COURSE_MARK);
        mark.setTargetId(Integer.parseInt(reqParams.get(ATTR_COURSE_ID)[0]));
        mark.setAccId(account.getId());
        if (reqParams.get(ATTR_COMMENT) != null) {
            String comment = reqParams.get(ATTR_COMMENT)[0];
            String correctComment;
            if (!val.validateCourseMarkCommentLength(comment)) {
                correctComment = comment.substring(0, FormValidator.COURSE_MARK_COMMENT_MAX_LENGTH);
            } else {
                correctComment = comment;
            }
            mark.setComment((new AccountService().esqapeQuotes(correctComment)));
        }
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

    List<Integer> insertMarkedCourseIdsIntoSession(SessionRequestContent requestContent) throws ServiceException {
        HashMap<String, Object> sessionAttrs = requestContent.getSessionAttributes();
        Account account = (Account) sessionAttrs.get(ATTR_USER);
        MarkRepository repository = new MarkRepository();
        try {
            List<Mark> marks = repository.query(new SelectMarksMadeByUserSpecification(COURSE_MARK, account.getId()));
            List<Integer> markedIds = new ArrayList<>();
            marks.forEach(mark -> markedIds.add(mark.getTargetId()));
            sessionAttrs.put(ATTR_MARKED_COURSES_IDS, markedIds);
            return markedIds;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
