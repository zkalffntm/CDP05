package harmony.central.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 고객으로부터 GPS(x,y) 정보를 받고, 해당 위치에 근접한 박물관/전시회의 <br>
 * 이름과 관리자 IP를 제공하는 서비스
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/4/4
 */
public class ProviderListService extends AbstractService {

	/**
	 * sql select문을 이용하여 ip를 찾음
	 * 
	 * @param argument
	 *          gps(x,y)정보를 담은 double[2]
	 * @return Object[][] = {박물관명,byte,byte,byte,byte,int}
	 */
	@Override
	protected Object doQuery(Object argument) throws SQLException {

		// gps 정보
		double[] gpsInfo = (double[]) argument;

		// 쿼리 실행
		String sql = "select e_name, e_ip from exhibition where e_gps_x=? and e_gps_y=?";
		PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
		pstmt.setDouble(1, gpsInfo[0]);
		pstmt.setDouble(2, gpsInfo[1]);
		ResultSet resultSet = pstmt.executeQuery();

		// 결과 레코드를 객체에 저장
		List<Object[]> objArrList = new ArrayList<Object[]>();
		while (resultSet.next()) {
			Object[] objArr = new Object[6];
			objArr[0] = resultSet.getString("e_name");
			String[] ips = resultSet.getString("e_ip").split(".");
			for (int i = 1; i < objArr.length - 1; i++) {
				objArr[i] = Byte.parseByte(ips[i - 1]);
			}
			objArr[5] = resultSet.getInt("e_port");
			objArrList.add(objArr);
		}

		// Object[][] 완성
		Object[][] objArrArr = new Object[objArrList.size()][];
		for (int i = 0; i < objArrArr.length; i++) {
			objArrArr[i] = objArrList.get(i);
		}

		return objArrArr;
	}
}
