package harmony.admin.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.Item;

public class Test {
  public static void main(String[] args) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ITEM + " order by "
        + DbLiteral.I_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Item> itemList = new ArrayList<Item>();
    while (resultSet.next()) {
      Item item = new Item();
      item.setNum(resultSet.getInt(DbLiteral.I_NUM));
      item.setTitle(resultSet.getString(DbLiteral.I_TITLE));
      item.setArtist(resultSet.getString(DbLiteral.I_ARTIST));
      item.setSimpleContent(resultSet.getString(DbLiteral.I_SIMPLE_CONTENT));
      item.setDetailContent(resultSet.getString(DbLiteral.I_DETAIL_CONTENT));
      item.setSize(resultSet.getString(DbLiteral.I_SIZE));
      item.setAreaNum((Integer) resultSet.getObject(DbLiteral.A_NUM));
      itemList.add(item);
    }
  }
}
