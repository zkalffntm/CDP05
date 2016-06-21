package harmony.admin;

import harmony.admin.communication.AdminServerThread;
import harmony.admin.gui.FramePage;
import harmony.admin.gui.GUI_console;
import harmony.common.AbstractServerThread;

public class GuiDriver {

  /**
   * Launch the application.
   * 
   * @throws InterruptedException
   */
  public static void main(String[] args) throws InterruptedException {
    AbstractServerThread serverThread = new AdminServerThread();
    serverThread.startServer();
    Thread guiThread = new Thread(new Runnable() {
      public void run() {
        try {
          FramePage framePage = new FramePage();
          GUI_console.getInstance().setFrame(framePage);
          framePage.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    guiThread.start();
    guiThread.join();
  }
}
