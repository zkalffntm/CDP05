package harmony.central.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import harmony.central.database.DbConnector;
import harmony.central.database.DbLiteral;
import harmony.central.model.Account;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class AccountController {

  public static Account getAccountByIdAndPassword(String id, String password)
      throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ACCOUNT + " where "
        + DbLiteral.A_ID + "=? and " + DbLiteral.A_PASSWORD + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, id);
    pstmt.setString(2, password);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    Account account = null;
    if (resultSet.next()) {
      account = new Account();
      account.setId(id);
      account.setPassword(password);
      account.setExhibitionNum(resultSet.getInt(DbLiteral.E_NUM));
    }

    return account;
  }
}
