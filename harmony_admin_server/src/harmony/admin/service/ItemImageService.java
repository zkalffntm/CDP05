package harmony.admin.service;

import java.io.IOException;
import java.sql.SQLException;

import harmony.admin.controller.ItemImageController;
import harmony.admin.model.ItemImage;
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
   *          전시물 이미지 번호를 담은 int
   * @return String = "이미지 파일 스트림"
   * @throws IOException
   */
  @Override
  public Object doService(Object argument) throws SQLException, IOException {

    // 쿼리 실행
    ItemImage itemImage = ItemImageController.getItemImageByNum((int) argument);

    // 결과 레코드를 객체에 저장
    String imageStream = "";
    if (itemImage != null) {
      imageStream = ImageManager.readByteStringFromImage(itemImage.getImage());
    }

    return imageStream;
  }
}
