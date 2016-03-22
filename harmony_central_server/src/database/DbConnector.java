package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB 접속을 위함
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/3/22
 */
public class DbConnector {

	// for singletone
	private static DbConnector instance = null;
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://psj.iptime.org:9393/harmony_central_server";
	private static final String USER = "test";
	private static final String PASSWORD = "test";
	
	private Connection connection;

	// 싱글톤 객체 반환
	public static DbConnector getInstance() {
		if (instance == null)
			instance = new DbConnector();
		return instance;
	}

	// 생성자 - DB와 연결함.
	private DbConnector() {

		try {
			Class.forName(DRIVER_NAME);
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Success - Connect MySQL");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Failed - Connect MySQL : " + e.getMessage());
		}
	}

	public void closeConnection() {
		try {
			connection.close();

			System.out.println("Success - Disconnect MySQL");
		} catch (SQLException e) {
			System.out.println("Failed - Disconnect MySQL : " + e.getMessage());
		}
	}

	// connection 반환
	public Connection getConnection() {
		return this.connection;
	}
}
