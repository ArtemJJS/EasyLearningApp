package by.anelkin.easylearning;

import by.anelkin.easylearning.entity.*;
import by.anelkin.easylearning.exeption.RepositoryException;
import by.anelkin.easylearning.repository.*;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import by.anelkin.easylearning.specification.account.SelectAllAccountSpecification;
import by.anelkin.easylearning.specification.course.SelectAllCourseSpecification;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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


        AccRepository repo = new AccRepository();
        List<Account> list = repo.query(new SelectAllAccountSpecification());
        list.forEach(System.out::println);

        System.out.println("===============");
        Account account = list.get(0);
        System.out.println("===============");
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(account);
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bin);
            Account clon = (Account) in.readObject();
            clon.setId(199);
            clon.setRegistrDate(dateFormat.parse("0001-01-01"));

            Account clon2 = (Account) account.clone();
            clon2.setId(333);
            clon2.setRegistrDate(dateFormat.parse("1111-01-01"));
            System.out.println(clon);
            System.out.println(account);
            System.out.println(clon2);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        ----------
        System.out.println("===============");
        System.out.println("===============");
        System.out.println("===============");

        CourseRepository repo1 = new CourseRepository();
        List<Course> list1 = repo1.query(new SelectAllCourseSpecification());
        list1.forEach(System.out::println);

        System.out.println("===============");
        Course course = list1.get(0);
        System.out.println("===============");
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(course);
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bin);
            Course clon = (Course) in.readObject();
            clon.setId(199);
            clon.setPrice(new BigDecimal("99.99"));

            Course clon2 = (Course) course.clone();
            clon2.setId(333);
            clon2.setPrice(new BigDecimal("111.11"));
            System.out.println(clon);
            System.out.println(course);
            System.out.println(clon2);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


    }

}
