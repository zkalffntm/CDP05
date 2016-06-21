package harmony.common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 서버를 가동하기 위한 스레드. 클라이언트는 {@link AbstractClientThread} 객체를<br>
 * 리스트로 관리한다. 추상 클래스 스레드의 타입을 결정해야 하므로 상속 시<br>
 * createClientThread()를 재구현해야한다.
 * 
 * @author Seongjun Park
 * @since 2016/3/25
 * @version 2016/4/4
 */
public abstract class AbstractServerThread extends Thread {
	private int port;
	private ServerSocket serverSocket = null;
	private List<AbstractClientThread> clientThreadList = new ArrayList<AbstractClientThread>();

	/**
	 * 추상 서버 스레드 생성자. 포트를 입력하여 생성한다.
	 * 
	 * @param port
	 *          서버 포트 번호
	 */
	public AbstractServerThread(int port) {
		this.port = port;
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
				System.out.println("Client 입장 : " + clientSocket.getInetAddress().getHostName());

				AbstractClientThread clientThread = this.createClientThread(clientSocket);
				clientThread.start();
				this.clientThreadList.add(clientThread);
			}

		} catch (IOException e) {
			System.out.println("ServerThread.run() exception : " + e.getMessage());
		}

		this.stopServer();
	}

	/**
	 * 서버 스레드 시작
	 */
	public void startServer() {
		this.start();
	}

	/**
	 * 서버 스레드 정지. 모든 클라이언트를 정지시킨다.
	 */
	@SuppressWarnings("deprecation")
	public void stopServer() {
		try {
			for (AbstractClientThread client : this.clientThreadList) {
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

	/**
	 * 서버가 가동 중인지를 반환
	 * 
	 * @return true or false
	 */
	public boolean isRunning() {
		return this.serverSocket != null;
	}

	/**
	 * 해당 클라이언트를 리스트로부터 제거한다.
	 * 
	 * @param clientThread
	 *          클라이언트
	 */
	protected void removeClientFromList(AbstractClientThread clientThread) {
		this.clientThreadList.remove(clientThread);
	}

	/**
	 * 클라이언트 스레드 객체를 생성하여 반환. 서버에서 accept 된 소켓을 파라미터로 한다.
	 * 
	 * @param clientSocket
	 *          accept된 소켓
	 * @return 클라이언트 스레드
	 */
	protected abstract AbstractClientThread createClientThread(Socket clientSocket);
}
