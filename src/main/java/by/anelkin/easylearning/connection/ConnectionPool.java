package by.anelkin.easylearning.connection;


import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class ConnectionPool {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String DRIVER_NAME;
    private static final int MIN_CONNECTIONS_AMOUNT;

    private static ConnectionPool instance;
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);
    private static Lock lock = new ReentrantLock();
    private BlockingQueue<ProxyConnection> availableConnections;
    private BlockingQueue<ProxyConnection> usedConnections;

    static {
        PoolInitializer poolInitializer = new PoolInitializer();
        URL = poolInitializer.getUrl();
        USER = poolInitializer.getUser();
        PASSWORD = poolInitializer.getPassword();
        DRIVER_NAME = poolInitializer.getDriverName();
        MIN_CONNECTIONS_AMOUNT = poolInitializer.getMinConnections();
    }

    private ConnectionPool() {
        try {
            Class.forName(DRIVER_NAME);
            initPool();
        } catch (Exception e) {
            log.fatal(e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private void initPool() {
        // TODO: 6/28/2019 реализовать max_connections_amount поле
        log.info("URL: " + URL);
        String temp = URL;
        System.out.println(temp);
        usedConnections = new LinkedBlockingQueue<>(MIN_CONNECTIONS_AMOUNT);
        availableConnections = new LinkedBlockingQueue<>(MIN_CONNECTIONS_AMOUNT);
        for (int i = 0; i < MIN_CONNECTIONS_AMOUNT; i++) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                availableConnections.offer(proxyConnection);
            } catch (SQLException e) {
                log.error("Connection to base wasn't created successfully! " + e);
            }
        }
        if (availableConnections.size() < 1) {
            log.fatal("ConnectionPool couldn't create any connection to database!!!");
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

    void returnConnection(Connection connection) {
        if (connection instanceof ProxyConnection) {
            usedConnections.remove(connection);
            availableConnections.offer((ProxyConnection) connection);
        } else {
            log.warn("Attempt to return wrong connection into the pool!");
        }
    }

    public void closePool() {
        for (int i = 0; i < MIN_CONNECTIONS_AMOUNT; i++) {
            try {
                ProxyConnection connection = availableConnections.take();
                connection.closeFromPool();
            } catch (InterruptedException | SQLException e) {
                log.warn("Problems with connection closing! " + e);
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                log.warn("Problems with driver deregister! " + e);
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
                if (availableAmount + usedAmount != MIN_CONNECTIONS_AMOUNT) {
                    log.warn("Connections leak!!! Current amount " + (availableAmount + usedAmount));
                }
            }
        };

//        Thread checker = new Thread(() -> {
//            Timer timer = new Timer();
//            timer.schedule(checkConnections, 0, 180_000); //period = 3 min
//        });
//        checker.setDaemon(true);
//        checker.start();
    }


}
