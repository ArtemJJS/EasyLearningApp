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
import by.anelkin.easylearning.service.MarkService;
import by.anelkin.easylearning.service.PaymentService;
import by.anelkin.easylearning.specification.account.SelectAccByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCourseByIdSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
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

    private PaymentService paymentService = new PaymentService();
    private ConnectionPool pool = ConnectionPool.getInstance();


    @AfterClass
    public void tearDownTestClass() {
        pool.closePool();
    }


    @BeforeMethod
    public void setUp() {
        requestContent = new SessionRequestContent();
        sessionAttrs = requestContent.getSessionAttributes();
        reqParams = requestContent.getRequestParameters();
        reqAttrs = requestContent.getRequestAttributes();

        sessionAttrs.put("locale", Locale.US);
    }

    @Test
    public void testProcessPurchaseFromBalanceShouldUpdateAccBalance() throws SQLException, RepositoryException, ServiceException {
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
            reqParams.put(ATTR_COURSE_ID, new String[]{"1"});

            paymentService.processPurchaseFromBalance(requestContent);

            BigDecimal actual = accRepo.query(new SelectAccByIdSpecification(3)).get(0).getBalance();

            assertEquals(actual, expected);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void testProcessPurchaseByCard() {
    }

    @Test
    public void testProcessDepositByCard() {
    }

    @Test
    public void testProcessCashOutFromBalance() {
    }

    @Test
    public void testInsertPaymentsIntoRequestAttributes() {
    }
}