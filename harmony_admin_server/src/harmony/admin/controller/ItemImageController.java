package harmony.admin.controller;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.ItemImage;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class ItemImageController {

  /**
   * 
   * @param num
   * @return
   * @throws SQLException
   */
  public static ItemImage getItemImageByNum(int num) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ITEM_IMAGE + " where "
        + DbLiteral.II_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    ItemImage itemImage = null;
    if (resultSet.next()) {
      itemImage = new ItemImage();
      itemImage.setNum(num);
      itemImage.setSeq(resultSet.getInt(DbLiteral.II_SEQ));
      itemImage.setImage(resultSet.getString(DbLiteral.II_IMAGE));
      itemImage.setMain(resultSet.getBoolean(DbLiteral.II_MAIN));
      itemImage.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      itemImage.setImageEdited(false);
    }

    return itemImage;
  }

  /**
   * 
   * @param itemNum
   * @return
   * @throws SQLException
   */
  public static ItemImage[] getItemImagseByItemNum(int itemNum)
      throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ITEM_IMAGE + " where "
        + DbLiteral.I_NUM + "=? order by " + DbLiteral.II_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<ItemImage> itemImageList = new ArrayList<ItemImage>();
    if (resultSet.next()) {
      ItemImage itemImage = new ItemImage();
      itemImage.setNum(resultSet.getInt(DbLiteral.II_NUM));
      itemImage.setSeq(resultSet.getInt(DbLiteral.II_SEQ));
      itemImage.setImage(resultSet.getString(DbLiteral.II_IMAGE));
      itemImage.setMain(resultSet.getBoolean(DbLiteral.II_MAIN));
      itemImage.setItemNum(itemNum);
      itemImage.setImageEdited(false);
      itemImageList.add(itemImage);
    }

    // 타입 변형 : List<> -> Object[]
    return (ItemImage[]) itemImageList
        .toArray(new ItemImage[itemImageList.size()]);
  }

  static void deleteItemImageByItemNum(int itemNum) throws SQLException {

    // 관련 이미지를 지움
    ItemImage[] itemImages = getItemImagseByItemNum(itemNum);
    for (ItemImage itemImage : itemImages) {
      removeItemImageFile(itemImage.getImage());
    }

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.ITEM_IMAGE + " where "
        + DbLiteral.I_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  /**
   * 
   * @param image
   */
  private static void removeItemImageFile(String image) {
    if (!"".equals(image)) {
      new File(image).delete();
    }
  }
}
