package harmony.admin.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ProblemDomain.WorkData;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class SelectDataPage extends JDialog{

	private GridBagConstraints constraints;
	private JPanel btnPanel;
	private JPanel mainPanel;
	private JScrollPane scroller;
	private JButton cancelBtn;
	private JButton okBtn;

	private WorkData workData;
	private ArrayList<SimpleDataPanel> simplePanelList;

	private GUI_console gui;

	private int selectedIdx = -1;


	public SelectDataPage(JFrame frame, String title) {
		super(frame, title, true);

		simplePanelList = new ArrayList<SimpleDataPanel>();

		setSize(500, 800);
		setResizable(false);

		gui = gui.getInstance();

		btnPanel = new JPanel();
		getContentPane().add(btnPanel, BorderLayout.SOUTH);
		btnPanel.setLayout(new BorderLayout(0, 0));

		mainPanel = new JPanel();

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTHWEST;

		GridBagLayout gbl_dataPanel = new GridBagLayout();

		gbl_dataPanel.columnWidths = new int[] { SimpleDataPanel.WIDTH };
		gbl_dataPanel.rowHeights = new int[] { SimpleDataPanel.HEIGHT };
		gbl_dataPanel.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_dataPanel.rowWeights = new double[] { Double.MIN_VALUE };

		mainPanel.setLayout(gbl_dataPanel);

		if(gui.getWorkCnt() > 0)
			for(int i=0; i<gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkCnt(); i++){
				constraints.gridy = i;
				SimpleDataPanel temp = new SimpleDataPanel(gui.getRoomDataList()
						.get(gui.getCurrentRoomNum()-1).getWorkDataList().get(i));

				simplePanelList.add(temp);

				mainPanel.add(temp, constraints);
			}


		scroller = new JScrollPane(mainPanel);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		getContentPane().add(scroller, BorderLayout.CENTER);


		cancelBtn = new JButton("���");
		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectedIdx = -1;
				gui.getMmp().getPopUpPage().setVisible(false);
			}
		});
		btnPanel.add(cancelBtn, BorderLayout.EAST);


		okBtn = new JButton("Ȯ��");
		okBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.getMmp().getPopUpPage().setVisible(false);
			}
		});
		btnPanel.add(okBtn, BorderLayout.CENTER);

		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				selectedIdx = -1;
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				selectedIdx = -1;
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				selectedIdx = -1;
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				selectedIdx = -1;
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
	}


	public void refresh(){
		selectedIdx = -1;

		simplePanelList.clear();
		mainPanel.removeAll();

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTHWEST;

		GridBagLayout gbl_dataPanel = new GridBagLayout();

		gbl_dataPanel.columnWidths = new int[] { SimpleDataPanel.WIDTH };
		gbl_dataPanel.rowHeights = new int[] { SimpleDataPanel.HEIGHT };
		gbl_dataPanel.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_dataPanel.rowWeights = new double[] { Double.MIN_VALUE };

		mainPanel.setLayout(gbl_dataPanel);

		if(gui.getRoomCnt() > 0)
			for(int i=0; i<gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkCnt(); i++){
				constraints.gridy = i;
				WorkData workData = gui.getRoomDataList()
						.get(gui.getCurrentRoomNum()-1).getWorkDataList().get(i);
				SimpleDataPanel temp = new SimpleDataPanel(workData);
				temp.setWorkIdx(i);
				if(!workData.getAssigned()){	// �̹� ������� �Ҵ� �Ǿ������� Ŭ�� �ȴ�.
					temp.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if(selectedIdx > -1 && simplePanelList.get(selectedIdx).isSelected())
							{
								simplePanelList.get(selectedIdx).setSelected(false);
								simplePanelList.get(selectedIdx).validate();
								simplePanelList.get(selectedIdx).repaint();
							}

							selectedIdx = temp.getWorkIdx();
							temp.setSelected(true);
							temp.validate();
							temp.repaint();

							System.out.println(selectedIdx + "���� Ŭ������");
						}
					});
				}
				else
					temp.setSelected(true);
				
				simplePanelList.add(temp);
				mainPanel.add(temp, constraints);
			}
	}

	public int getSelectedIdx() {
		return selectedIdx;
	}

	public void setSelectedIdx(int selectedIdx) {
		this.selectedIdx = selectedIdx;
	}

	public void paintComponents(Graphics g){

	}
}
