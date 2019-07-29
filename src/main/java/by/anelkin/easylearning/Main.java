package by.anelkin.easylearning;


import by.anelkin.easylearning.exception.RepositoryException;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws RepositoryException, IOException, NoSuchAlgorithmException {

        System.out.println(ResourceBundle.getBundle("file_storage").getString("file_folder"));

    }
}
