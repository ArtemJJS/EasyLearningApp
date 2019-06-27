package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, exclude = {"about", "pathToPhoto", "registrDate"})
public class Account extends AppEntity{
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

    public enum AccountType{
        GUEST,
        ADMIN,
        AUTHOR,
        USER
    }
}
