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
import harmony.admin.model.Area;
import harmony.admin.model.Block;
import harmony.admin.model.Item;
import harmony.admin.model.ItemImage;
import harmony.common.ImageManager;

/**
 * 지도 관리를 위한 컨트롤러.
 * 
 * @author Seongjun Park
 * @since 2016/5/14
 * @version 2016/5/26
 */
public class AreaController {
  private static final String AREA_IMAGE_DIR = "image" + File.separator
      + "area";

  /**
   * 
   * @return
   * @throws SQLException
   */
  public static Area[] getAreas() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.AREA + " order by "
        + DbLiteral.A_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Area> areaList = new ArrayList<Area>();
    while (resultSet.next()) {
      Area area = new Area();
      area.setNum(resultSet.getInt(DbLiteral.A_NUM));
      area.setName(resultSet.getString(DbLiteral.A_NAME));
      area.setImage(resultSet.getString(DbLiteral.A_IMAGE));
      area.setImageEdited(false);
      areaList.add(area);
    }

    // 타입 변형 : List<> -> Object[]
    return (Area[]) areaList.toArray(new Area[areaList.size()]);
  }

  public static void saveAreas(Area[] areas, Item[][] items,
      ItemImage[][][] itemImages) throws SQLException, IOException {

    // 삽입 또는 갱신
    for (int i = 0; i < areas.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (areas[i].getNum() == 0) {
        int areaNum = insertArea(areas[i]);
        areas[i].setNum(areaNum);
        for (int j = 0; j < items[i].length; j++) {
          items[i][j].setAreaNum(areaNum);
        }
      } else {
        updateArea(areas[i]);
      }
    }

    // 1차원화 후 하위 레코드 추가
    List<Item> itemList = new ArrayList<Item>();
    for (int i = 0; i < items.length; i++) {
      for (int j = 0; j < items[i].length; j++) {
        itemList.add(items[i][j]);
      }
    }
    List<ItemImage[]> itemImagesList = new ArrayList<ItemImage[]>();
    for (int i = 0; i < itemImages.length; i++) {
      for (int j = 0; j < itemImages[i].length; j++) {
        itemImagesList.add(itemImages[i][j]);
      }
    }
    ItemController.saveItems(
        (Item[]) itemList.toArray(new Item[itemList.size()]),
        (ItemImage[][]) itemImagesList
            .toArray(new ItemImage[itemImagesList.size()][]));

    // 삭제
    Area[] resultAreas = getAreas();
    for (Area resultArea : resultAreas) {
      boolean exists = false;
      for (Area recommend : areas) {
        if (resultArea.getNum() == recommend.getNum()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteAreaByNum(resultArea.getNum());
      }
    }
  }

  public static void saveAreas(Area[] areas, Block[][] blocks,
      Block[][][] blockPairs) throws SQLException, IOException {

    // 삽입 또는 갱신
    for (int i = 0; i < areas.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (areas[i].getNum() == 0) {
        int areaNum = insertArea(areas[i]);
        areas[i].setNum(areaNum);
        for (int j = 0; j < blocks[i].length; j++) {
          blocks[i][j].setAreaNum(areaNum);
        }
        for (int j = 0; j < blockPairs[i].length; j++) {
          blockPairs[i][j][0].setAreaNum(areaNum);
          blockPairs[i][j][1].setAreaNum(areaNum);
        }
      } else {
        updateArea(areas[i]);
      }
    }

    // 1차원화 후 하위 레코드 저장
    List<Block> blockList = new ArrayList<Block>();
    for (int i = 0; i < blocks.length; i++) {
      for (int j = 0; j < blocks[i].length; j++) {
        blockList.add(blocks[i][j]);
      }
    }
    BlockController
        .saveBlocks((Block[]) blockList.toArray(new Block[blockList.size()]));
    List<Block[]> blockPairList = new ArrayList<Block[]>();
    for (int i = 0; i < blockPairs.length; i++) {
      for (int j = 0; j < blockPairs[i].length; j++) {
        blockPairList.add(blockPairs[i][j]);
      }
    }
    ShareBlockController.saveShareBlocks(
        (Block[][]) blockPairList.toArray(new Block[blockPairList.size()][]));

    // 삭제
    Area[] resultAreas = getAreas();
    for (Area resultArea : resultAreas) {
      boolean exists = false;
      for (Area recommend : areas) {
        if (resultArea.getNum() == recommend.getNum()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteAreaByNum(resultArea.getNum());
      }
    }
  }

  /**
   * 
   * @param num
   * @return
   * @throws SQLException
   */
  public static Area getAreaByNum(int num) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.AREA + " where " + DbLiteral.A_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    Area area = null;
    if (resultSet.next()) {
      area = new Area();
      area.setNum(resultSet.getInt(DbLiteral.A_NUM));
      area.setName(resultSet.getString(DbLiteral.A_NAME));
      area.setImage(resultSet.getString(DbLiteral.A_IMAGE));
      area.setImageEdited(false);
    }

    return area;
  }

  /**
   * 
   * @param area
   * @throws SQLException
   * @throws IOException
   */
  private static int insertArea(Area area) throws SQLException, IOException {

    // 이미지 파일 업로드
    area.setImage(uploadAreaImageFile(area.getImage()));

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int areaNum = getMaxAreaNum() + 1;
    String sql = "insert into " + DbLiteral.AREA + " values (?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, areaNum);
    pstmt.setString(2, area.getName());
    pstmt.setString(3, area.getImage());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return areaNum;
  }

  private static void updateArea(Area area) throws SQLException, IOException {

    // 변경했다면 이미지 파일 업로드
    if (area.isImageEdited()) {
      removeAreaImageFile(getAreaByNum(area.getNum()).getImage());
      area.setImage(uploadAreaImageFile(area.getImage()));
    }

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 갱신 쿼리 실행
    String sql = "update " + DbLiteral.AREA + " set " + DbLiteral.A_NAME
        + "=?, " + DbLiteral.A_IMAGE + "=? where " + DbLiteral.A_NUM + "=?";

    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, area.getName());
    pstmt.setString(2, area.getImage());
    pstmt.setInt(3, area.getNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static void deleteAreaByNum(int num) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    BlockController.deleteBlockByAreaNum(num);
    ItemController.deleteItemByAreaNum(num);

    // 관련 이미지를 지움
    removeAreaImageFile(getAreaByNum(num).getImage());

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.AREA + " where " + DbLiteral.A_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  /**
   * 
   * @param imageSource
   * @return
   * @throws IOException
   */
  private static String uploadAreaImageFile(String imageSource)
      throws IOException {

    // 스킵 조건
    if ("".equals(imageSource)) {
      return imageSource;
    }

    // 디렉토리 경로 확보
    new File(AREA_IMAGE_DIR).mkdirs();

    // 이미지 파일명 중복 체크 후 최종 저장될 경로 지정
    String imageName = new File(imageSource).getName();
    int indexOfDot = imageName.lastIndexOf('.');
    String prefix = indexOfDot > -1 ? imageName.substring(0, indexOfDot)
        : imageName;
    String suffix = indexOfDot > -1 ? imageName.substring(indexOfDot) : "";
    int i = 1;
    while (new File(AREA_IMAGE_DIR + File.separator + imageName).exists()) {
      imageName = prefix + "_" + (i++) + suffix;
    }
    String imageDest = AREA_IMAGE_DIR + File.separator + imageName;

    // 이미지 파일 업로드
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage(imageSource), imageDest);

    // 업로드된 이미지 파일명 반환
    return imageDest;
  }

  private static void removeAreaImageFile(String image) {
    if (!"".equals(image)) {
      new File(image).delete();
    }
  }

  /**
   * 
   * @return
   * @throws SQLException
   */
  private static int getMaxAreaNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.A_NUM + ") as " + DbLiteral.A_NUM
        + " from " + DbLiteral.AREA;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.A_NUM);
    }

    return maxNum;
  }
}