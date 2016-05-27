package harmony.central.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import harmony.central.controller.ExhibitionController;
import harmony.central.controller.GpsController;
import harmony.central.model.Exhibition;
import harmony.central.model.Gps;
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
   * @return Object[][8] = {{박물관번호,박물관명,ip1,ip2,ip3,ip4,port,major},...}
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   * @throws JSONException
   *           JSON 관련 예외
   */
  @Override
  public Object doService(Object argument)
      throws SQLException, IOException, JSONException {

    // gps 정보
    JSONArray gpsInfo = (JSONArray) argument;
    double x = gpsInfo.getDouble(0);
    double y = gpsInfo.getDouble(1);

    List<Object[]> objArrList = new ArrayList<Object[]>();
    List<Integer> exhibitionNumList = new ArrayList<Integer>();
    Gps[] gpss = GpsController.getGpssWithXYCoverage(x, y);

    for (Gps gps : gpss) {
      int exhibitionNum = gps.getExhibitionNum();

      if (!exhibitionNumList.contains(exhibitionNum)) {
        exhibitionNumList.add(exhibitionNum);

        Exhibition exhibition = ExhibitionController
            .getExhibitionByNum(exhibitionNum);

        Object[] objArr = new Object[8];
        objArr[0] = exhibitionNum;
        objArr[1] = exhibition.getName();
        String[] ipParts = exhibition.getIp().split("\\.");
        for (int i = 0; i < 4; i++) {
          objArr[i + 2] = Integer.parseInt(ipParts[i]);
        }
        objArr[6] = exhibition.getPort();
        objArr[7] = exhibition.getBeaconMajor();
        objArrList.add(objArr);
      }
    }

    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][8]);
  }
}
