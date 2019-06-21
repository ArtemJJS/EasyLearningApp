package by.anelkin.easylearning;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.repository.*;
import by.anelkin.easylearning.specification.account_spec.SelectAllAccount;
import by.anelkin.easylearning.specification.chapter_spec.SelectAllChapter;
import by.anelkin.easylearning.specification.course_spec.SelectAllCourse;
import by.anelkin.easylearning.specification.lesson_spec.SelectAllLesson;
import by.anelkin.easylearning.specification.payment_spec.SelectAllPayment;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
//        log.info("Start of main.");
//        ConnectionPool connectionPool = ConnectionPool.getInstance();
//        Connection connection = connectionPool.getConnection();
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


        PaymentRepository repo = new PaymentRepository();
        List<Payment> list = repo.query(new SelectAllPayment());

        Payment payment = list.get(0);
        payment.setAmount(new BigDecimal("99.99"));
        payment.setDescription("Updated from main");
        repo.update(payment);

        list = repo.query(new SelectAllPayment());
        list.forEach(System.out::println);


    }

}
