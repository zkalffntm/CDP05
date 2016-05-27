package harmony.central.service;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.central.controller.ExhibitionController;
import harmony.central.model.Exhibition;
import harmony.common.AbstractService;
import harmony.common.ImageManager;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class ProviderImageService extends AbstractService {

  @Override
  public Object doService(Object argument)
      throws SQLException, IOException, JSONException {

    // 쿼리 실행
    Exhibition exhibition = ExhibitionController.getExhibitionByNum((int) argument);

    // 결과 레코드를 객체에 저장
    String imageStream = "";
    if (exhibition != null) {
      imageStream = ImageManager.readByteStringFromImage(exhibition.getImage());
    }

    return imageStream;
  }
}
