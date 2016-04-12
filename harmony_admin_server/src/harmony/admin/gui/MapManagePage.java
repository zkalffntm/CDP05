package harmony.admin.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class MapManagePage extends ManagePage{

	private JToolBar tb;
	private JButton addMapBtn, addNodeBtn, addWorkBtn;
	
	private ArrayList<MapPanel> mapPanelList;
	
	public MapManagePage(){
		
		scroller.setBounds(60, 42, 980, 740);
		
		tb = new JToolBar("Toolbar tb", SwingConstants.VERTICAL);
		
		addMapBtn = new JButton("���� �ø���");
		addMapBtn.setBounds(100, 1, 50, 50);
		addNodeBtn = new JButton("��� �߰�");
		addNodeBtn.setBounds(100, 1, 50, 50);
		addWorkBtn = new JButton("���ù� �߰�");
		addWorkBtn.setBounds(100, 1, 50, 50);
		addWorkBtn.setHorizontalAlignment(SwingConstants.LEFT);

		tb.add(addMapBtn);
		tb.add(addNodeBtn);
		tb.add(addWorkBtn);
		
		setLayout(new BorderLayout());
		add(tb, BorderLayout.WEST);
		
		add(this.tabPanel, BorderLayout.NORTH);
		add(this.dataPanel, BorderLayout.CENTER);
	}
	
	/*
	 * 
	 */
	private class MapPanel extends JScrollPane{
		
		private int workCnt, nodeCnt;
		
		private ArrayList<JButton> workList, nodeList;
		
		public MapPanel(){
			workCnt = 0;
			nodeCnt = 0;
			
			workList = new ArrayList<JButton>();
			nodeList = new ArrayList<JButton>();
		}
	}
	
	/*
	 * 
	 */
	
	private class WorkBtn extends JButton{
		private int workNum;
	}
	
	private class NodeBtn extends JButton{
		private int nodeNum;
	}
	/*
	 * (non-Javadoc)
	 * @see GUI.ManagePage#setView(int)
	 */
	@Override
	public void setView(int num) {
		// TODO Auto-generated method stub
		gui.setCurrentRoomNum(num);
		remove(scroller);
		scroller = new JScrollPane(mapPanelList.get(num-1));
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setBounds(2, 42, 980, 740);
		
		add(scroller);
	}

	@Override
	public void addPane() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRoom(int btnNum) {
		// TODO Auto-generated method stub
		
	}


}
