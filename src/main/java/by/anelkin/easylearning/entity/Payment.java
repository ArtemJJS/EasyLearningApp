package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Payment extends AppEntity{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private int id;
    private int accountId;
    private int courseId;
    private int paymentCode;
    private BigDecimal amount;
    private long paymentDate;
    private int currencyId;
    private String description;

    public enum PaymentCode{
        BUY_COURSE_WITH_CARD(10),
        BUY_COURSE_FROM_BALANCE(11),
        DEPOSIT_FROM_CARD(15),
        CASH_OUT_TO_CARD(20),
        SALE_COURSE(21);

        private final int code;

        PaymentCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static PaymentCode getPaymentCodeInstanceByCode(int code){
          Object[] result = Arrays.stream(values()).filter(item->item.getCode()==code).toArray();
          return result.length > 0 ? (PaymentCode) result[0] : null;
        }
    }

    public enum CurrencyType{
        USD;
        //multiple calls of values() in custom tag (everytime create clone), so i can cache it:
        public static final CurrencyType[] valueList = values();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", courseId=" + courseId +
                ", paymentCode=" + paymentCode +
                ", amount=" + amount +
                ", paymentDate=" + DATE_FORMAT.format(new Date(paymentDate)) +
                ", currencyId=" + currencyId +
                ", description='" + description + '\'' +
                '}';
    }
}
