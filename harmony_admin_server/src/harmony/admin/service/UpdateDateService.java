package harmony.admin.service;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.common.AbstractService;
import harmony.common.DatFileManager;

/**
 * 
 * @author Seongjun Park
 * @since 2016/4/13
 * @version 2016/4/13
 */
public class UpdateDateService extends AbstractService {
  public static final String DEFAULT_DATA_KEY = "update";

  /**
   * configuration file에서 최근 업데이트 날짜의 Long형을 가져옴.
   * 
   * @param argument
   *          사용 안 함
   * @return long = 업데이트 날짜 값
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
    return Long.parseLong(new DatFileManager().getData(DEFAULT_DATA_KEY));
  }

}
