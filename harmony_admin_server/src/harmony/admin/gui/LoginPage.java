package harmony.admin.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import harmony.admin.communication.ConnectionToCentral;

public class LoginPage extends JPanel {

  private JTextPane editID, editPW;
  private JButton loginButton;

  private GUI_console gui;

  public LoginPage() {
    gui = GUI_console.getInstance();

    setLayout(null);

    // component
    editID = new JTextPane();
    editID.setBounds(68, 233, 191, 33);

    editPW = new JTextPane();
    editPW.setBounds(68, 268, 191, 30);

    loginButton = new JButton("\uB85C\uADF8\uC778");
    loginButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        String id, pw;
        id = editID.getText();
        pw = editPW.getText();

        // 로그인 시도
        boolean result = false;
        try {
          result = new ConnectionToCentral().doLogin(id, pw);
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null,
              "로그인 중 문제가 발생하였습니다. :" + e.getMessage(), "로그인",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        // 로그인 결과에 따라 메인 페이지로 넘어감
        if (result) {
          gui.moveMainPage();
        } else {
          JOptionPane.showMessageDialog(null, "로그인 실패", "로그인",
              JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });
    loginButton.setBounds(68, 305, 191, 30);

    add(editID);
    add(editPW);
    add(loginButton);
  }

}
