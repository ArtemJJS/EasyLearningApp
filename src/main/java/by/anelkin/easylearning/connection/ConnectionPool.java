package by.anelkin.easylearning.connection;


import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class ConnectionPool {
    private static ConnectionPool instance;
    private static Lock lock = new ReentrantLock();
    private int connectionCount = 25;
    private BlockingQueue<Connection> queue;

    private ConnectionPool() {
        initPool();
    }

    private void initPool() {
        queue = new ArrayBlockingQueue<>(connectionCount);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < connectionCount; i++) {
                // TODO: 6/18/2019 сделать чтение параметров из файла
                queue.put(DriverManager.getConnection("jdbc:mysql://localhost/easylearning?serverTimezone=Europe/Moscow&useSSL=true", "root", "106116"));
            }
        } catch (ClassNotFoundException | InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionPool getInstance() {
        lock.lock();
        if (instance == null) {
            instance = new ConnectionPool();
            log.debug("ConnectionPool instance created.");
        }
        lock.unlock();
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void returnConnection(Connection connection) {
        try {
            queue.put(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int connectionsAvailable() {
        return queue.size();
    }


}
