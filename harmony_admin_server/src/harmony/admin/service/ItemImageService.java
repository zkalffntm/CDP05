package harmony.admin.service;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.admin.database.DbLiteral;
import harmony.common.AbstractService;
import harmony.common.ImageManager;

/**
 * 고객으로부터 전시물 번호 정보를 받고, 그에 대한 전시물 이미지를<br>
 * 제공하는 서비스
 * 
 * @author Seongjun Park
 * @since 2016/4/10
 * @version 2016/4/10
 */
public class ItemImageService extends AbstractService {

  /**
   * sql select문을 이용하여 전시물 이미지경로를 찾고 그 경로의 이미지를 가져온다.
   * 
   * @param argument
   *          전시물 번호를 담은 int
   * @return String = "이미지 파일 스트림"
   */
  @Override
  protected Object doQuery(Object argument)
      throws SQLException, JSONException, IOException {

    // 전시물 번호
    int itemNum = (int) argument;

    // 쿼리 실행
    String sql = "select " + DbLiteral.I_IMAGE + " from " + DbLiteral.ITEM
        + " where " + DbLiteral.I_NUM + "=?";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    String imageStream = null;
    if (resultSet.next()) {
      imageStream = ImageManager
          .readByteStringFromImage(resultSet.getString(DbLiteral.I_IMAGE));
    }

    return imageStream;
  }
}
