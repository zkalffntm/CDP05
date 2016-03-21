package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
	/**
	 * ��� ������
	 */
	private int port = 6500; // ���� ��Ʈ��ȣ
	private ServerSocket serverSocket; // ���� accept�� ����
	private List<ClientThread> clientThreadList; // Ŭ���̾�Ʈ ����Ʈ

	/**
	 * ������. ���� ������� �ʱ�ȭ.
	 */
	public ServerThread() {

		this.serverSocket = null;
		this.clientThreadList = new ArrayList<ClientThread>();
	}

	/**
	 * ������ ����κ�. while������ Ŭ���̾�Ʈ�� accept��.
	 */
	@Override
	public void run() {

		// �̹� ���� ���̸� ����.
		if (this.serverSocket != null) {
			return;
		}

		try {

			// ���� ���� ����
			this.serverSocket = new ServerSocket(this.port);

			// ���� ����
			while (true) {

				// Ŭ���̾�Ʈ �ϳ� accept �� (��ϵ�).
				Socket clientSocket = this.serverSocket.accept();
				System.out.println("Client ���� : " + clientSocket.getInetAddress().getHostName());

				// �� Ŭ���̾�Ʈ ���� ������ ���� �� ����Ʈ�� �߰�
				ClientThread clientThread = new ClientThread(this, clientSocket);
				clientThread.start();
				this.clientThreadList.add(clientThread);
			}

		} catch (IOException e) {
			System.out.println("ServerThread.run()���� ���� �߻� : " + e.getMessage());
		}

		// ���� ����
		this.stopServer();
	}

	/**
	 * ���� ����. �ܼ��� ������ run�κ��� ������.
	 */
	public void startServer() {

		// run() �޼ҵ� ȣ��.
		this.start();
	}

	/**
	 * ���� ����. ��� Ŭ���̾�Ʈ ���� ������ ���� �� ������ ����.
	 */
	@SuppressWarnings("deprecation")
	public void stopServer() {

		try {

			// �� Ŭ���̾�Ʈ �����带 ���߰� ���� ����
			for (ClientThread client : this.clientThreadList) {

				client.stop();
				client.closeClient();
			}

			// Ŭ���̾�Ʈ ����Ʈ �ʱ�ȭ
			this.clientThreadList.clear();

			// ���� ���� ����
			if (this.serverSocket != null) {

				this.serverSocket.close();
				this.serverSocket = null;
			}

		} catch (IOException e) {
			System.out.println("ServerThread.stopServer()���� ���� �߻� : " + e.getMessage());
		}
	}

	/**
	 * �Ѱ��� Ŭ���̾�Ʈ�� ����Ʈ�κ��� ����.
	 * 
	 * @param clientThread
	 */
	public void removeClientFromList(ClientThread clientThread) {
		this.clientThreadList.remove(clientThread);
	}

	/**
	 * ���� ���ۿ��θ� ��ȯ.
	 * 
	 * @return isRunning
	 */
	public boolean isRunning() {
		return this.serverSocket != null;
	}
}
