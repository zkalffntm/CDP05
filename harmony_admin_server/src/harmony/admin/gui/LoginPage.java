package harmony.admin.gui;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

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
				
				// ������ ���
				/*
				 * 
				 */
				
				gui.moveMainPage();
			}
		});
		loginButton.setBounds(68, 305, 191, 30);
		
		add(editID);
		add(editPW);
		add(loginButton);
	}
}
