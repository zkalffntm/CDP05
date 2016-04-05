package harmony.central.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 중앙 서버 테스트 클라이언트. 통신 송수신 확인용
 * 
 * @author Seongjun Park
 * @since 2016/4/4
 * @version 2016/4/4
 */
public class TestClient {

	/**
	 * 메인 메소드
	 * 
	 * @param args
	 *          사용 안 함
	 * @throws Exception
	 *           throws 되는 모든 예외
	 */
	public static void main(String[] args) throws Exception {

		// 1. 서버와 연결 시도
		Socket socket = new Socket("psjpi.iptime.org", 9595);

		// 2. 스트림 생성
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new DataInputStream(socket.getInputStream())));
		PrintWriter printWriter = new PrintWriter(new DataOutputStream(socket.getOutputStream()));

		// 3. 서비스 테스트
		// 3-1. req_provider_list
		System.out.println("test : req_provider_list");
		doProviderListTest(bufferedReader, printWriter);

		socket.close();
	}

	/**
	 * req_provider_list 테스트
	 * 
	 * @param bufferedReader
	 *          수신 스트림
	 * @param printWriter
	 *          송신 스트림
	 * @throws Exception
	 *           모든 예외
	 */
	public static void doProviderListTest(BufferedReader bufferedReader, PrintWriter printWriter) throws Exception {
		JSONObject sendJson = new JSONObject();
		sendJson.put("key", "req_provider_list");
		sendJson.put("value", new double[] { 1.1, 2.2 });
		printWriter.println(sendJson.toString());
		printWriter.flush();

		String line = bufferedReader.readLine();
		if (line == null) {
			throw new Exception("req_provider_list 응답 수신 실패");
		}

		JSONObject recvJson = new JSONObject(line);
		System.out.println("\tkey   : " + recvJson.getString("key"));

		System.out.println("\tvalue : ");
		JSONArray recvValue = recvJson.getJSONArray("value");
		for (int i = 0; i < recvValue.length(); i++) {
			JSONArray row = recvValue.getJSONArray(i);

			System.out.println("\t\t[" + i + "] : ");
			System.out.println("\t\t\te_name : " + row.getString(0));
			System.out
					.println("\t\t\te_ip   : " + row.getInt(1) + '.' + row.getInt(2) + '.' + row.getInt(3) + '.' + row.getInt(4));
			System.out.println("\t\t\te_port : " + row.getInt(4));
		}
	}
}