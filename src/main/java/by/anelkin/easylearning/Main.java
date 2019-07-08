package by.anelkin.easylearning;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.repository.*;
import by.anelkin.easylearning.specification.account.SelectAllAccountSpecification;
import by.anelkin.easylearning.specification.course.SelectAllCourseSpecification;
import by.anelkin.easylearning.specification.course.SelectCoursesPurchasedByUserSpecification;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectAllMarkSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
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
//        log.info("Start of main.");
//        ConnectionPool connectionPool = ConnectionPool.getInstance();
//        Connection connection = connectionPool.takeConnection();
//        log.debug("Connection = " + connection);
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet result = statement.executeQuery("SELECT * from account");
//            while (result.next()){
//                System.out.println(result.getString("id_user"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/easylearning", "root", "106116");
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery("select * from account");
//        while(resultSet.next()){
//            System.out.println(resultSet.getString("id_user"));
//        }

//        Long start = System.currentTimeMillis();
//        ExecutorService executorService = Executors.newFixedThreadPool(100);
//        for (int i = 0; i < 30000; i++) {
//            executorService.submit(new TestThread(i));
//        }
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(30, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(ConnectionPool.getInstance().connectionsAvailable());
//        Long finish = System.currentTimeMillis();
//        System.out.println("Time seconds = " + (finish-start)/1000d);
//        log.info("Main FINISHED!");
//

//       Mark mark = new Mark(Mark.MarkType.AUTHOR_MARK);
//       mark.setMarkDate(new Date(System.currentTimeMillis()));
//       mark.setComment("ASDasdasdasdas");
//       mark.setMarkValue(4);
//       mark.setTargetId(2);
//       mark.setAccId(16);

        MarkRepository repo = new MarkRepository();
        List<Mark> list = repo.query(new SelectAllMarkSpecification(COURSE_MARK));
        list.forEach(System.out::println);
        Mark mark = list.get(0);
        repo.delete(mark);
    }

}
