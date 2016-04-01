package harmony.admin.communication;

import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import harmony.admin.service.ItemContentService;

/**
 * 관리자 서버의 클라이언트 스레드 클래스.<br>
 * 제공하는 서비스는 다음과 같다.<br>
 * - 공지사항<br>
 * - 업데이트 날짜 확인<br>
 * - 업데이트<br>
 * - 전시물 이미지<br>
 * - 전시물 설명<br>
 * - 추천경로<br>
 * - 등등
 * 
 * @author Seongjun Park
 * @since 2016/4/1
 * @version 2016/4/1
 */
public class AdminClientThread extends AbstractClientThread {

	/**
	 * 생성자. 서버스레드와 클라이언트 소켓 객체를 설정한다.
	 * 
	 * @param serverThread
	 *          서버스레드. 이 때 AdminServerThread이어야 함.
	 * @param clientSocket
	 *          클라이언트 소켓
	 */
	public AdminClientThread(AbstractServerThread serverThread, Socket clientSocket) {
		super(serverThread, clientSocket);
	}

	@Override
	protected void process(JSONObject recvJson) throws JSONException, SQLException, Exception {
		String key = recvJson.getString("key");
		Object value = recvJson.get("value");

		switch (key) {
		case "req_notice":
			this.doNoticeService();
			break;
		case "req_item_content":
			this.doItemContentService(value);
			break;
		default:
			throw new Exception("unacceptable message");
		}
	}

	/**
	 * 클라이언트에게 공지사항 정보를 전송한다.<br>
	 * {@link }를 수행한 결과를 클라이언트에게 응답한다.
	 * 
	 * @throws SQLException
	 *           SQL 관련 예외
	 * @throws JSONException
	 *           JSON 관련 예외
	 */
	private void doNoticeService() throws SQLException, JSONException {
		// 미완성
	}

	/**
	 * 전시물 번호를 활용해{@link }를 수행한 결과를 클라이언트에게 응답한다.
	 * 
	 * @param value
	 *          전시물 번호 정보가 있는 Object 객체
	 * @throws SQLException
	 *           SQL 관련 예외
	 * @throws JSONException
	 *           JSON 관련 예외
	 */
	private void doItemContentService(Object value) throws JSONException, SQLException {
		JSONObject sendJson = new JSONObject();

		sendJson.put("key", "res_item_content");
		sendJson.put("value", new ItemContentService().doService(value));

		this.getPrintWriter().println(sendJson.toString());
		this.getPrintWriter().flush();
	}
}
