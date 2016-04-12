package harmony.admin.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.CardLayout;

/*
 * 
 * 
 */

public abstract class ManagePage extends JPanel{
	protected GUI_console gui;

	protected JButton addTabBtn;					// �� �߰� ��ư
	protected ArrayList<RoomButton> roomBtnList;	// �� ��ư ����Ʈ

	// protect�� �ٲٱ�
	protected JPanel tabPanel, dataPanel;
	protected JScrollPane scroller; 
	
	/*
	 * 
	 */
	
	public ManagePage() {
		gui = gui.getInstance();
		
		setSize(1000, 800);
		setLayout(null);
		
		roomBtnList = new ArrayList<RoomButton>();

		dataPanel = new JPanel();
		
		scroller = new JScrollPane(dataPanel);
		
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		addTabBtn = new JButton();

		/*
		 * ���� �ø��� �̺�Ʈ 
		 * ��ư�� ������ â�� �����Ǽ� ����Ʈ�� ����
		 */
		addTabBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String str = null;
				str = JOptionPane.showInputDialog("���ý� �̸�");
				
				gui.roomNumIncrement();
				RoomButton roomBtn = new RoomButton(gui.getRoomNum(), str);

				roomBtnList.add(roomBtn);
				tabPanel.add(roomBtn);

				addPane();
				refreshView();

				System.out.println("��ư�� ���Ƚ��ϴ�. roomNum : " +gui.getRoomNum());
				
			}
		});

		addTabBtn.setText("+");
		addTabBtn.setSize(41, 26);

		tabPanel = new JPanel();
		tabPanel.setBounds(2, 2, 980, 40);
		tabPanel.setVisible(true);

		tabPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		tabPanel.add(addTabBtn);

		add(tabPanel);
		add(scroller);
	}

	/*
	 * 
	 * 
	 */

	public RoomButton getRoomButton(int roomNum){
		return roomBtnList.get(roomNum);
	}


	/*
	 * abstract method
	 */
	public abstract void setView(int num);	// �� ��ư �������� ȭ�� ��ȯ

	public abstract void addPane();			// �� �߰� ���� �� ȭ�� ����

	public abstract void refreshView();		// ȭ�� ���� �׸���(���� ��)

	public abstract void removeRoom(int btnNum);	// ���ý� ����
	
	/*
	 * �� ��ư
	 */
	protected class RoomButton extends JPanel{
		int num;

		JButton delBtn;
		JLabel title;
		
		public RoomButton(int num, String name){
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
					System.out.println("�� Ŭ������!");
					setView(num);
					refreshView();
				}
			});
			
			add(title);
			add(delBtn);
		}

		public String getName(){
			return title.getText();
		}

		public int getRoomNum(){
			return num;
		}
	}
}

