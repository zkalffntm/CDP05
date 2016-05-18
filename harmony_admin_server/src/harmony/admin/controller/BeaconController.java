package harmony.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.Beacon;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class BeaconController {

  public static Beacon[] getBeacons() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BEACON + " order by "
        + DbLiteral.BE_MINOR;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Beacon> beaconList = new ArrayList<Beacon>();
    while (resultSet.next()) {
      Beacon beacon = new Beacon();
      beacon.setMinor(resultSet.getInt(DbLiteral.BE_MINOR));
      beacon.setComment(resultSet.getString(DbLiteral.BE_COMMENT));
      beacon.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      beaconList.add(beacon);
    }

    // 타입 변형 : List<> -> Object[]
    return (Beacon[]) beaconList.toArray(new Beacon[beaconList.size()]);
  }

  /**
   * 
   * @param itemNum
   * @throws SQLException
   */
  static void deleteBeaconByItemNum(int itemNum) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.BEACON + " where " + DbLiteral.I_NUM
        + "=?";

    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }
}
