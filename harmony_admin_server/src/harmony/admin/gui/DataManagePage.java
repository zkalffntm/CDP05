package harmony.admin.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import harmony.admin.controller.AreaController;
import harmony.admin.gui.probremdomain.RoomData;
import harmony.admin.gui.probremdomain.WorkData;
import harmony.admin.model.Area;
import net.miginfocom.swing.MigLayout;

public class DataManagePage extends ManagePage {

	private ImageUploadPage popUpPage = new ImageUploadPage(this, "��ǰ ���� ����");
	private JPanel panel;
	private JButton saveBtn;
	private ImageIcon saveBtnImg;

	private ArrayList<DataManagePanel> dataManagePanelList;

	public DataManagePage() {
		super();

		saveBtnImg = gui.getSaveImg();

		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dataManagePanelList = new ArrayList<DataManagePanel>();

		getContentPane().setLayout(new BorderLayout());

		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		saveBtn = new JButton("�� ��");
		saveBtn.setFont(new Font("����", Font.BOLD, 20));
		saveBtn.setText("����");
		saveBtn.setForeground(Color.white);
		saveBtn.setBackground(new Color(107, 57, 49));

		panel.add(saveBtn, BorderLayout.CENTER);
		panel.add(backBtn, BorderLayout.EAST);

		saveBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ������ ����

				for(int i=0; i<dataManagePanelList.size(); i++){

					DataManagePanel dataManagePanel = dataManagePanelList.get(i);
					RoomData roomData = gui.getRoomDataList().get(i);

					for(int j=0; j<dataManagePanel.dataPanelList.size(); j++){
						DataManagePage.DataManagePanel.DataPanel dataPanel = dataManagePanel.dataPanelList.get(j);

						roomData.getWorkDataList().get(j).setWorkData(dataPanel.imageList, dataPanel.imageScr, dataPanel.title.getText(), dataPanel.artist.getText(),
								dataPanel.simpleContents.getText(), dataPanel.contents.getText(), i, gui.getDataCnt());
					}
				}

				for(int i=0; i<gui.getRoomDataList().size(); i++){
					for(int j=0; j<gui.getRoomDataList().get(i).getWorkDataList().size(); j++){
						System.out.println(i + "�� ���ð��� " + j + "��° ���ù� : " + gui.getRoomDataList().get(i).getWorkDataList().get(j).getTitle());
					}
				}
				
				
				
				/*
				 * ������ ������ ����
				 */
			//	gui.save();
			}

			public void mousePressed(MouseEvent arg0){
				saveBtnImg = gui.getSaveImg_s();
				validate();
				repaint();
			}

