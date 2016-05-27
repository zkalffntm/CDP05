package harmony.admin.communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

import harmony.common.PacketLiteral;

public class ConnectionToCentral {
  private static final String CENTRAL_IP = "psjpi.iptime.org";
  private static final int CENTRAL_PORT = 9595;

  private Socket socket = null;
  private BufferedReader bufferedReader = null;
  private PrintWriter printWriter = null;

  private void connect() throws IOException {
    if (this.socket != null) {
      return;
    }

    try {
      this.socket = new Socket(CENTRAL_IP, CENTRAL_PORT);
      this.bufferedReader = new BufferedReader(
          new InputStreamReader(new DataInputStream(socket.getInputStream())));
      this.printWriter = new PrintWriter(
          new DataOutputStream(socket.getOutputStream()));
    } catch (IOException e) {
      this.disconnect();
      throw e;
    }
  }

  private JSONObject sendJson(String reqKey, Object value, String resKey)
      throws Exception {
    JSONObject recvJson = null;
  
    try {
      JSONObject sendJson = new JSONObject();
      sendJson.put(PacketLiteral.KEY, reqKey);
      sendJson.put(PacketLiteral.VALUE, value);
      printWriter.println(sendJson.toString());
      printWriter.flush();
  
      String line = bufferedReader.readLine();
      if (line == null) {
        throw new Exception(reqKey + " 응답 수신 실패");
      }
      recvJson = new JSONObject(line);
      if (!resKey.equals(recvJson.getString(PacketLiteral.KEY))) {
        throw new Exception(reqKey + " 응답 수신 실패");
      }
    } catch (Exception e) {
      this.disconnect();
      throw e;
    }
  
    return recvJson;
  }

  private void disconnect() {
    if (this.printWriter != null) {
      this.printWriter.close();
      this.printWriter = null;
    }
    if (this.bufferedReader != null) {
      try {
        this.bufferedReader.close();
      } catch (IOException e) {
      } finally {
        this.bufferedReader = null;
      }
    }
    if (this.socket != null) {
      try {
        this.socket.close();
      } catch (IOException e) {
      } finally {
        this.socket = null;
      }
    }
  }

  public boolean doLogin(String id, String password) throws Exception {

    this.connect();

    JSONObject recvJson = this.sendJson(PacketLiteral.REQ_LOGIN_ADMIN,
        new String[] { id, password }, PacketLiteral.RES_LOGIN_ADMIN);

    this.disconnect();

    return recvJson.getBoolean(PacketLiteral.VALUE);
  }
}
