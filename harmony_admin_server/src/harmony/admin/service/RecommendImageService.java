package harmony.admin.service;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.admin.controller.RecommendController;
import harmony.admin.model.Recommend;
import harmony.common.AbstractService;
import harmony.common.ImageManager;

public class RecommendImageService extends AbstractService {

  @Override
  public Object doService(Object argument)
      throws SQLException, IOException, JSONException {

    // 쿼리 실행
    Recommend recommend = RecommendController.getRecommendByNum((int) argument);

    // 결과 레코드를 객체에 저장
    String imageStream = "";
    if (recommend != null) {
      imageStream = ImageManager.readByteStringFromImage(recommend.getImage());
    }

    return imageStream;
  }

}
