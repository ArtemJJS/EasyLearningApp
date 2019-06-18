package by.anelkin.easylearning.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestThread extends Thread {
    private ConnectionPool pool;
    private String threadName;

    public TestThread(int threadName) {
        this.threadName = "Thread" + threadName;
    }

    @Override
    public void run() {
        pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        int objectsCount = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from account");
            while (resultSet.next()) {
                objectsCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            pool.returnConnection(connection);
        }

        System.out.println(threadName + " read: " + objectsCount + " objects from SQL base.");
    }
}

