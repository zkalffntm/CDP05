package harmony.admin.gui;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollBar;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;

public class MainPage extends JPanel {
	JButton dataButton, mapButton, routeButton;

	private GUI_console gui;
	
	public MainPage() {
		gui = gui.getInstance();
		
		this.setBounds(super.getBounds());
		setLayout(null);

		dataButton = new JButton("\uB85C\uADF8\uC778");
		dataButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.moveDataManagePage();
			}
		});
		dataButton.setBounds(124, 86, 191, 30);
		dataButton.setText("��ǰ ������");
		
		mapButton = new JButton("\uB85C\uADF8\uC778");
		mapButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.moveMapManagePage();
			}
		});
		mapButton.setBounds(124, 181, 191, 30);
		mapButton.setText("���� �ø���");
		
		routeButton = new JButton("\uB85C\uADF8\uC778");
		routeButton.setBounds(124, 276, 191, 30);
		routeButton.setText("��õ��� ����");
		
		add(dataButton);
		add(mapButton);
		add(routeButton);
	}

}
