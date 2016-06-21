package harmony.admin.service;

import java.io.IOException;
import java.sql.SQLException;

import harmony.common.AbstractService;
import harmony.common.DatFileManager;

/**
 * 고객에게 공지사항 URL을 보내는 서비스.
 * 
 * @author Seongjun Park
 * @since 2016/4/9
 * @version 2016/4/13
 */
public class NoitceService extends AbstractService {
  public static final String DEFAULT_NOTICE_KEY = "notice";

  /**
   * configuration file에서 url 정보를 받아옴
   * 
   * @param argument
   *          사용 안 함
   * @return String = "URL"
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  @Override
  public Object doService(Object argument) throws SQLException, IOException {
    return new DatFileManager().getData(DEFAULT_NOTICE_KEY);
  }
}
