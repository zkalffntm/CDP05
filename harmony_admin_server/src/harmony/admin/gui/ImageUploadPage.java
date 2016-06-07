package harmony.admin.gui;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

public class ImageUploadPage extends JDialog{

	private JPanel mainPanel;
	private JPanel imageListPanel;
	private JPanel panel;
	private JButton addImageBtn;

	private ImageIcon addImgBtnImg;
	
	private ArrayList<Image> imageList;
	private ArrayList<ImageButton> buttonList;
	private ArrayList<String> imageScr;
	
	private GUI_console gui;
	
	public ImageUploadPage(JFrame frame, String title) {
		
		super(frame, title, true);

		gui = gui.getInstance();
		
		addImgBtnImg = gui.getAddImageImg();
				
		setSize(800, 250);
		
		imageList = new ArrayList<Image>();
		buttonList = new ArrayList<ImageButton>();
		imageScr = new ArrayList<String>();
		
		mainPanel = new JPanel();

		imageListPanel = new JPanel();

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scroll = new JScrollPane(imageListPanel);
		GridBagLayout gbl_imageListPanel = new GridBagLayout();
		gbl_imageListPanel.columnWidths = new int[] {150};
		gbl_imageListPanel.rowHeights = new int[] {200};
		gbl_imageListPanel.columnWeights = new double[]{0.0};
		gbl_imageListPanel.rowWeights = new double[]{0.0};
		imageListPanel.setLayout(gbl_imageListPanel);

		mainPanel.add(scroll, BorderLayout.CENTER);

		panel = new JPanel();
		mainPanel.add(panel, BorderLayout.EAST);
		panel.setLayout(new CardLayout(0, 0));

		addImageBtn = new JButton("add Work Image"){
			public void paint(Graphics g){
				ImageIcon img = addImgBtnImg;
				g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		addImageBtn.setBorderPainted(false);
		addImageBtn.setFocusPainted(false);
		addImageBtn.setOpaque(false);
		
		panel.add(addImageBtn);
		
		addImageBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("image files", "jpg", "png", "jpeg", "bmp");
				chooser.addChoosableFileFilter(filter);

				int value = chooser.showOpenDialog(null);
				if (value == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					/*
					 *  �̹��� ����(DB)
					 */
					try {
						Image img = ImageIO.read(file);
						imageList.add(img);
						imageScr.add(chooser.getSelectedFile().toString());
						
						ImageButton button = new ImageButton(imageList.size()-1, img);
						
						buttonList.add(button);
						
						GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
						gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
						gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
						gbc_btnNewButton.anchor = GridBagConstraints.WEST;
						gbc_btnNewButton.gridx = buttonList.size()-1;
						gbc_btnNewButton.gridy = 0;
						
						imageListPanel.add(button, gbc_btnNewButton);
						refreshView();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			
			public void mousePressed(MouseEvent arg0){
				addImgBtnImg = gui.getAddImageImg_s();
				validate();
				repaint();
			}
			
			public void mouseReleased(MouseEvent arg0){
				addImgBtnImg = gui.getAddImageImg();
				validate();
				repaint();
			}
		});

	}

	public void refresh(ArrayList<Image> img){
		System.out.println("���� �г��� �̹��� �� : " + img.size());
		
		imageListPanel.removeAll();
		System.out.println("��ư �� ����. ��ư �� : " + imageListPanel.getComponentCount());
		buttonList.removeAll(buttonList);
		
		this.imageList = (ArrayList<Image>) img.clone();
		
		for(int i=0; i<img.size(); i++){
			ImageButton button = new ImageButton(i, img.get(i)); 
			buttonList.add(button);
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
			gbc_btnNewButton.anchor = GridBagConstraints.WEST;
			gbc_btnNewButton.gridx = i;
			gbc_btnNewButton.gridy = 0;
			
			imageListPanel.add(button, gbc_btnNewButton);
		}
	}
	
	public void refresh(Image img){
		
	}
	
	public ArrayList<String> getImageScr() {
		return imageScr;
	}

	public void setImageScr(ArrayList<String> imageScr) {
		this.imageScr = imageScr;
	}

	public ArrayList<Image> getImageList() {
		return imageList;
	}

	public void setImageList(ArrayList<Image> imageList) {
		this.imageList = imageList;
	}

	public JPanel getImageListPanel() {
		return imageListPanel;
	}

	public void setImageListPanel(JPanel imageListPanel) {
		this.imageListPanel = imageListPanel;
	}

	public class ImageButton extends JPanel{

		private JButton delBtn;
		private int index;
		private Image img;
		private ImageIcon delBtnImg;
		
		public ImageButton(int index, Image img) {
			setSize(170, 200);

			delBtnImg = gui.getxImg();
			
			this.index = index;
			this.img = img;

			setBackground(Color.lightGray);

			/*
			 * ���� ������ ��
			 */
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					JFileChooser chooser = new JFileChooser();
					FileFilter filter = new FileNameExtensionFilter("image files", "jpg", "png", "jpeg", "bmp");
					chooser.addChoosableFileFilter(filter);
					
					int value = chooser.showOpenDialog(null);
					if (value == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();		
						/*
						 *  �̹��� ����(DB)
						 */
						try{
							Image img = ImageIO.read(file);
							setImage(img);
							imageList.set(index, img);
							refreshView();
						}catch(IOException e){
							e.printStackTrace();
						}
					}
				}
			});
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] {40, 40, 40, 40, 40};
			gridBagLayout.rowHeights = new int[]{27, 0};
			gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			setLayout(gridBagLayout);

			delBtn = new JButton("����"){
				public void paintComponent(Graphics g){
					g.drawImage(delBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
					setOpaque(false);
				}
				
				public void setBorder(Border border){
					
				}
			};
			
			delBtn.setSize(30, 30);
			delBtn.setBorderPainted(false);
			delBtn.setFocusPainted(false);
			
			GridBagConstraints gbc_delBtn = new GridBagConstraints();
			gbc_delBtn.anchor = GridBagConstraints.NORTHWEST;
			gbc_delBtn.gridx = 4;
			gbc_delBtn.gridy = 0;
			add(delBtn, gbc_delBtn);
			
			/*
			 * ���� ��ư ������ ��
			 */
			delBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					
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

			
		}

		public int getIndex(){
			return index;
		}
		
		public void setIndex(int index){
			this.index = index;
		}
		
		public void setImage(Image img){
			this.img = img;
		}
		
		public void paintComponent(Graphics g){
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
			setOpaque(false);
		}
	
	}
	
	public boolean imageIsValid(){
		if(imageList.size() < 1)
			return false;
		return true;
	}
	
	public Image getImage(){
		if(imageList.size() > 0)
			return imageList.get(0);
		else
			return null;
	}

	public void refreshView(){
		validate();
		repaint();
	}
	
}
