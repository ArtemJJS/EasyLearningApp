package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Payment extends AppEntity{
    private int id;
    private int accountId;
    private int courseId;
    private int paymentCode;
    private BigDecimal amount;
    private Date paymentDate;
    private String currency;
    private String description;
}
