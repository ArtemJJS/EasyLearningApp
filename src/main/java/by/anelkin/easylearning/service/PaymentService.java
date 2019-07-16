package by.anelkin.easylearning.service;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Payment;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.PaymentRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;

import java.math.BigDecimal;

public class PaymentService {
    private static final String SESSION_ATTR_USER = "user";
    private static final String REQUEST_PARAM_AMOUNT = "amount";
    private static final String REQUEST_PARAM_CURRENCY = "currency";
    private static final String REQUEST_PARAM_CARD = "card";
    private static final String CARD_NUMBER_SPLITTER = " ";
    private static final String DESCRIPTION_DEPOSIT_BY_CARD = "Deposit from card ends with ";
    private static final int PAYMENT_CODE_DEPOSIT_FROM_CARD = 15;
    private static final int FANTOM_COURSE_ID = -1;


    public void processDepositByCard(SessionRequestContent requestContent) throws ServiceException {
        PaymentRepository repository = new PaymentRepository();
        Payment payment = initBasicPaymentParams(requestContent);
        payment.setCourseId(FANTOM_COURSE_ID);
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

    // accId, amount, date, currencyId
    private Payment initBasicPaymentParams(SessionRequestContent requestContent) {
        Payment payment = new Payment();
        Account account = (Account) requestContent.getSessionAttributes().get(SESSION_ATTR_USER);
        payment.setAccountId(account.getId());
        payment.setAmount(new BigDecimal(requestContent.getRequestParameters().get(REQUEST_PARAM_AMOUNT)[0]));
        payment.setPaymentDate(System.currentTimeMillis());
        payment.setCurrencyId(Payment.CurrencyType.valueOf(requestContent.getRequestParameters()
                .get(REQUEST_PARAM_CURRENCY)[0].toUpperCase()).ordinal() + 1);
        return payment;
    }

    private String selectLastCardDigits(SessionRequestContent requestContent) {
        String[] numberParts = requestContent.getRequestParameters()
                .get(REQUEST_PARAM_CARD)[0].split(CARD_NUMBER_SPLITTER);
        return numberParts[numberParts.length - 1];
    }
}
