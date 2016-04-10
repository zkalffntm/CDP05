package harmony.central.communication;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import harmony.central.service.ProviderListService;
import harmony.common.AbstractClientThread;
import harmony.common.AbstractServerThread;

/**
 * 중앙 서버의 클라이언트 스레드 클래스.<br>
 * 제공하는 서비스는 다음과 같다.<br>
 * - 박물관 정보<br>
 * 
 * @author Seongjun Park
 * @since 2016/3/25
 * @version 2016/4/1
 */
public class CentralClientThread extends AbstractClientThread {

	/**
	 * 생성자. 서버스레드와 클라이언트 소켓 객체를 설정한다.
	 * 
	 * @param serverThread
	 *          서버스레드. 이 때 CentralServerThread이어야 함.
	 * @param clientSocket
	 *          클라이언트 소켓
	 */
	public CentralClientThread(AbstractServerThread serverThread, Socket clientSocket) {
		super(serverThread, clientSocket);
	}

	@Override
	protected void process(JSONObject recvJson) throws JSONException, SQLException, Exception {
		String key = recvJson.getString("key");
		Object value = recvJson.get("value");

		switch (key) {
		case "req_provider_list":
			this.doProviderListService(value);
			break;
		default:
			throw new Exception("unacceptable message");
		}
	}

	/**
	 * 클라이언트의 위치 gps를 JSON의 value에서 가져와 그 정보를 활용해<br>
	 * {@link ProviderListService}를 수행한 결과를 클라이언트에게 응답한다.
	 * 
	 * @param value
	 *          GPS 정보가 있는 Object 객체
	 * @throws SQLException
	 *           SQL 관련 예외
	 * @throws JSONException
	 *           JSON 관련 예외
	 * @throws IOException
	 *           IO 관련 예외
	 */
	private void doProviderListService(Object value) throws JSONException, SQLException, IOException {
		JSONObject sendJson = new JSONObject();

		sendJson.put("key", "res_provider_list");
		sendJson.put("value", new ProviderListService().doService(value));

		this.getPrintWriter().println(sendJson.toString());
		this.getPrintWriter().flush();
	}
}
