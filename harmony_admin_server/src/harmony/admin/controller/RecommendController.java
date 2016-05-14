package harmony.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;

/**
 * 추천경로 관리를 위한 컨트롤러
 * 
 * @author Seongjun Park
 * @since 2016/5/14
 * @version 2016/5/14
 *
 */
public class RecommendController {
  /**
   * 
   * @return
   * @throws SQLException
   */
  public String[][] getRecommends() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    List<String[]> recommedList = new ArrayList<String[]>();

    // 조회 쿼리 실행
    String sql = "select " + DbLiteral.R + "." + DbLiteral.R_NUM + ", "
        + DbLiteral.RI + "." + DbLiteral.SEQ_NUM + ", " + DbLiteral.R + "."
        + DbLiteral.R_CONTENT + ", " + DbLiteral.RI + "." + DbLiteral.I_NUM
        + " from " + DbLiteral.RECOMMEND + " as " + DbLiteral.R + ", "
        + DbLiteral.RECOMMEND_ITEM + " as " + DbLiteral.RI + " where "
        + DbLiteral.R + "." + DbLiteral.R_NUM + "=" + DbLiteral.RI + "."
        + DbLiteral.R_NUM + " order by " + DbLiteral.R_NUM + ", "
        + DbLiteral.SEQ_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 추천 레코드를 리스트에 추가
    List<String> recommend = null;
    int prev = -1;
    while (resultSet.next()) {
      int rNum = resultSet.getInt(DbLiteral.R_NUM);
      if (prev != rNum) {
        if (prev != -1) {
          recommedList
              .add((String[]) recommend.toArray(new String[recommend.size()]));
        }
        recommend = new ArrayList<String>();
        recommend.add(Integer.toString(rNum));
        recommend.add(resultSet.getString(DbLiteral.R_CONTENT));
      }
      recommend.add(Integer.toString(resultSet.getInt(DbLiteral.I_NUM)));
      prev = rNum;
    }
    if (recommend != null) {
      recommedList.add(
          (String[]) recommedList.toArray(new Object[recommedList.size()]));
    }

    return (String[][]) recommedList.toArray(new String[recommedList.size()][]);
  }

  /**
   * 
   * @param content
   * @param items
   * @throws SQLException
   */
  public void addRecommend(String content, int... items) throws SQLException {

    // 추천경로 레코드 삽입
    int num = this.insertRecommendRecord(content);

    // 추천경로의 전시물 레코드들을 삽입
    this.insertRecommendItemRecords(num, items);
  }

  /**
   * 
   * @param content
   * @return
   * @throws SQLException
   */
  private int insertRecommendRecord(String content) throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 조회 쿼리 실행
    String sql = "select max(" + DbLiteral.R_NUM + ") as " + DbLiteral.R_NUM
        + " from " + DbLiteral.RECOMMEND;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 추천경로 부여 번호 결정
    int num = 1;
    if (resultSet.next()) {
      num = resultSet.getInt(DbLiteral.M_NUM) + 1;
    }

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 삽입 쿼리 실행
    sql = "insert into " + DbLiteral.RECOMMEND + " values (?, ?)";
    pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.setString(2, content);
    pstmt.executeUpdate();

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);

    return num;
  }

  /**
   * 
   * @param num
   * @param items
   * @throws SQLException
   */
  private void insertRecommendItemRecords(int num, int... items)
      throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 삽입 쿼리 실행
    String sql = "insert into " + DbLiteral.RECOMMEND_ITEM
        + " values (?, ?, ?)";
    for (int i = 0; i < items.length; i++) {
      PreparedStatement pstmt = dbConnection.prepareStatement(sql);
      pstmt.setInt(1, num);
      pstmt.setInt(2, i + 1);
      pstmt.setInt(3, items[i]);
      pstmt.executeUpdate();
    }

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);
  }

  /**
   * 
   * @param num
   * @param content
   * @param items
   * @throws SQLException
   */
  public void modifyRecommend(int num, String content, int... items)
      throws SQLException {

    // 추천 경로 레코드 갱신
    this.updateRecommendRecord(num, content);

    // 이전 추천 경로 전시물 레코드들을 삭제
    this.deleteRecommendItemRecords(num);

    // 새 추천 경로 전시물 레코드들을 추가
    this.insertRecommendItemRecords(num, items);
  }

  /**
   * 
   * @param num
   * @param content
   * @throws SQLException
   */
  private void updateRecommendRecord(int num, String content)
      throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 삽입 쿼리 실행
    String sql = "update " + DbLiteral.RECOMMEND + " set " + DbLiteral.R_CONTENT
        + "=? where " + DbLiteral.R_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, content);
    pstmt.setInt(2, num);
    pstmt.executeUpdate();

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);
  }

  public void removeRecommend(int num) throws SQLException {

    // 추천 경로 레코드 삭제
    this.deleteRecommendRecord(num);

    // 추천 경로 전시물 레코드 삭제
    this.deleteRecommendItemRecords(num);
  }

  private void deleteRecommendRecord(int num) throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 이전 레코드들은 삭제
    String sql = "delete from " + DbLiteral.RECOMMEND + " where "
        + DbLiteral.R_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.executeUpdate();

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);
  }

  private void deleteRecommendItemRecords(int num) throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 이전 레코드들은 삭제
    String sql = "delete from " + DbLiteral.RECOMMEND_ITEM + " where "
        + DbLiteral.R_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.executeUpdate();

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);
  }
}
