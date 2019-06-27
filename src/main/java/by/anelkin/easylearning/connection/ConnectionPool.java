package by.anelkin.easylearning.connection;


import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class ConnectionPool {
    private static ConnectionPool instance;
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);
    private static Lock lock = new ReentrantLock();
    private int minConnectionCount = 10;
    private int maxConnectionCount = 25;
    private BlockingQueue<ProxyConnection> availableConnections;
    private BlockingQueue<ProxyConnection> usedConnections;

    private ConnectionPool() {
        // TODO: 6/18/2019 сделать чтение параметров из файла
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        initPool();
    }

    private void initPool() {
        usedConnections = new ArrayBlockingQueue<>(maxConnectionCount);
        availableConnections = new ArrayBlockingQueue<>(maxConnectionCount);
        for (int i = 0; i < minConnectionCount; i++) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/easylearning?serverTimezone=Europe/Moscow&useSSL=true", "root", "106116");
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                availableConnections.offer(proxyConnection);
            } catch (SQLException e) {
                log.error("Connection to base wasn't created successfully! " + e);
            }
        }
        if (availableConnections.size() < 1) {
            throw new ExceptionInInitializerError("Unable to create any connection to base!");
        }
        log.info("Pool was initialized. Connections amount: " + availableConnections.size());
        checkConnectionsAmount();
    }

    public static ConnectionPool getInstance() {
        if (!isInitialized.get()) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new ConnectionPool();
                    isInitialized.set(true);
                    log.debug("ConnectionPool instance created.");
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public Connection takeConnection() {
        ProxyConnection connection = null;
        try {
            connection = availableConnections.take();
            usedConnections.offer(connection);
        } catch (InterruptedException e) {
            log.error("Problems with ConnectionPool: couldn't take connection from pool." + e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void returnConnection(Connection connection) {
        if (connection instanceof ProxyConnection) {
            log.debug("Returning of connection into the pool.");
            usedConnections.remove(connection);
            availableConnections.offer((ProxyConnection) connection);
        } else {
            log.info("Attempt to return wrong connection into the pool!");
        }
    }

    public void closePool() {
        for (int i = 0; i < minConnectionCount; i++) {
            try {
                ProxyConnection connection = availableConnections.take();
                connection.closeFromPool();
                // TODO: 6/21/2019 дерегистрация драйвера?
                Driver driver = DriverManager.getDriver("com.mysql.cj.jdbc.Driver");
                DriverManager.deregisterDriver(driver);
            } catch (InterruptedException | SQLException e) {
                log.warn("Problems with connection closing! " + e);
                // TODO: 6/21/2019 надо ли здесь interrupt?
            }
        }
    }

    // TODO: 6/21/2019 как правильно реагировать на утечку соединений
    private void checkConnectionsAmount() {
        TimerTask checkConnections = new TimerTask() {
            @Override
            public void run() {
                int availableAmount = availableConnections.size();
                int usedAmount = usedConnections.size();
                if (availableAmount + usedAmount != minConnectionCount) {
                    log.warn("Connections leak!!!");
                }
            }
        };

        Thread checker = new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(checkConnections, 0, 180_000); //period = 3 min
        });
        checker.setDaemon(true);
        checker.start();
    }


}
