package by.anelkin.easylearning;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Mark;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.repository.MarkRepository;
import by.anelkin.easylearning.specification.accountspec.SelectAccByType;
import by.anelkin.easylearning.specification.accountspec.SelectAllAccount;
import by.anelkin.easylearning.specification.accountspec.SelectRegistrDateAfter;
import by.anelkin.easylearning.specification.markspec.SelectAllMark;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static by.anelkin.easylearning.entity.Account.AccountType.*;
import static by.anelkin.easylearning.entity.Mark.MarkType.*;

@Log4j
public class Main {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
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

        MarkRepository markRepo = new MarkRepository();

        List<Mark> authorsMarks = markRepo.query(new SelectAllMark(AUTHOR_MARK));
        authorsMarks.forEach(System.out::println);

        Mark mark = authorsMarks.get(1);
        mark.setComment("Updated from main comment!");

        markRepo.update(mark);
        System.out.println("================");
        authorsMarks = markRepo.query(new SelectAllMark(AUTHOR_MARK));
        authorsMarks.forEach(System.out::println);


     //   accRepository.delete(accountList.get(6));



    }

}
