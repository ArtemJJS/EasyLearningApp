package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, exclude = {"about", "pathToPhoto", "registrDate"})
public class Account extends AppEntity {
    private int id;
    private String login;
    private String password;
    private String email;
    private String name;
    private String surname;
    private Date birthDate;
    private String phoneNumber;
    private Date registrDate;
    private String about;
    private String pathToPhoto;
    private AccountType type;
    private double avgMark;
    private BigDecimal balance;

    public enum AccountType{
        GUEST,
        USER,
        AUTHOR,
        ADMIN
    }

    @Override
    public Account clone() throws CloneNotSupportedException {
        return (Account) super.clone();
    }
}
