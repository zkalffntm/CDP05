package harmony.central.communication;

import java.net.Socket;

/**
 * 중앙 서버의 스레드.
 * 
 * @author Seongjun Park
 * @since 2016/3/25
 * @version 2016/4/4
 */
public class CentralServerThread extends AbstractServerThread {
	public static final int DEFAULT_CENTRAL_PORT = 6000;

	/**
	 * 중앙 서버 스레드 생성자. 기본 포트로 생성한다.
	 */
	public CentralServerThread() {
		this(DEFAULT_CENTRAL_PORT);
	}

	/**
	 * 중앙 서버 스레드 생성자. 포트를 입력하여 생성한다.
	 * 
	 * @param port
	 *          서버 포트 번호
	 */
	public CentralServerThread(int port) {
		super(port);
	}

	@Override
	protected AbstractClientThread createClientThread(Socket clientSocket) {
		return new CentralClientThread(this, clientSocket);
	}
}
