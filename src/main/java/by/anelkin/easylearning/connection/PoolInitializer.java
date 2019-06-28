package by.anelkin.easylearning.connection;

import lombok.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
class PoolInitializer {
    private static final String PATH_TO_PROPERTIES = "src/main/resources/db.properties";
    private Properties property;

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
    String getMinConnections(){
        return property.getProperty("min_connections");
    }

}
