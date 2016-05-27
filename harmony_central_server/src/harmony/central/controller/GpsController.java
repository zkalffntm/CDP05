package harmony.central.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.central.database.DbConnector;
import harmony.central.database.DbLiteral;
import harmony.central.model.Gps;
import harmony.common.DistanceManager;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class GpsController {

  private static Gps[] getGpss() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.GPS + " order by "
        + DbLiteral.G_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    List<Gps> gpsList = new ArrayList<Gps>();
    while (resultSet.next()) {
      Gps gps = new Gps();
      gps.setNum(resultSet.getInt(DbLiteral.G_NUM));
      gps.setX(resultSet.getDouble(DbLiteral.G_X));
      gps.setY(resultSet.getDouble(DbLiteral.G_Y));
      gps.setCoverage(resultSet.getInt(DbLiteral.G_COVERAGE));
      gps.setExhibitionNum(resultSet.getInt(DbLiteral.E_NUM));
      gpsList.add(gps);
    }

    // 타입 변형 : List<> -> Object[]
    return (Gps[]) gpsList.toArray(new Gps[gpsList.size()]);
  }

  public static Gps[] getGpssWithXYCoverage(double x, double y)
      throws SQLException {
    List<Gps> filteredGpsList = new ArrayList<Gps>();

    // coverage에 충족하는 gps 레코드만을 선별
    Gps[] gpss = getGpss();
    for (Gps gps : gpss) {
      if (((int) DistanceManager.distance(x, y, gps.getX(), gps.getY())) <= gps
          .getCoverage()) {
        filteredGpsList.add(gps);
      }
    }

    return (Gps[]) filteredGpsList.toArray(new Gps[filteredGpsList.size()]);
  }
}
