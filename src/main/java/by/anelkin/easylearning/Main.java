package by.anelkin.easylearning;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.*;
import by.anelkin.easylearning.specification.course.SelectByStateAndAuthorIdSpecification;
import lombok.extern.log4j.Log4j;

import java.text.SimpleDateFormat;
import java.util.List;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws RepositoryException {
//    CourseRepository repo = new CourseRepository();
//    List<Course> list = repo
//            .query(new SelectByStateAndAuthorIdSpecification(2, true));
////                .query(new SelectByAuthorIdSpecification(2));
//     Course course = list.get(list.size()-1);
//        course.setState((byte)-1);
//        repo.update(course);
//    list.forEach(System.out::println);
//
int index = 0;
        System.out.println(Course.CourseState.values()[index]);


    }

}
