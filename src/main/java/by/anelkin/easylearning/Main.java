package by.anelkin.easylearning;

//import by.anelkin.easylearning.connection.PoolInitializer;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.*;
import by.anelkin.easylearning.specification.course.SelectByStateAndAuthorIdSpecification;
import lombok.extern.log4j.Log4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws RepositoryException, IOException {
//        Properties property = new Properties();
//        property.load(Objects.requireNonNull
//                (Main.class.getClassLoader().getResourceAsStream("test.properties")));
//        System.out.println(property.size());

        Locale locale = new Locale("ru", "RU");
        ResourceBundle rb = ResourceBundle.getBundle("text_resources", Locale.FRANCE);
        System.out.println(rb.getString("hello"));
    }

}
