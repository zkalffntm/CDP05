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
import harmony.admin.model.Recommend;
import harmony.admin.model.RecommendItem;
import harmony.common.ImageManager;

/**
 * 추천경로 관리를 위한 컨트롤러
 * 
 * @author Seongjun Park
 * @since 2016/5/14
 * @version 2016/5/31
 *
 */
public class RecommendController {

  /** 추천코스 이미지 파일 저장 디렉토리 */
  private static final String RECOMMEND_IMAGE_DIR = "image" + File.separator
      + "recommend";

  /**
   * 추천경로 레코드들을 가져옴. 추천경로 목록을 볼때 호출을 함.
   * 
   * @return Recommend 객체 배열
   * @throws SQLException
   *           SQL 관련 예외
   */
  public static Recommend[] getRecommends() throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.RECOMMEND + " order by "
        + DbLiteral.R_NUM;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 레코드 하나씩 리스트에 추가
    List<Recommend> recommendList = new ArrayList<Recommend>();
    while (resultSet.next()) {
      Recommend recommend = new Recommend();
      recommend.setNum(resultSet.getInt(DbLiteral.R_NUM));
      recommend.setContent(resultSet.getString(DbLiteral.R_CONTENT));
      recommend.setImage(resultSet.getString(DbLiteral.R_IMAGE));
      recommend.setImageEdited(false);
      recommendList.add(recommend);
    }

