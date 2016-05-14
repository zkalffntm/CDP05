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
import harmony.common.ImageManager;

/**
 * 지도 관리를 위한 컨트롤러.
 * 
 * @author Seongjun Park
 * @since 2016/5/14
 * @version 2016/5/14
 */
public class MapController {
  private static final String MAP_IMAGE_DIR = "img" + File.separator + "map";

  /**
   * 
   * @return
   * @throws SQLException
   */
  public String[][] getAllMap() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    List<String[]> mapList = new ArrayList<String[]>();

    // 조회 쿼리 실행
    String sql = "select * from " + DbLiteral.MAP;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 지도 레코드 하나씩 리스트에 추가
    while (resultSet.next()) {
      String[] map = new String[3];
      map[0] = Integer.toString(resultSet.getInt(DbLiteral.M_NUM));
      map[1] = resultSet.getString(DbLiteral.M_NAME);
      map[2] = resultSet.getString(DbLiteral.M_IMAGE_FORMAT);
      mapList.add(map);
    }

    // 리스트를 배열화
    return (String[][]) mapList.toArray(new String[mapList.size()][3]);
  }

  /**
   * 
   * @param num
   * @return
   * @throws SQLException
   */
  public String getMapImagePath(int num) throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 조회 쿼리 실행
    String sql = "select " + DbLiteral.M_NAME + ", " + DbLiteral.M_IMAGE_FORMAT
        + " from " + DbLiteral.MAP + " where " + DbLiteral.M_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 지도 부여 번호 결정
    String imagePath = "";
    if (resultSet.next()) {
      imagePath = MAP_IMAGE_DIR + File.separator + num + "_"
          + resultSet.getString(DbLiteral.M_NAME) + "."
          + resultSet.getString(DbLiteral.M_IMAGE_FORMAT);
    }

    return imagePath;
  }

  /**
   * 
   * @param name
   * @param imageToUpload
   * @throws SQLException
   * @throws IOException
   */
  public void addMap(String name, String imageToUpload)
      throws SQLException, IOException {

    // 이미지 포맷 결정
    String imageName = new File(imageToUpload).getName();
    String imageFormat = imageName.substring(imageName.lastIndexOf('.') + 1);

    // 지도 레코드 삽입
    int num = this.insertMapRecord(name, imageFormat);

    // 이미지 파일 저장
    String newImagePath = MAP_IMAGE_DIR + File.separator + num + "_" + name
        + "." + imageFormat;
    this.uploadMapImage(imageToUpload, newImagePath);

  }

  /**
   * 
   * @param name
   * @param imageFormat
   * @throws SQLException
   */
  private int insertMapRecord(String name, String imageFormat)
      throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 조회 쿼리 실행
    String sql = "select max(" + DbLiteral.M_NUM + ") as " + DbLiteral.M_NUM
        + " from " + DbLiteral.MAP;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 지도 부여 번호 결정
    int num = 1;
    if (resultSet.next()) {
      num = resultSet.getInt(DbLiteral.M_NUM) + 1;
    }

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 삽입 쿼리 실행
    sql = "insert into " + DbLiteral.MAP + " values (?, ?, ?)";
    pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.setString(2, name);
    pstmt.setString(3, imageFormat);
    pstmt.executeUpdate();

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);

    return num;
  }

  /**
   * 
   * @param src
   * @param dest
   * @param imageToUpload
   * @throws IOException
   */
  private void uploadMapImage(String src, String dest) throws IOException {

    // 이미지를 저장할 경로 디렉토리 생성 (없을 때를 대비)
    new File(MAP_IMAGE_DIR).mkdirs();

    // 이미지 업로드
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage(src), dest);
  }

  /**
   * 
   * @param num
   * @param name
   * @throws SQLException
   */
  public void modifyMap(int num, String name) throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 조회 쿼리 실행
    String sql = "select " + DbLiteral.M_NAME + ", " + DbLiteral.M_IMAGE_FORMAT
        + " from " + DbLiteral.MAP + " where " + DbLiteral.M_NUM + " =?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 이전 지도명, 이미지 포맷 결정
    String oldName = "";
    String oldImageFormat = "";
    if (resultSet.next()) {
      oldName = resultSet.getString(DbLiteral.M_NAME);
      oldImageFormat = resultSet.getString(DbLiteral.M_IMAGE_FORMAT);
    }

    // 지도 레코드 갱신
    this.updateMapRecord(num, name, oldImageFormat);

    // 이전 지도 이미지 파일명 바꿈
    String oldImagePath = MAP_IMAGE_DIR + File.separator + num + "_" + oldName
        + "." + oldImageFormat;
    String newImagePath = MAP_IMAGE_DIR + File.separator + num + "_" + name
        + "." + oldImageFormat;
    new File(oldImagePath).renameTo(new File(newImagePath));
  }

  /**
   * 
   * @param num
   * @param name
   * @param imageToUpload
   * @throws SQLException
   * @throws IOException
   */
  public void modifyMap(int num, String name, String imageToUpload)
      throws SQLException, IOException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 조회 쿼리 실행
    String sql = "select " + DbLiteral.M_NAME + ", " + DbLiteral.M_IMAGE_FORMAT
        + " from " + DbLiteral.MAP + " where " + DbLiteral.M_NUM + " =?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 이전 지도명, 이미지 포맷
    String oldName = "";
    String oldImageFormat = "";
    if (resultSet.next()) {
      oldName = resultSet.getString(DbLiteral.M_NAME);
      oldImageFormat = resultSet.getString(DbLiteral.M_IMAGE_FORMAT);
    }

    // 새 이미지 포맷 결정
    String imageName = new File(imageToUpload).getName();
    String imageFormat = imageName.substring(imageName.lastIndexOf('.') + 1);

    // 지도 레코드 갱신
    this.updateMapRecord(num, name, imageFormat);

    // 이전 지도 이미지 삭제
    String oldImagePath = MAP_IMAGE_DIR + File.separator + num + "_" + oldName
        + "." + oldImageFormat;
    new File(oldImagePath).delete();

    // 새 이미지 저장
    String newImagePath = MAP_IMAGE_DIR + File.separator + num + "_" + name
        + "." + imageFormat;
    this.uploadMapImage(imageToUpload, newImagePath);
  }

  /**
   * 
   * @param num
   * @param name
   * @param imageFormat
   * @throws SQLException
   */
  private void updateMapRecord(int num, String name, String imageFormat)
      throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 갱신 쿼리 실행
    String sql = "update " + DbLiteral.MAP + " set " + DbLiteral.M_NAME + "=?, "
        + DbLiteral.M_IMAGE_FORMAT + "=? where " + DbLiteral.M_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, name);
    pstmt.setString(2, imageFormat);
    pstmt.setInt(3, num);
    pstmt.executeUpdate();

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);
  }

  /**
   * 
   * @param num
   * @throws SQLException
   */
  public void removeMap(int num) throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 조회 쿼리 실행
    String sql = "select " + DbLiteral.M_NAME + ", " + DbLiteral.M_IMAGE_FORMAT
        + " from " + DbLiteral.MAP + " where " + DbLiteral.M_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 지도 이미지 경로 결정
    String imagePath = "";
    if (resultSet.next()) {
      imagePath = num + "_" + resultSet.getString(DbLiteral.M_NAME) + "."
          + resultSet.getString(DbLiteral.M_IMAGE_FORMAT);
    }

    // 지도 레코드 삭제
    this.deleteMapRecord(num);

    // 지도 이미지 삭제
    new File(imagePath).delete();
  }

  /**
   * 
   * @param num
   * @throws SQLException
   */
  private void deleteMapRecord(int num) throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();

    // 오토커밋 비활성화
    boolean commit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.MAP + " where " + DbLiteral.M_NUM
        + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.executeUpdate();

    // 자동commit 여부 원상태
    dbConnection.commit();
    dbConnection.setAutoCommit(commit);
  }
}
