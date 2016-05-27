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
 * @version 2016/5/18
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
      block.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      block.setAreaNum(resultSet.getInt(DbLiteral.A_NUM));
      blockList.add(block);
    }

    // 타입 변형 : List<> -> Object[]
    return (Block[]) blockList.toArray(new Block[blockList.size()]);

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
      block.setAreaNum(resultSet.getInt(DbLiteral.A_NUM));
      blockList.add(block);
    }

    // 타입 변형 : List<> -> Object[]
    return (Block[]) blockList.toArray(new Block[blockList.size()]);
  }

  private static Block[] getBlocksByAreaNum(int areaNum) throws SQLException {

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
      block.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
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
        + DbLiteral.BL_SEQ + "=? and " + DbLiteral.A_NUM + "=? order by "
        + DbLiteral.A_NUM + ", " + DbLiteral.BL_SEQ;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, areaNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    Block block = null;
    if (resultSet.next()) {
      block = new Block();
      block.setNum(resultSet.getInt(DbLiteral.BL_NUM));
      block.setSeq(seq);
      block.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      block.setAreaNum(areaNum);
    }

    return block;
  }

  static void saveBlocks(Block[] blocks) throws SQLException {

    // 삽입 또는 갱신
    for (int i = 0; i < blocks.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (blocks[i].getNum() == 0) {
        int blockNum = insertBlock(blocks[i]);
        blocks[i].setNum(blockNum);
      } else {
        updateBlock(blocks[i]);
      }
    }

    // 삭제
    Block[] resultBlocks = getBlocks();
    for (Block resultBlock : resultBlocks) {
      boolean exists = false;
      for (Block block : blocks) {
        if (resultBlock.getNum() == block.getNum()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteBlockByNum(resultBlock.getNum());
      }
    }
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
    pstmt.setInt(3, block.getItemNum());
    pstmt.setInt(4, block.getAreaNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return num;
  }

  private static void updateBlock(Block block) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 갱신 쿼리 실행
    String sql = "update " + DbLiteral.BLOCK + " set " + DbLiteral.BL_SEQ
        + "=?, " + DbLiteral.I_NUM + "=?, " + DbLiteral.A_NUM + "=? where "
        + DbLiteral.BL_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, block.getSeq());
    pstmt.setInt(2, block.getItemNum());
    pstmt.setInt(3, block.getAreaNum());
    pstmt.setInt(4, block.getNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  /**
   * 
   * @param num
   * @throws SQLException
   */
  private static void deleteBlockByNum(int num) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    ShareBlockController.deleteShareBlockByBlockNum(num);

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.BLOCK + " where " + DbLiteral.BL_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
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
