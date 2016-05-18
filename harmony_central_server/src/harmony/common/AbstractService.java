package harmony.common;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 각 종 서비스를 수행하는 클래스들의 추상 클래스. 상속하여 doService(Object)를 구현하여야 함.
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/5/18
 */
public abstract class AbstractService {

	/**
	 * 
	 * @param argument
	 *            파라미터 객체
	 * @return 결과 객체
	 * @throws SQLException
	 *             SQL 관련 예외
	 * @throws IOException
	 *             IO 관련 예외
	 */
	public abstract Object doService(Object argument) throws SQLException, IOException;
}
