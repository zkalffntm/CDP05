package harmony.admin.service;

import java.io.IOException;
import java.sql.SQLException;

import harmony.admin.controller.ItemController;
import harmony.admin.model.Item;
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
   * 전시물명, 작가, 설명을 제공
   * 
   * @param argument
   *          전시물 번호를 담은 int
   * @return String[] = {"작품명", "작가", 설명 내용"}
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  @Override
  public Object doService(Object argument) throws SQLException, IOException {

    // 쿼리 실행
    Item item = ItemController.getItemByNum((int) argument);

    // 결과 레코드를 객체에 저장
    String[] strings = null;
    if (item != null) {
      strings = new String[3];
      strings[0] = item.getTitle();
      strings[1] = item.getArtist();
      strings[2] = item.getDetailContent();
    }

    return strings;
  }
}
