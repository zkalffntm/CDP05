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
 * @version 2016/5/25
 */
public class RecommendItemController {

  private static RecommendItem[] getRecommendItems() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.RECOMMEND_ITEM + " order by "
        + DbLiteral.R_NUM + ", " + DbLiteral.RI_SEQ;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<RecommendItem> recommendItemList = new ArrayList<RecommendItem>();
    while (resultSet.next()) {
      RecommendItem recommendItem = new RecommendItem();
      recommendItem.setNum(resultSet.getInt(DbLiteral.RI_NUM));
      recommendItem.setSeq(resultSet.getInt(DbLiteral.RI_SEQ));
      recommendItem.setRecommendNum(resultSet.getInt(DbLiteral.R_NUM));
      recommendItem.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      recommendItemList.add(recommendItem);
    }

    // 타입 변형 : List<> -> Object[]
    return (RecommendItem[]) recommendItemList
        .toArray(new RecommendItem[recommendItemList.size()]);
  }

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
      recommendItem.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      recommendItemList.add(recommendItem);
    }

    // 타입 변형 : List<> -> Object[]
    return (RecommendItem[]) recommendItemList
        .toArray(new RecommendItem[recommendItemList.size()]);

  }

  public static void saveRecommendItems(RecommendItem[] recommendItems)
      throws SQLException {

    // 삽입 또는 갱신
    for (int i = 0; i < recommendItems.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (recommendItems[i].getNum() == 0) {
        int recommendItemNum = insertRecommendItem(recommendItems[i]);
        recommendItems[i].setNum(recommendItemNum);
      } else {
        updateRecommendItem(recommendItems[i]);
      }
    }

    // 삭제
    RecommendItem[] resultRecommendItems = getRecommendItems();
    for (RecommendItem resultItemImage : resultRecommendItems) {
      boolean exists = false;
      for (RecommendItem recommendItem : recommendItems) {
        if (resultItemImage.getNum() == recommendItem.getNum()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteRecommendItemByNum(resultItemImage.getNum());
      }
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
    pstmt.setInt(3, recommendItem.getRecommendNum());
    pstmt.setInt(4, recommendItem.getItemNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return num;
  }

  private static void updateRecommendItem(RecommendItem recommendItem)
      throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 갱신 쿼리 실행
    String sql = "update recommend_item set ri_seq=?, r_num=?, i_num=? where ri_num=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, recommendItem.getSeq());
    pstmt.setInt(2, recommendItem.getRecommendNum());
    pstmt.setInt(3, recommendItem.getItemNum());
    pstmt.setInt(4, recommendItem.getNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static void deleteRecommendItemByNum(int num) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.RECOMMEND_ITEM + " where "
        + DbLiteral.RI_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
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
