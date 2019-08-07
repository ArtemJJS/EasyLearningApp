package service;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.repository.PaymentRepository;
import by.anelkin.easylearning.service.MarkService;
import by.anelkin.easylearning.service.PaymentService;
import by.anelkin.easylearning.specification.account.SelectAccByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import by.anelkin.easylearning.specification.payment.SelectAllPaymentSpecification;
import by.anelkin.easylearning.specification.payment.SelectPaymentByAccountIdSpecification;
import org.intellij.lang.annotations.Language;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static by.anelkin.easylearning.entity.Mark.MarkType.AUTHOR_MARK;
import static org.testng.Assert.*;

public class PaymentServiceTest {
    private static final String ATTR_MESSAGE = "message";
    private static final String BUNDLE_PAYMENT_SUCCEEDED = "msg.payment_approved";
    private static final String BUNDLE_PAYMENT_DECLINED = "msg.payment_declined";
    private static final String BUNDLE_WRONG_DEPOSIT_DATA = "msg.wrong_deposit_data";
    private static final String BUNDLE_PROBLEMS_PURCHASING_COURSE = "msg.problems_purchasing_course";
    private static final String RESOURCE_BUNDLE_BASE = "text_resources";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final String ATTR_USER = "user";
    private static final String ATTR_AMOUNT = "amount";
    private static final String ATTR_CURRENCY = "currency";
    private static final String ATTR_CARD = "card";
    private static final String ATTR_PAYMENTS = "payments";
    private static final String ATTR_PREVIOUS_OPERATION_MSG = "previous_operation_message";
    private static final String CARD_NUMBER_SPLITTER = " ";
    private static final String DESCRIPTION_DEPOSIT_BY_CARD = "Deposit from card ends with ";
    private static final String DESCRIPTION_CASH_OUT_TO_CARD = "Cash out to card ends with ";
    private static final String DESCRIPTION_BUY_WITH_CARD = "Purchasing by card ends with %s. Course: ";
    private static final String DESCRIPTION_BUY_FROM_BALANCE = "Purchasing from balance. Course: ";
    private static final String DESCRIPTION_SALE_COURSE = "Sale course: ";
    private static final int NOT_EXISTS_COURSE_ID = -1;
    @Language("sql")
    private static final String CREATE_TABLES = "call createTables()";
    @Language("sql")
    private static final String DROP_TABLES = "call dropTables()";

    private SessionRequestContent requestContent;
    private HashMap<String, Object> sessionAttrs;
    private Map<String, String[]> reqParams;
    private HashMap<String, Object> reqAttrs;

    private CourseRepository courseRepo = new CourseRepository();
    private AccRepository accRepo = new AccRepository();
    private MarkRepository markRepo = new MarkRepository();
    private PaymentRepository paymentRepo = new PaymentRepository();

    private PaymentService paymentService = new PaymentService();
    private ConnectionPool pool;


//    @AfterClass
//    public void tearDownTestClass() {
//        pool.closePool();
//    }


    @BeforeMethod
    public void setUp() {
        pool = ConnectionPool.getInstance();
        requestContent = new SessionRequestContent();
        sessionAttrs = requestContent.getSessionAttributes();
        reqParams = requestContent.getRequestParameters();
        reqAttrs = requestContent.getRequestAttributes();

        sessionAttrs.put("locale", Locale.US);
    }

