package harmony.admin.gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ProblemDomain.WorkData;
import net.miginfocom.swing.MigLayout;
import javax.swing.JRadioButton;

public class SimpleDataPanel extends JPanel {

	private Image mainImage = null;
	private JLabel image;
	private JTextField title, artist, simpleContents;
	private WorkData workData;
	
	private int workIdx;
	private boolean selected = false;
	
	public SimpleDataPanel(WorkData workData){
		
		this.workData = workData; 
		
		setSize(new Dimension(491, 200));

		if(workData.getImage()!= null && workData.getImage().size() > 0)
			this.mainImage = workData.getImage().get(0);
		
		image = new JLabel("���� ����"){
			public void paintComponent(Graphics g){
				if(mainImage != null)
					g.drawImage(mainImage, 0, 0, getWidth(), getHeight(), null);
				else
					super.paintComponent(g);
			}
		};
		
		setLayout(new MigLayout("", "[115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:47.00,grow,fill][115:115,grow,fill][115:177.00,grow,fill][115:115,grow,fill]", "[43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill]"));
		image.setVerticalAlignment(SwingConstants.CENTER);
		image.setHorizontalAlignment(SwingConstants.CENTER);
		image.setBackground(Color.WHITE);
		image.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(image, "cell 0 0 1 4,alignx center,aligny center");

		this.title = new JTextField("\uC791\uD488\uBA85");
		this.title.setText(workData.getTitle());
		this.title.setEditable(false);
		add(this.title, "cell 1 0 1 2,alignx center,aligny center");

		this.simpleContents = new JTextField("\uAC04\uB2E8\uD55C \uC124\uBA85");
		this.simpleContents.setText(workData.getSimpleContents());
		this.simpleContents.setEditable(false);
		this.simpleContents.setColumns(1);
		add(this.simpleContents, "cell 2 0 2 4,alignx center,aligny center");

		this.artist = new JTextField("\uC791\uAC00");
		this.artist.setText(workData.getArtist());
		this.artist.setEditable(false);
		add(this.artist, "cell 1 2 1 2,alignx center,aligny center");
		
		
	}

	public Image getMainImage() {
		return mainImage;
	}

	public void setMainImage(Image mainImage) {
		this.mainImage = mainImage;
	}

	public JLabel getImage() {
		return image;
	}

	public void setImage(JLabel image) {
		this.image = image;
	}

	public JTextField getTitle() {
		return title;
	}

	public void setTitle(JTextField title) {
		this.title = title;
	}

	public JTextField getArtist() {
		return artist;
	}

	public void setArtist(JTextField artist) {
		this.artist = artist;
	}

	public JTextField getSimpleContents() {
		return simpleContents;
	}

	public void setSimpleContents(JTextField simpleContents) {
		this.simpleContents = simpleContents;
	}

	public WorkData getWorkData() {
		return workData;
	}

	public void setWorkData(WorkData workData) {
		this.workData = workData;
	}

	public int getWorkIdx() {
		return workIdx;
	}

	public void setWorkIdx(int workIdx) {
		this.workIdx = workIdx;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(selected)
			this.setBackground(new Color(107, 57, 49));
		else
			this.setBackground(Color.LIGHT_GRAY);
	}
	
	
}
