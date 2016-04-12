package harmony.admin.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import harmony.common.ImageManager;

/**
 * 관리자 서버 테스트 클라이언트. 통신 송수신 확인용
 * 
 * @author Seongjun Park
 * @since 2016/4/5
 * @version 2016/4/13
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
    PrintWriter printWriter = new PrintWriter(
        new DataOutputStream(socket.getOutputStream()));

    // 3. 서비스 테스트
    // 3-1. req_item_content
    System.out.println("test : req_item_content");
    doItemContentTest(bufferedReader, printWriter);

    // 3-2. req_notice
    System.out.println("test : req_notice");
    doNoticeTest(bufferedReader, printWriter);

    // 3-3. req_item_image
    System.out.println("test : req_item_image");
    doItemImageTest(bufferedReader, printWriter);

    System.out.println("test : req_update_date");
    doUpdateDateService(bufferedReader, printWriter);

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
  private static void doItemContentTest(BufferedReader bufferedReader,
      PrintWriter printWriter) throws Exception {
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

  /**
   * 
   * @param bufferedReader
   * @param printWriter
   * @throws Exception
   */
  private static void doNoticeTest(BufferedReader bufferedReader,
      PrintWriter printWriter) throws Exception {
    // 1. 요청 메시지 전송
    JSONObject sendJson = new JSONObject();
    sendJson.put("key", "req_notice");
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception("req_notice 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 메시지 출력
    System.out.println("\tkey   : " + recvJson.getString("key"));
    System.out.println("\tvalue : " + recvJson.getString("value"));
  }

  /**
   * req_item_image 테스트
   * 
   * @param bufferedReader
   * @param printWriter
   * @throws Exception
   */
  private static void doItemImageTest(BufferedReader bufferedReader,
      PrintWriter printWriter) throws Exception {
    // 1. 요청 메시지 전송
    JSONObject sendJson = new JSONObject();
    sendJson.put("key", "req_item_image");
    sendJson.put("value", 2);
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception("req_notice 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 이미지 저장
    System.out.println("\tkey   : " + recvJson.getString("key"));
    ImageManager.writeImageFromByteString(recvJson.getString("value"),
        "test2.jpg");
    System.out.println("\tvalue : test1.jpg 저장 완료");
  }

  /**
   * req_update_date 테스트
   * 
   * @param bufferedReader
   * @param printWriter
   * @throws Exception
   */
  private static void doUpdateDateService(BufferedReader bufferedReader,
      PrintWriter printWriter) throws Exception {
    // 1. 요청 메시지 전송
    JSONObject sendJson = new JSONObject();
    sendJson.put("key", "req_update_date");
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception("req_update_date 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 메시지 출력
    System.out.println("\tkey   : " + recvJson.getString("key"));
    System.out.println("\tvalue : " + recvJson.getLong("value"));
  }
}
