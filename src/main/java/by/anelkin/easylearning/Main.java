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
import java.util.List;
import java.util.Properties;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws RepositoryException, IOException {
//    CourseRepository repo = new CourseRepository();
//    List<Course> list = repo
//            .query(new SelectByStateAndAuthorIdSpecification(2, true));
////                .query(new SelectByAuthorIdSpecification(2));
//     Course course = list.get(list.size()-1);
//        course.setState((byte)-1);
//        repo.update(course);
//    list.forEach(System.out::println);
//

        String PATH_TO_PROPERTIES = "db.properties";
//        String PATH_TO_PROPERTIES = "src/main/java/by/anelkin/easylearning/resources/db.properties";
        Properties property = new Properties();


//        try (InputStream inputStream = new FileInputStream(PATH_TO_PROPERTIES)) {
//            property.load(inputStream);
//            System.out.println(property.get("user"));
//        } catch (IOException e) {
//            log.error("Can't read properties!");
//            throw new ExceptionInInitializerError("Unable to read initial connectionPool parameters. " + e);
//        }



        property.load((new Course()).getClass().getClassLoader().getResourceAsStream(PATH_TO_PROPERTIES));

        System.out.println(property.size());


}

}
