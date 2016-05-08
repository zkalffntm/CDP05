package harmony.admin.service;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.common.AbstractService;
import harmony.common.RecommandRoute;

/**
 * 고객에게 추천경로 객체를 보내주는 서비스.
 * 
 * @author Seongjun Park
 * @since 2016/5/8
 * @version 2016/5/8
 */
public class RecommandRouteService extends AbstractService {

  /**
   * configuration file에 있는 정보를<br>
   * {@link RecommandRoute}객체화한 여러개를 전송.
   * 
   * @param argument
   *          사용 안 함
   * @return RecommandRoute[]
   * @throws SQLException
   *           SQL 관련 예외
   * @throws JSONException
   *           JSON 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  @Override
  protected Object doQuery(Object argument)
      throws SQLException, JSONException, IOException {
    // TODO Auto-generated method stub
    return null;
  }
}
