package harmony.admin.service;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.admin.database.DbLiteral;
import harmony.common.AbstractService;

/**
 * 고객으로부터 전시물 번호 정보를 받고, 그에 대한 전시물 설명을<br>
 * 제공하는 서비스
 * 
 * @author Seongjun Park
 * @since 2016/4/1
 * @version 2016/4/1
 */
public class ItemContentService extends AbstractService {

  /**
   * sql select문을 이용하여 전시물 명, 작가, 설명을 찾음
   * 
   * @param argument
   *          전시물 번호를 담은 int
   * @return String[] = {"작품명", "작가", 설명 내용"}
   */
  @Override
  protected Object doQuery(Object argument)
      throws SQLException, JSONException, IOException {

    // 전시물 번호
    int itemNum = (int) argument;

    // 쿼리 실행
    String sql = "select " + DbLiteral.I_TITLE + ", " + DbLiteral.I_ARTIST
        + ", " + DbLiteral.I_CONTENT + " from " + DbLiteral.ITEM + " where "
        + DbLiteral.I_NUM + "=?";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    pstmt.setInt(1, itemNum);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    String[] strings = null;
    if (resultSet.next()) {
      strings = new String[3];
      strings[0] = resultSet.getString(DbLiteral.I_TITLE);
      strings[1] = resultSet.getString(DbLiteral.I_ARTIST);
      strings[2] = resultSet.getString(DbLiteral.I_CONTENT);
    }

    return strings;
  }
}
