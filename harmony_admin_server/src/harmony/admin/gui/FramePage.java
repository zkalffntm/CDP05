package harmony.admin.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FramePage extends JFrame{
	private JPanel contentPane;

	public FramePage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 600);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		set();
	}

	public void set() {
		LoginPage p = new LoginPage();
		setContentPane(p);
		setVisible(true);
	}
}
