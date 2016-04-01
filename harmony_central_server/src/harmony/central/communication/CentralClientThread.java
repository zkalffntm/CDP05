package harmony.central.communication;

import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import harmony.central.service.IpSearchService;

/**
 * 중앙 서버의 클라이언트 스레드 클래스.<br>
 * 클라이언트로부터 접속 요청이 오면 해당 GPS 정보에 대해<br>
 * 알맞는 박물관 및 전시관 정보를 전송한다.
 * 
 * @author Seongjun Park
 * @since 2016/3/25
 * @version 2016/3/25
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
		switch (recvJson.getString("key")) {
		case "req_test":
			this.serviceIpSearch(recvJson.get("value"));
			break;
		default:
			throw new Exception("unacceptable message");
		}
	}

	/**
	 * 클라이언트의 위치 gps를 JSON의 value에서 가져와 그 정보를 활용해<br>
	 * IpSearchService를 수행한 결과를 클라이언트에게 응답한다.
	 * 
	 * @param value
	 *          GPS 정보가 있는 Object 객체
	 * @throws SQLException
	 *           SQL 관련 예외
	 * @throws JSONException
	 *           JSON 관련 예외
	 */
	private void serviceIpSearch(Object value) throws JSONException, SQLException {

		JSONObject sendJson = new JSONObject();
		sendJson.put("value", new IpSearchService().doService(value));

		this.getPrintWriter().println(sendJson.toString());
		this.getPrintWriter().flush();
	}
}
