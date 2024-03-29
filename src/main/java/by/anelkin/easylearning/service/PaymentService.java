package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.repository.PaymentRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import by.anelkin.easylearning.specification.account.SelectAuthorOfCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.payment.SelectPaymentByAccountIdSpecification;
import by.anelkin.easylearning.specification.payment.SelectPaymentPaginationByAccIdSpecification;
import by.anelkin.easylearning.validator.FormValidator;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static by.anelkin.easylearning.entity.Course.*;
import static by.anelkin.easylearning.entity.Payment.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

/**
 * Represents logic to execute operations, required mostly {@link Payment} data
 * from data store. Used to operation that can change {@link Payment} state inside
 * of the data store or required course data.
 * Common repository - {@link PaymentRepository}
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
public class PaymentService {
    /**
     * amount of payment on one page on view. Used in {@link PaymentRepository} query
     */
    private static final int PAGINATION_LIMIT_PAYMENT_HISTORY = 8;
    /**
     * representation of {@link Course#getId()} if payment is not linked
     * with purchase or sell {@link Course}
     */
    private static final int NOT_EXISTS_COURSE_ID = -1;

    /**
     * process purchasing {@link Course} from balance
     * invoke {@link PaymentService#processAuthorSaleOperation(Course)} to update author of the course balance
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @return true if payment proceeded, otherwise false (if course purchased already or invalid data)
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public boolean processPurchaseFromBalance(SessionRequestContent requestContent) throws ServiceException {
        PaymentRepository repository = new PaymentRepository();
        CourseRepository courseRepository = new CourseRepository();
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        Payment payment = new Payment();
        Account account = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        try {
        int courseId = Integer.parseInt(reqParams.get(ATTR_COURSE_ID)[0]);
            Course course = courseRepository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            boolean isCourseApproved = course.getState() == CourseState.APPROVED;
            boolean isCoursePurchasedAlready = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId())).contains(course);
            boolean isEnoughFunds = account.getBalance().subtract(course.getPrice()).compareTo(BigDecimal.ZERO) >= 0;
            if (!isEnoughFunds || isCoursePurchasedAlready || !isCourseApproved) {
                return false;
            }
            payment.setAccountId(account.getId());
            payment.setCourseId(courseId);
            payment.setPaymentCode(PaymentCode.BUY_COURSE_FROM_BALANCE.getCode());
            payment.setAmount(course.getPrice().negate());
            payment.setPaymentDate(System.currentTimeMillis());
            payment.setCurrencyId(CurrencyType.USD.ordinal() + 1);
            payment.setDescription(DESCRIPTION_BUY_FROM_BALANCE);
            repository.insert(payment);
            AccountService accService = new AccountService();
            accService.refreshSessionAttributeUser(requestContent, account);
            accService.refreshSessionAttributeAvailableCourses(requestContent, account);
            try {
                processAuthorSaleOperation(course);
            } catch (Exception e) {
                repository.delete(payment);
                return false;
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    /**
     * process purchasing {@link Course} by card
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @return true if payment proceeded, otherwise false (if course purchased already or invalid data)
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public boolean processPurchaseByCard(SessionRequestContent requestContent) throws ServiceException {
        FormValidator validator = new FormValidator();
        PaymentRepository repository = new PaymentRepository();
        CourseRepository courseRepository = new CourseRepository();
        Map<String, String[]> reqParams = requestContent.getRequestParameters();
        Payment payment = new Payment();
        Account account = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        try {
        int courseId = Integer.parseInt(reqParams.get(ATTR_COURSE_ID)[0]);
            Course course = courseRepository.query(new SelectCourseByIdSpecification(courseId)).get(0);
            boolean isCoursePurchasedAlready = courseRepository.query(new SelectCoursesPurchasedByUserSpecification(account.getId())).contains(course);
            String cardNumber = requestContent.getRequestParameters().get(ATTR_CARD)[0];
            boolean isCardNumberCorrect = validator.validateCard(cardNumber);
            boolean isCourseApproved = course.getState() == CourseState.APPROVED;
            if (isCoursePurchasedAlready || !isCardNumberCorrect || !isCourseApproved) {
                return false;
            }
            payment.setAccountId(account.getId());
            payment.setCourseId(courseId);
            payment.setPaymentCode(PaymentCode.BUY_COURSE_WITH_CARD.getCode());
            payment.setAmount(course.getPrice());
            payment.setPaymentDate(System.currentTimeMillis());
            payment.setCurrencyId(CurrencyType.USD.ordinal() + 1);
            payment.setDescription(String.format(DESCRIPTION_BUY_WITH_CARD, selectLastCardDigits(requestContent)));
            repository.insert(payment);
            AccountService accService = new AccountService();
            accService.refreshSessionAttributeUser(requestContent, account);
            accService.refreshSessionAttributeAvailableCourses(requestContent, account);
            try {
                processAuthorSaleOperation(course);
            } catch (Exception e) {
                log.error("Transaction rollback. Author Sale operation fail! " + e);
                repository.delete(payment);
                return false;
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }


    /**
     * process deposit by card
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @return true if payment proceeded, otherwise false
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public boolean processDepositByCard(SessionRequestContent requestContent) throws ServiceException {
        Locale locale = (new CourseService()).takeLocaleFromSession(requestContent);
        FormValidator validator = new FormValidator();
        PaymentRepository repository = new PaymentRepository();
        Payment payment;
        try {
            payment = initBasicPaymentParams(requestContent);
        } catch (IllegalArgumentException e) {
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_WRONG_DEPOSIT_DATA);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
            return false;
        }
        String cardNumber = requestContent.getRequestParameters().get(ATTR_CARD)[0];
        if (!validator.validatePrice(payment.getAmount().toString()) ||
                !validator.validateCard(cardNumber)) {
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_WRONG_DEPOSIT_DATA);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
            return false;
        }
        payment.setCourseId(NOT_EXISTS_COURSE_ID);
        payment.setPaymentCode(PaymentCode.DEPOSIT_FROM_CARD.getCode());
        String cardEndNumbers = selectLastCardDigits(requestContent);
        payment.setDescription(DESCRIPTION_DEPOSIT_BY_CARD + cardEndNumbers);
        try {
            repository.insert(payment);
            Account currUser = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
            Account updatedUser = (new AccRepository()).query(new SelectAccByLoginSpecification(currUser.getLogin())).get(0);
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_PAYMENT_SUCCEEDED);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
            requestContent.getSessionAttributes().put(ATTR_USER, updatedUser);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    /**
     * process cash-out on card
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @return true if payment proceeded, otherwise false
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public boolean processCashOutFromBalance(SessionRequestContent requestContent) throws ServiceException {
        FormValidator validator = new FormValidator();
        Locale locale = (new CourseService()).takeLocaleFromSession(requestContent);
        String cardEndNumbers = selectLastCardDigits(requestContent);
        Payment payment;
        try {
            payment = initBasicPaymentParams(requestContent);
        } catch (IllegalArgumentException e) {
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_WRONG_DEPOSIT_DATA);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
            return false;
        }
        payment.setAmount(payment.getAmount().negate());
        Account currAcc = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        boolean isEnoughFunds = currAcc.getBalance().add(payment.getAmount()).compareTo(BigDecimal.ZERO) >= 0;
        boolean isCorrectAmount = validator.validatePrice(payment.getAmount().negate().toString());
        String cardNumber = requestContent.getRequestParameters().get(ATTR_CARD)[0];
        boolean isCorrectCard = validator.validateCard(cardNumber);
        if (!isEnoughFunds || !isCorrectAmount || !isCorrectCard) {
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_PAYMENT_DECLINED);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
            return false;
        }
        payment.setCourseId(NOT_EXISTS_COURSE_ID);
        payment.setPaymentCode(PaymentCode.CASH_OUT_TO_CARD.getCode());
        payment.setDescription(DESCRIPTION_CASH_OUT_TO_CARD + cardEndNumbers);
        PaymentRepository repository = new PaymentRepository();
        try {
            repository.insert(payment);
            (new AccountService()).refreshSessionAttributeUser(requestContent, currAcc);
            String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale).getString(BUNDLE_PAYMENT_SUCCEEDED);
            requestContent.getRequestAttributes().put(ATTR_MESSAGE, message);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    /**
     * inserts {@link Payment} into request attributes
     *
     * @param requestContent - entity represents separated HTTPRequest attributes and parameters
     *                       and session attributes. Include "referer" header from request
     * @throws ServiceException if faced {@link RepositoryException}
     */
    public void insertPaymentsIntoRequestAttributes(SessionRequestContent requestContent) throws ServiceException {
        PaymentRepository paymentRepository = new PaymentRepository();
        Map<String, String[]> reqParam = requestContent.getRequestParameters();
        Account currAccount = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        try {
            String[] reqParamPageNumbers = reqParam.get(ATTR_PAGE);
            String searchPageNumber = reqParamPageNumbers == null ? null : reqParamPageNumbers[0];
            int currPageNumber;
            if (searchPageNumber == null || searchPageNumber.isEmpty()) {
                currPageNumber = 0;
            } else {
                int tempPageNumber = Integer.parseInt(searchPageNumber);
                currPageNumber = tempPageNumber < 0 ? 0 : tempPageNumber;
            }
            int offset = currPageNumber * PAGINATION_LIMIT_PAYMENT_HISTORY;
            List<Payment> payments = paymentRepository.query(new SelectPaymentPaginationByAccIdSpecification(currAccount.getId(), PAGINATION_LIMIT_PAYMENT_HISTORY + 1, offset));
            if (payments.size() > PAGINATION_LIMIT_PAYMENT_HISTORY) {
                requestContent.getRequestAttributes().put(ATTR_HAS_MORE_PAGES, "true");
                payments.remove(payments.size() - 1);
            }
            requestContent.getRequestAttributes().put(ATTR_PAYMENTS, payments);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    /**\
     * process operation of refilling {@link Account} author balance
     * when some user purchase {@link Course} athered by Author
     * In this version full sale amount goes to author balance
     *      *
     * @param course - purchased course
     * @throws ServiceException if faced {@link RepositoryException}
     */
    private void processAuthorSaleOperation(Course course) throws ServiceException {
        AccRepository accRepository = new AccRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        Payment payment = new Payment();
        try {
            Account author = accRepository.query(new SelectAuthorOfCourseSpecification(course.getId())).get(0);
            payment.setAccountId(author.getId());
            payment.setCourseId(course.getId());
            payment.setPaymentCode(PaymentCode.SALE_COURSE.getCode());
            payment.setAmount(course.getPrice());
            payment.setPaymentDate(System.currentTimeMillis());
            payment.setCurrencyId(CurrencyType.USD.ordinal() + 1);
            payment.setDescription(DESCRIPTION_SALE_COURSE);
            paymentRepository.insert(payment);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    // accId, amount, date, currencyId
    private Payment initBasicPaymentParams(SessionRequestContent requestContent) {
        Payment payment = new Payment();
        Account account = (Account) requestContent.getSessionAttributes().get(ATTR_USER);
        payment.setAccountId(account.getId());
        payment.setAmount(new BigDecimal(requestContent.getRequestParameters().get(ATTR_AMOUNT)[0]));
        payment.setPaymentDate(System.currentTimeMillis());
        payment.setCurrencyId(CurrencyType.valueOf(requestContent.getRequestParameters()
                .get(ATTR_CURRENCY)[0].toUpperCase()).ordinal() + 1);
        return payment;
    }

    private String selectLastCardDigits(SessionRequestContent requestContent) {
        String[] numberParts = requestContent.getRequestParameters()
                .get(ATTR_CARD)[0].split(CARD_NUMBER_SPLITTER);
        return numberParts[numberParts.length - 1];
    }
}
