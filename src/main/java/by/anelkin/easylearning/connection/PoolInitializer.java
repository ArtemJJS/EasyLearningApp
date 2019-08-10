package by.anelkin.easylearning.connection;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Log4j
class PoolInitializer {
    private static final String PATH_TO_PROPERTIES = "db.properties";

    private static final String PROPERTY_NAME_DRIVER = "driver";
    private static final String PROPERTY_NAME_URL= "url";
    private static final String PROPERTY_NAME_USER = "user";
    private static final String PROPERTY_NAME_PASSWORD = "password";
    private static final String PROPERTY_NAME_MIN_CONNECTIONS = "min_connections";
    private Properties property;
    private static final int DEFAULT_MIN_CONNECTIONS_AMOUNT = 10;

    PoolInitializer() {
        property = new Properties();
        initProperty();
    }

    private void initProperty() {
        try  {
            property.load(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(PATH_TO_PROPERTIES)));
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Unable to read initial connectionPool parameters. " + e);
        }
    }

    String getDriverName(){
        return property.getProperty(PROPERTY_NAME_DRIVER);
    }

    String getUrl(){
        return property.getProperty(PROPERTY_NAME_URL);
    }
    String getUser(){
        return property.getProperty(PROPERTY_NAME_USER);
    }
    String getPassword(){
        return property.getProperty(PROPERTY_NAME_PASSWORD);
    }
    int getMinConnections(){
        int connAmount;
        try{
            connAmount = Integer.parseInt(property.getProperty(PROPERTY_NAME_MIN_CONNECTIONS));
        }catch (NumberFormatException e){
            log.warn("Wrong minimal amount of connections in properties file!!!");
            connAmount = DEFAULT_MIN_CONNECTIONS_AMOUNT;
        }
        return connAmount;
    }
}
