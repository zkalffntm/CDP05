package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 고객으로부터 GPS(x,y) 정보를 받고, 해당 위치에 근접한 박물관/전시회의 <br>
 * 이름과 관리자 IP를 제공하는 서비스
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/3/22
 */
public class IpSearchService extends AbstractService {

	/**
	 * sql select문을 이용하여 ip를 찾음
	 * 
	 * @param argument
	 *          gps(x,y)정보를 담은 double[2]
	 * @return {박물관명,byte,byte,byte,byte}
	 */
	@Override
	protected Object doQuery(Object argument) throws SQLException {

		// gps 정보
		double[] gpsInfo = (double[]) argument;

		// 쿼리 실행
		String sql = "select exhibition_name, exhibition_ip from exhibition where gps_x=? and gps_y=?";
		PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
		pstmt.setDouble(1, gpsInfo[0]);
		pstmt.setDouble(2, gpsInfo[1]);
		ResultSet resultSet = pstmt.executeQuery();

		// 결과 레코드를 객체에 저장
		Object[] objects = null;
		if (resultSet.next()) {
			objects = new Object[5];
			objects[0] = resultSet.getString("exhibition_name");
			String[] ips = resultSet.getString("exhibition_ip").split(".");
			for (int i = 1; i < objects.length; i++) {
				objects[i] = ips[i - 1];
			}
		}

		return objects;
	}

}
