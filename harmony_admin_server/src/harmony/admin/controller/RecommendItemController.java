package harmony.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.RecommendItem;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class RecommendItemController {

  public static RecommendItem[] getRecommendItemsByRecommendNum(
      int recommendNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.RECOMMEND_ITEM + " where "
        + DbLiteral.R_NUM + "=? order by " + DbLiteral.RI_NUM + ", "
        + DbLiteral.RI_SEQ;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, recommendNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<RecommendItem> recommendItemList = new ArrayList<RecommendItem>();
    while (resultSet.next()) {
      RecommendItem recommendItem = new RecommendItem();
      recommendItem.setNum(resultSet.getInt(DbLiteral.RI_NUM));
      recommendItem.setSeq(resultSet.getInt(DbLiteral.RI_SEQ));
      recommendItem.setRecommendNum(recommendNum);
      recommendItem.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      recommendItemList.add(recommendItem);
    }

    // 타입 변형 : List<> -> Object[]
    return (RecommendItem[]) recommendItemList
        .toArray(new RecommendItem[recommendItemList.size()]);

  }

  static void deleteRecommendItemByItemNum(int itemNum) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.RECOMMEND_ITEM + " where "
        + DbLiteral.I_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  public static void deleteRecommendItemByRecommendNum(int recommendNum)
      throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.RECOMMEND_ITEM + " where "
        + DbLiteral.R_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, recommendNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }
}
