package harmony.admin.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MapManagePage extends ManagePage {

	/*
	 * ����
	 */

	private final int NONE = 0;		// �ƹ��͵� �ƴ�
	private final int NODE = 1;		// ��� �߰� ��ư ������ ��
	private final int WORK = 2;		// ���ù� �߰� ��ư ������ ��

	private final int MOUSE_PRESSED = 1;	// ���콺 ��ư ������ ��
	private final int MOUSE_DRAGED = 2;		// ���콺 �巡������ ��
	private final int MOUSE_RELEASED = 3;	// ���콺 ��ư ���� ��
	private final int MOUSE_ENTERED = 4; 	// ���콺�� ��ư�� ������ ��
	private final int MOUSE_EXITED = 5;

	private int editState = NONE;

	private boolean viewValid = false;		// ���� ����, ������ ���̱�

	///////////////////////////////////////////////////////

	private BufferedImage blockImage, enterBlockImage; 	// ��� �̹���
	private BufferedImage nodeBlockImage, enterNodeBlockImage; 	// ��� �̹���
	private BufferedImage workBlockImage, enterWorkBlockImage; 	// ��� �̹���
	private JButton addMapBtn;

	private ArrayList<MapManagePanel> mapManagePanelList;
	private JPanel panel;
	private JLabel filePath_lbl;
	private JLabel label;

	private JButton zoomInBtn, zoomOutBtn;
	private JButton addNodeBtn, addWorkBtn;

	public MapManagePage() {

		mapManagePanelList = new ArrayList<MapManagePanel>();

		scroller.setBounds(3, 42, 980, 730);

		/*
		 * ��� �̹��� �б�
		 */
		try {
			blockImage = ImageIO.read(new File("C:/Users/���/GUI/src/block.png"));
			enterBlockImage = ImageIO.read(new File("C:/Users/���/GUI/src/enter_block.png"));
			nodeBlockImage = ImageIO.read(new File("C:/Users/���/GUI/src/block.png"));
			enterNodeBlockImage = ImageIO.read(new File("C:/Users/���/GUI/src/enter_block.png"));
			workBlockImage = ImageIO.read(new File("C:/Users/���/GUI/src/workBlock.png"));
			enterWorkBlockImage = ImageIO.read(new File("C:/Users/���/GUI/src/enter_workBlock.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Ȯ�� ��� ��ư
		zoomInBtn = new JButton("Ȯ��");
		zoomInBtn.setBounds(30, 50, 50, 50);
		zoomOutBtn = new JButton("���");
		zoomOutBtn.setBounds(30, 100, 50, 50);

		// ���, ���ù� �߰� ��ư
		addNodeBtn = new JButton("��� �߰�");
		addNodeBtn.setBounds(30, 150, 50, 50);
		addWorkBtn = new JButton("��ǰ �߰�");
		addWorkBtn.setBounds(30, 200, 50, 50);

		layer.add(zoomInBtn, new Integer(400));
		layer.add(zoomOutBtn, new Integer(400));
		layer.add(addWorkBtn, new Integer(400));
		layer.add(addNodeBtn, new Integer(400));

		if (roomBtnList.size() == 0)
			btnVisible(false);

		btnVisible(viewValid);

		setLayout(new BorderLayout());

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

		label = new JLabel("\uD30C\uC77C\uACBD\uB85C : ");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);

		filePath_lbl = new JLabel("");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		panel.add(filePath_lbl, gbc_lblNewLabel);

		addMapBtn = new JButton("���� �ø���");
		GridBagConstraints gbc_addMapBtn = new GridBagConstraints();
		gbc_addMapBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_addMapBtn.gridx = 3;
		gbc_addMapBtn.gridy = 0;
		panel.add(addMapBtn, gbc_addMapBtn);
		addMapBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		addMapBtn.setBounds(100, 1, 50, 50);

		addMapBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (viewValid) {
					JFileChooser chooser = new JFileChooser();
					FileFilter filter = new FileNameExtensionFilter("image files", "jpg", "png", "jpeg", "bmp");
					chooser.addChoosableFileFilter(filter);

					int value = chooser.showOpenDialog(null);
					if (value == JFileChooser.APPROVE_OPTION) {
						filePath_lbl.setText(chooser.getSelectedFile().toString());
						File file = chooser.getSelectedFile();
						try {
							MapManagePanel currentPanel = mapManagePanelList.get(gui.getCurrentRoomNum() - 1);
							Image img = ImageIO.read(file);
							currentPanel.img = img;
							currentPanel.validate();
							currentPanel.repaint();

							int x, y;

							x = (int)(img.getWidth(null) / 50) + 1;
							y = (int)(img.getHeight(null) / 50) + 1;

							System.out.println("x : " + x + " y : " + y);
							// ����������� 5/10
							GridBagLayout gridBagLayout = new GridBagLayout();
							gridBagLayout.columnWidths = new int[x];
							for(int i=0; i<x; i++)
								gridBagLayout.columnWidths[i] = 50;
							gridBagLayout.rowHeights = new int[y];
							for(int i=0; i<y; i++)
								gridBagLayout.rowHeights[i] = 50;
							gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
							gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
							currentPanel.setLayout(gridBagLayout);

							GridBagConstraints gbc_button = new GridBagConstraints();
							gbc_button.fill = GridBagConstraints.BOTH;

							for(int i=0; i<x; i++){
								for(int j=0; j<y; j++){
									Block block = new Block(i, j);
									block.addMouseListener(new MouseAdapter() {
										@Override
										public void mouseClicked(MouseEvent arg0) {
											System.out.println("x : " + block.x + " y : " + block.y);

											switch(editState)
											{
											case NONE:
												break;
											case NODE:

												break;
											case WORK:
												break;
											}
										}

										public void mouseDraged(MouseEvent arg0){

										}

										public void mouseEntered(MouseEvent arg0){
											System.out.println("x : " + block.x + " y : " + block.y);
											block.mouseState = MOUSE_ENTERED;

										}
										public void mouseExited(MouseEvent arg0){
											if(block.mouseState != MOUSE_PRESSED && block.mouseState != MOUSE_DRAGED)
												block.mouseState = MOUSE_EXITED;
										}
									});
									gbc_button.gridx = i;
									gbc_button.gridy = j;
									currentPanel.add(block, gbc_button);
								}
							}

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	/*
	 * 
	 */
	private class MapManagePanel extends JPanel {
		private int panelNum;
		private Graphics gTemp;

		private int workCnt, nodeCnt;
		private ArrayList<JButton> workList, nodeList;
		private Image img = null;

		public MapManagePanel() {
			panelNum = gui.getRoomNum();

			workCnt = 0;
			nodeCnt = 0;

			workList = new ArrayList<JButton>();
			nodeList = new ArrayList<JButton>();
		}

		public int getRoomNum() {
			return panelNum;
		}

		@Override
		public void paintComponent(Graphics g){
			if(img != null)
				g.drawImage(img, (getWidth() - img.getWidth(null))/2, 0, null);
			setOpaque(false);
		}
	}

	/*
	 * 
	 */

	private class WorkBtn extends JButton {
		private int workNum;
	}

	private class NodeBtn extends JButton {
		private int nodeNum;
	}

	private class Block extends JButton{
		public int x;
		public int y;
		private int type = NONE;
		private int mouseState = NONE;

		public Block(int x, int y){
			this.x = x;
			this.y = y;
			setOpaque(false);
		}

		public void setType(int type){
			this.type = type;
		}
		public int getType(){
			return type;
		}

		public void setMouseState(int state){
			this.mouseState = state;
		}

		public int getMouseState(){
			return mouseState;
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#paint(java.awt.Graphics)
		 * �� ��� ��ư�� �׸��� �Լ�
		 */
		public void paint(Graphics g){

			Graphics gButton = null;
			BufferedImage img = null;

			// ��Ͽ� �ƹ��͵� �������� �ʾ��� ��
			if(type == NONE){
				if(mouseState == NONE || mouseState == MOUSE_EXITED){
					if(blockImage == null || blockImage.getWidth() != getWidth() || blockImage.getHeight() != getHeight())
						blockImage = (BufferedImage)createImage(getWidth(), getHeight());

					img = blockImage;
					gButton = blockImage.getGraphics();
				}
				else if(mouseState == MOUSE_ENTERED){
					if(enterBlockImage == null || enterBlockImage.getWidth() != getWidth() || enterBlockImage.getHeight() != getHeight())
						enterBlockImage = (BufferedImage)createImage(getWidth(), getHeight());
					System.out.println("���콺 ����");

					img = enterBlockImage;
					gButton = enterBlockImage.getGraphics();
				}
			}

			// ����� ��ǰ���� ���� �Ǿ��� ��
			else if(type == WORK){
				if(mouseState == NONE || mouseState == MOUSE_EXITED){

				}
				else if(mouseState == MOUSE_ENTERED){

				}
			}

			gButton.setClip(g.getClip());

			Graphics2D g2d = (Graphics2D)g;
			AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f);
			g2d.setComposite(newComposite);

			g2d.drawImage(img, 0, 0, null);

			// ����� ���� �����Ǿ��� ��
			if(type == NODE){
				if(mouseState == NONE || mouseState == MOUSE_EXITED){
						
				}
				else if(mouseState == MOUSE_ENTERED){

				}
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

		scroller = new JScrollPane(mapManagePanelList.get(num - 1));
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setBounds(3, 42, 980, 700);

		add(this.scroller, BorderLayout.CENTER);

		viewValid = true;
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.ManagePage#addPane()
	 */
	@Override
	public void addPane() {
		// TODO Auto-generated method stub
		MapManagePanel mapManagePanel = new MapManagePanel();
		mapManagePanelList.add(mapManagePanel);
	}

	/*
	 * (non-Javadoc)
	 * @see GUI.ManagePage#refreshView()
	 */
	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		this.validate();
		this.repaint();

		btnVisible(viewValid);
	}

	@Override
	public void removeRoom(int btnNum) {
		// TODO Auto-generated method stub
		tabPanel.remove(roomBtnList.get(btnNum - 1));
		scroller.remove(mapManagePanelList.get(btnNum - 1));
		mapManagePanelList.remove(btnNum - 1);
		roomBtnList.remove(btnNum - 1);

		viewValid = false;
		refreshView();
	}


	public void btnVisible(boolean b) {
		zoomInBtn.setVisible(b);
		zoomOutBtn.setVisible(b);
		addWorkBtn.setVisible(b);
		addNodeBtn.setVisible(b);
	}
}
