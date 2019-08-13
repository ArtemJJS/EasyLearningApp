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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class thread-safe singleton to provide {@link Connection} to db
 * controls opened connections amount
 * Keeps connections open for increased application performance
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
public class ConnectionPool {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String DRIVER_NAME;
    private static int MIN_CONNECTIONS_AMOUNT;
    private Thread connCountChecker;
    private AtomicBoolean connAmountCheckingFlag = new AtomicBoolean(false);
    private static int CONN_AMOUNT_CHECK_INTERVAL = 60 * 60 * 1000;

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

    /**
     * inits entry params for instance
     */
    private void initPool() {
        log.info("URL: " + URL);
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

    /**
     * @return instance {@link ConnectionPool}
     * thread-safe initialisation inside
     */
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

    /**
     * provides active connection to db on logic request
     *
     * @return {@link Connection}
     */
    public Connection takeConnection() {
        ProxyConnection connection = null;
        try {
            while (connAmountCheckingFlag.get()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            connection = availableConnections.take();
            usedConnections.offer(connection);
        } catch (InterruptedException e) {
            log.error("Problems with ConnectionPool: couldn't take connection from pool." + e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    /**
     * returns connection to pool insted of closing it
     *
     * @param connection - connection to be returned
     */
    void returnConnection(Connection connection) {
        if (connection instanceof ProxyConnection) {
            usedConnections.remove(connection);
            availableConnections.offer((ProxyConnection) connection);
        } else {
            log.warn("Attempt to return wrong connection into the pool!");
        }
    }

    /**
     * close connections, stop check connections amount task
     * incoe {@link ConnectionPool#deregisterDrivers()}
     */
    public void closePool() {
        connCountChecker.interrupt();
        for (int i = 0; i < MIN_CONNECTIONS_AMOUNT; i++) {
            try {
                ProxyConnection connection = availableConnections.take();
                connection.closeFromPool();
            } catch (InterruptedException | SQLException e) {
                log.warn("Problems with connection closing! " + e);
            }
        }
        deregisterDrivers();
        log.info("Pool has been closed, drivers deregistered.");
    }

    /**
     * deregister drivers
     */
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


    /**
     * task for scheduled check active connection amount in separated thread
     */
    private void checkConnectionsAmount() {
        TimerTask checkConnections = new TimerTask() {
            @Override
            public void run() {
                int availableAmount = availableConnections.size();
                int usedAmount = usedConnections.size();

                try {
                    connAmountCheckingFlag.set(true);
                    TimeUnit.SECONDS.sleep(1);
                    if (availableAmount + usedAmount < MIN_CONNECTIONS_AMOUNT) {
                        log.warn("Connections leak!!! Current amount " + (availableAmount + usedAmount)
                                + ". Attempt to create additional connection instead.");
                        for (int i = 0; i < MIN_CONNECTIONS_AMOUNT - availableAmount - usedAmount; i++) {
                            try {
                                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                                ProxyConnection proxyConnection = new ProxyConnection(connection);
                                availableConnections.offer(proxyConnection);
                                log.info("Additional connection has been created due to connection leak.");
                            } catch (SQLException e) {
                                log.error("Connection to base wasn't created successfully! MinConnectionsAmount decreased by 1. " + e);
                                MIN_CONNECTIONS_AMOUNT--;
                            }
                        }
                    } else {
                        log.debug("Connections amount checked. Amount: " + (availableAmount + usedAmount));
                    }
                } catch (InterruptedException e) {
                    log.error(e);
                } finally {
                    connAmountCheckingFlag.set(false);
                }
            }
        };

        connCountChecker = new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(checkConnections, 3, CONN_AMOUNT_CHECK_INTERVAL); // once in an hour
        });
        connCountChecker.start();
    }


}
