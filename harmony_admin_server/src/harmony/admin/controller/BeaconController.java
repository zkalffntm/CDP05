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
      beacon.setItemNum((Integer) resultSet.getObject(DbLiteral.I_NUM));
      beaconList.add(beacon);
    }

    // 타입 변형 : List<> -> Object[]
    return (Beacon[]) beaconList.toArray(new Beacon[beaconList.size()]);
  }

  public static void saveBeacons(Beacon[] beacons) throws SQLException {

    // 삽입 또는 갱신
    for (int i = 0; i < beacons.length; i++) {

      // 존재하지 않은 레코드인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (getBeaconByMinor(beacons[i].getMinor()) == null) {
        insertBeacon(beacons[i]);
      } else {
        updateBeacon(beacons[i]);
      }
    }

    // 삭제
    Beacon[] resultBeacons = getBeacons();
    for (Beacon resultBeacon : resultBeacons) {
      boolean exists = false;
      for (Beacon beacon : beacons) {
        if (resultBeacon.getMinor() == beacon.getMinor()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteBeaconByMinor(resultBeacon.getMinor());
      }
    }
  }

  private static Beacon getBeaconByMinor(int minor) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BEACON + " where "
        + DbLiteral.BE_MINOR + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, minor);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 객체화
    Beacon beacon = null;
    if (resultSet.next()) {
      beacon = new Beacon();
      beacon.setMinor(minor);
      beacon.setComment(resultSet.getString(DbLiteral.BE_COMMENT));
      beacon.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
    }

    return beacon;
  }

  private static int insertBeacon(Beacon beacon) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int beaconMinor = beacon.getMinor();
    String sql = "insert into " + DbLiteral.BEACON + " values (?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, beacon.getMinor());
    pstmt.setString(2, beacon.getComment());
    if (beacon.getItemNum() == 0) {
      pstmt.setObject(3, null);
    } else {
      pstmt.setInt(3, beacon.getItemNum());
    }
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return beaconMinor;
  }

  private static void updateBeacon(Beacon beacon) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    String sql = "update " + DbLiteral.BEACON + " set " + DbLiteral.BE_COMMENT
        + "=?, " + DbLiteral.I_NUM + "=? where " + DbLiteral.BE_MINOR + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, beacon.getComment());
    if (beacon.getItemNum() == 0) {
      pstmt.setObject(2, null);
    } else {
      pstmt.setInt(2, beacon.getItemNum());
    }
    pstmt.setInt(3, beacon.getMinor());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static void deleteBeaconByMinor(int minor) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.BEACON + " where "
        + DbLiteral.BE_MINOR + "=?";

    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, minor);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

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
