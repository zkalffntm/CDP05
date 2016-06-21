package harmony.admin.communication;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import harmony.admin.service.ItemContentService;
import harmony.admin.service.ItemImageService;
import harmony.admin.service.MapImageService;
import harmony.admin.service.NoitceService;
import harmony.admin.service.RecommendImageService;
import harmony.admin.service.UpdateDateService;
import harmony.admin.service.UpdateService;
import harmony.common.AbstractClientThread;
import harmony.common.AbstractServerThread;
import harmony.common.PacketLiteral;

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
 * @version 2016/5/8
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
  public AdminClientThread(AbstractServerThread serverThread,
      Socket clientSocket) {
    super(serverThread, clientSocket);
  }

  @Override
  protected void process(JSONObject recvJson)
      throws JSONException, SQLException, Exception {
    String key = recvJson.getString(PacketLiteral.KEY);
    Object value = null;
    try {
      // JSON객체를 get했을때 null이면 예외가 발생하는데 이때는 선택적으로 무시 가능.
      // 예를 들면 공지사항 기능은 req에선 value가 null임.
      value = recvJson.get(PacketLiteral.VALUE);
    } catch (JSONException e) {
    }

    switch (key) {
    case PacketLiteral.REQ_ITEM_CONTENT:
      this.doItemContentService(value);
      break;
    case PacketLiteral.REQ_ITEM_IMAGE:
      this.doItemImageService(value);
      break;
    case PacketLiteral.REQ_AREA_IMAGE:
      this.doMapImageService(value);
      break;
    case PacketLiteral.REQ_NOTICE:
      this.doNoticeService();
      break;
    case PacketLiteral.REQ_UPDATE_DATE:
      this.doUpdateDateService();
      break;
    case PacketLiteral.REQ_UPDATE:
      this.doUpdateService();
      break;
    case PacketLiteral.REQ_RECOMMEND_IMAGE:
      this.doRecommendImageService(value);
      break;
    default:
      throw new Exception("unacceptable message");
    }
  }

  /**
   * 전시물 번호를 활용해 {@link ItemContentService}를 수행한 결과를 클라이언트에게 응답한다.
   * 
   * @param value
   *          전시물 번호 정보가 있는 Object 객체
   * @throws SQLException
   *           SQL 관련 예외
   * @throws JSONException
   *           JSON 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  private void doItemContentService(Object value)
      throws JSONException, SQLException, IOException {
    JSONObject sendJson = new JSONObject();

    sendJson.put(PacketLiteral.KEY, PacketLiteral.RES_ITEM_CONTENT);
    sendJson.put(PacketLiteral.VALUE,
        new ItemContentService().doService(value));

    this.getPrintWriter().println(sendJson.toString());
    this.getPrintWriter().flush();
  }

  /**
   * 전시물 번호를 활용해 {@link ItemImageService}를 수행한 결과를 클라이언트에게 응답한다.
   * 
   * @param value
   *          전시물 번호 정보가 있는 Object 객체
   * @throws JSONException
   *           JSON 관련 예외
   * @throws IOException
   *           IO 관련 예외
   * @throws SQLException
   *           sQL 관련 예외
   */
  private void doItemImageService(Object value)
      throws JSONException, SQLException, IOException {
    JSONObject sendJson = new JSONObject();

    sendJson.put(PacketLiteral.KEY, PacketLiteral.RES_ITEM_IMAGE);
    sendJson.put(PacketLiteral.VALUE, new ItemImageService().doService(value));

    this.getPrintWriter().println(sendJson.toString());
    this.getPrintWriter().flush();

  }

  /**
   * 지도 번호를 활용해 {@link MapImageService}를 수행한 결과를 클라이언트에게 응답한다.
   * 
   * @param value
   *          지도 번호 정보가 있는 Object 객체
   * @throws JSONException
   *           JSON 관련 예외
   * @throws IOException
   *           IO 관련 예외
   * @throws SQLException
   *           sQL 관련 예외
   */
  private void doMapImageService(Object value)
      throws JSONException, SQLException, IOException {
    JSONObject sendJson = new JSONObject();

    sendJson.put(PacketLiteral.KEY, PacketLiteral.RES_AREA_IMAGE);
    sendJson.put(PacketLiteral.VALUE, new MapImageService().doService(value));

    this.getPrintWriter().println(sendJson.toString());
    this.getPrintWriter().flush();
  }

  /**
   * 클라이언트에게 공지사항 정보를 전송한다.<br>
   * {@link NoitceService}를 수행한 결과를 클라이언트에게 응답한다.
   * 
   * @throws SQLException
   *           SQL 관련 예외
   * @throws JSONException
   *           JSON 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  private void doNoticeService()
      throws SQLException, JSONException, IOException {
    JSONObject sendJson = new JSONObject();

    sendJson.put(PacketLiteral.KEY, PacketLiteral.RES_NOTICE);
    sendJson.put(PacketLiteral.VALUE, new NoitceService().doService(null));

    this.getPrintWriter().println(sendJson.toString());
    this.getPrintWriter().flush();
  }

  /**
   * {@link UpdateDateService}를 수행한 결과를 클라이언트에게 응답한다.
   * 
   * @throws SQLException
   *           SQL 관련 예외
   * @throws JSONException
   *           JSON 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  private void doUpdateDateService()
      throws JSONException, SQLException, IOException {
    JSONObject sendJson = new JSONObject();

    sendJson.put(PacketLiteral.KEY, PacketLiteral.RES_UPDATE_DATE);
    sendJson.put(PacketLiteral.VALUE, new UpdateDateService().doService(null));

    this.getPrintWriter().println(sendJson.toString());
    this.getPrintWriter().flush();
  }

  /**
   * {@link UpdateService}를 수행한 결과를 클라이언트에게 응답한다.
   * 
   * @throws JSONException
   *           JSON 관련 예외
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  private void doUpdateService()
      throws JSONException, SQLException, IOException {
    JSONObject sendJson = new JSONObject();

    sendJson.put(PacketLiteral.KEY, PacketLiteral.RES_UPDATE);
    sendJson.put(PacketLiteral.VALUE, new UpdateService().doService(null));

    this.getPrintWriter().println(sendJson.toString());
    this.getPrintWriter().flush();
  }

  private void doRecommendImageService(Object value)
      throws JSONException, SQLException, IOException {
    JSONObject sendJson = new JSONObject();

    sendJson.put(PacketLiteral.KEY, PacketLiteral.RES_RECOMMEND_IMAGE);
    sendJson.put(PacketLiteral.VALUE,
        new RecommendImageService().doService(value));

    this.getPrintWriter().println(sendJson.toString());
    this.getPrintWriter().flush();
  }
}
