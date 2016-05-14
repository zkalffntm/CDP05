package harmony.admin;

import java.awt.EventQueue;

import harmony.admin.gui.FramePage;

public class GuiDriver {

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          FramePage framePage = new FramePage();
          framePage.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
