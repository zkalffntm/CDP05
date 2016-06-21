package harmony.central.service;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.central.controller.ExhibitionController;
import harmony.central.controller.VisitedController;
import harmony.central.model.Exhibition;
import harmony.central.model.Visited;
import harmony.common.AbstractService;

/**
 * 
 * @author Seongjun Park
 * @since 2016/6/6
 * @version 2016/6/6
 */
public class VisitedService extends AbstractService {

  @Override
  public Object doService(Object argument)
      throws SQLException, IOException, JSONException {

    // 쿼리 실행
    Visited[] visiteds = VisitedController
        .getVisitedByUserId((String) argument);

    // 방문한 전시관들의 major와 이름을 저장
    Object[][] objs = new Object[visiteds.length][];
    for (int i = 0; i < visiteds.length; i++) {
      Exhibition exhibition = ExhibitionController
          .getExhibitionByNum(visiteds[i].getExhibitionNum());
      objs[i] = new Object[2];
      objs[i][0] = exhibition.getBeaconMajor();
      objs[i][1] = exhibition.getName();
    }

    return objs;
  }
}
