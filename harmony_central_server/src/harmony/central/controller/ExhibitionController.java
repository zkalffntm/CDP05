package harmony.central.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import harmony.central.database.DbConnector;
import harmony.central.database.DbLiteral;
import harmony.central.model.Exhibition;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class ExhibitionController {
  public static Exhibition getExhibitionByNum(int num) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.EXHIBITION + " where "
        + DbLiteral.E_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    Exhibition exhibition = null;
    if (resultSet.next()) {
      exhibition = new Exhibition();
      exhibition.setNum(num);
      exhibition.setName(resultSet.getString(DbLiteral.E_NAME));
      exhibition.setBeaconMajor(resultSet.getInt(DbLiteral.E_BEACON_MAJOR));
      exhibition.setIp(resultSet.getString(DbLiteral.E_IP));
      exhibition.setPort(resultSet.getInt(DbLiteral.E_PORT));
      exhibition.setImage(resultSet.getString(DbLiteral.E_IMAGE));
      exhibition.setImageEdited(false);
    }

    return exhibition;
  }
}
