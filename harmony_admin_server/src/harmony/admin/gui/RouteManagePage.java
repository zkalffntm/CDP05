package harmony.admin.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import GUI.ManagePage.RoomButton;
import GUI.MapManagePage.Block.BlockMouseListener;
import ProblemDomain.RoomData;
import ProblemDomain.RouteData;
import ProblemDomain.WorkData;
import java.awt.Insets;

public class RouteManagePage extends JFrame{

	private AddWorkToRoutePage popUpPage = new AddWorkToRoutePage(this, "��õ ��� ����");

	private int x=1, y=1;

	private ArrayList<RoutePanel> routePanelList;

	private JScrollPane scroller;
	private JPanel mainPanel;
	private JPanel panel;

	private GridBagConstraints constraints;

	ImageIcon backBtnImg;

	private JButton backBtn;
	private JButton saveBtn;
	private JButton addRouteBtn;

	private int selectedIdx = -1;


	private JButton btnNewButton;

	private GUI_console gui;

	public RouteManagePage(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gui = gui.getInstance();

		RouteManagerKeyListener listener = new RouteManagerKeyListener();
		addKeyListener(listener);

		backBtnImg = gui.getBackImg();

		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));

		GridBagLayout gbl_dataPanel = new GridBagLayout();

		gbl_dataPanel.columnWidths = new int[] {30, 250, 30, 250, 30, 250};
		gbl_dataPanel.rowHeights = new int[] {30, 300, 30, 300, 30, 300};
		gbl_dataPanel.columnWeights = new double[] { 0.0 };
		gbl_dataPanel.rowWeights = new double[] { 0.0 };

		mainPanel.setLayout(gbl_dataPanel);
		routePanelList = new ArrayList<RoutePanel>();
		scroller = new JScrollPane(mainPanel);

		panel = new JPanel();

		saveBtn = new JButton("�� ��");
		saveBtn.setFont(new Font("����", Font.BOLD, 20));
		saveBtn.setText("����");
		saveBtn.setForeground(Color.white);
		saveBtn.setBackground(new Color(107, 57, 49));
		saveBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.save();
			}
		});

		backBtn = new JButton("   "){
			public void paintComponent(Graphics g){
				g.drawImage(backBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
				setOpaque(false);
			}
		};
		backBtn.setBorderPainted(false);
		backBtn.setContentAreaFilled(false);
		backBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.moveMainPage();
			}


			public void mousePressed(MouseEvent arg0){
				backBtnImg = gui.getBackImg_s();
				validate();
				repaint();
			}

			public void mouseReleased(MouseEvent arg0){
				backBtnImg = gui.getBackImg();
				validate();
				repaint();
			}
		});

		panel.setLayout(new BorderLayout(0,0));
		panel.add(backBtn, BorderLayout.EAST);
		panel.add(saveBtn, BorderLayout.CENTER);

		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 0, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 1;

		addRouteBtn = new JButton();
		addRouteBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String str = null;
				str = JOptionPane.showInputDialog("��õ ��� �̸�");

				// ��� ��������
				if (str == "" || str == null)
					return;

				gui.routeCntIncrement();
				RouteData routeData = new RouteData(str, gui.getRouteCnt());
				gui.getRouteList().add(routeData);

				RoutePanel routePanel = new RoutePanel(str, gui.getRouteCnt());

				x += 2;

				if(x > 5){
					y+=2;
					x = 1;
				}

				routePanel.x = x;
				routePanel.y = y;

				routePanelList.add(routePanel);
				routePanel.routeNum = routePanelList.size();

				constraints.gridx = x;
				constraints.gridy = y;

				mainPanel.add(routePanel, constraints);

				refreshView();
			}
		});

		getContentPane().setLayout(new BorderLayout(0, 0));

		getContentPane().add(scroller, BorderLayout.CENTER);
		getContentPane().add(panel, BorderLayout.SOUTH);

		mainPanel.add(addRouteBtn, constraints);

		
	}


	public void removeRoom(int btnNum) {
		// TODO Auto-generated method stub

	}

	public void refreshView(){
		validate();
		repaint();
	}


	public AddWorkToRoutePage getPopUpPage() {
		return popUpPage;
	}

	public class RoutePanel extends JPanel{

		private ImageIcon delBtnImg;
		private ImageIcon pathImg;
		private ImageIcon addImgBtnImg;
		private ImageIcon addWorkBtnImg;

		private int x, y; 
		private int routeNum;

		private JPanel topPanel;
		private JPanel centerPanel;
		private JPanel bottomPanel;

		private Label title; 
		private Label filePath;

		private JButton delBtn;
		private JButton addWorkBtn;
		private JButton addImageBtn;

		private Image image = null;

		public RoutePanel(String str, int routeNum) {
			setBorder(new LineBorder(new Color(139, 69, 19), 5));
			setSize(350, 500);

			this.routeNum = routeNum;

			delBtnImg = gui.getxImg();
			pathImg = gui.getPathImg();
			addImgBtnImg = gui.getAddImage2Img();
			addWorkBtnImg = gui.getPlusImg();

			setLayout(new BorderLayout(0, 0));

			topPanel = new JPanel();

			centerPanel = new JPanel(){
				public void paint(Graphics g){
					if(image == null)
						super.paint(g);
					else{
						g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
						setOpaque(false);
					}
				}
			};
			centerPanel.setLayout(null);
			centerPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					selectedIdx = routeNum-1;
					System.out.println(selectedIdx + " �г� ���õ�");

					for(int i=0; i<routePanelList.size(); i++){
						routePanelList.get(i).title.validate();
						routePanelList.get(i).title.repaint();
					}
				}
			});

			bottomPanel = new JPanel();

			title = new Label(str){
				public void paint(Graphics g){
					super.paint(g);

					if(selectedIdx == routeNum-1)
						title.setBackground(Color.gray);
					else
						title.setBackground(Color.lightGray);
				}
			};

			filePath = new Label(){
				public void paint(Graphics g){
					g.drawImage(pathImg.getImage(), 0, 0, getWidth(), getHeight(), null);
					setOpaque(false);
				}
			};

			delBtn = new JButton(){
				public void paint(Graphics g){
					g.drawImage(delBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
					setOpaque(false);
				}
			};

			delBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					gui.getRouteList().remove(routeNum-1);
					gui.routeCntDecrement();
					routePanelList.remove(routeNum-1);

					x = 1;
					y = 1;

					constraints.gridx = x;
					constraints.gridy = y;

					mainPanel.removeAll();
					mainPanel.add(addRouteBtn, constraints);

					for(int i=routeNum-1; i<gui.getRouteCnt(); i++){
						routePanelList.get(i).routeNum = i+1;
						gui.getRouteList().get(i).setRouteNum(i+1);

						if(x + 2 > 5){
							x = 1;
							y += 2;
						}
						else 
							x += 2; 

						constraints.gridx = x;
						constraints.gridy = y;

						routePanelList.get(i).x = x;
						routePanelList.get(i).y = y;

						mainPanel.add(routePanelList.get(i), constraints);
					}
					mainPanel.validate();
					mainPanel.repaint();
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

			addWorkBtn = new JButton(){
				public void paint(Graphics g){
					g.drawImage(addWorkBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
					setOpaque(false);
				}
			};

			centerPanel.add(addWorkBtn);

			addWorkBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					popUpPage.setRouteNum(routeNum);
					popUpPage.refreshList();
					popUpPage.refresh();
					popUpPage.setVisible(true);
				}

				public void mousePressed(MouseEvent arg0){
					addWorkBtnImg = gui.getPlusImg();
					validate();
					repaint();
				}

				public void mouseReleased(MouseEvent arg0){
					addWorkBtnImg = gui.getPlusImg_s();
					validate();
					repaint();
				}
			});
			addWorkBtn.setSize(50, 50);
			addWorkBtn.setLocation(95, 100);

			addImageBtn = new JButton("��ǥ �̹���"){
				public void paint(Graphics g){
					g.drawImage(addImgBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
					setOpaque(false);
				}
			};
			addImageBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					JFileChooser chooser = new JFileChooser();
					FileFilter filter = new FileNameExtensionFilter("image files", "jpg", "png", "jpeg", "bmp");
					chooser.addChoosableFileFilter(filter);

					int value = chooser.showOpenDialog(null);
					if (value == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						try {
							image = ImageIO.read(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				public void mousePressed(MouseEvent arg0){
					addImgBtnImg = gui.getAddImage2Img_s();
					validate();
					repaint();
				}

				public void mouseReleased(MouseEvent arg0){
					addImgBtnImg = gui.getAddImage2Img();
					validate();
					repaint();
				}
			});

			add(topPanel, BorderLayout.NORTH);
			add(centerPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);

			topPanel.setLayout(new BorderLayout());
			bottomPanel.setLayout(new BorderLayout());

			topPanel.add(title, BorderLayout.CENTER);
			topPanel.add(delBtn, BorderLayout.EAST);

			bottomPanel.add(filePath, BorderLayout.CENTER);
			bottomPanel.add(addImageBtn, BorderLayout.EAST);
		}


	}

	class RouteManagerKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			System.out.println(e.getKeyCode());

			if(selectedIdx != -1)
				switch(e.getKeyCode()){
				case 127:	// delete
					mainPanel.remove(routePanelList.get(selectedIdx));
					routePanelList.remove(selectedIdx);

					// �����ѵ� �г� �ٽ� �����ؾ���

					break;
				case 113:	// F2
					String str = null;
					str = JOptionPane.showInputDialog("��õ ��� �̸� ����");

					// ��� ��������
					if (str == "" || str == null)
						return;

					routePanelList.get(selectedIdx).title.setText(str);
					break;
				case 27:	// esc
					selectedIdx = -1;
					break;
				}
		}
	}
}
