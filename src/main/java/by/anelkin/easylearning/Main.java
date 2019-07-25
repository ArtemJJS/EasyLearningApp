package by.anelkin.easylearning;

//import by.anelkin.easylearning.connection.PoolInitializer;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.specification.account.SelectAllAccountSpecification;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws RepositoryException, IOException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int i = 0;
        while (i++ < 10) {
            char ch = (char) (random.nextInt(74) + 48);
            sb.append(ch);
        }
        char ch = 122;
        System.out.println(ch);
        System.out.println(sb.toString());
    }

    private static String generateSaltForPassword() {
        Random random = new Random();
        int part1 = (new Random()).nextInt(5) + 2;
        String salt = RandomStringUtils.randomAlphabetic(part1);
        salt += random.nextInt(200);
        salt += RandomStringUtils.randomAlphabetic(3);

        return salt;
    }
}
