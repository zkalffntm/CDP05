package communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

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
				JSONObject recvMsg = new JSONObject(line);
				switch (recvMsg.getString("key")) {
				case "req_test":
					break;
				}
			}
		} catch (Exception e) {
		}

		System.out.println("Client out : " + clientSocket.getInetAddress().getHostName());

		this.closeClient();
		this.serverThread.removeClientFromList(this);
	}

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
			System.out.println("ClientThread.stopClient() : " + e.getMessage());
		}
	}
}
