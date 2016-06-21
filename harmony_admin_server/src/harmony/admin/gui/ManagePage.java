package harmony.admin.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import harmony.admin.gui.probremdomain.RoomData;

/*
 * 
 * 
 */

public abstract class ManagePage extends JFrame {

  private ImageIcon addWorkBtnImg, addTabBtnImg, backBtnImg, tabBtnImg;

  protected GUI_console gui;

  protected JButton backBtn;

  protected JButton addTabBtn; // �� �߰� ��ư
  protected ArrayList<RoomButton> roomBtnList; // �� ��ư ����Ʈ

  // protect�� �ٲٱ�
  protected JPanel tabPanel, mainPanel;
  protected JScrollPane scroller, tabScroller;

  protected JLayeredPane layer;
  protected int roomCnt = 0;

  /*
   * 
   */

  public ManagePage() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    gui = gui.getInstance();

    backBtnImg = gui.getBackImg();
    addWorkBtnImg = gui.getAddWorkImg();
    addTabBtnImg = gui.getPlus3Img();
    tabBtnImg = gui.getTabImg();

    layer = getLayeredPane();

    scroller = new JScrollPane(mainPanel = new JPanel());

    setSize(1000, 800);
    setLayout(new BorderLayout());

    backBtn = new JButton("   ") {
      public void paintComponent(Graphics g) {
        g.drawImage(backBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
        setOpaque(false);
      }
    };
    backBtn.setBorderPainted(false);
    backBtn.setContentAreaFilled(false);

    backBtn.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent arg0) {
        gui.moveMainPage();
      }

      public void mousePressed(MouseEvent arg0) {
        backBtnImg = gui.getBackImg_s();
        validate();
        repaint();
      }

      public void mouseReleased(MouseEvent arg0) {
        backBtnImg = gui.getBackImg();
        validate();
        repaint();
      }
    });
    roomBtnList = new ArrayList<RoomButton>();

    addTabBtn = new JButton() {
      public void paint(Graphics g) {
        g.drawImage(addTabBtnImg.getImage(), 0, 0, getWidth(), getHeight(),
            null);
      }
    };
    addTabBtn.setBorderPainted(false); // �׵θ�
    addTabBtn.setFocusPainted(false);
    addTabBtn.setOpaque(false);

    /*
     * ���� �ø��� �̺�Ʈ ��ư�� ������ â�� �����Ǽ� ����Ʈ�� ����
     */
    addTabBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {

        String str = null;
        str = JOptionPane.showInputDialog("���ý� �̸�");

        // ��� ��������
        if (str == "" || str == null)
          return;

        RoomData roomData = new RoomData(str);
        gui.roomCntIncrement();
        roomData.setRealNum(0);
        roomData.setRoomNum(gui.getRoomCnt());
        gui.getRoomDataList().add(roomData);

        RoomButton roomBtn = new RoomButton(gui.getRoomCnt(), str);

        roomBtnList.add(roomBtn);
        tabPanel.add(roomBtn);

        addPane(roomData);
        System.out.println("���ý� ���� : " + gui.getRoomCnt());
        refreshView();
      }
    });

    addTabBtn.setText("+");
    addTabBtn.setSize(41, 26);

    tabPanel = new JPanel();
    tabPanel.setVisible(true);
    tabPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    tabPanel.add(addTabBtn);
    tabPanel.setBounds(2, 2, 980, 40);

    add(tabPanel, BorderLayout.NORTH);
    add(scroller, BorderLayout.CENTER);

  }

  /*
   * 
   * 
   */

  public RoomButton getRoomButton(int roomNum) {
    return roomBtnList.get(roomNum);
  }

  /*
   * abstract method
   */
  public abstract void setView(int num); // �� ��ư �������� ȭ�� ��ȯ

  public abstract void addPane(RoomData roomData); // �� �߰� ���� �� ȭ�� ����

  public abstract void refreshView(); // ȭ�� ���� �׸���(���� ��)

  public abstract void removeRoom(int btnNum); // ���ý� ����

  /*
   * �� ��ư
   */
  protected class RoomButton extends JPanel {
    int num;

    JButton delBtn;
    JLabel title;

    ImageIcon delBtnImg;

    public RoomButton(int num, String name) {

      this.num = num;
      title = new JLabel(name);

      delBtnImg = gui.getxImg();

      setLayout(new FlowLayout());
      setSize(100, 50);

      delBtn = new JButton() {
        public void paintComponent(Graphics g) {
          g.drawImage(delBtnImg.getImage(), 0, 0, getWidth(), getHeight(),
              null);
          setOpaque(false);
        }
      };
      delBtn.setSize(20, 20);
      delBtn.setBorderPainted(false);
      delBtn.setFocusPainted(false);

      delBtn.setText("x");
      setBackground(Color.lightGray);

      /*
       * ���ý� ����
       */
      delBtn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent arg0) {
          System.out.println(num + " �� �� ����� Ŭ��");
          removeRoom(num);
        }

        public void mousePressed(MouseEvent arg0) {
          delBtnImg = gui.getxImg_s();
        }

        public void mouseReleased(MouseEvent arg0) {
          delBtnImg = gui.getxImg();
        }
      });

      /*
       * ���ý� �� ������ ��
       */
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent arg0) {
          System.out.println(num + " �� �� Ŭ������!");
          System.out.println("roomBtnSize : " + roomBtnList.size());
          System.out.println("roomDataSize : " + gui.getRoomDataList().size());
          setView(num);
          refreshView();
        }

        public void mousePressed(MouseEvent arg0) {
          tabBtnImg = gui.getTabImg_s();
        }

        public void mouseReleased(MouseEvent arg0) {
          tabBtnImg = gui.getTabImg();
        }
      });

      add(title);
      add(delBtn);
    }

    public String getName() {
      return title.getText();
    }

    public int getRoomNum() {
      return num;
    }

    public void setRoomNum(int num) {
      this.num = num;
    }

    public void mouseListenerRefresh() {
      delBtn.removeMouseListener(delBtn.getMouseListeners()[1]);
      removeMouseListener(getMouseListeners()[0]);

      delBtn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent arg0) {
          removeRoom(num);
        }
      });
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent arg0) {
          setView(num);
          refreshView();
        }
      });
    }

    public void paintComponents(Graphics g) {
      g.drawImage(tabBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
      setOpaque(false);
    }
  }
}
