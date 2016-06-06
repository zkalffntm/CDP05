package harmony.central.service;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;

import harmony.central.controller.VisitedController;
import harmony.common.AbstractService;

/**
 * 
 * @author Seongjun Park
 * @since 2016/6/6
 * @version 2016/6/6
 */
public class AppendVisitService extends AbstractService {

  @Override
  public Object doService(Object argument)
      throws SQLException, IOException, JSONException {

    // 사용자 id와 비콘의 major
    JSONArray info = (JSONArray) argument;
    String id = info.getString(0);
    int major = info.getInt(1);

    // 방문내역을 저장을 시도. 실패 시 false 반환
    try {
      VisitedController.saveVisited(id, major);
    } catch (SQLException e) {
      return false;
    }

    // 성공 시 true 반환
    return true;
  }
}
