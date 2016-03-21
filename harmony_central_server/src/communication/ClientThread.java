package communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class ClientThread extends Thread {
	/**
	 * ��� ������
	 */
	private ServerThread serverThread; // ���� ������ (parent)
	private Socket clientSocket; // Ŭ���̾�Ʈ ����

	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	/**
	 * ������. ���� ��������� �ʱ�ȭ.
	 * 
	 * @param serverThread
	 * @param clientSocket
	 */
	public ClientThread(ServerThread serverThread, Socket clientSocket) {

		this.serverThread = serverThread;
		this.clientSocket = clientSocket;
		this.bufferedReader = null;
		this.printWriter = null;
	}

	/**
	 * ������ ����κ�. while������ Ŭ���̾�Ʈ�κ��� �޽����� �ް� ���񽺸� ������.
	 */
	@Override
	public void run() {

		try {

			// ����� ��Ʈ�� ����
			this.bufferedReader = new BufferedReader(
					new InputStreamReader(new DataInputStream(this.clientSocket.getInputStream())));
			this.printWriter = new PrintWriter(new DataOutputStream(this.clientSocket.getOutputStream()));

			// ���� ����
			while (true) {

				// Ŭ���̾�Ʈ�κ��� JSON �޽����� ����
				String line = this.bufferedReader.readLine();
				if (line == null) {
					break;
				}
				JSONObject recvMsg = new JSONObject(line);
				System.out.println(recvMsg.getString("MessageType"));
				switch (recvMsg.getString("MessageType")) {
				case "req_login":
					// sendlogin(recvMsg);
					break;
				}
			}
		} catch (Exception e) {
		}

		// Ŭ���̾�Ʈ ���� �� �������� ����Ʈ�κ��� ���ŵ�.
		System.out.println("Client ���� : " + clientSocket.getInetAddress().getHostName());

		this.closeClient();
		this.serverThread.removeClientFromList(this);
	}

	/**
	 * Ŭ���̾�Ʈ ����. ����½�Ʈ���� ������ ������.
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

		} catch (Exception e) {
			System.out.println("ClientThread.stopClient()���� ���� �߻� : " + e.getMessage());
		}
	}
}
