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

        String pattern = ".{1,4}";
        String test = "asd";
        System.out.println(test.matches(pattern));

    }
}
