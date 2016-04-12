package harmony.admin.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;


public class DataPanel extends JPanel{
	private int dataNum = 0;
	private JButton addDataBtn;

	private ArrayList<Data> dataList;	// �� �����忡 �ִ� ���ù� ������ ����Ʈ

	public DataPanel(){
		setLayout(new FlowLayout());
		setSize(980, 740);
		addDataBtn = new JButton();
		addDataBtn.setSize(50, 50);
		addDataBtn.setText("+");

		dataList = new ArrayList<Data>();

		addDataBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Data data = new Data();
				dataList.add(data);
				dataNum++;
				add(data);
			}
		});

		setBackground(Color.darkGray);

		add(addDataBtn);
		
	}

	/*
	 * ���ù� ���� Ŭ����
	 */

	public class Data extends JPanel{

		private JLabel image;
		private JTextField title, artist, simpleContents, contents;
		private JButton editBtn, delBtn; 

		public Data() {
			setSize(980, 200);
			SpringLayout springLayout = new SpringLayout();
			setLayout(springLayout);

			image = new JLabel("\uC0AC\uC9C4 \uC5C6\uC74C");

			/*
			 * ���� ���ε�
			 */
			image.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			springLayout.putConstraint(SpringLayout.NORTH, image, 10, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.SOUTH, image, -10, SpringLayout.SOUTH, this);
			image.setVerticalAlignment(SwingConstants.CENTER);
			image.setHorizontalAlignment(SwingConstants.CENTER);
			image.setBackground(Color.WHITE);
			image.setAlignmentX(Component.CENTER_ALIGNMENT);
			springLayout.putConstraint(SpringLayout.WEST, image, 10, SpringLayout.WEST, this);
			add(image);

			delBtn = new JButton("x");
			
			/*
			 * 
			 */
			delBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
				}
			});
			springLayout.putConstraint(SpringLayout.WEST, delBtn, 6, SpringLayout.EAST, contents);
			springLayout.putConstraint(SpringLayout.SOUTH, delBtn, -5, SpringLayout.NORTH, editBtn);
			springLayout.putConstraint(SpringLayout.EAST, delBtn, -10, SpringLayout.EAST, this);
			add(delBtn);
			
			title = new JTextField("\uC791\uD488\uBA85");
			springLayout.putConstraint(SpringLayout.WEST, title, 143, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.EAST, image, -6, SpringLayout.WEST, title);
			springLayout.putConstraint(SpringLayout.NORTH, title, 10, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.SOUTH, title, -138, SpringLayout.SOUTH, this);
			add(title);

			artist = new JTextField("\uC791\uAC00");
			springLayout.putConstraint(SpringLayout.NORTH, artist, 5, SpringLayout.SOUTH, title);
			springLayout.putConstraint(SpringLayout.WEST, artist, 6, SpringLayout.EAST, image);
			springLayout.putConstraint(SpringLayout.SOUTH, artist, -91, SpringLayout.SOUTH, this);
			add(artist);

			contents = new JTextField("\uC790\uC138\uD55C \uC124\uBA85");
			springLayout.putConstraint(SpringLayout.EAST, artist, -6, SpringLayout.WEST, contents);
			springLayout.putConstraint(SpringLayout.EAST, title, -6, SpringLayout.WEST, contents);
			springLayout.putConstraint(SpringLayout.WEST, contents, 422, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.EAST, contents, -102, SpringLayout.EAST, this);
			springLayout.putConstraint(SpringLayout.NORTH, contents, 10, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.SOUTH, contents, -10, SpringLayout.SOUTH, this);
			contents.setAutoscrolls(true);
			add(contents);

			editBtn = new JButton("\uC800\uC7A5");

			/*
			 * ��ǰ ������ ����
			 */
			editBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			springLayout.putConstraint(SpringLayout.NORTH, editBtn, 10, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.WEST, editBtn, 6, SpringLayout.EAST, contents);
			springLayout.putConstraint(SpringLayout.SOUTH, editBtn, 0, SpringLayout.SOUTH, image);
			springLayout.putConstraint(SpringLayout.EAST, editBtn, -10, SpringLayout.EAST, this);
			add(editBtn);

			simpleContents = new JTextField("\uAC04\uB2E8\uD55C \uC124\uBA85");
			springLayout.putConstraint(SpringLayout.NORTH, simpleContents, 6, SpringLayout.SOUTH, artist);
			springLayout.putConstraint(SpringLayout.WEST, simpleContents, 0, SpringLayout.WEST, title);
			springLayout.putConstraint(SpringLayout.SOUTH, simpleContents, -10, SpringLayout.SOUTH, this);
			springLayout.putConstraint(SpringLayout.EAST, simpleContents, 0, SpringLayout.EAST, title);
			add(simpleContents);
		}
	}

	public ArrayList<Data> getDataList(){
		return dataList;
	}
}
