package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
	
	private int port = 6500;
	private ServerSocket serverSocket;
	private List<ClientThread> clientThreadList;

	public ServerThread() {

		this.serverSocket = null;
		this.clientThreadList = new ArrayList<ClientThread>();
	}

	@Override
	public void run() {

		if (this.serverSocket != null) {
			return;
		}

		try {

			this.serverSocket = new ServerSocket(this.port);

			while (true) {

				Socket clientSocket = this.serverSocket.accept();
				System.out.println("Client ���� : " + clientSocket.getInetAddress().getHostName());

				ClientThread clientThread = new ClientThread(this, clientSocket);
				clientThread.start();
				this.clientThreadList.add(clientThread);
			}

		} catch (IOException e) {
			System.out.println("ServerThread.run() exception : " + e.getMessage());
		}

		this.stopServer();
	}

	public void startServer() {
		this.start();
	}

	@SuppressWarnings("deprecation")
	public void stopServer() {

		try {

			for (ClientThread client : this.clientThreadList) {

				client.stop();
				client.closeClient();
			}

			this.clientThreadList.clear();

			if (this.serverSocket != null) {

				this.serverSocket.close();
				this.serverSocket = null;
			}

		} catch (IOException e) {
			System.out.println("ServerThread.stopServer() : " + e.getMessage());
		}
	}

	public void removeClientFromList(ClientThread clientThread) {
		this.clientThreadList.remove(clientThread);
	}

	public boolean isRunning() {
		return this.serverSocket != null;
	}
}
