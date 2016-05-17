package harmony.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.Block;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class BlockController {

  private static Block[] getBlocksByItemNum(int itemNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BLOCK + " where "
        + DbLiteral.I_NUM + "=? order by " + DbLiteral.BL_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Block> blockList = new ArrayList<Block>();
    while (resultSet.next()) {
      Block block = new Block();
      block.setNum(resultSet.getInt(DbLiteral.BL_NUM));
      block.setItemNum(itemNum);
      block.setAreaNum(resultSet.getInt(DbLiteral.A_NUM));
      blockList.add(block);
    }

    // 타입 변형 : List<> -> Object[]
    return (Block[]) blockList.toArray(new Block[blockList.size()]);
  }

  public static Block[] getBlocksByAreaNum(int areaNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BLOCK + " where "
        + DbLiteral.A_NUM + "=? order by " + DbLiteral.BL_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, areaNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Block> blockList = new ArrayList<Block>();
    while (resultSet.next()) {
      Block block = new Block();
      block.setNum(resultSet.getInt(DbLiteral.BL_NUM));
      block.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      block.setAreaNum(areaNum);
      blockList.add(block);
    }

    // 타입 변형 : List<> -> Object[]
    return (Block[]) blockList.toArray(new Block[blockList.size()]);
  }

  static void deleteBlockByItemNum(int itemNum) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    Block[] blocks = getBlocksByItemNum(itemNum);
    for (Block block : blocks) {
      ShareBlockController.deleteShareBlockByBlockNum(block.getNum());
    }

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.BLOCK + " where " + DbLiteral.I_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  /**
   * 
   * @param areaNum
   * @throws SQLException
   */
  static void deleteBlockByAreaNum(int areaNum) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    Block[] blocks = getBlocksByAreaNum(areaNum);
    for (Block block : blocks) {
      ShareBlockController.deleteShareBlockByBlockNum(block.getNum());
    }

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.BLOCK + " where " + DbLiteral.A_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, areaNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }
}
