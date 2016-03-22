import communication.ServerThread;
import database.DbConnector;

/**
 * @author Seongjun Park
 * @since 2016/3/21
 * @version 2016/3/21
 */
public class Driver {
	public static void main(String[] args) throws InterruptedException {
		ServerThread serverThread = new ServerThread();
		serverThread.startServer();
		serverThread.join();
		DbConnector.getInstance().closeConnection();
	}
}
