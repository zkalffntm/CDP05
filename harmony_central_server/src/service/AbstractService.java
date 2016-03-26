package service;

import java.sql.Connection;
import java.sql.SQLException;

import database.DbConnector;

/**
 * 각 종 서비스를 수행하는 클래스들의 추상 클래스. 상속하여 doQuery(Object)를 구현하여야 함.
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/3/22
 */
public abstract class AbstractService {

	/** DB 연결용 */
	private Connection dbConnection = DbConnector.getInstance().getConnection();

	/**
	 * DbConnector 인스턴스를 호출하여 자동 commit 비활성화 후 서비스를 한다. <br>
	 * 쿼리문이 끝나면 자동 commit 다시 설정한다.
	 * 
	 * @param argument
	 *          파라미터로 사용
	 * @return 결과 객체
	 * @throws SQLException
	 */
	public Object doService(Object argument) throws SQLException {
		boolean commit = dbConnection.getAutoCommit();

		// 오토커밋 비활성화
		dbConnection.setAutoCommit(false);

		// 쿼리 실행
		Object result = this.doQuery(argument);

		// 자동commit 여부 원상태
		DbConnector.getInstance().getConnection().commit();
		DbConnector.getInstance().getConnection().setAutoCommit(commit);

		return result;
	}

	/**
	 * 지정된 DbConnection 객체 반환
	 * 
	 * @return DbConnection
	 */
	protected Connection getDbConnection() {
		return this.dbConnection;
	}

	/**
	 * 서비스의 쿼리문 부분
	 * 
	 * @param argument
	 *          파라미터로 사용
	 * @return 결과 객체
	 * @exception SQLException
	 */
	protected abstract Object doQuery(Object argument) throws SQLException;
}
