package harmony.admin.gui;


import javax.swing.JFrame;
import test.PaneTest;

public class GUI_console {

	private int currentRoomNum = 0;

	private int roomCnt; // ���ý� �� ����
	private int workCnt; // ���ù� �� ����
	private int nodeCnt; // ��� �� ����

	private JFrame frame;
	private static GUI_console gui_console = new GUI_console();

	private GUI_console() {
		roomCnt = 0;
	}

	public void setFrame(JFrame f) {
		frame = f;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void roomNumIncrement() {
		roomCnt++;
	}

	public void roomNumDecrement() {
		roomCnt--;
	}

	public void dataNumIncrement() {
		workCnt++;
	}

	public void dataNumdecrement() {
		workCnt--;
	}

	public void nodeNumIncrement() {
		nodeCnt++;
	}

	public void nodeNumdecrement() {
		nodeCnt--;
	}

	public int getDataNum() {
		return workCnt;
	}

	public int getRoomNum() {
		return roomCnt;
	}

	public int getnodeNum() {
		return nodeCnt;
	}

	public void setCurrentRoomNum(int num) {
		currentRoomNum = num;
	}

	public int getCurrentRoomNum() {
		return currentRoomNum;
	}

	// --------------page move-----------//

	public void moveMainPage() {
		MainPage p = new MainPage();
		frame.dispose();
		frame = p;
		frame.setBounds(0, 0, 450, 600);

		frame.setVisible(true);
	}

	public void moveDataManagePage() {
		DataManagePage p = new DataManagePage();
		frame.setBounds(0, 0, 1010, 810);
		p.setSize(frame.getSize());
		frame.dispose();
		frame = p;
		frame.setVisible(true);
	}

	public void moveMapManagePage() {
		MapManagePage p = new MapManagePage();

		frame.setBounds(0, 0, 1010, 810);
		p.setSize(frame.getSize());
		frame.dispose();
		frame = p;
		frame.setVisible(true);
	}

	public void moveRouteManagePage() {
		RouteManagePage p = new RouteManagePage();
		frame.setBounds(0, 0, 1000, 800);
		frame.setContentPane(p);
		frame.setVisible(true);
	}

	public void moveTestPage() {
		PaneTest p = new PaneTest();
		frame.setContentPane(p);
		frame.setVisible(true);
	}

	// ----------------------------------//

	public static GUI_console getInstance() {
		return gui_console;
	}

}
