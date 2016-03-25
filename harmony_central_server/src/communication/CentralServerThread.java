package communication;

import java.net.Socket;

public class CentralServerThread extends AbstractServerThread {

	@Override
	protected AbstractClientThread createClientThread(Socket clientSocket) {
		return new CentralClientThread(this, clientSocket);
	}
}
