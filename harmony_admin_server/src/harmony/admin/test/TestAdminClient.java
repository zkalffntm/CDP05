package harmony.admin.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 관리자 서버 테스트 클라이언트. 통신 송수신 확인용
 * 
 * @author Seongjun Park
 * @since 2016/4/5
 * @version 2016/4/5
 */
public class TestAdminClient {

	/**
	 * 메인 메소드
	 * 
	 * @param args
	 *          사용 안 함
	 * @throws Exception
	 *           모든 예외
	 */
	public static void main(String[] args) throws Exception {

		// 1. 서버와 연결 시도
		Socket socket = new Socket("psjpi.iptime.org", 9696);

		// 2. 스트림 생성
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new DataInputStream(socket.getInputStream())));
		PrintWriter printWriter = new PrintWriter(new DataOutputStream(socket.getOutputStream()));

		// 3. 서비스 테스트
		// 3-1. req_item_content
		System.out.println("test : req_item_content");
		doItemContentTest(bufferedReader, printWriter);

		// 4. 연결 종료
		socket.close();
	}

	/**
	 * req_item_content 테스트
	 * 
	 * @param bufferedReader
	 *          수신 스트림
	 * @param printWriter
	 *          송신 스트림
	 * @throws Exception
	 *           모든 예외
	 */
	private static void doItemContentTest(BufferedReader bufferedReader, PrintWriter printWriter) throws Exception {
		// 1. 요청 메시지 전송
		JSONObject sendJson = new JSONObject();
		sendJson.put("key", "req_item_content");
		sendJson.put("value", 2);
		printWriter.println(sendJson.toString());
		printWriter.flush();

		// 2. 응답 메시지 수신
		String line = bufferedReader.readLine();
		if (line == null) {
			throw new Exception("req_item_content 응답 수신 실패");
		}
		JSONObject recvJson = new JSONObject(line);

		// 3. 수신 메시지 출력
		System.out.println("\tkey   : " + recvJson.getString("key"));
		System.out.println("\tvalue : ");

		if (recvJson.isNull("value")) {
			System.out.println("\t\t정보가 없습니다.");
		} else {
			JSONArray recvValue = recvJson.getJSONArray("value");
			System.out.println("\t\ti_title   : " + recvValue.getString(0));
			System.out.println("\t\ti_artist  : " + recvValue.getString(1));
			System.out.println("\t\ti_content : " + recvValue.getString(2));
		}
	}
}
