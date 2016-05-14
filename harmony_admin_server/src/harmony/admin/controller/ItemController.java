package harmony.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;

public class ItemController {

  /**
   * 
   * @return
   * @throws SQLException
   */
  public String[][] getItems() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    List<String[]> itemList = new ArrayList<String[]>();

    // 조회 쿼리 실행
    String sql = "select * from " + DbLiteral.ITEM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 전시물 레코드 하나씩 리스트에 추가
    while (resultSet.next()) {
      String[] item = new String[7];
      item[0] = Integer.toString(resultSet.getInt(DbLiteral.I_NUM));
      item[1] = Integer.toString(resultSet.getInt(DbLiteral.M_NUM));
      item[2] = Integer.toString(resultSet.getInt(DbLiteral.BL_NUM));
      item[3] = resultSet.getString(DbLiteral.I_TITLE);
      item[4] = resultSet.getString(DbLiteral.I_ARTIST);
      item[5] = resultSet.getString(DbLiteral.I_CONTENT);
      item[6] = resultSet.getString(DbLiteral.I_IMAGE_FORMAT);
      itemList.add(item);
    }

    // 리스트를 배열화
    return (String[][]) itemList.toArray(new String[itemList.size()][]);
  }

  
}
