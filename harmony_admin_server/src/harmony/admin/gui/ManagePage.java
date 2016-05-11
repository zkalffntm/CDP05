package harmony.admin.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/*
 * 
 * 
 */

public abstract class ManagePage extends JFrame {
	protected GUI_console gui;

	protected JButton addTabBtn; // �� �߰� ��ư
	protected ArrayList<RoomButton> roomBtnList; // �� ��ư ����Ʈ

	// protect�� �ٲٱ�
	protected JPanel tabPanel, mainPanel;
	protected JScrollPane scroller;

	protected JLayeredPane layer;

	/*
	 * 
	 */

	public ManagePage() {

		gui = gui.getInstance();

		layer = getLayeredPane();

		setSize(1000, 800);
		setLayout(null);

		roomBtnList = new ArrayList<RoomButton>();

		mainPanel = new JPanel();

		scroller = new JScrollPane(mainPanel);

		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		addTabBtn = new JButton();

		/*
		 * ���� �ø��� �̺�Ʈ ��ư�� ������ â�� �����Ǽ� ����Ʈ�� ����
		 */
		addTabBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String str = null;
				str = JOptionPane.showInputDialog("���ý� �̸�");

				// ��� ��������
				if (str == "" || str == null)
					return;

				gui.roomNumIncrement();
				RoomButton roomBtn = new RoomButton(gui.getRoomNum(), str);

				roomBtnList.add(roomBtn);
				tabPanel.add(roomBtn);

				addPane();
				refreshView();

				System.out.println("��ư�� ���Ƚ��ϴ�. roomNum : " + gui.getRoomNum());
			}
		});

		addTabBtn.setText("+");
		addTabBtn.setSize(41, 26);

		tabPanel = new JPanel();
		tabPanel.setBounds(2, 2, 980, 40);
		tabPanel.setVisible(true);

		tabPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		tabPanel.add(addTabBtn);

		layer.add(tabPanel, new Integer(300));
		layer.add(scroller, new Integer(300));
	}

	/*
	 * 
	 * 
	 */

	public RoomButton getRoomButton(int roomNum) {
		return roomBtnList.get(roomNum);
	}

	/*
	 * abstract method
	 */
	public abstract void setView(int num); // �� ��ư �������� ȭ�� ��ȯ

	public abstract void addPane(); // �� �߰� ���� �� ȭ�� ����

	public abstract void refreshView(); // ȭ�� ���� �׸���(���� ��)

	public abstract void removeRoom(int btnNum); // ���ý� ����

	/*
	 * �� ��ư
	 */
	protected class RoomButton extends JPanel {
		int num;

		JButton delBtn;
		JLabel title;

		public RoomButton(int num, String name) {
			this.num = num;
			title = new JLabel(name);

			setLayout(new FlowLayout());
			setSize(100, 50);

			delBtn = new JButton();
			delBtn.setText("x");
			delBtn.setSize(20, 20);

			setBackground(Color.lightGray);

			/*
			 * ���ý� ����
			 */
			delBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					removeRoom(num);
				}
			});

			/*
			 * ���ý� �� ������ ��
			 */
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					System.out.println(num + " �� �� Ŭ������!");
					setView(num);
					refreshView();
				}
			});

			add(title);
			add(delBtn);
		}
		
		public String getName() {
			return title.getText();
		}

		public int getRoomNum() {
			return num;
		}
		
		public void setRoomNum(int num){
			this.num = num;
		}
	}
}
