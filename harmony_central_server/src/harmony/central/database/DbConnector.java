package harmony.central.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB 접속을 위함. 싱글톤으로 사용.
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/6/2
 */
public class DbConnector {
  private static DbConnector instance = null;
  private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
  private static final String URL = "jdbc:mysql://psjpi.iptime.org:9393/"
      + DbLiteral.DB_NAME;
  private static final String USER = "test";
  private static final String PASSWORD = "test";

  private Connection connection;

  /**
   * 생성자. DB와 연동함. 싱글톤으로 사용해야하므로 외부에서는 getInstance()로 호출된다.
   */
  private DbConnector() {
    try {
      Class.forName(DRIVER_NAME);
      this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("Success - Connect database");
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("Failed - Connect database : " + e.getMessage());
    }
  }

  /**
   * 싱글톤 객체 반환. 처음 호출인 경우 한번 생성된다.
   * 
   * @return DbConnector 싱글톤 객체
   */
  public static DbConnector getInstance() {
    if (instance == null)
      instance = new DbConnector();
    return instance;
  }

  /**
   * DB 연결 종료.
   */
  public void closeConnection() {
    try {
      if (this.connection != null && !this.connection.isClosed()) {
        connection.close();
      }
      System.out.println("Success - Disconnect MySQL");
    } catch (SQLException e) {
      System.out.println("Failed - Disconnect MySQL : " + e.getMessage());
    }
    this.connection = null;
  }

  /**
   * DB 연결 객체 반환. DB 작업을 위해 필요하다.
   * 
   * @return Connection 객체
   */
  public Connection getConnection() {
    return this.connection;
  }
}
