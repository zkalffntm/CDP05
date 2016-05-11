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

	private ArrayList<Image> imageList;
	private ArrayList<ImageButton> buttonList;

	public ImageUploadPage(JFrame frame, String title) {
		
		super(frame, title, true);
		
		setSize(800, 250);
		
		imageList = new ArrayList<Image>();
		buttonList = new ArrayList<ImageButton>();

		mainPanel = new JPanel();

		imageListPanel = new JPanel();

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scroll = new JScrollPane(imageListPanel);
		GridBagLayout gbl_imageListPanel = new GridBagLayout();
		gbl_imageListPanel.columnWidths = new int[] {150, 0};
		gbl_imageListPanel.rowHeights = new int[] {200};
		gbl_imageListPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_imageListPanel.rowWeights = new double[]{0.0};
		imageListPanel.setLayout(gbl_imageListPanel);

		mainPanel.add(scroll, BorderLayout.CENTER);

		panel = new JPanel();
		mainPanel.add(panel, BorderLayout.EAST);
		panel.setLayout(new CardLayout(0, 0));

		addImageBtn = new JButton("add Image");
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
	
						ImageButton button = new ImageButton(imageList.size()-1, img){
							public void paintComponent(Graphics g){
								g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
							}
						};
						
						button.addMouseListener(new MouseAdapter() {
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
										button.setImage(img);
										button.validate();
										button.repaint();
									}catch(IOException e){
										e.printStackTrace();
									}
								}
							}
						});
						
						imageList.add(img);
						buttonList.add(button);
						
						GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
						gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
						gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
						gbc_btnNewButton.anchor = GridBagConstraints.WEST;
						gbc_btnNewButton.gridx = buttonList.size()-1;
						gbc_btnNewButton.gridy = 0;
						
						imageListPanel.add(button, gbc_btnNewButton);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		});

	}

	public class ImageButton extends JPanel{

		private JButton delBtn;
		private int index;
		private Image img;
		
		public ImageButton(int index, Image img) {
			setSize(170, 200);

			this.index = index;
			this.img = img;
			
			setBackground(Color.lightGray);

			/*
			 * ���� ������ ��
			 */
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {

				}
			});
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] {40, 40, 40, 40, 40};
			gridBagLayout.rowHeights = new int[]{27, 0};
			gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			setLayout(gridBagLayout);

			delBtn = new JButton();
			delBtn.setText("x");
			delBtn.setSize(20, 20);
			
			GridBagConstraints gbc_delBtn = new GridBagConstraints();
			gbc_delBtn.anchor = GridBagConstraints.NORTHWEST;
			gbc_delBtn.gridx = 3;
			gbc_delBtn.gridy = 0;
			add(delBtn, gbc_delBtn);
			/*
			 * ���� ��ư ������ ��
			 */
			delBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {

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
			super.paintComponent(g);
			setOpaque(false);
		}
	}
	
	public boolean imageIsValid(){
		if(imageList.size() < 1)
			return false;
		return true;
	}
	
	public Image getImage(){
		return imageList.get(0);
	}

}
