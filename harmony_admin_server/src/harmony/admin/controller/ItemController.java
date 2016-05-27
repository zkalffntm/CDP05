package harmony.admin.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.Item;
import harmony.admin.model.ItemImage;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/15
 * @version 2016/5/17
 */
public class ItemController {

  public static Item[] getItems() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ITEM + " order by "
        + DbLiteral.I_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Item> itemList = new ArrayList<Item>();
    while (resultSet.next()) {
      Item item = new Item();
      item.setNum(resultSet.getInt(DbLiteral.I_NUM));
      item.setTitle(resultSet.getString(DbLiteral.I_TITLE));
      item.setArtist(resultSet.getString(DbLiteral.I_ARTIST));
      item.setSimpleContent(resultSet.getString(DbLiteral.I_SIMPLE_CONTENT));
      item.setDetailContent(resultSet.getString(DbLiteral.I_DETAIL_CONTENT));
      item.setSize(resultSet.getString(DbLiteral.I_SIZE));
      item.setAreaNum(resultSet.getInt(DbLiteral.A_NUM));
      itemList.add(item);
    }

    // 타입 변형 : List<> -> Object[]
    return (Item[]) itemList.toArray(new Item[itemList.size()]);
  }

  /**
   * 
   * @param num
   * @return
   * @throws SQLException
   */
  public static Item getItemByNum(int num) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ITEM + " where " + DbLiteral.I_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    Item item = null;
    if (resultSet.next()) {
      item = new Item();
      item.setNum(num);
      item.setTitle(resultSet.getString(DbLiteral.I_TITLE));
      item.setArtist(resultSet.getString(DbLiteral.I_ARTIST));
      item.setSimpleContent(resultSet.getString(DbLiteral.I_SIMPLE_CONTENT));
      item.setDetailContent(resultSet.getString(DbLiteral.I_DETAIL_CONTENT));
      item.setSize(resultSet.getString(DbLiteral.I_SIZE));
      item.setAreaNum(resultSet.getInt(DbLiteral.A_NUM));
    }

    return item;
  }

  static void saveItems(Item[] items, ItemImage[][] itemImages)
      throws SQLException, IOException {

    // 삽입 또는 갱신
    for (int i = 0; i < items.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (items[i].getNum() == 0) {
        int itemNum = insertItem(items[i]);
        items[i].setNum(itemNum);
        for (int j = 0; j < itemImages[i].length; j++) {
          itemImages[i][j].setItemNum(itemNum);
        }
      } else {
        updateItem(items[i]);
      }
    }

    // 1차원화 후 하위 레코드 저장
    List<ItemImage> itemImageList = new ArrayList<ItemImage>();
    for (int i = 0; i < itemImages.length; i++) {
      for (int j = 0; j < itemImages[i].length; j++) {
        itemImageList.add(itemImages[i][j]);
      }
    }
    ItemImageController.saveItemImages((ItemImage[]) itemImageList
        .toArray(new ItemImage[itemImageList.size()]));

    // 삭제
    Item[] resultItems = getItems();
    for (Item resultItem : resultItems) {
      boolean exists = false;
      for (Item item : items) {
        if (resultItem.getNum() == item.getNum()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteItemByNum(resultItem.getNum());
      }
    }
  }

  /**
   * 
   * @param areaNum
   * @return
   * @throws SQLException
   */
  private static Item[] getItemsByAreaNum(int areaNum) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ITEM + " where " + DbLiteral.A_NUM
        + "=? order by " + DbLiteral.I_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, areaNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Item> itemList = new ArrayList<Item>();
    while (resultSet.next()) {
      Item item = new Item();
      item.setNum(resultSet.getInt(DbLiteral.I_NUM));
      item.setTitle(resultSet.getString(DbLiteral.I_TITLE));
      item.setArtist(resultSet.getString(DbLiteral.I_ARTIST));
      item.setSimpleContent(resultSet.getString(DbLiteral.I_SIMPLE_CONTENT));
      item.setDetailContent(resultSet.getString(DbLiteral.I_DETAIL_CONTENT));
      item.setSize(resultSet.getString(DbLiteral.I_SIZE));
      item.setAreaNum(areaNum);
      itemList.add(item);
    }

    // 타입 변형 : List<> -> Object[]
    return (Item[]) itemList.toArray(new Item[itemList.size()]);
  }

  private static int insertItem(Item item) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int itemNum = getMaxItemNum() + 1;
    String sql = "insert into " + DbLiteral.ITEM
        + " values (?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    pstmt.setString(2, item.getTitle());
    pstmt.setString(3, item.getArtist());
    pstmt.setString(4, item.getSimpleContent());
    pstmt.setString(5, item.getDetailContent());
    pstmt.setString(6, item.getSize());
    pstmt.setInt(7, item.getAreaNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return itemNum;
  }

  private static void updateItem(Item item) throws SQLException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 갱신 쿼리 실행
    String sql = "update " + DbLiteral.ITEM + " set " + DbLiteral.I_TITLE
        + "=?, " + DbLiteral.I_ARTIST + "=?, " + DbLiteral.I_SIMPLE_CONTENT
        + "=?, " + DbLiteral.I_DETAIL_CONTENT + "=?, " + DbLiteral.I_SIZE
        + "=?, " + DbLiteral.A_NUM + "=? where " + DbLiteral.I_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, item.getTitle());
    pstmt.setString(2, item.getArtist());
    pstmt.setString(3, item.getSimpleContent());
    pstmt.setString(4, item.getDetailContent());
    pstmt.setString(5, item.getSize());
    pstmt.setInt(6, item.getAreaNum());
    pstmt.setInt(7, item.getNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static void deleteItemByNum(int num) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    ItemImageController.deleteItemImageByItemNum(num);
    BeaconController.deleteBeaconByItemNum(num);
    RecommendItemController.deleteRecommendItemByItemNum(num);
    BlockController.deleteBlockByItemNum(num);

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.ITEM + " where " + DbLiteral.I_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  static void deleteItemByAreaNum(int areaNum) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    Item[] items = getItemsByAreaNum(areaNum);
    for (Item item : items) {
      ItemImageController.deleteItemImageByItemNum(item.getNum());
      BeaconController.deleteBeaconByItemNum(item.getNum());
      RecommendItemController.deleteRecommendItemByItemNum(item.getNum());
      BlockController.deleteBlockByItemNum(item.getNum());
    }

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 구역 레코드 갱신 쿼리 실행
    String sql = "delete from " + DbLiteral.BLOCK + " where " + DbLiteral.A_NUM
        + "=?";

    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, areaNum);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static int getMaxItemNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.I_NUM + ") as " + DbLiteral.I_NUM
        + " from " + DbLiteral.ITEM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.I_NUM);
    }

    return maxNum;
  }
}
