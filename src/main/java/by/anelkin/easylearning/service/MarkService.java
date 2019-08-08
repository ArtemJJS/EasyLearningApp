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
import by.anelkin.easylearning.specification.mark.SelectByTargetIdWithWriterInfoSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarksMadeByUserSpecification;
import by.anelkin.easylearning.validator.FormValidator;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
public class MarkService {

    public void deleteCourseMarkComment(SessionRequestContent requestContent) throws ServiceException {
        MarkRepository repo = new MarkRepository();
        try {
            int markId = Integer.parseInt(requestContent.getRequestParameters().get(ATTR_MARK_ID)[0]);
            Mark mark = repo.query(new SelectMarkByIdSpecification(COURSE_MARK, markId)).get(0);
            mark.setComment(EMPTY_STRING);
            repo.update(mark);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException e){
            log.error(e);
        }
    }

    public void markAuthor(SessionRequestContent requestContent) throws ServiceException {
        MarkRepository repository = new MarkRepository();
        AccRepository accRepository = new AccRepository();
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        try {
            Account currAcc = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
            int authorId = Integer.parseInt(reqParams.get(ATTR_TARGET_ID)[0]);
            Account author = accRepository.query(new SelectAccByIdSpecification(authorId)).get(0);
            List<Course> availableCourses = (List<Course>) requestContent.getSessionAttributes().get(ATTR_AVAILABLE_COURSES);
            List<Course> purchasedCoursesOfCurrentAuthor = availableCourses.stream().filter(course -> course.getAuthorId() == authorId).collect(Collectors.toList());
            List<Mark> allMarksOfAuthor = repository.query(new SelectMarkByTargetIdSpecification(AUTHOR_MARK, authorId));
            List<Mark> marksOfThisAuthorByCurrAcc = allMarksOfAuthor.stream().filter(mark -> mark.getAccId() == currAcc.getId()).collect(Collectors.toList());
            if (purchasedCoursesOfCurrentAuthor.size() == 0 || marksOfThisAuthorByCurrAcc.size() > 0) {
                log.error("Denied access attempt to rate an author. Intruder acc-id: " + currAcc.getId());
                throw new ServiceException("You have already rated or have no access to rate current author!");
            }

            Mark mark = initMarkFromRequestParams(new Mark(AUTHOR_MARK), reqParams, currAcc.getId());

            repository.insert(mark);
            requestContent.getRequestAttributes().put(ATTR_AUTHOR_LOGIN, author.getLogin());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            log.error(e);
            throw new ServiceException(e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


    public void markCourse(SessionRequestContent requestContent) throws ServiceException {
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        MarkRepository repository = new MarkRepository();
        try {
            Account account = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
            int courseId = Integer.parseInt(reqParams.get(ATTR_TARGET_ID)[0]);
            List<Integer> markedCourseIds = (List<Integer>) requestContent.getSessionAttributes().get(ATTR_MARKED_COURSES_IDS);
            List<Course> purchasedCourses = (List<Course>) requestContent.getSessionAttributes().get(ATTR_AVAILABLE_COURSES);
            List<Integer> purchasedCoursesIds = purchasedCourses.stream().map(Course::getId).collect(Collectors.toList());
            if (markedCourseIds.contains(courseId) || !purchasedCoursesIds.contains(courseId)) {
                log.error("Denied access attempt to rate a course. Intruder acc-id: " + account.getId());
                throw new ServiceException("You have already rated or have no access to rate current course!");
            }

            Mark mark = initMarkFromRequestParams(new Mark(COURSE_MARK), reqParams, account.getId());

            repository.insert(mark);
            insertMarkedCourseIdsIntoSession(requestContent);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            log.error(e);
            throw new ServiceException(e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    private Mark initMarkFromRequestParams(Mark mark, Map<String, String[]> reqParams, int currAccId) {
        FormValidator validator = new FormValidator();
        if (reqParams.get(ATTR_COMMENT) != null) {
            String comment = reqParams.get(ATTR_COMMENT)[0];
            String correctComment;
            if (!validator.validateMarkCommentLength(comment)) {
                correctComment = comment.substring(0, FormValidator.MARK_COMMENT_MAX_LENGTH);
            } else {
                correctComment = comment;
            }
            mark.setComment((new AccountService().escapeQuotes(correctComment)));
        }
        mark.setTargetId(Integer.parseInt(reqParams.get(ATTR_TARGET_ID)[0]));
        mark.setAccId(currAccId);
        mark.setMarkDate(System.currentTimeMillis());
        mark.setMarkValue(Integer.parseInt(reqParams.get(ATTR_MARK_VALUE)[0]));
        return mark;
    }

    List<Mark> takeMarksOfCourse(int courseId) throws ServiceException {
        MarkRepository repo = new MarkRepository();
        List<Mark> marks;
        try {
            marks = repo.query(new SelectByTargetIdWithWriterInfoSpecification(COURSE_MARK, courseId));
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