			public void mouseReleased(MouseEvent arg0){
				saveBtnImg = gui.getSaveImg();
				validate();
				repaint();
			}
		});
		//	saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

		add(tabPanel, BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		
		System.out.println("���ý� �� : " + gui.getRoomCnt());
		
		for(int i=0; i<gui.getRoomCnt(); i++){
			RoomButton roomBtn = new RoomButton(i+1, gui.getRoomDataList().get(i).getName());
			tabPanel.add(roomBtn);
			roomBtnList.add(roomBtn);
			DataManagePanel temp = new DataManagePanel(gui.getRoomDataList().get(i));
			dataManagePanelList.add(temp);
			System.out.println("�ʱ�ȭ��..");
		}
		
	}

	/*
	 * �� ���ð��� ��ǰ ���� ���� ȭ��
	 */
	private class DataManagePanel extends JPanel {

		private GridBagConstraints constraints;
		private JButton addDataBtn;
		private RoomData roomData = null;

		private ImageIcon addWorkBtnImg;
		/*
		 * ���� ������ ����Ʈ���� ���� �����;ߴ�
		 */
		private ArrayList<DataPanel> dataPanelList; // �� �����忡 �ִ� ���ù� ������ ����Ʈ

		public DataManagePanel(RoomData roomData) {

			this.roomData = roomData;
			addWorkBtnImg = gui.getAddWorkImg();

			constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.NORTHWEST;

			GridBagLayout gbl_dataPanel = new GridBagLayout();

			gbl_dataPanel.columnWidths = new int[] { 0 };
			gbl_dataPanel.rowHeights = new int[] { DataPanel.HEIGHT };
			gbl_dataPanel.columnWeights = new double[] { Double.MIN_VALUE };
			gbl_dataPanel.rowWeights = new double[] { Double.MIN_VALUE };

			setLayout(gbl_dataPanel);

			addDataBtn = new JButton(){
				public void paintComponent(Graphics g){
					g.drawImage(addWorkBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
					setOpaque(false);
				}
			};
			addDataBtn.setBorderPainted(false);		// �׵θ�
			addDataBtn.setFocusPainted(false);		
			addDataBtn.setOpaque(false);
			addDataBtn.setSize(50, 50);
			addDataBtn.setText("+");

			dataPanelList = new ArrayList<DataPanel>();

			/*
			 *  ��ǰ ������ �߰� ��ư �̺�Ʈ
			 */
			addDataBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					DataPanel dataPanel = new DataPanel();
					dataPanelList.add(dataPanel);
					constraints.gridy = dataPanelList.size();

					WorkData workData = new WorkData();
					workData.setRealNum(gui.realWorkNumIncrement());
					workData.setRoomNum(gui.getCurrentRoomNum());

					gui.getWorkHashMap().put(workData.getRealNum(), workData);
					gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().add(workData);

					add(dataPanel, constraints);

					if (dataPanel.getHeight() * dataPanelList.size() + addDataBtn.getHeight() > getHeight())
						setSize(980, 740 + dataPanel.getHeight());

					refreshView();
					System.out.println("���� �г� ���� : " + getHeight());
					System.out.println("�� ������ �г� ���� �� : " + dataPanelList.size() * dataPanel.getHeight());
				}

				public void mousePressed(MouseEvent arg0){
					addWorkBtnImg = gui.getAddWorkImg_s();
					validate();
					repaint();
				}

				public void mouseReleased(MouseEvent arg0){
					addWorkBtnImg = gui.getAddWorkImg();
					validate();
					repaint();
				}
			});

			setBackground(Color.WHITE);
			setAutoscrolls(true);

			add(addDataBtn, constraints);

			// ����� roomData���� �ڷ� �ҷ�����. 
			if(roomData != null && roomData.getWorkCnt() > 0){
				for(int i=0; i<roomData.getWorkCnt(); i++){
					DataPanel dataPanel = new DataPanel(roomData.getWorkDataList().get(i));
					dataPanelList.add(dataPanel);
					constraints.gridy = i+1;
					dataPanel.title.setText(roomData.getWorkDataList().get(i).getTitle());
					dataPanel.artist.setText(roomData.getWorkDataList().get(i).getArtist());
					dataPanel.simpleContents.setText(roomData.getWorkDataList().get(i).getSimpleContents());
					dataPanel.contents.setText(roomData.getWorkDataList().get(i).getContents());
					add(dataPanel, constraints);
				}
			}
		}

		public RoomData getRoomData() {
			return roomData;
		}


		public void setRoomData(RoomData roomData) {
			this.roomData = roomData;
		}

		/*
		 * �� ���ù� ���� ���� �г� Ŭ����
		 */
		public class DataPanel extends JPanel {

			private int workNum;
			private int roomNum;

			private Image mainImage = null;
			private ArrayList<Image> imageList;
			private ArrayList<String> imageScr;

			private JLabel image;
			private JTextField title, artist, simpleContents, contents;
			private JButton delBtn;

			private ImageIcon delBtnImg;

			public DataPanel(WorkData workData){
				
				workNum = workData.getWorkNum();
				roomNum = workData.getRoomNum();
				
				if(workData.getImage().size() > 0){
					mainImage = workData.getImage().get(0);
					imageList = workData.getImage();
					imageScr = workData.getImageScr();
				}
				else {
					mainImage = null;
					imageList = new ArrayList<Image>();
					imageScr = new ArrayList<String>();
				}
				

				delBtnImg = gui.getxImg();

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
					public void mouseClicked(MouseEvent arg0) {

						Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

						popUpPage.setLocation((d.width - popUpPage.getWidth())/2, (d.height- popUpPage.getHeight())/2);
						popUpPage.refresh(imageList);
						popUpPage.setVisible(true);

						if(popUpPage.getImage() != null)
						{
							mainImage = popUpPage.getImage();

							imageList = (ArrayList<Image>) popUpPage.getImageList().clone();
							imageScr = (ArrayList<String>) popUpPage.getImageScr().clone();

							System.out.println("�гο� ��ϵ� �̹��� �� : " + imageList.size());
							validate();
							repaint();
						}
					}
				});

				setLayout(new MigLayout("", "[115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][30:30,grow,fill]", "[43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill]"));
				image.setVerticalAlignment(SwingConstants.CENTER);
				image.setHorizontalAlignment(SwingConstants.CENTER);
				image.setBackground(Color.WHITE);
				image.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(image, "cell 0 0 1 4,alignx center,aligny center");

				title = new JTextField("\uC791\uD488\uBA85");
				add(title, "cell 1 0 1 2,alignx center,aligny center");

				delBtn = new JButton(){
					public void paintComponent(Graphics g){
						g.drawImage(delBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
						setOpaque(false);
					}
				};

				delBtn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {

						if(gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().get(workNum-1).getAssigned()){
							
						}
						
						for(int i=workNum; i<gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().size(); i++){
							gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().get(i).setWorkNum(i);
							dataManagePanelList.get(gui.getCurrentRoomNum()-1).dataPanelList.get(i).workNum = i;
						}

						dataManagePanelList.get(gui.getCurrentRoomNum()-1).remove(dataPanelList.get(workNum-1));
						gui.getWorkHashMap().remove(gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().get(workNum-1));
						gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().remove(workNum-1);

						refreshView();
					}


					public void mousePressed(MouseEvent arg0){
						delBtnImg = gui.getxImg_s();
						validate();
						repaint();
					}

					public void mouseReleased(MouseEvent arg0){
						delBtnImg = gui.getxImg();
						validate();
						repaint();
					}
				});
				add(delBtn, "cell 7 0,alignx left,aligny center");

				simpleContents = new JTextField("\uAC04\uB2E8\uD55C \uC124\uBA85");
				simpleContents.setColumns(1);
				add(simpleContents, "cell 2 0 2 4,alignx center,aligny center");

				contents = new JTextField("\uC790\uC138\uD55C \uC124\uBA85");
				contents.setAutoscrolls(true);
				add(contents, "cell 4 0 3 4,alignx center,aligny center");

				artist = new JTextField("\uC791\uAC00");
				add(artist, "cell 1 2 1 2,alignx center,aligny center");
			}
			
			public DataPanel() {

				delBtnImg = gui.getxImg();

				gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).workCntIncrement();
				workNum = gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkCnt();
				roomNum = gui.getCurrentRoomNum();

				setMinimumSize(new Dimension(980, 200));
				setSize(new Dimension(980, 200));

				imageList = new ArrayList<Image>();

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
					public void mouseClicked(MouseEvent arg0) {

						Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

						popUpPage.setLocation((d.width - popUpPage.getWidth())/2, (d.height- popUpPage.getHeight())/2);
						popUpPage.refresh(imageList);
						popUpPage.setVisible(true);

						if(popUpPage.getImage() != null)
						{
							mainImage = popUpPage.getImage();

							imageList = (ArrayList<Image>) popUpPage.getImageList().clone();
							imageScr = (ArrayList<String>) popUpPage.getImageScr().clone();

							System.out.println("�гο� ��ϵ� �̹��� �� : " + imageList.size());
							validate();
							repaint();
						}
					}
				});

				setLayout(new MigLayout("", "[115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][30:30,grow,fill]", "[43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill]"));
				image.setVerticalAlignment(SwingConstants.CENTER);
				image.setHorizontalAlignment(SwingConstants.CENTER);
				image.setBackground(Color.WHITE);
				image.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(image, "cell 0 0 1 4,alignx center,aligny center");

				title = new JTextField("\uC791\uD488\uBA85");
				add(title, "cell 1 0 1 2,alignx center,aligny center");

				delBtn = new JButton(){
					public void paintComponent(Graphics g){
						g.drawImage(delBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
						setOpaque(false);
					}
				};

				delBtn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {

						if(gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().get(workNum-1).getAssigned()){
							
						}
						
						for(int i=workNum; i<gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().size(); i++){
							gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().get(i).setWorkNum(i);
							dataManagePanelList.get(gui.getCurrentRoomNum()-1).dataPanelList.get(i).workNum = i;
						}

						dataManagePanelList.get(gui.getCurrentRoomNum()-1).remove(dataPanelList.get(workNum-1));
						gui.getWorkHashMap().remove(gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().get(workNum-1));
						gui.getRoomDataList().get(gui.getCurrentRoomNum()-1).getWorkDataList().remove(workNum-1);

						refreshView();
					}


					public void mousePressed(MouseEvent arg0){
						delBtnImg = gui.getxImg_s();
						validate();
						repaint();
					}

					public void mouseReleased(MouseEvent arg0){
						delBtnImg = gui.getxImg();
						validate();
						repaint();
					}
				});
				add(delBtn, "cell 7 0,alignx left,aligny center");

				simpleContents = new JTextField("\uAC04\uB2E8\uD55C \uC124\uBA85");
				simpleContents.setColumns(1);
				add(simpleContents, "cell 2 0 2 4,alignx center,aligny center");

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
		System.out.println(num + " �� ���ý� Ŭ����");

		remove(scroller);

		scroller = new JScrollPane(dataManagePanelList.get(num - 1));
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setBounds(2, 42, 980, 740);

		add(this.scroller, BorderLayout.CENTER);
	}

	@Override
	public void addPane(RoomData roomData) {
		// TODO Auto-generated method stub
		DataManagePanel dataManagePanel = new DataManagePanel(roomData);
		dataManagePanelList.add(dataManagePanel);
		
		if(gui.getMmp() != null)
			gui.getMmp().addPane2(roomData);
	}
	
	public void addPane2(RoomData roomData) {
		// TODO Auto-generated method stub
		RoomButton roomBtn = new RoomButton(roomData.getRoomNum(), roomData.getName());
		tabPanel.add(roomBtn);
		roomBtnList.add(roomBtn);
		
		DataManagePanel dataManagePanel = new DataManagePanel(roomData);
		dataManagePanelList.add(dataManagePanel);
	}
	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		gui.getDmp().validate();
		gui.getDmp().repaint();

		// System.out.println("������ �г� ���� : " + dataPanelList.size());
	}

	@Override
	public void removeRoom(int btnNum) {
		// TODO Auto-generated method stub
		gui.roomCntDecrement();

		tabPanel.remove(roomBtnList.get(btnNum - 1));
		dataManagePanelList.remove(btnNum - 1);
		
		roomBtnList.remove(btnNum - 1);
		
		for(int i=0; i<gui.getRoomDataList().get(btnNum-1).getWorkCnt(); i++)
			gui.getWorkHashMap().remove(gui.getRoomDataList().get(btnNum-1).getWorkDataList().get(i));
		gui.getRoomDataList().remove(btnNum-1);
		
		if(gui.getMmp() != null)
			gui.getMmp().removeRoom2(btnNum);
		
		for(int i=btnNum-1; i<gui.getRoomCnt(); i++){
			roomBtnList.get(i).setRoomNum(i+1);
			roomBtnList.get(i).mouseListenerRefresh();
			gui.getRoomDataList().get(i).setRoomNum(i+1);
			System.out.println();
		}

		remove(scroller);
		refreshView();
	}
	
	public void removeRoom2(int btnNum) {
		// TODO Auto-generated method stub
		tabPanel.remove(roomBtnList.get(btnNum - 1));
		dataManagePanelList.remove(btnNum - 1);
		roomBtnList.remove(btnNum - 1);

		for(int i=btnNum-1; i<gui.getRoomCnt(); i++){
			roomBtnList.get(i).setRoomNum(i+1);
			roomBtnList.get(i).mouseListenerRefresh();
			gui.getRoomDataList().get(i).setRoomNum(i+1);
			System.out.println();
		}
		remove(scroller);
		refreshView();
	}

	public ArrayList<DataManagePanel> getDataManagePanelList() {
		return dataManagePanelList;
	}
}
