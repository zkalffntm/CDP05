package harmony.admin;

import java.awt.EventQueue;

import harmony.admin.gui.FramePage;
import harmony.admin.gui.GUI_console;

public class GuiDriver {

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
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
  }
}
