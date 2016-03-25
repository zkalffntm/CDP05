import communication.AbstractServerThread;
import communication.CentralServerThread;
import database.DbConnector;

/**
 * 중앙 서버를 돌리기 위한 메인 메소드가 있는 클래스.
 * 
 * @author Seongjun Park
 * @since 2016/3/21
 * @version 2016/3/21
 */
public class Driver {
	public static void main(String[] args) throws InterruptedException {
		AbstractServerThread serverThread = new CentralServerThread();
		serverThread.startServer();
		serverThread.join();
		DbConnector.getInstance().closeConnection();
	}
}
