package harmony.admin.service;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.common.AbstractService;
import harmony.common.ImageManager;

/**
 * 고객으로부터 지도 번호 정보를 받고, 그에 대한 지도 이미지를<br>
 * 제공하는 서비스
 * 
 * @author Seongjun Park
 * @since 2016/5/8
 * @version 2016/5/8
 */
public class MapImageService extends AbstractService {

  /**
   * sql select문을 이용하여 전시물 이미지경로를 찾고 그 경로의 이미지를 가져온다.
   * 
   * @param argument
   *          지도 번호를 담은 int
   * @return String = "이미지 파일 스트림"
   */
  @Override
  protected Object doQuery(Object argument)
      throws SQLException, JSONException, IOException {

    // 지도 번호
    int mapNum = (int) argument;

    // 쿼리 실행
    String sql = "select m_image from map where m_num=?";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    pstmt.setInt(1, mapNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    String imageStream = null;
    if (resultSet.next()) {
      imageStream = ImageManager.readByteStringFromImage(resultSet.getString("m_image"));
    }

    return imageStream;
  }
}
