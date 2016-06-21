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
import harmony.admin.model.Node;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/31
 */
public class BlockController {

  public static Block[] getBlocks() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BLOCK + " order by "
        + DbLiteral.BL_NUM + ", " + DbLiteral.BL_SEQ;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Block> blockList = new ArrayList<Block>();
    while (resultSet.next()) {
      Block block = new Block();
      block.setNum(resultSet.getInt(DbLiteral.BL_NUM));
      block.setSeq(resultSet.getInt(DbLiteral.BL_SEQ));
      block.setItemNum((Integer) resultSet.getObject(DbLiteral.I_NUM));
      block.setAreaNum((Integer) resultSet.getObject(DbLiteral.A_NUM));
      blockList.add(block);
    }

    // 타입 변형 : List<> -> Object[]
    return (Block[]) blockList.toArray(new Block[blockList.size()]);

  }

  public static Block getBlockByNum(int num) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BLOCK + " where "
        + DbLiteral.BL_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    Block block = null;
    while (resultSet.next()) {
      block = new Block();
      block.setNum(num);
      block.setSeq(resultSet.getInt(DbLiteral.BL_SEQ));
      block.setItemNum((Integer) resultSet.getObject(DbLiteral.I_NUM));
      block.setAreaNum((Integer) resultSet.getObject(DbLiteral.A_NUM));
    }

    return block;
  }

  private static Block[] getBlocksByItemNum(int itemNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BLOCK + " where "
        + DbLiteral.I_NUM + "=? order by " + DbLiteral.A_NUM + ", "
        + DbLiteral.BL_SEQ;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Block> blockList = new ArrayList<Block>();
    while (resultSet.next()) {
      Block block = new Block();
      block.setNum(resultSet.getInt(DbLiteral.BL_NUM));
      block.setSeq(resultSet.getInt(DbLiteral.BL_SEQ));
      block.setItemNum(itemNum);
      block.setAreaNum((Integer) resultSet.getObject(DbLiteral.A_NUM));
      blockList.add(block);
    }

    // 타입 변형 : List<> -> Object[]
    return (Block[]) blockList.toArray(new Block[blockList.size()]);
  }

  public static Block[] getBlocksByAreaNum(int areaNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BLOCK + " where "
        + DbLiteral.A_NUM + "=? order by " + DbLiteral.A_NUM + ", "
        + DbLiteral.BL_SEQ;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, areaNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Block> blockList = new ArrayList<Block>();
    while (resultSet.next()) {
      Block block = new Block();
      block.setNum(resultSet.getInt(DbLiteral.BL_NUM));
      block.setSeq(resultSet.getInt(DbLiteral.BL_SEQ));
      block.setItemNum((Integer) resultSet.getObject(DbLiteral.I_NUM));
      block.setAreaNum(areaNum);
      blockList.add(block);
    }

    // 타입 변형 : List<> -> Object[]
    return (Block[]) blockList.toArray(new Block[blockList.size()]);
  }

  static Block getBlockBySeqAndAreaNum(int seq, int areaNum)
      throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.BLOCK + " where "
        + DbLiteral.BL_SEQ + "=? and " + DbLiteral.A_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, seq);
    pstmt.setInt(2, areaNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    Block block = null;
    if (resultSet.next()) {
      block = new Block();
      block.setNum(resultSet.getInt(DbLiteral.BL_NUM));
      block.setSeq(seq);
      block.setItemNum((Integer) resultSet.getObject(DbLiteral.I_NUM));
      block.setAreaNum(areaNum);
    }

    return block;
  }

  static void saveBlocks(Block[] blocks, Node[] nodes) throws SQLException {

    // 삽입 전 전부 삭제
    deleteBlocks();

    // 삽입
    for (int i = 0; i < blocks.length; i++) {
      blocks[i].setNum(insertBlock(blocks[i]));
      if (nodes[i] != null) {
        nodes[i].setBlockNum(blocks[i].getNum());
      }
    }

    // 하위 레코드 저장
    NodeController.saveNodes(nodes);
  }

  private static int insertBlock(Block block) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int num = getMaxBlockNum() + 1;
    String sql = "insert into " + DbLiteral.BLOCK + " values (?, ?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.setInt(2, block.getSeq());
    if (block.getItemNum() == 0) {
      pstmt.setObject(3, null);
    } else {
      pstmt.setInt(3, block.getItemNum());
    }
    if (block.getAreaNum() == 0) {
      pstmt.setObject(4, null);
    } else {
      pstmt.setInt(4, block.getAreaNum());
    }
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return num;
  }

  private static void deleteBlocks() throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    Block[] blocks = getBlocks();
    for (Block block : blocks) {
      ShareBlockController.deleteShareBlockByBlockNum(block.getNum());
      NodeController.deleteNodeByBlockNum(block.getNum());
    }

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.BLOCK;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  static void deleteBlockByItemNum(int itemNum) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    Block[] blocks = getBlocksByItemNum(itemNum);
    for (Block block : blocks) {
      ShareBlockController.deleteShareBlockByBlockNum(block.getNum());
      NodeController.deleteNodeByBlockNum(block.getNum());
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
      NodeController.deleteNodeByBlockNum(block.getNum());
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

  private static int getMaxBlockNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.BL_NUM + ") as " + DbLiteral.BL_NUM
        + " from " + DbLiteral.BLOCK;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.BL_NUM);
    }

    return maxNum;
  }
}
