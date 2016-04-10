package harmony.admin.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;

import harmony.common.AbstractService;
import harmony.common.DatFileReader;

/**
 * 고객에게 공지사항 URL을 보내는 서비스.
 * 
 * @author Seongjun Park
 * @since 2016/4/9
 * @version 2016/4/9
 */
public class NoitceService extends AbstractService {
	public static final String DEFAULT_CONFIG_FILE = "dat" + File.separator + "config.dat";

	/**
	 * configuration file에서 url 정보를 받아옴
	 * 
	 * @param argument
	 *          사용 안 함
	 * @return String = "URL"
	 * @throws SQLException
	 *           SQL 관련 예외
	 * @throws JSONException
	 *           JSON 관련 예외
	 * @throws IOException
	 *           IO 관련 예외
	 */
	@Override
	protected Object doQuery(Object argument) throws SQLException, JSONException, IOException {
		return new DatFileReader(DEFAULT_CONFIG_FILE).getData("notice");
	}

}
