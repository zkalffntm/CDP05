package GUI;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Label;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class RoutePanel extends JPanel{
	
	private JPanel topPanel;
	private JPanel centerPanel;
	private JPanel bottomPanel;
	
	private Label title; 
	private Label filePath;
	
	private JButton delBtn;
	private JButton addWorkBtn;
	private JButton addImageBtn;
	
	private int routeNum;
	
	public RoutePanel(int routeNum) {
		this.routeNum = routeNum;
		
		setBorder(new LineBorder(new Color(139, 69, 19), 5));
		setSize(250, 300);
		
		setLayout(new BorderLayout(0, 0));
		
		topPanel = new JPanel();
		centerPanel = new JPanel();
		centerPanel.setBackground(new Color(255, 255, 255));
		bottomPanel = new JPanel();
		
		title = new Label();
		title.setBackground(new Color(255, 255, 255));
		filePath = new Label("이미지 경로 : ");
		filePath.setBackground(new Color(255, 255, 255));
		
		delBtn = new JButton(){
			public void paint(Graphics g){
				ImageIcon img = new ImageIcon("C:/Users/재민/GUI/src/btn_zoomOut.png");
				g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
				setOpaque(false);
			}
		};
		addImageBtn = new JButton();
		
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		topPanel.setLayout(new BorderLayout());
		bottomPanel.setLayout(new BorderLayout());
		
		topPanel.add(title, BorderLayout.CENTER);
		topPanel.add(delBtn, BorderLayout.EAST);
		centerPanel.setLayout(null);
		addWorkBtn = new JButton();
		addWorkBtn.setSize(50, 50);
		addWorkBtn.setLocation(95, 100);
		centerPanel.add(addWorkBtn);
		
		bottomPanel.add(filePath, BorderLayout.CENTER);
		bottomPanel.add(addImageBtn, BorderLayout.EAST);
	}
	
}
