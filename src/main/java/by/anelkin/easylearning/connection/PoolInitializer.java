package by.anelkin.easylearning.connection;

import lombok.extern.log4j.Log4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j
class PoolInitializer {
    private static final String PATH_TO_PROPERTIES = "src/main/resources/db.properties";
    private Properties property;
    private static final int DEFAULT_MIN_CONNECTIONS_AMOUNT = 10;

    PoolInitializer() {
        property = new Properties();
        initProperty();
    }

    private void initProperty() {
        try (InputStream inputStream = new FileInputStream(PATH_TO_PROPERTIES)) {
           property.load(inputStream);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Unable to read initial connectionPool parameters. " + e);
        }
    }

    String getDriverName(){
        return property.getProperty("driver");
    }
    String getUrl(){
        return property.getProperty("url");
    }
    String getUser(){
        return property.getProperty("user");
    }
    String getPassword(){
        return property.getProperty("password");
    }
    int getMinConnections(){
        int connAmount;
        try{
            connAmount = Integer.parseInt(property.getProperty("min_connections"));
        }catch (NumberFormatException e){
            log.warn("Wrong minimal amount of connections in properties file!!!");
            connAmount = DEFAULT_MIN_CONNECTIONS_AMOUNT;
        }
        return connAmount;
    }

}
