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
 * @version 2016/5/31
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

  /**
   * 구역, 전시물, 전시물이미지를 저장하는 기능. GUI의 전시물데이터 관리 화면에서 "저장"버튼에 해당
   * 
   * @param areas
   *          구역 객체 1차원 배열 [구역수]
   * @param items
   *          전시물 객체 2차원 배열 [구역수][전시물수]
   * @param itemImages
   *          전시물이미지 객체3차원 배열 [구역수][전시물수][전시물이미지수]
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  public static void saveAreas(Area[] areas, Item[][] items,
      ItemImage[][][] itemImages) throws SQLException, IOException {

    // 삽입 또는 갱신
    for (int i = 0; i < areas.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (areas[i].getNum() == 0) {
        areas[i].setNum(insertArea(areas[i]));
      } else {
        updateArea(areas[i]);
      }

      for (int j = 0; j < items[i].length; j++) {
        items[i][j].setAreaNum(areas[i].getNum());
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
      for (Area area : areas) {
        if (resultArea.getNum() == area.getNum()) {
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
   * 구역, 전시물, 블록, 공유블록을 저장하는 기능. GUI의 지도 관리 화면에서 "저장"버튼에 해당.
   * 
   * @param areas
   *          구역 객체 1차원 배열 [구역수]
   * @param items
   *          블록에 설정된 전시물 객체 3차원 배열 [구역수][블록수]. item 설정이 안된 index 부분은 item=null
   * @param blockPairs
   *          공유블럭용 객체 3차원 배열 [구역수][공유블록쌍수][공유할블록2개]
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  public static void saveAreas(Area[] areas, Item[][] items,
      Block[][][] blockPairs) throws SQLException, IOException {

    Block[][] blocks = new Block[areas.length][];

    // 삽입 또는 갱신
    for (int i = 0; i < areas.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (areas[i].getNum() == 0) {
        areas[i].setNum(insertArea(areas[i]));
      } else {
        updateArea(areas[i]);
      }

      // 하위 레코드에 FK 번호를 부여
      for (int j = 0; j < items[i].length; j++) {
        if (items[i][j] != null) {
          items[i][j].setAreaNum(areas[i].getNum());
        }
      }
      blocks[i] = new Block[items[i].length];
      for (int j = 0; j < blocks[i].length; j++) {
        blocks[i][j] = new Block();
        blocks[i][j].setAreaNum(areas[i].getNum());
      }
      for (int j = 0; j < blockPairs[i].length; j++) {
        for (int k = 0; k < blockPairs[i][j].length; k++) {
          blockPairs[i][j][k].setAreaNum(areas[i].getNum());
        }
      }
    }

    // 1차원화 후 하위 레코드 저장
    List<Item> itemList = new ArrayList<Item>();
    for (int i = 0; i < items.length; i++) {
      for (int j = 0; j < items[i].length; j++) {
        itemList.add(items[i][j]);
      }
    }
    ItemController.saveItemsOnAreaPanel(
        (Item[]) itemList.toArray(new Item[itemList.size()]));

    List<Block> blockList = new ArrayList<Block>();
    for (int i = 0; i < items.length; i++) {
      for (int j = 0; j < items[i].length; j++) {
        blocks[i][j].setSeq(j + 1);
        if (items[i][j] != null) {
          blocks[i][j].setItemNum(items[i][j].getNum());
        }
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
