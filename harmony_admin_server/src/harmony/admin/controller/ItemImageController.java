package harmony.admin.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.database.DbConnector;
import harmony.admin.database.DbLiteral;
import harmony.admin.model.ItemImage;
import harmony.common.ImageManager;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/31
 */
public class ItemImageController {
  private static final String ITEM_IMAGE_DIR = "image" + File.separator
      + "item";

  private static ItemImage[] getItemImages() throws SQLException {
  
    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.ITEM_IMAGE + " order by "
        + DbLiteral.I_NUM + ", " + DbLiteral.II_SEQ + ", " + DbLiteral.II_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();
  
    // 레코드 하나씩 리스트에 추가
    List<ItemImage> itemImageList = new ArrayList<ItemImage>();
    while (resultSet.next()) {
      ItemImage area = new ItemImage();
      area.setNum(resultSet.getInt(DbLiteral.II_NUM));
      area.setSeq(resultSet.getInt(DbLiteral.II_SEQ));
      area.setImage(resultSet.getString(DbLiteral.II_IMAGE));
      area.setImageEdited(false);
      area.setMain(resultSet.getBoolean(DbLiteral.II_MAIN));
      area.setItemNum(resultSet.getInt(DbLiteral.I_NUM));
      itemImageList.add(area);
    }
  
    // 타입 변형 : List<> -> Object[]
    return (ItemImage[]) itemImageList
        .toArray(new ItemImage[itemImageList.size()]);
  }

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
        + DbLiteral.I_NUM + "=? order by " + DbLiteral.I_NUM + ", "
        + DbLiteral.II_SEQ + ", " + DbLiteral.II_NUM;
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

  static void saveItemImages(ItemImage[] itemImages)
      throws SQLException, IOException {

    // 삽입 또는 갱신
    for (int i = 0; i < itemImages.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (itemImages[i].getNum() == 0) {
        itemImages[i].setNum(insertItemImage(itemImages[i]));
      } else {
        updateItemImage(itemImages[i]);
      }
    }

    // 삭제
    ItemImage[] resultItemImages = getItemImages();
    for (ItemImage resultItemImage : resultItemImages) {
      boolean exists = false;
      for (ItemImage itemImage : itemImages) {
        if (resultItemImage.getNum() == itemImage.getNum()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteItemImageByNum(resultItemImage.getNum());
      }
    }
  }

  private static int insertItemImage(ItemImage itemImage)
      throws SQLException, IOException {
  
    // 이미지 파일 업로드
    itemImage.setImage(uploadItemImageFile(itemImage.getImage()));
  
    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);
  
    // 레코드 삽입 쿼리 실행
    int itemImageNum = getMaxItemImageNum() + 1;
    String sql = "insert into " + DbLiteral.ITEM_IMAGE
        + " values (?, ?, ?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemImageNum);
    pstmt.setInt(2, itemImage.getSeq());
    pstmt.setString(3, itemImage.getImage());
    pstmt.setBoolean(4, itemImage.isMain());
    pstmt.setInt(5, itemImage.getItemNum());
    pstmt.executeUpdate();
  
    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  
    return itemImageNum;
  }

  private static void updateItemImage(ItemImage itemImage)
      throws SQLException, IOException {
  
    // 변경했다면 이미지 파일 업로드
    if (itemImage.isImageEdited()) {
      removeItemImageFile(getItemImageByNum(itemImage.getNum()).getImage());
      itemImage.setImage(uploadItemImageFile(itemImage.getImage()));
    }
  
    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);
  
    // 레코드 갱신 쿼리 실행
    String sql = "update " + DbLiteral.ITEM_IMAGE + " set " + DbLiteral.II_SEQ
        + "=?, " + DbLiteral.II_IMAGE + "=?, " + DbLiteral.II_MAIN + "=?, "
        + DbLiteral.I_NUM + "=? where " + DbLiteral.II_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, itemImage.getSeq());
    pstmt.setString(2, itemImage.getImage());
    pstmt.setBoolean(3, itemImage.isMain());
    pstmt.setInt(4, itemImage.getItemNum());
    pstmt.setInt(5, itemImage.getNum());
    pstmt.executeUpdate();
  
    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);  
  }

  private static void deleteItemImageByNum(int num) throws SQLException {

    // 관련 이미지를 지움
    removeItemImageFile(getItemImageByNum(num).getImage());

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.ITEM_IMAGE + " where "
        + DbLiteral.II_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
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

  private static String uploadItemImageFile(String imageSource)
      throws IOException {
  
    // 스킵 조건
    if ("".equals(imageSource)) {
      return imageSource;
    }
  
    // 디렉토리 경로 확보
    new File(ITEM_IMAGE_DIR).mkdirs();
  
    // 이미지 파일명 중복 체크 후 최종 저장될 경로 지정
    String imageName = new File(imageSource).getName();
    int indexOfDot = imageName.lastIndexOf('.');
    String prefix = indexOfDot > -1 ? imageName.substring(0, indexOfDot)
        : imageName;
    String suffix = indexOfDot > -1 ? imageName.substring(indexOfDot) : "";
    int i = 1;
    while (new File(ITEM_IMAGE_DIR + File.separator + imageName).exists()) {
      imageName = prefix + "_" + (i++) + suffix;
    }
    String imageDest = ITEM_IMAGE_DIR + File.separator + imageName;
  
    // 이미지 파일 업로드
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage(imageSource), imageDest);
  
    // 업로드된 이미지 파일명 반환
    return imageDest;
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

  private static int getMaxItemImageNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.II_NUM + ") as " + DbLiteral.II_NUM
        + " from " + DbLiteral.ITEM_IMAGE;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.II_NUM);
    }

    return maxNum;
  }
}
