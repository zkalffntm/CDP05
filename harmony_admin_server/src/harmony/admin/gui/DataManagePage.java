package harmony.admin.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class DataManagePage extends ManagePage{

	private ArrayList<DataPanel> dataPanelList;
	
	public DataManagePage() {
		super();
		scroller.setBounds(2, 42, 980, 740);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dataPanelList = new ArrayList<DataPanel>();

		setLayout(null);
	}

	/*
	 * �� ���ð��� ��ǰ ���� ���� ȭ��
	 */
	private class DataPanel extends JPanel{
		private int panelNum;
		private int dataNum = 0;
		private JButton addDataBtn;

		private ArrayList<Data> dataList;	// �� �����忡 �ִ� ���ù� ������ ����Ʈ

		public DataPanel(){

			panelNum = gui.getRoomNum();
			
			GridBagLayout gbl_dataPanel = new GridBagLayout();
			gbl_dataPanel.columnWidths = new int[]{0};
			gbl_dataPanel.rowHeights = new int[]{Data.HEIGHT};
			gbl_dataPanel.columnWeights = new double[]{Double.MIN_VALUE};
			gbl_dataPanel.rowWeights = new double[]{Double.MIN_VALUE};
			setLayout(gbl_dataPanel);

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
					
					GridBagConstraints temp = new GridBagConstraints();
					temp.gridx = 0;
					temp.gridy = dataNum;
					add(data, temp);
					
					if(data.getHeight() * dataNum + addDataBtn.getHeight() > getHeight())
						setSize(980, 740 + data.getHeight());
						
					refreshView();
					System.out.println("���� �г� ���� : " + getHeight());
					System.out.println("�� ������ �г� ���� �� : " + dataNum * data.getHeight());
					
				}
			});

			setBackground(Color.darkGray);
			setAutoscrolls(true);
			GridBagConstraints temp = new GridBagConstraints();
			temp.gridx = 0;
			temp.gridy = 0;
			add(addDataBtn, temp);
		}

		/*
		 * �� ���ù� ���� ���� �г� Ŭ����
		 */
		public class Data extends JPanel{
			
			private JLabel image;
			private JTextField title, artist, simpleContents, contents;
			private JButton editBtn; 
			private JButton delBtn;
			
			public Data() {
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
	}
		/*
		 * (non-Javadoc)
		 * @see GUI.ManagePage#setView(int)
		 */

		@Override
		public void setView(int num) {
			// TODO Auto-generated method stub
			gui.setCurrentRoomNum(num);
			remove(scroller);
			scroller = new JScrollPane(dataPanelList.get(num-1));
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroller.setBounds(2, 42, 980, 740);
			
			add(scroller);
		}

		@Override
		public void addPane() {
			// TODO Auto-generated method stub
			DataPanel dataPanel = new DataPanel();
			dataPanelList.add(dataPanel);
		}

		@Override
		public void refreshView() {
			// TODO Auto-generated method stub
			gui.getFrame().setContentPane(this);

		//	System.out.println("������ �г� ���� : " + dataPanelList.size());
		}

		@Override
		public void removeRoom(int btnNum) {
			// TODO Auto-generated method stub
			gui.roomNumDecrement();
			tabPanel.remove(roomBtnList.get(btnNum-1));
			scroller.remove(dataPanelList.get(btnNum-1));
			dataPanelList.remove(btnNum-1);
			roomBtnList.remove(btnNum-1);
			
			refreshView();
		}

	}
