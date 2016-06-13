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
import harmony.admin.model.ShareBlock;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/31
 */
public class ShareBlockController {

  public static ShareBlock[] getShareBlocks() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.SHARE_BLOCK + " order by "
        + DbLiteral.SB_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<ShareBlock> shareBlockList = new ArrayList<ShareBlock>();
    while (resultSet.next()) {
      ShareBlock shareBlock = new ShareBlock();
      shareBlock.setNum(resultSet.getInt(DbLiteral.SB_NUM));
      shareBlock.setBlockNum1((Integer) resultSet.getObject(DbLiteral.BL_NUM1));
      shareBlock.setBlockNum2((Integer) resultSet.getObject(DbLiteral.BL_NUM2));
      shareBlockList.add(shareBlock);
    }

    // 타입 변형 : List<> -> Object[]
    return (ShareBlock[]) shareBlockList
        .toArray(new ShareBlock[shareBlockList.size()]);

  }

  public static ShareBlock[] getShareBlocksByBlockNum1(int blockNum1)
      throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.SHARE_BLOCK + " where "
        + DbLiteral.BL_NUM1 + "=? order by " + DbLiteral.SB_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, blockNum1);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<ShareBlock> shareBlockList = new ArrayList<ShareBlock>();
    while (resultSet.next()) {
      ShareBlock shareBlock = new ShareBlock();
      shareBlock.setNum(resultSet.getInt(DbLiteral.SB_NUM));
      shareBlock.setBlockNum1((Integer) resultSet.getObject(blockNum1));
      shareBlock.setBlockNum2((Integer) resultSet.getObject(DbLiteral.BL_NUM2));
      shareBlockList.add(shareBlock);
    }

    // 타입 변형 : List<> -> Object[]
    return (ShareBlock[]) shareBlockList
        .toArray(new ShareBlock[shareBlockList.size()]);
  }

  static void saveShareBlocks(Block[][] blockPairs) throws SQLException {

    // 모든 레코드 삭제
    deleteShareBlocks();

    // 삽입
    for (int i = 0; i < blockPairs.length; i++) {
      int blockNum1 = BlockController.getBlockBySeqAndAreaNum(
          blockPairs[i][0].getSeq(), blockPairs[i][0].getAreaNum()).getNum();
      int blockNum2 = BlockController.getBlockBySeqAndAreaNum(
          blockPairs[i][1].getSeq(), blockPairs[i][1].getAreaNum()).getNum();

      ShareBlock shareBlock = new ShareBlock();
      shareBlock.setBlockNum1(blockNum1);
      shareBlock.setBlockNum2(blockNum2);
      shareBlock.setNum(insertShareBlock(shareBlock));
    }
  }

  private static int insertShareBlock(ShareBlock shareBlock)
      throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int num = getMaxShareBlockNum() + 1;
    String sql = "insert into " + DbLiteral.SHARE_BLOCK + " values (?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    if (shareBlock.getBlockNum1() == 0) {
      pstmt.setObject(2, null);
    } else {
      pstmt.setInt(2, shareBlock.getBlockNum1());
    }
    if (shareBlock.getBlockNum2() == 0) {
      pstmt.setObject(3, null);
    } else {
      pstmt.setInt(3, shareBlock.getBlockNum2());
    }
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return num;
  }

  private static void deleteShareBlocks() throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.SHARE_BLOCK;
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
  static void deleteShareBlockByBlockNum(int blockNum) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.SHARE_BLOCK + " where "
        + DbLiteral.BL_NUM1 + "=? or " + DbLiteral.BL_NUM2 + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, blockNum);
    pstmt.setInt(2, blockNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static int getMaxShareBlockNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.SB_NUM + ") as " + DbLiteral.SB_NUM
        + " from " + DbLiteral.SHARE_BLOCK;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.SB_NUM);
    }

    return maxNum;
  }
}
