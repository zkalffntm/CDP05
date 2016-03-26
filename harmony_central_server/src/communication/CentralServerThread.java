package communication;

import java.net.Socket;

/**
 * 중앙 서버의 스레드.
 * 
 * @author Seonjun Park
 * @since 2016/3/25
 * @version 2016/3/25
 */
public class CentralServerThread extends AbstractServerThread {

	@Override
	protected AbstractClientThread createClientThread(Socket clientSocket) {
		return new CentralClientThread(this, clientSocket);
	}
}