    @Test
    public void PaymentRepository_ProcessPurchaseFromBalance_ShouldUpdateAccBalance() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            BigDecimal balanceBefore = accRepo.query(new SelectAccByIdSpecification(3)).get(0).getBalance();
            BigDecimal coursePrice = courseRepo.query(new SelectCourseByIdSpecification(1)).get(0).getPrice();
            BigDecimal expected = balanceBefore.subtract(coursePrice);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});

            paymentService.processPurchaseFromBalance(requestContent);

            BigDecimal actual = accRepo.query(new SelectAccByIdSpecification(3)).get(0).getBalance();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessPurchaseFromBalance_ShouldInsertTwoPayments() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = paymentRepo.query(new SelectAllPaymentSpecification()).size() + 2;

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});

            paymentService.processPurchaseFromBalance(requestContent);

            int actual = paymentRepo.query(new SelectAllPaymentSpecification()).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessPurchaseFromBalance_ShouldAddCourseToPurchased() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size() + 1;

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});

            paymentService.processPurchaseFromBalance(requestContent);

            int actual = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessPurchaseFromBalance_ShouldNotInsertPaymentsIfPurchasedAlready() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"1"});

            paymentService.processPurchaseFromBalance(requestContent);

            int actual = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessPurchaseFromBalance_ShouldDoNothingIfInsufficientFunds() throws SQLException, RepositoryException, ServiceException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"2"});

            paymentService.processPurchaseFromBalance(requestContent);

            int actual = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void PaymentRepository_ProcessPurchaseByCard_ShouldDoNothingIfPurchasedAlready() throws SQLException, ServiceException, RepositoryException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"1"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});

            paymentService.processPurchaseByCard(requestContent);

            int actual = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessPurchaseByCard_ShouldInsertTwoPayments() throws SQLException, ServiceException, RepositoryException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = paymentRepo.query(new SelectAllPaymentSpecification()).size() + 2;

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});

            paymentService.processPurchaseByCard(requestContent);

            int actual = paymentRepo.query(new SelectAllPaymentSpecification()).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessPurchaseByCard_ShouldAddCourseInPurchased() throws SQLException, ServiceException, RepositoryException {
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size() + 1;

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});

            paymentService.processPurchaseByCard(requestContent);

            int actual = courseRepo.query(new SelectCoursesPurchasedByUserSpecification(3)).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }




    @Test
    public void PaymentRepository_ProcessDepositByCard_ShouldUpdateAccBalance() throws SQLException, RepositoryException, ServiceException {
        String amount = "50";
        BigDecimal depositAmount = new BigDecimal(amount);
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            BigDecimal balanceBefore = accRepo.query(new SelectAccByIdSpecification(3)).get(0).getBalance();
            BigDecimal expected = balanceBefore.add(depositAmount);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});
            reqParams.put(ATTR_AMOUNT, new String[]{amount});
            reqParams.put(ATTR_CURRENCY, new String[]{"USD"});

            paymentService.processDepositByCard(requestContent);

            BigDecimal actual = accRepo.query(new SelectAccByIdSpecification(3)).get(0).getBalance();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessDepositByCard_ShouldInsertOnePayment() throws SQLException, RepositoryException, ServiceException {
        String amount = "50";
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = paymentRepo.query(new SelectAllPaymentSpecification()).size() + 1;

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(3));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});
            reqParams.put(ATTR_AMOUNT, new String[]{amount});
            reqParams.put(ATTR_CURRENCY, new String[]{"USD"});

            paymentService.processDepositByCard(requestContent);

            int actual = paymentRepo.query(new SelectAllPaymentSpecification()).size();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }


    @Test
    public void PaymentRepository_ProcessCashOutFromBalance_ShouldUpdateAccBalance() throws SQLException, ServiceException, RepositoryException {
        String amount = "50";
        BigDecimal cahOutAmount = new BigDecimal(amount);
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            BigDecimal balanceBefore = accRepo.query(new SelectAccByIdSpecification(2)).get(0).getBalance();
            BigDecimal expected = balanceBefore.subtract(cahOutAmount);

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(2));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});
            reqParams.put(ATTR_AMOUNT, new String[]{amount});
            reqParams.put(ATTR_CURRENCY, new String[]{"USD"});

            paymentService.processCashOutFromBalance(requestContent);

            BigDecimal actual = accRepo.query(new SelectAccByIdSpecification(2)).get(0).getBalance();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessCashOutFromBalance_ShouldNotChangeAccBalanceIfInsufficientFunds() throws SQLException, RepositoryException, ServiceException {
        String amount = "1050";
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            BigDecimal expected = accRepo.query(new SelectAccByIdSpecification(2)).get(0).getBalance();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(2));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});
            reqParams.put(ATTR_AMOUNT, new String[]{amount});
            reqParams.put(ATTR_CURRENCY, new String[]{"USD"});

            paymentService.processCashOutFromBalance(requestContent);

            BigDecimal actual = accRepo.query(new SelectAccByIdSpecification(2)).get(0).getBalance();
//            int actual = paymentRepo.query(new SelectAllPaymentSpecification()).size();

            assertEquals(actual, expected);
        }  finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void PaymentRepository_ProcessCashOutFromBalance_ShouldNotInsertPaymentsIfInsufficientFunds() throws SQLException, RepositoryException, ServiceException {
        String amount = "1050";
        Connection connection = pool.takeConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(DROP_TABLES);
            statement.execute(CREATE_TABLES);

            int expected = paymentRepo.query(new SelectAllPaymentSpecification()).size();

            List<Account> accounts = accRepo.query(new SelectAccByIdSpecification(2));
            sessionAttrs.put(ATTR_USER, accounts.get(0));
            reqParams.put(ATTR_COURSE_ID, new String[]{"3"});
            reqParams.put(ATTR_CARD, new String[]{"1123 1231 1231 1231"});
            reqParams.put(ATTR_AMOUNT, new String[]{amount});
            reqParams.put(ATTR_CURRENCY, new String[]{"USD"});

            paymentService.processCashOutFromBalance(requestContent);

            int actual = paymentRepo.query(new SelectAllPaymentSpecification()).size();

            assertEquals(actual, expected);
        }  finally {
            statement.close();
            connection.close();
        }
    }

}