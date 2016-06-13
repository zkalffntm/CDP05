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
 * @version 2016/5/31
 */
public class RecommendItemController {

  /**
   * 추천경로별 전시물 레코드들을 가져옴. 추천목록 보기 시 호출함.
   * 
   * @param recommendNum
   *          추천경로 번호
   * @return RecommendItem[]
   * @throws SQLException
   *           SQL 관련 예외
   */
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
      recommendItem.setItemNum((Integer) resultSet.getObject(DbLiteral.I_NUM));
      recommendItemList.add(recommendItem);
    }

    // 타입 변형 : List<> -> Object[]
    return (RecommendItem[]) recommendItemList
        .toArray(new RecommendItem[recommendItemList.size()]);

  }

  static void saveRecommendItems(RecommendItem[] recommendItems)
      throws SQLException {

    // 삽입 전 전부 삭제
    deleteRecommendItems();

    // 삽입
    for (int i = 0; i < recommendItems.length; i++) {
      recommendItems[i].setNum(insertRecommendItem(recommendItems[i]));
    }
  }

  private static int insertRecommendItem(RecommendItem recommendItem)
      throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int num = getMaxRecommendItemNum() + 1;
    String sql = "insert into " + DbLiteral.RECOMMEND_ITEM
        + " values (?, ?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.setInt(2, recommendItem.getSeq());
    if (recommendItem.getRecommendNum() == 0) {
      pstmt.setObject(3, null);
    } else {
      pstmt.setInt(3, recommendItem.getRecommendNum());
    }
    if (recommendItem.getItemNum() == 0) {
      pstmt.setObject(4, null);
    } else {
      pstmt.setInt(4, recommendItem.getItemNum());
    }
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return num;
  }

  private static void deleteRecommendItems() throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.RECOMMEND_ITEM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
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

  static void deleteRecommendItemByRecommendNum(int recommendNum)
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

  private static int getMaxRecommendItemNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.RI_NUM + ") as " + DbLiteral.RI_NUM
        + " from " + DbLiteral.RECOMMEND_ITEM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.RI_NUM);
    }

    return maxNum;
  }
}
