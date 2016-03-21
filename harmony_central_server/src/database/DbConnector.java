package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
//for singletone
	private static DbConnector instance = null;

	private String url = "jdbc:mysql://psj.iptime.org:9393/harmony_central_server";
	private String user = "test";
	private String password = "test";
	private Connection connection;

	// 싱글톤 객체 반환
	public static DbConnector getInstance() {
		if(instance == null)
			instance = new DbConnector();
		
		return instance;
	}
	
	// 생성자 - DB와 연결함.
	private DbConnector() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(url, user, password);
			this.connection.setAutoCommit(false);

			System.out.println("Success - Connect MySQL");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Failed - Connect MySQL : " + e.getMessage());
		}
	}
	
	public void closeConnection() {
		try {
			connection.close();
			
			System.out.println("Success - Disconnect MySQL");
		} catch(SQLException e) {
			System.out.println("Failed - Disconnect MySQL : " + e.getMessage());
		}
	}
	
	// connection 반환
	public Connection getConnection() {
		return this.connection;
	}
}
