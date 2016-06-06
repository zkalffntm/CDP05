package harmony.central.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import harmony.central.database.DbConnector;
import harmony.central.database.DbLiteral;
import harmony.central.model.Visited;

/**
 * 
 * @author Seongjun Park
 * @since 2016/6/6
 * @version 2016/6/6
 */
public class VisitedController {

  /**
   * 
   * @param userId
   * @return
   * @throws SQLException
   */
  public static Visited[] getVisitedByUserId(String userId)
      throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.VISITED + " where "
        + DbLiteral.V_USER_ID + "=? order by " + DbLiteral.V_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, userId);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Visited> visitedList = new ArrayList<Visited>();
    while (resultSet.next()) {
      Visited visited = new Visited();
      visited.setNum(resultSet.getInt(DbLiteral.V_NUM));
      visited.setUserId(userId);
      visited.setDate(resultSet.getTimestamp(DbLiteral.V_DATE));
      visited.setExhibitionNum(resultSet.getInt(DbLiteral.E_NUM));
      visitedList.add(visited);
    }

    // 타입 변형 : List<> -> Object[]
    return (Visited[]) visitedList.toArray(new Visited[visitedList.size()]);
  }

  /**
   * 
   * @param userId
   * @param exhibitionNum
   * @return
   * @throws SQLException
   */
  private static Visited getVisitedByIdAndExhibitionNum(String userId,
      int exhibitionNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.VISITED + " where "
        + DbLiteral.V_USER_ID + "=? and " + DbLiteral.E_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, userId);
    pstmt.setInt(2, exhibitionNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    Visited visited = null;
    if (resultSet.next()) {
      visited = new Visited();
      visited.setNum(resultSet.getInt(DbLiteral.V_NUM));
      visited.setUserId(userId);
      visited.setDate(resultSet.getTimestamp(DbLiteral.V_DATE));
      visited.setExhibitionNum(exhibitionNum);
    }

    return visited;
  }

  /**
   * 
   * @param userId
   * @param major
   * @throws SQLException
   */
  public static void saveVisited(String userId, int major) throws SQLException {

    // 중복 체크
    int exhibitionNum = ExhibitionController.getExhibitionByMajor(major)
        .getNum();

    // 중복이 아닌 경우 객체를 레코드로 삽입
    if (getVisitedByIdAndExhibitionNum(userId, exhibitionNum) == null) {
      Visited visited = new Visited();
      visited.setUserId(userId);
      visited
          .setDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
      visited.setExhibitionNum(exhibitionNum);
      insertVisited(visited);
    }
  }

  /**
   * 
   * @param visited
   * @return
   * @throws SQLException
   */
  private static int insertVisited(Visited visited) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int visitedNum = getMaxVisitedNum() + 1;
    String sql = "insert into " + DbLiteral.VISITED + " values (?, ?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, visitedNum);
    pstmt.setString(2, visited.getUserId());
    pstmt.setTimestamp(3, visited.getDate());
    pstmt.setInt(4, visited.getExhibitionNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return visitedNum;
  }

  private static int getMaxVisitedNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.V_NUM + ") as " + DbLiteral.V_NUM
        + " from " + DbLiteral.VISITED;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.V_NUM);
    }

    return maxNum;
  }
}
