package communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import service.IpSearchService;

public class ClientThread extends Thread {

	private ServerThread serverThread;
	private Socket clientSocket;

	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	public ClientThread(ServerThread serverThread, Socket clientSocket) {

		this.serverThread = serverThread;
		this.clientSocket = clientSocket;
		this.bufferedReader = null;
		this.printWriter = null;
	}

	@Override
	public void run() {

		try {
			this.bufferedReader = new BufferedReader(
					new InputStreamReader(new DataInputStream(this.clientSocket.getInputStream())));
			this.printWriter = new PrintWriter(new DataOutputStream(this.clientSocket.getOutputStream()));

			while (true) {

				String line = this.bufferedReader.readLine();
				if (line == null) {
					break;
				}
				JSONObject recvJson = new JSONObject(line);
				switch (recvJson.getString("key")) {
				case "req_test":
					this.serviceIpSearch(recvJson.get("value"));
					break;
				default:
					throw new Exception("unacceptable message");
				}
			}
		} catch (Exception e) {
		}

		System.out.println("Client out : " + clientSocket.getInetAddress().getHostName());

		this.closeClient();
		this.serverThread.removeClientFromList(this);
	}

	/**
	 * 클라이언트의 소켓 및 스트림 정리
	 */
	public void closeClient() {

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
	 * 클라이언트의 위치 gps를 JSON의 value에서 가져와 그 정보를 활용해<br>
	 * IpSearchService를 수행한 결과를 클라이언트에게 응답한다.
	 * 
	 * @param recvJson
	 * @throws SQLException 
	 * @throws JSONException 
	 */
	public void serviceIpSearch(Object value) throws JSONException, SQLException {
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("value", new IpSearchService().doService(value));
		
		this.printWriter.println(sendJson.toString());
		this.printWriter.flush();
	}
}
