package by.anelkin.easylearning;


import by.anelkin.easylearning.exception.RepositoryException;
import lombok.extern.log4j.Log4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws RepositoryException, IOException, NoSuchAlgorithmException, MessagingException {
    UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }
}
