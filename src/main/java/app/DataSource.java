package app;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataSource {
  private static BasicDataSource dataSource;
  

  // Static block to initialize the app.DataSource
  static {
    try {
      Properties properties = loadProperties();

      // Load MySQL connection details from properties
      String hostName = properties.getProperty("MySQL_IP_ADDRESS");
      String port = properties.getProperty("MySQL_PORT");
      String database = properties.getProperty("DB_NAME");
      String username = properties.getProperty("DB_USERNAME");
      String password = properties.getProperty("DB_PASSWORD");

      // Load connection pool configurations
      int initialSize = Integer.parseInt(properties.getProperty("dbcp2.initialSize", "5"));
      int maxTotal = Integer.parseInt(properties.getProperty("dbcp2.maxTotal", "50"));

      // Construct the JDBC URL
      String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", hostName, port, database);

      // Initialize the BasicDataSource
      dataSource = new BasicDataSource();
      dataSource.setUrl(url);
      dataSource.setUsername(username);
      dataSource.setPassword(password);
      dataSource.setInitialSize(initialSize);
      dataSource.setMaxTotal(maxTotal);

      // Ensure MySQL JDBC driver is loaded
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize app.DataSource", e);
    }
  }

  // Static method to load properties
  private static Properties loadProperties() throws IOException {
    Properties properties = new Properties();
    try (InputStream input = DataSource.class.getClassLoader().getResourceAsStream("application.properties")) {
      if (input == null) {
        throw new IOException("application.properties file is missing.");
      }
      properties.load(input);
    } catch (IOException ex) {
      throw new IOException("Error loading application.properties", ex);
    }
    return properties;
  }

  // Public method to retrieve the app.DataSource
  public static BasicDataSource getDataSource() {
    return dataSource;
  }
}
