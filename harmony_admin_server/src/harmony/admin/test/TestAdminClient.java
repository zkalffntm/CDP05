package harmony.admin.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import harmony.admin.database.DbLiteral;
import harmony.common.ImageManager;
import harmony.common.PacketLiteral;

/**
 * 관리자 서버 테스트 클라이언트. 통신 송수신 확인용
 * 
 * @author Seongjun Park
 * @since 2016/4/5
 * @version 2016/5/11
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
    System.out.println("test : " + PacketLiteral.REQ_ITEM_CONTENT);
    doItemContentTest(bufferedReader, printWriter);

    System.out.println("test : " + PacketLiteral.REQ_ITEM_IMAGE);
    doItemImageTest(bufferedReader, printWriter);

    System.out.println("test : " + PacketLiteral.REQ_AREA_IMAGE);
    doMapImageTest(bufferedReader, printWriter);

    System.out.println("test : " + PacketLiteral.REQ_NOTICE);
    doNoticeTest(bufferedReader, printWriter);

    System.out.println("test : " + PacketLiteral.REQ_UPDATE_DATE);
    doUpdateDateService(bufferedReader, printWriter);

    System.out.println("test : " + PacketLiteral.REQ_UPDATE);
    doUpdateService(bufferedReader, printWriter);

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
    sendJson.put(PacketLiteral.KEY, PacketLiteral.REQ_ITEM_CONTENT);
    sendJson.put(PacketLiteral.VALUE, 1);
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception(PacketLiteral.REQ_ITEM_CONTENT + " 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 메시지 출력
    System.out.println("\t" + PacketLiteral.KEY + "   : "
        + recvJson.getString(PacketLiteral.KEY));
    System.out.println("\t" + PacketLiteral.VALUE + " : ");

    if (recvJson.isNull(PacketLiteral.VALUE)) {
      System.out.println("\t\t정보가 없습니다.");
    } else {
      JSONArray recvValue = recvJson.getJSONArray(PacketLiteral.VALUE);
      System.out.println(
          "\t\t" + DbLiteral.I_TITLE + "   : " + recvValue.getString(0));
      System.out.println(
          "\t\t" + DbLiteral.I_ARTIST + "  : " + recvValue.getString(1));
      System.out.println(
          "\t\t" + DbLiteral.I_SIMPLE_CONTENT + " : " + recvValue.getString(2));
    }
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
    sendJson.put(PacketLiteral.KEY, PacketLiteral.REQ_ITEM_IMAGE);
    sendJson.put(PacketLiteral.VALUE, 1);
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception(PacketLiteral.REQ_ITEM_IMAGE + " 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 이미지 저장
    System.out.println("\t" + PacketLiteral.KEY + "   : "
        + recvJson.getString(PacketLiteral.KEY));
    ImageManager.writeImageFromByteString(
        recvJson.getString(PacketLiteral.VALUE), "test_item_1.jpg");
    System.out.println("\t" + PacketLiteral.VALUE + " : test_item_1.jpg 저장 완료");
  }

  /**
   * req_map_image 테스트
   * 
   * @param bufferedReader
   * @param printWriter
   * @throws Exception
   */
  private static void doMapImageTest(BufferedReader bufferedReader,
      PrintWriter printWriter) throws Exception {
    // 1. 요청 메시지 전송
    JSONObject sendJson = new JSONObject();
    sendJson.put(PacketLiteral.KEY, PacketLiteral.REQ_AREA_IMAGE);
    sendJson.put(PacketLiteral.VALUE, 1);
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception(PacketLiteral.REQ_AREA_IMAGE + " 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 이미지 저장
    System.out.println("\t" + PacketLiteral.KEY + "   : "
        + recvJson.getString(PacketLiteral.KEY));
    ImageManager.writeImageFromByteString(
        recvJson.getString(PacketLiteral.VALUE), "test_map_1.jpg");
    System.out.println("\t" + PacketLiteral.VALUE + " : test_map_1.jpg 저장 완료");
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
    sendJson.put(PacketLiteral.KEY, PacketLiteral.REQ_NOTICE);
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception(PacketLiteral.REQ_NOTICE + " 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 메시지 출력
    System.out.println("\t" + PacketLiteral.KEY + "   : "
        + recvJson.getString(PacketLiteral.KEY));
    System.out.println("\t" + PacketLiteral.VALUE + " : "
        + recvJson.getString(PacketLiteral.VALUE));
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
    sendJson.put(PacketLiteral.KEY, PacketLiteral.REQ_UPDATE_DATE);
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception(PacketLiteral.REQ_UPDATE_DATE + " 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 메시지 출력
    System.out.println("\t" + PacketLiteral.KEY + "   : "
        + recvJson.getString(PacketLiteral.KEY));
    System.out.println("\t" + PacketLiteral.VALUE + " : "
        + recvJson.getLong(PacketLiteral.VALUE));
  }

  private static void doUpdateService(BufferedReader bufferedReader,
      PrintWriter printWriter) throws Exception {
    // 1. 요청 메시지 전송
    JSONObject sendJson = new JSONObject();
    sendJson.put(PacketLiteral.KEY, PacketLiteral.REQ_UPDATE);
    printWriter.println(sendJson.toString());
    printWriter.flush();

    // 2. 응답 메시지 수신
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new Exception(PacketLiteral.REQ_UPDATE + " 응답 수신 실패");
    }
    JSONObject recvJson = new JSONObject(line);

    // 3. 수신 메시지 출력
    System.out.println("\t" + PacketLiteral.KEY + "   : "
        + recvJson.getString(PacketLiteral.KEY));
    System.out.println("\t" + PacketLiteral.VALUE + " : ");
    JSONArray recvValue = recvJson.getJSONArray(PacketLiteral.VALUE);

    // 3-1. 전시물 테이블
    System.out.println("\t\t전시물");
    JSONArray items = recvValue.getJSONArray(0);
    for (int i = 0; i < items.length(); i++) {
      JSONArray item = items.getJSONArray(i);
      System.out.println("\t\t" + item.getInt(0) + " " + item.getInt(1) + " "
          + item.getInt(2));
    }
    System.out.println();

    // 3-2. 블록 테이블
    System.out.println("\t\t블록");
    JSONArray blocks = recvValue.getJSONArray(1);
    for (int i = 0; i < blocks.length(); i++) {
      JSONArray block = blocks.getJSONArray(i);
      System.out.println("\t\t" + block.getInt(0) + " " + block.getInt(1));
    }
    System.out.println();

    // 3-3. 신호세기율 테이블
    System.out.println("\t\t신호세기율");
    JSONArray ratioOffsets = recvValue.getJSONArray(2);
    for (int i = 0; i < ratioOffsets.length(); i++) {
      JSONArray ratioOffset = ratioOffsets.getJSONArray(i);
      System.out.println("\t\t" + ratioOffset.getInt(0) + " "
          + ratioOffset.getInt(1) + " " + ratioOffset.getInt(2) + " "
          + ((float) ratioOffset.getDouble(3)));
    }
    System.out.println();

    // 3-4. 지도 테이블
    System.out.println("\t\t지도");
    JSONArray maps = recvValue.getJSONArray(3);
    for (int i = 0; i < maps.length(); i++) {
      JSONArray map = maps.getJSONArray(i);
      System.out.println("\t\t" + map.getInt(0) + " " + map.getString(1));
    }
    System.out.println();

    // 3-5. 공유블록 테이블
    System.out.println("\t\t공유블록");
    JSONArray shareBlocks = recvValue.getJSONArray(4);
    for (int i = 0; i < shareBlocks.length(); i++) {
      JSONArray shareBlock = shareBlocks.getJSONArray(i);
      System.out
          .println("\t\t" + shareBlock.getInt(0) + " " + shareBlock.getInt(1)
              + " " + shareBlock.getInt(2) + " " + shareBlock.getInt(3));
    }
    System.out.println();

    // 3-6. 추천경로 테이블
    System.out.println("\t\t추천경로");
    JSONArray recommends = recvValue.getJSONArray(5);
    for (int i = 0; i < recommends.length(); i++) {
      JSONArray recommend = recommends.getJSONArray(i);
      System.out.print(
          "\t\t" + recommend.getInt(0) + " " + recommend.getString(1) + " ");
      for (int j = 2; j < recommend.length(); j++) {
        System.out.print(recommend.getInt(j) + " ");
      }
      System.out.println();
    }
    System.out.println();
  }
}
