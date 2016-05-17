package harmony.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.ShareBlock;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class ShareBlockController {

  public static ShareBlock[] getShareBlocksByBlock(int blockNum)
      throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.SHARE_BLOCK + " where "
        + DbLiteral.BL_NUM1 + "=? or " + DbLiteral.BL_NUM2 + "=? order by "
        + DbLiteral.SB_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, blockNum);
    pstmt.setInt(2, blockNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<ShareBlock> shareBlockList = new ArrayList<ShareBlock>();
    while (resultSet.next()) {
      ShareBlock shareBlock = new ShareBlock();
      shareBlock.setNum(resultSet.getInt(DbLiteral.SB_NUM));
      shareBlock.setBlockNum1(resultSet.getInt(DbLiteral.BL_NUM1));
      shareBlock.setBlockNum2(resultSet.getInt(DbLiteral.BL_NUM2));
      shareBlockList.add(shareBlock);
    }

    // 타입 변형 : List<> -> Object[]
    return (ShareBlock[]) shareBlockList
        .toArray(new ShareBlock[shareBlockList.size()]);
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
}
