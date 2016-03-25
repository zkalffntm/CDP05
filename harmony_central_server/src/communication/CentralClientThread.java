package communication;

import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import service.IpSearchService;

/**
 * 
 * @author Seongjun Park
 * @
 */
public class CentralClientThread extends AbstractClientThread {

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
	 * @param recvJson
	 * @throws SQLException
	 * @throws JSONException
	 */
	private void serviceIpSearch(Object value) throws JSONException, SQLException {

		JSONObject sendJson = new JSONObject();
		sendJson.put("value", new IpSearchService().doService(value));

		this.getPrintWriter().println(sendJson.toString());
		this.getPrintWriter().flush();
	}
}