    // 타입 변형 : List<> -> Object[]
    return (Recommend[]) recommendList
        .toArray(new Recommend[recommendList.size()]);
  }

  /**
   * 추천경로와 그 추천 전시물들을 레코드로 저장함.
   * 
   * @param recommends
   *          추천경로 객체 배열
   * @param recommendItems
   *          추천경로별 전시물 객체 2차 배열
   * @throws IOException
   *           IO 관련 예외
   * @throws SQLException
   *           SQL 관련 예외
   */
  public static void saveRecommends(Recommend[] recommends,
      RecommendItem[][] recommendItems) throws IOException, SQLException {

    // 삽입 또는 갱신
    for (int i = 0; i < recommends.length; i++) {

      // 번호가 0인 경우 DB에 새 레코드 삽입, 그렇지 않은 경우 기존 레코드 갱신
      if (recommends[i].getNum() == 0) {
        recommends[i].setNum(insertRecommend(recommends[i]));
      } else {
        updateRecommend(recommends[i]);
      }

      // 하위 레코드에 FK 번호를 부여
      for (int j = 0; j < recommendItems[i].length; j++) {
        recommendItems[i][j].setRecommendNum(recommends[i].getNum());
      }
    }

    // 1차원화 후 하위 레코드 추가
    List<RecommendItem> recommendItemList = new ArrayList<RecommendItem>();
    for (int i = 0; i < recommendItems.length; i++) {
      for (int j = 0; j < recommendItems[i].length; j++) {
        recommendItems[i][j].setSeq(j + 1);
        recommendItemList.add(recommendItems[i][j]);
      }
    }
    RecommendItemController
        .saveRecommendItems((RecommendItem[]) recommendItemList
            .toArray(new RecommendItem[recommendItemList.size()]));

    // 삭제
    Recommend[] resultRecommends = getRecommends();
    for (Recommend resultRecommend : resultRecommends) {
      boolean exists = false;
      for (Recommend recommend : recommends) {
        if (resultRecommend.getNum() == recommend.getNum()) {
          exists = true;
          break;
        }
      }

      // 병합된 레코드들(result) 중 입력 레코드에 없는 것이면 삭제
      if (!exists) {
        deleteRecommendByNum(resultRecommend.getNum());
      }
    }
  }

  public static Recommend getRecommendByNum(int num) throws SQLException {

    // 레코드 조회 쿼리 실행
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select * from " + DbLiteral.RECOMMEND + " where "
        + DbLiteral.R_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    ResultSet resultSet = pstmt.executeQuery();

    // 반환
    Recommend recommend = null;
    if (resultSet.next()) {
      recommend = new Recommend();
      recommend.setNum(num);
      recommend.setContent(resultSet.getString(DbLiteral.R_CONTENT));
      recommend.setImage(resultSet.getString(DbLiteral.R_IMAGE));
      recommend.setImageEdited(false);
    }

    return recommend;
  }

  private static int insertRecommend(Recommend recommend)
      throws IOException, SQLException {

    // 이미지 파일 업로드
    recommend.setImage(uploadRecommendImageFile(recommend.getImage()));

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삽입 쿼리 실행
    int num = getMaxRecommendNum() + 1;
    String sql = "insert into " + DbLiteral.RECOMMEND + " values (?, ?, ?)";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.setString(2, recommend.getContent());
    pstmt.setString(3, recommend.getImage());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    return num;
  }

  private static void updateRecommend(Recommend recommend)
      throws SQLException, IOException {

    // 변경했다면 이미지 파일 업로드
    if (recommend.isImageEdited()) {
      removeRecommendImageFile(
          getRecommendByNum(recommend.getNum()).getImage());
      recommend.setImage(uploadRecommendImageFile(recommend.getImage()));
    }

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 갱신 쿼리 실행
    String sql = "update " + DbLiteral.RECOMMEND + " set " + DbLiteral.R_CONTENT
        + "=?, " + DbLiteral.R_IMAGE + "=? where " + DbLiteral.R_NUM + "=?";

    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setString(1, recommend.getContent());
    pstmt.setString(2, recommend.getImage());
    pstmt.setInt(3, recommend.getNum());
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static void deleteRecommendByNum(int num) throws SQLException {

    // 하위 테이블의 관련 레코드들을 지움
    RecommendItemController.deleteRecommendItemByRecommendNum(num);

    // 관련 이미지를 지움
    removeRecommendImageFile(getRecommendByNum(num).getImage());

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 삭제 쿼리 실행
    String sql = "delete from " + DbLiteral.RECOMMEND + " where "
        + DbLiteral.R_NUM + "=?";
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    pstmt.setInt(1, num);
    pstmt.executeUpdate();

    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);
  }

  private static String uploadRecommendImageFile(String imageSource)
      throws IOException {

    // 스킵 조건
    if ("".equals(imageSource)) {
      return imageSource;
    }

    // 디렉토리 경로 확보
    new File(RECOMMEND_IMAGE_DIR).mkdirs();

    // 이미지 파일명 중복 체크 후 최종 저장될 경로 지정
    String imageName = new File(imageSource).getName();
    int indexOfDot = imageName.lastIndexOf('.');
    String prefix = indexOfDot > -1 ? imageName.substring(0, indexOfDot)
        : imageName;
    String suffix = indexOfDot > -1 ? imageName.substring(indexOfDot) : "";
    int i = 1;
    while (new File(RECOMMEND_IMAGE_DIR + File.separator + imageName)
        .exists()) {
      imageName = prefix + "_" + (i++) + suffix;
    }
    String imageDest = RECOMMEND_IMAGE_DIR + File.separator + imageName;

    // 이미지 파일 업로드
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage(imageSource), imageDest);

    // 업로드된 이미지 파일명 반환
    return imageDest;
  }

  private static void removeRecommendImageFile(String image) {
    if (!"".equals(image)) {
      new File(image).delete();
    }
  }

  private static int getMaxRecommendNum() throws SQLException {
    Connection dbConnection = DbConnector.getInstance().getConnection();
    String sql = "select max(" + DbLiteral.R_NUM + ") as " + DbLiteral.R_NUM
        + " from " + DbLiteral.RECOMMEND;
    PreparedStatement pstmt = dbConnection.prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    int maxNum = 0;
    if (resultSet.next()) {
      maxNum = resultSet.getInt(DbLiteral.R_NUM);
    }

    return maxNum;
  }
}
