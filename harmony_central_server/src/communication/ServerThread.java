package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
	/**
	 * 멤버 변수들
	 */
	private int port = 6500; // 서버 포트번호
	private ServerSocket serverSocket; // 서버 accept용 소켓
	private List<ClientThread> clientThreadList; // 클라이언트 리스트

	/**
	 * 생성자. 각종 멤버변수 초기화.
	 */
	public ServerThread() {

		this.serverSocket = null;
		this.clientThreadList = new ArrayList<ClientThread>();
	}

	/**
	 * 스레드 실행부분. while문에서 클라이언트를 accept함.
	 */
	@Override
	public void run() {

		// 이미 실행 중이면 리턴.
		if (this.serverSocket != null) {
			return;
		}

		try {

			// 서버 소켓 설정
			this.serverSocket = new ServerSocket(this.port);

			// 무한 루프
			while (true) {

				// 클라이언트 하나 accept 함 (블록됨).
				Socket clientSocket = this.serverSocket.accept();
				System.out.println("Client 입장 : " + clientSocket.getInetAddress().getHostName());

				// 한 클라이언트 전용 스레드 실행 및 리스트에 추가
				ClientThread clientThread = new ClientThread(this, clientSocket);
				clientThread.start();
				this.clientThreadList.add(clientThread);
			}

		} catch (IOException e) {
			System.out.println("ServerThread.run()에서 예외 발생 : " + e.getMessage());
		}

		// 서버 종료
		this.stopServer();
	}

	/**
	 * 서버 시작. 단순히 스레드 run부분을 실행함.
	 */
	public void startServer() {

		// run() 메소드 호출.
		this.start();
	}

	/**
	 * 서버 종료. 모든 클라이언트 서비스 스레드 종료 후 서버도 종료.
	 */
	@SuppressWarnings("deprecation")
	public void stopServer() {

		try {

			// 각 클라이언트 스레드를 멈추고 소켓 정리
			for (ClientThread client : this.clientThreadList) {

				client.stop();
				client.closeClient();
			}

			// 클라이언트 리스트 초기화
			this.clientThreadList.clear();

			// 서버 소켓 정리
			if (this.serverSocket != null) {

				this.serverSocket.close();
				this.serverSocket = null;
			}

		} catch (IOException e) {
			System.out.println("ServerThread.stopServer()에서 예외 발생 : " + e.getMessage());
		}
	}

	/**
	 * 한개의 클라이언트를 리스트로부터 제거.
	 * 
	 * @param clientThread
	 */
	public void removeClientFromList(ClientThread clientThread) {
		this.clientThreadList.remove(clientThread);
	}

	/**
	 * 서버 동작여부를 반환.
	 * 
	 * @return isRunning
	 */
	public boolean isRunning() {
		return this.serverSocket != null;
	}
}
