package harmony.admin.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class DataManagePage extends ManagePage {

	private ImageUploadPage popUpPage = new ImageUploadPage(this, "��ǰ ���� ����");

	private ArrayList<DataManagePanel> dataManagePanelList;

	private JPanel panel;
	private JButton saveBtn;

	public DataManagePage() {
		super();
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dataManagePanelList = new ArrayList<DataManagePanel>();


		getContentPane().setLayout(new BorderLayout());

		add(this.tabPanel, BorderLayout.NORTH);
		add(this.scroller, BorderLayout.CENTER);

		panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 214, 113, 517, 107, 0 };
		gbl_panel.rowHeights = new int[] { 27, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		saveBtn = new JButton("����");
		GridBagConstraints gbc_addMapBtn = new GridBagConstraints();
		gbc_addMapBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_addMapBtn.gridx = 3;
		gbc_addMapBtn.gridy = 0;
		panel.add(saveBtn, gbc_addMapBtn);
		saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveBtn.setBounds(100, 1, 50, 50);

	}

	/*
	 * �� ���ð��� ��ǰ ���� ���� ȭ��
	 */
	private class DataManagePanel extends JPanel {

		private GridBagConstraints constraints;
		private int panelNum;
		private int dataNum = 0;
		private JButton addDataBtn;

		private ArrayList<DataPanel> dataPanelList; // �� �����忡 �ִ� ���ù� ������ ����Ʈ

		public DataManagePanel() {
			constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.NORTHWEST;

			panelNum = gui.getRoomNum();

			GridBagLayout gbl_dataPanel = new GridBagLayout();

			gbl_dataPanel.columnWidths = new int[] { 0 };
			gbl_dataPanel.rowHeights = new int[] { DataPanel.HEIGHT };
			gbl_dataPanel.columnWeights = new double[] { Double.MIN_VALUE };
			gbl_dataPanel.rowWeights = new double[] { Double.MIN_VALUE };

			setLayout(gbl_dataPanel);

			addDataBtn = new JButton();
			addDataBtn.setSize(50, 50);
			addDataBtn.setText("+");

			dataPanelList = new ArrayList<DataPanel>();

			addDataBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					DataPanel dataPanel = new DataPanel();
					dataPanelList.add(dataPanel);
					dataNum++;

					constraints.gridy = dataNum;

					add(dataPanel, constraints);

					if (dataPanel.getHeight() * dataNum + addDataBtn.getHeight() > getHeight())
						setSize(980, 740 + dataPanel.getHeight());

					refreshView();
					System.out.println("���� �г� ���� : " + getHeight());
					System.out.println("�� ������ �г� ���� �� : " + dataNum * dataPanel.getHeight());

				}
			});

			setBackground(Color.darkGray);
			setAutoscrolls(true);

			add(addDataBtn, constraints);
		}


		/*
		 * �� ���ù� ���� ���� �г� Ŭ����
		 */
		public class DataPanel extends JPanel {
			
			private Image mainImage = null;
			private JLabel image;
			private JTextField title, artist, simpleContents, contents;
			private JButton editBtn;
			private JButton delBtn;

			public DataPanel() {
				setMinimumSize(new Dimension(980, 200));
				setSize(new Dimension(980, 200));

				image = new JLabel("���� ����"){
					public void paintComponent(Graphics g){
						if(mainImage != null)
							g.drawImage(mainImage, 0, 0, getWidth(), getHeight(), null);
						else
							super.paintComponent(g);
					}
				};
				image.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						System.out.println("�̹��� ��ư Ŭ��");
						Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
						popUpPage.setLocation((d.width - popUpPage.getWidth())/2, (d.height- popUpPage.getHeight())/2);
						popUpPage.setVisible(true);

						System.out.println("�̹��� ���ε� â ����");
						
						if(mainImage == null){
							mainImage = popUpPage.getImage();
							image.validate();
							image.repaint();
						}
					}
				});
				setLayout(new MigLayout("",
						"[115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill]",
						"[43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill]"));
				image.setVerticalAlignment(SwingConstants.CENTER);
				image.setHorizontalAlignment(SwingConstants.CENTER);
				image.setBackground(Color.WHITE);
				image.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(image, "cell 0 0 1 4,alignx center,aligny center");

				title = new JTextField("\uC791\uD488\uBA85");
				add(title, "cell 1 0 1 2,alignx center,aligny center");

				delBtn = new JButton("x");
				add(delBtn, "cell 7 0,alignx left,aligny center");

				simpleContents = new JTextField("\uAC04\uB2E8\uD55C \uC124\uBA85");
				simpleContents.setColumns(1);
				add(simpleContents, "cell 2 0 2 4,alignx center,aligny center");

				/*
				 * ��ǰ ������ ����
				 */

				editBtn = new JButton("\uC800\uC7A5");
				editBtn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
					}
				});
				add(editBtn, "cell 7 1 1 3,alignx left,aligny center");

				contents = new JTextField("\uC790\uC138\uD55C \uC124\uBA85");
				contents.setAutoscrolls(true);
				add(contents, "cell 4 0 3 4,alignx center,aligny center");

				artist = new JTextField("\uC791\uAC00");
				add(artist, "cell 1 2 1 2,alignx center,aligny center");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GUI.ManagePage#setView(int)
	 */

	@Override
	public void setView(int num) {
		// TODO Auto-generated method stub
		gui.setCurrentRoomNum(num);

		remove(scroller);

		scroller = new JScrollPane(dataManagePanelList.get(num - 1));
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setBounds(2, 42, 980, 740);

		add(this.scroller, BorderLayout.CENTER);
	}

	@Override
	public void addPane() {
		// TODO Auto-generated method stub
		DataManagePanel dataManagePanel = new DataManagePanel();
		dataManagePanelList.add(dataManagePanel);
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		this.validate();
		this.repaint();

		// System.out.println("������ �г� ���� : " + dataPanelList.size());
	}

	@Override
	public void removeRoom(int btnNum) {
		// TODO Auto-generated method stub

		gui.roomNumDecrement();
		tabPanel.remove(roomBtnList.get(btnNum - 1));
		scroller.remove(dataManagePanelList.get(btnNum - 1));
		dataManagePanelList.remove(btnNum - 1);
		roomBtnList.remove(btnNum - 1);

		for(int i=btnNum; i<roomBtnList.size(); i++){
			roomBtnList.get(i).setRoomNum(i-1);
		}

		refreshView();
	}
}
