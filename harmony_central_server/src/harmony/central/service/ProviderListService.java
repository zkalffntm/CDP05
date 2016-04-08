package harmony.central.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import harmony.common.AbstractService;

/**
 * 고객으로부터 GPS(x,y) 정보를 받고, 해당 위치에 근접한 박물관/전시회의 <br>
 * 이름과 관리자 IP를 제공하는 서비스
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/4/8
 */
public class ProviderListService extends AbstractService {

	/**
	 * sql select문을 이용하여 ip를 찾음
	 * 
	 * @param argument
	 *          JSONArray = {double, double}
	 * @return Object[][7] = {{박물관명,ip1,ip2,ip3,ip4,port,major},...}
	 * @throws SQLException
	 *           SQL 관련 예외
	 * @throws JSONException
	 *           JSON 관련 예외
	 */
	@Override
	protected Object doQuery(Object argument) throws SQLException, JSONException {

		// gps 정보
		JSONArray gpsInfo = (JSONArray) argument;

		// 쿼리 실행
		String sql = "select e.e_name, e.e_ip, e.e_port, e.e_beacon_major from exhibition as e, gps as g where g.g_x=? and g.g_y=? and e.e_num=g.e_num";
		PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
		pstmt.setDouble(1, gpsInfo.getDouble(0));
		pstmt.setDouble(2, gpsInfo.getDouble(1));
		ResultSet resultSet = pstmt.executeQuery();

		// 결과 레코드를 객체에 저장
		List<Object[]> objArrList = new ArrayList<Object[]>();
		while (resultSet.next()) {
			Object[] objArr = new Object[7];
			objArr[0] = resultSet.getString("e_name");
			String[] ips = resultSet.getString("e_ip").split("\\.");
			for (int i = 1; i < 5; i++) {
				objArr[i] = Integer.parseInt(ips[i - 1]);
			}
			objArr[5] = resultSet.getInt("e_port");
			objArr[6] = resultSet.getInt("e_beacon_major");
			objArrList.add(objArr);
		}

		// List<Object[]> to Object[][]
		Object[][] objArrArr = new Object[objArrList.size()][];
		for (int i = 0; i < objArrArr.length; i++) {
			objArrArr[i] = objArrList.get(i);
		}

		return objArrArr;
	}
}
