package harmony.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.Node;

/**
 * 
 * @author Seongjun Park
 * @since 2016/6/10
 * @version 2016/6/10
 */
public class NodeController {

  /**
   * 
   * @return
   * @throws SQLException
   */
  public static Node[] getNodes() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.NODE + " order by "
        + DbLiteral.N_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Node> nodeList = new ArrayList<Node>();
    while (resultSet.next()) {
      Node node = new Node();
      node.setNum(resultSet.getInt(DbLiteral.N_NUM));
      node.setBlockNum((Integer) resultSet.getObject(DbLiteral.BL_NUM));
      nodeList.add(node);
    }

    // 타입 변형 : List<> -> Object[]
    return (Node[]) nodeList.toArray(new Node[nodeList.size()]);
  }

  public static Node getNodeByBlockNum(int blockNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.NODE + " where "
        + DbLiteral.BL_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, blockNum);
    ResultSet resultSet = pstmt.executeQuery();

    Node node = null;
    if (resultSet.next()) {
      node = new Node();
      node.setNum(resultSet.getInt(DbLiteral.N_NUM));
      node.setBlockNum((Integer) resultSet.getObject(DbLiteral.BL_NUM));
    }

    return node;
  }

  /**
   * 
   * @param nodes
   * @throws SQLException
   */
  static void saveNodes(Node[] nodes) throws SQLException {

    // 삽입 전 전부 삭제
    deleteNodes();

    // 삽입
    for (int i = 0; i < nodes.length; i++) {
      if (nodes[i] != null) {
        nodes[i].setNum(insertNode(nodes[i]));
      }
    }
  }

  private static int insertNode(Node node) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int num = getMaxBlockNum() + 1;
    String sql = "insert into " + DbLiteral.BLOCK + " values (?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    if (node.getBlockNum() == 0) {
      pstmt.setObject(2, null);
    } else {
      pstmt.setInt(2, node.getBlockNum());
    }
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return num;
  }

  /**
   * 
   * @throws SQLException
   */
  private static void deleteNodes() throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.NODE;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  /**
   * 
   * @param blockNum
   * @throws SQLException
   */
  static void deleteNodeByBlockNum(int blockNum) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.NODE + " where " + DbLiteral.BL_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, blockNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  /**
   * 
   * @return
   * @throws SQLException
   */
  private static int getMaxBlockNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.N_NUM + ") as " + DbLiteral.N_NUM
        + " from " + DbLiteral.NODE;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.N_NUM);
    }

    return maxNum;
  }
}
