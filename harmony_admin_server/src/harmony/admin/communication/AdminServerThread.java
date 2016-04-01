package harmony.admin.communication;

import java.net.Socket;

/**
 * 관리자 서버의 스레드.
 * 
 * @author Seongjun Park
 * @since 2016/4/1
 * @version 2016/4/1
 */
public class AdminServerThread extends AbstractServerThread {

	@Override
	protected AbstractClientThread createClientThread(Socket clientSocket) {
		return new AdminClientThread(this, clientSocket);
	}
}
