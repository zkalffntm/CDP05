package harmony.admin.communication;

import java.net.Socket;

/**
 * 관리자 서버의 스레드.
 * 
 * @author Seongjun Park
 * @since 2016/4/1
 * @version 2016/4/4
 */
public class AdminServerThread extends AbstractServerThread {
	public static final int DEFAULT_ADMIN_SERVER_PORT = 6500;

	/**
	 * 관리자 서버 스레드 생성자. 기본 포트로 생성한다.
	 */
	public AdminServerThread() {
		this(DEFAULT_ADMIN_SERVER_PORT);
	}

	/**
	 * 관리자 서버 스레드 생성자. 포트를 입력하여 생성한다.
	 * 
	 * @param port
	 *          서버 포트 번호
	 */
	public AdminServerThread(int port) {
		super(port);
	}

	@Override
	protected AbstractClientThread createClientThread(Socket clientSocket) {
		return new AdminClientThread(this, clientSocket);
	}
}
