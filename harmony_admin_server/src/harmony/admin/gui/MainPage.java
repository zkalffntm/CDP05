package harmony.admin.gui;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollBar;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MainPage extends JFrame {
	JButton dataButton, mapButton, routeButton;

	private GUI_console gui;
	private ImageIcon icon;

	public MainPage() {

		gui = gui.getInstance();

		this.setBounds(super.getBounds());

		dataButton = new JButton(){
			public void paintComponent(Graphics g) {
				ImageIcon img = new ImageIcon("C:/Users/���/GUI/src/btn_data.png");
				g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};;
		dataButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.moveDataManagePage();
			}
		});
		dataButton.setText("��ǰ ������");

		mapButton = new JButton(){
			public void paintComponent(Graphics g) {
				ImageIcon img = new ImageIcon("C:/Users/���/GUI/src/btn_map.png");
				g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		mapButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.moveMapManagePage();
			}
		});
		mapButton.setText("���� �ø���");

		routeButton = new JButton(){
			public void paintComponent(Graphics g) {
				ImageIcon img = new ImageIcon("C:/Users/���/GUI/src/btn_route.png");
				g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		routeButton.setText("��õ��� ����");
		
		JLabel mainIcon = new JLabel("\uB85C\uACE0"){
			public void paintComponent(Graphics g) {
				ImageIcon img = new ImageIcon("C:/Users/���/GUI/src/main_icon.png");
				g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(129)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(mapButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
						.addComponent(routeButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(dataButton, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(126))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(169)
					.addComponent(mainIcon, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
					.addGap(163))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(57)
					.addComponent(mainIcon, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
					.addGap(44)
					.addComponent(dataButton, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addGap(44)
					.addComponent(mapButton, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addGap(48)
					.addComponent(routeButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addGap(105))
		);
		getContentPane().setLayout(groupLayout);
	}
}
