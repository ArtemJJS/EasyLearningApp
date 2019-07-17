package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.PaymentRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import by.anelkin.easylearning.specification.payment.SelectPaymentByAccountIdSpecification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static by.anelkin.easylearning.entity.Payment.*;

public class PaymentService {
    private static final String SESSION_ATTR_USER = "user";
    private static final String REQUEST_PARAM_AMOUNT = "amount";
    private static final String REQUEST_PARAM_CURRENCY = "currency";
    private static final String REQUEST_PARAM_CARD = "card";
    private static final String REQUEST_ATTR_PAYMENTS = "payments";
    private static final String CARD_NUMBER_SPLITTER = " ";
    private static final String DESCRIPTION_DEPOSIT_BY_CARD = "Deposit from card ends with ";
    private static final String DESCRIPTION_CASH_OUT_TO_CARD = "Cash out to card ends with ";
    private static final String NOT_ENOUGH_MONEY_MSG = "You have not enough funds to proceed operation!";
    private static final String ATTR_PREVIOUS_OPERATION_MSG = "previous_operation_message";
    private static final int PAYMENT_CODE_DEPOSIT_FROM_CARD = 15;
    private static final int PAYMENT_CODE_CASH_OUT_TO_CARD = 20;
    private static final int PHANTOM_COURSE_ID = -1;


    public void processDepositByCard(SessionRequestContent requestContent) throws ServiceException {
        PaymentRepository repository = new PaymentRepository();
        Payment payment = initBasicPaymentParams(requestContent);
        payment.setCourseId(PHANTOM_COURSE_ID);
        payment.setPaymentCode(PAYMENT_CODE_DEPOSIT_FROM_CARD);
        String cardEndNumbers = selectLastCardDigits(requestContent);
        payment.setDescription(DESCRIPTION_DEPOSIT_BY_CARD + cardEndNumbers);
        try {
            repository.insert(payment);
        } catch (RepositoryException e) {
            // FIXME: 7/16/2019
            throw new ServiceException(e);
        }
        Account currUser = (Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER);
        try {
            Account updatedUser = (new AccRepository()).query(new SelectAccByLoginSpecification(currUser.getLogin())).get(0);
            requestContent.getSessionAttributes().put(SESSION_ATTR_USER, updatedUser);
        } catch (RepositoryException e) {
            // FIXME: 7/16/2019
            throw new ServiceException(e);
        }
    }

    public boolean processCashOutFromBalance(SessionRequestContent requestContent) throws ServiceException {
        String cardEndNumbers = selectLastCardDigits(requestContent);
        Payment payment = initBasicPaymentParams(requestContent);
        payment.setAmount(payment.getAmount().negate());
        Account currAcc = (Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER);
        //not enough funds:
        if (currAcc.getBalance().add(payment.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            requestContent.getRequestAttributes().put(ATTR_PREVIOUS_OPERATION_MSG, NOT_ENOUGH_MONEY_MSG);
            return false;
        }
        payment.setCourseId(PHANTOM_COURSE_ID);
        payment.setPaymentCode(PAYMENT_CODE_CASH_OUT_TO_CARD);
        payment.setDescription(DESCRIPTION_CASH_OUT_TO_CARD + cardEndNumbers);
        PaymentRepository repository = new PaymentRepository();
        try {
            repository.insert(payment);
            (new AccountService()).refreshSessionAttributeUser(requestContent, currAcc);
        } catch (RepositoryException e) {
            // FIXME: 7/17/2019
            throw new ServiceException(e);
        }
        return true;
    }


    public void insertPaymentsIntoRequestAttributes(SessionRequestContent requestContent) {
        PaymentRepository paymentRepository = new PaymentRepository();
        Account currAccount = (Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER);
        try {
            List<Payment> payments = paymentRepository.query(new SelectPaymentByAccountIdSpecification(currAccount.getId()));
            requestContent.getRequestAttributes().put(REQUEST_ATTR_PAYMENTS, payments);
        } catch (RepositoryException e) {
            // FIXME: 7/16/2019
            throw new RuntimeException(e);
        }
    }

    // accId, amount, date, currencyId
    private Payment initBasicPaymentParams(SessionRequestContent requestContent) {
        Payment payment = new Payment();
        Account account = (Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER);
        payment.setAccountId(account.getId());
        payment.setAmount(new BigDecimal(requestContent.getRequestParameters().get(REQUEST_PARAM_AMOUNT)[0]));
        payment.setPaymentDate(System.currentTimeMillis());
        payment.setCurrencyId(CurrencyType.valueOf(requestContent.getRequestParameters()
                .get(REQUEST_PARAM_CURRENCY)[0].toUpperCase()).ordinal() + 1);
        return payment;
    }

    private String selectLastCardDigits(SessionRequestContent requestContent) {
        String[] numberParts = requestContent.getRequestParameters()
                .get(REQUEST_PARAM_CARD)[0].split(CARD_NUMBER_SPLITTER);
        return numberParts[numberParts.length - 1];
    }
}
