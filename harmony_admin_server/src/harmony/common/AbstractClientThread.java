package harmony.common;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link AbstractServerThread}에서 한 클라이언트가 accept된 후<br>
 * 생성되어 클라이언트 리스트에 추가될 클래스. JSON을 읽을 스트림을<br>
 * 생성한 후, 클라이언트로 부터 받은 JSON을 읽어 동작을 수행하도록<br>
 * process()를 오버라이드 해야한다.
 * 
 * @author Seongjun Park
 * @since 2016/3/25
 * @version 2016/4/5
 */
public abstract class AbstractClientThread extends Thread {
  private AbstractServerThread serverThread;
  private Socket clientSocket;
  private BufferedReader bufferedReader = null;
  private PrintWriter printWriter = null;

  /**
   * 생성자. 서버 스레드와 클라이언트 소켓 설정.
   * 
   * @param serverThread
   *          서버 스레드
   * @param clientSocket
   *          클라이언트 소켓
   */
  public AbstractClientThread(AbstractServerThread serverThread,
      Socket clientSocket) {
    this.serverThread = serverThread;
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    try {
      // 소켓 메시지를 읽고 쓰는 스트림 생성
      this.bufferedReader = new BufferedReader(new InputStreamReader(
          new DataInputStream(this.clientSocket.getInputStream())));
      this.printWriter = new PrintWriter(
          new DataOutputStream(this.clientSocket.getOutputStream()));

      // 서비스 무한 루프
      while (true) {

        // 메시지를 수신
        String line = this.bufferedReader.readLine();
        if (line == null) {
          break;
        }

        // JSON메시지를 수행
        this.process(new JSONObject(line));
      }
    } catch (JSONException e) {
      System.out.println("Client " + clientSocket.getInetAddress().getHostName()
          + " : JSON 예외 - " + e.getMessage());
    } catch (IOException e) {
      System.out.println("Client " + clientSocket.getInetAddress().getHostName()
          + " : IO 예외 - " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Client " + clientSocket.getInetAddress().getHostName()
          + " : 예외 - " + e.getMessage());
    }

    // 클라이언트와 접속을 끝내고 서버스레드에 있는 클라이언트 리스트에서
    // 이 객체를 제외시킴
    System.out
        .println("Client 퇴장 : " + clientSocket.getInetAddress().getHostName());
    this.closeClient();
    this.serverThread.removeClientFromList(this);
  }

  /**
   * 클라이언트의 소켓 및 스트림 정리
   */
  void closeClient() {
    try {
      if (this.bufferedReader != null) {
        this.bufferedReader.close();
        this.bufferedReader = null;
      }

      if (this.printWriter != null) {
        this.printWriter.close();
        this.printWriter = null;
      }

      if (this.clientSocket != null) {
        this.clientSocket.close();
        this.clientSocket = null;
      }
    } catch (IOException e) {
      System.out.println("ClientThread.stopClient() : " + e.getMessage());
    }
  }

  /**
   * 쓰기 스트림 반환.
   * 
   * @return 쓰기 스트림
   */
  protected PrintWriter getPrintWriter() {
    return this.printWriter;
  }

  /**
   * 수신 JSON 메시지를 받아 본 서비스를 작업한다.
   * 
   * @param recvJson
   *          수신 JSON 메시지
   * @throws JSONException
   *           JSON 관련 예외
   * @throws SQLException
   *           SQL 관련 예외
   * @throws Exception
   *           기타 사용자 정의를 포함한 예외
   */
  protected abstract void process(JSONObject recvJson)
      throws JSONException, SQLException, Exception;
}
