package by.anelkin.easylearning;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.*;
import by.anelkin.easylearning.specification.account.SelectAllAccountSpecification;
import by.anelkin.easylearning.specification.account.SelectByCourseIdSpecification;
import by.anelkin.easylearning.specification.course.SelectAllCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.lesson.SelectAllLessonSpecification;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectAllMarkSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import by.anelkin.easylearning.specification.payment.SelectAllPaymentSpecification;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static by.anelkin.easylearning.entity.Mark.MarkType.*;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws RepositoryException {
//        CourseLesson lesson = new CourseLesson();
////        lesson.setChapterId(6);
////        lesson.setCreationDate(new Date(System.currentTimeMillis()));
////        lesson.setDuration(11111);
////        lesson.setName("Про всякое разное 123");
////        lesson.setPathToContent("https://drive.google.com/file/d/1iEEp7Yxcta_g6tRhC8b0J_EzTEbvacqi/preview");
//        lesson.setId(11);

        Payment payment = new Payment();
        payment.setAccountId(23);
        payment.setCurrencyId(1);
        payment.setDescription("Покупка по карте курса ");
        payment.setCourseId(1);

        payment.setAmount((new BigDecimal("-9.99")));
        payment.setPaymentCode(10);
        payment.setPaymentDate(System.currentTimeMillis());

        PaymentRepository repo = new PaymentRepository();
        repo.insert(payment);
        List<Payment> list = repo.query(new SelectAllPaymentSpecification());
        list.forEach(System.out::println);


    }

}
