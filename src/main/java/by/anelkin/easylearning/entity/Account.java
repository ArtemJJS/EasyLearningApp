package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * model account entity
 * contains {@link AccountType} enum
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"about", "pathToPhoto", "updatePhotoPath"})
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
    private String updatePhotoPath;
    private String passSalt;


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
