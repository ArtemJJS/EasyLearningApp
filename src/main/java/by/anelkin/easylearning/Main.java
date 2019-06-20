package by.anelkin.easylearning;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import by.anelkin.easylearning.entity.CourseChapter;
import by.anelkin.easylearning.entity.CourseLesson;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.ChapterRepository;
import by.anelkin.easylearning.repository.CourseRepository;
import by.anelkin.easylearning.repository.LessonRepository;
import by.anelkin.easylearning.specification.account_spec.SelectAllAccount;
import by.anelkin.easylearning.specification.chapter_spec.SelectAllChapter;
import by.anelkin.easylearning.specification.course_spec.SelectAllCourse;
import by.anelkin.easylearning.specification.lesson_spec.SelectAllLesson;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
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



        LessonRepository repo = new LessonRepository();
        List<CourseLesson> list = repo.query(new SelectAllLesson());

        CourseLesson lesson = list.get(0);
        File content = lesson.getContent();

        list = repo.query(new SelectAllLesson());
        list.forEach(System.out::println);


    }

}
