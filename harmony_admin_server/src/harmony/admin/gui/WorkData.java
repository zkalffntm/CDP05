package harmony.admin.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class WorkData extends JPanel{
	
	private JLabel image;
	private JTextField title, artist, simpleContents, contents;
	private JButton editBtn; 
	private JButton delBtn;
	
	public WorkData() {
		setMaximumSize(new Dimension(980, 200));
		setMinimumSize(new Dimension(980, 200));
		setSize(new Dimension(980, 200));
		
		
		image = new JLabel("\uC0AC\uC9C4 \uC5C6\uC74C");
		image.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		setLayout(new MigLayout("", "[115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill][115:115,grow,fill]", "[43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill][43:43:43,grow,fill]"));
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
