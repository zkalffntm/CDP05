package harmony.admin;

import harmony.admin.communication.AdminServerThread;
import harmony.admin.database.DbConnector;
import harmony.common.AbstractServerThread;

/**
 * 관리자 서버를 돌리기 위한 메인 메소드가 있는 CUI 버전클래스.
 * 
 * @author Seongjun Park
 * @since 2016/4/5
 * @version 2016/4/5
 */
public class CuiDriver {

  /**
   * 메인 메소드.
   * 
   * @param args
   *          사용 안 함.
   * @throws InterruptedException
   *           스레드 인터럽트 예외
   */
  public static void main(String[] args) throws InterruptedException {
    AbstractServerThread serverThread = new AdminServerThread();
    serverThread.startServer();
    serverThread.join();
    DbConnector.getInstance().closeConnection();
  }
}
