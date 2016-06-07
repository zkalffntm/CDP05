package harmony.admin.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import harmony.admin.gui.probremdomain.RoomData;
import harmony.admin.gui.probremdomain.WorkData;

public class MapManagePage extends ManagePage {

  private SelectDataPage popUpPage = new SelectDataPage(this, "��ǰ ���� ����");
  private JPanel panel, panel2;
  private JButton saveBtn;
  private final int BLOCK_SIZE = 50;

  /*
   * ����
   */
  private final int NONE = 0; // �ƹ��͵� �ƴ�
  private final int NODE = 1; // ��� �߰� ��ư ������ ��
  private final int WORK = 2; // ���ù� �߰� ��ư ������ ��
  private final int SHARE = 3; // ���� ���

  private final int MOUSE_PRESSED = 1; // ���콺 ��ư ������ ��
  private final int MOUSE_RELEASED = 2; // ���콺 ��ư ���� ��
  private final int MOUSE_ENTERED = 3; // ���콺�� ��ư�� ������ ��
  private final int MOUSE_EXITED = 4; // ���콺�� ��ư���� ���� ��
  private final int MOUSE_CLICKED = 5; // ���õ� ��

  private int editState = NONE; // ���콺 ���� (�Ϲ�, ���ù� ����, ��� ����)

  private boolean drag = false; // ���콺 ���� (�巡�� ����)
  private boolean shareEdit = false; // ���� ��� ���� ��
  private boolean viewValid = false; // ���� ����, ������ ���̱�

  ///////////////////////////////////////////////////////

  private BufferedImage blockImage, enterBlockImage; // ��� �̹���

  private BufferedImage nodeBlockImage, enterNodeBlockImage; // ��� �̹���
  private BufferedImage workBlockImage, enterWorkBlockImage; // ��� �̹���
  private JButton addMapBtn;
  private Image mapImage;

  private ArrayList<Block> tempBlock; // ����� �巡�������� �ӽ÷� ����

  private ArrayList<MapManagePanel> mapManagePanelList;
  private JLabel filePath_lbl;
  private JLabel label;

  private JButton zoomInBtn, zoomOutBtn;
  private JButton addNodeBtn, addWorkBtn, addShareBlockBtn;

  private ImageIcon zoomInBtnImg, zoomOutBtnImg, addNodeBtnImg, addWorkBtnImg,
      addMapBtnImg, saveBtnImg, pathImg;

  public MapManagePage() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mapManagePanelList = new ArrayList<MapManagePanel>();

    tempBlock = new ArrayList<Block>();

    zoomInBtnImg = gui.getInImg();
    zoomOutBtnImg = gui.getOutImg();
    addNodeBtnImg = gui.getNodeImg();
    addWorkBtnImg = gui.getWorkImg();
    addMapBtnImg = gui.getAddMapImg();
    saveBtnImg = gui.getSaveImg();
    pathImg = gui.getPathImg();

    /*
     * ��� �̹��� �б�
     */
    try {
      blockImage = ImageIO.read(new File("image/block.png"));
      enterBlockImage = ImageIO.read(new File("image/enter_block.png"));
      nodeBlockImage = ImageIO.read(new File("image/block.png"));
      enterNodeBlockImage = ImageIO.read(new File("image/block.png"));
      workBlockImage = ImageIO.read(new File("image/workBlock.png"));
      enterWorkBlockImage = ImageIO.read(new File("image/enter_workBlock.png"));
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    saveBtn = new JButton(" ��   ��  ") {
      public void paintComponent(Graphics g) {
        g.drawImage(saveBtnImg.getImage(), 0, 0, getWidth(), getHeight(), null);
        setOpaque(false);
      }
    };

    saveBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // ������ ����

      }

      public void mousePressed(MouseEvent arg0) {
        saveBtnImg = gui.getSaveImg_s();
        validate();
        repaint();
      }

      public void mouseReleased(MouseEvent arg0) {
        saveBtnImg = gui.getSaveImg();
        validate();
        repaint();
      }
    });

    // Ȯ�� ��� ��ư
    zoomInBtn = new JButton() {
      public void paintComponent(Graphics g) {
        g.drawImage(zoomInBtnImg.getImage(), 0, 0, getWidth(), getHeight(),
            null);
        setOpaque(false);
      }
    };
    zoomInBtn.setBounds(30, 50, 50, 50);
    zoomInBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (mapManagePanelList.get(gui.getCurrentRoomNum() - 1).img != null
            && mapManagePanelList
                .get(gui.getCurrentRoomNum() - 1).zoom <= 250) {
          System.out.println("Ȯ�� ��ư Ŭ��");

          mapManagePanelList.get(gui.getCurrentRoomNum() - 1).zoom += 30;

          zoomRefresh();
          scroller.validate();
          scroller.repaint();
        }
      }
    });

    zoomOutBtn = new JButton() {
      public void paintComponent(Graphics g) {
        g.drawImage(zoomOutBtnImg.getImage(), 0, 0, getWidth(), getHeight(),
            null);
        setOpaque(false);
      }
    };
    zoomOutBtn.setBounds(30, 100, 50, 50);
    zoomOutBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (mapManagePanelList.get(gui.getCurrentRoomNum() - 1).img != null
            && mapManagePanelList.get(gui.getCurrentRoomNum() - 1).zoom > 70) {
          System.out.println("��� ��ư Ŭ��");

          mapManagePanelList.get(gui.getCurrentRoomNum() - 1).zoom -= 30;

          zoomRefresh();
          scroller.validate();
          scroller.repaint();
        }
      }
    });

    // ��� �߰� ��ư
    addNodeBtn = new JButton() {
      public void paintComponent(Graphics g) {
        g.drawImage(addNodeBtnImg.getImage(), 0, 0, getWidth(), getHeight(),
            null);
        setOpaque(false);
      }
    };
    addNodeBtn.setBounds(30, 150, 50, 50);
    addNodeBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (editState == NODE) {
          editState = NONE;
          addNodeBtnImg = gui.getNodeImg();
        } else {
          editState = NODE;
          addNodeBtnImg = gui.getNodeImg_s();
        }
      }
    });

    // ���ù� �߰� ��ư
    addWorkBtn = new JButton("��ǰ �߰�") {
      public void paintComponent(Graphics g) {
        g.drawImage(addWorkBtnImg.getImage(), 0, 0, getWidth(), getHeight(),
            null);
        setOpaque(false);
      }
    };
    addWorkBtn.setBounds(30, 200, 50, 50);
    addWorkBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (editState == WORK) {
          editState = NONE;
          addWorkBtnImg = gui.getWorkImg();
        } else {
          editState = WORK;
          addWorkBtnImg = gui.getWorkImg_s();
        }
      }
    });

    addShareBlockBtn = new JButton() {

    };
    addShareBlockBtn.setBounds(30, 250, 50, 50);
    addShareBlockBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (editState == SHARE) {
          editState = NONE;
          // addWorkBtnImg = gui.getWorkImg();
        } else {
          editState = SHARE;
          // addWorkBtnImg = gui.getWorkImg_s();
        }
      }
    });

    layer.add(zoomInBtn, new Integer(400));
    layer.add(zoomOutBtn, new Integer(400));
    layer.add(addWorkBtn, new Integer(400));
    layer.add(addNodeBtn, new Integer(400));
    layer.add(addShareBlockBtn, new Integer(400));
    // �ڷι�ư �ӽ÷�

    if (roomBtnList.size() == 0)
      btnVisible(false);

    btnVisible(viewValid);

    getContentPane().setLayout(new BorderLayout());

    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[] { 214, 113, 517, 107, 0 };
    gbl_panel.rowHeights = new int[] { 27, 0 };
    gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
        Double.MIN_VALUE };
    gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };

    panel = new JPanel();
    panel2 = new JPanel();

    panel.setLayout(new BorderLayout());
    panel2.setLayout(new BorderLayout());

    label = new JLabel("file path\nfile path") {
      public void paintComponent(Graphics g) {
        g.drawImage(pathImg.getImage(), 0, 0, getWidth(), getHeight(), null);
        setOpaque(false);
      }
    };

    GridBagConstraints gbc_label = new GridBagConstraints();
    gbc_label.insets = new Insets(0, 0, 0, 5);
    gbc_label.gridx = 1;
    gbc_label.gridy = 0;
    panel.add(label, BorderLayout.WEST);

    filePath_lbl = new JLabel("   ���� ����");
    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
    gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
    gbc_lblNewLabel.gridx = 2;
    gbc_lblNewLabel.gridy = 0;

    addMapBtn = new JButton("�� ��   �� �� ��") {
      public void paintComponent(Graphics g) {
        g.drawImage(addMapBtnImg.getImage(), 0, 0, getWidth(), getHeight(),
            null);
        setOpaque(false);
      }
    };
    GridBagConstraints gbc_addMapBtn = new GridBagConstraints();
    gbc_addMapBtn.anchor = GridBagConstraints.NORTHWEST;
    gbc_addMapBtn.gridx = 3;
    gbc_addMapBtn.gridy = 0;
    addMapBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    addMapBtn.setBounds(100, 1, 50, 50);

    addMapBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (viewValid) {
          JFileChooser chooser = new JFileChooser();
          FileFilter filter = new FileNameExtensionFilter("image files", "jpg",
              "png", "jpeg", "bmp");
          chooser.addChoosableFileFilter(filter);

          int value = chooser.showOpenDialog(null);
          if (value == JFileChooser.APPROVE_OPTION) {
            filePath_lbl.setText(chooser.getSelectedFile().toString());
            mapManagePanelList
                .get(gui.getCurrentRoomNum() - 1).filePath = chooser
                    .getSelectedFile().toString();
            mapManagePanelList.get(gui.getCurrentRoomNum() - 1).roomData
                .setFilePath(chooser.getSelectedFile().toString());
            File file = chooser.getSelectedFile();
            try {
              MapManagePanel currentPanel = mapManagePanelList
                  .get(gui.getCurrentRoomNum() - 1);
              mapImage = ImageIO.read(file);
              currentPanel.img = mapImage;

              gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                  .setMap(mapImage);

              currentPanel.originImgWidth = mapImage.getWidth(null);
              currentPanel.originImgHeight = mapImage.getHeight(null);

              currentPanel.validate();
              currentPanel.repaint();

              currentPanel.x = (int) (mapImage.getWidth(null) / BLOCK_SIZE) + 1;
              currentPanel.y = (int) (mapImage.getHeight(null) / BLOCK_SIZE)
                  + 1;

              System.out
                  .println("x : " + currentPanel.x + " y : " + currentPanel.y);
              // ����������� 5/10
              GridBagLayout gridBagLayout = new GridBagLayout();
              gridBagLayout.columnWidths = new int[currentPanel.x + 2]; // ����
                                                                        // �¿�
                                                                        // 2��

              for (int i = 1; i < currentPanel.x + 1; i++)
                gridBagLayout.columnWidths[i] = BLOCK_SIZE;

              gridBagLayout.columnWidths[0] = 0;
              gridBagLayout.columnWidths[currentPanel.x + 1] = 0;

              if (currentPanel.originImgWidth < scroller.getWidth()) {
                gridBagLayout.columnWidths[0] = (currentPanel.getWidth()
                    - (currentPanel.originImgWidth * currentPanel.zoom / 100))
                    / 2;
                gridBagLayout.columnWidths[currentPanel.x + 1] = (currentPanel
                    .getWidth()
                    - (currentPanel.originImgWidth * currentPanel.zoom / 100))
                    / 2;
              }

              gridBagLayout.rowHeights = new int[currentPanel.y + 1]; // ���� ��
                                                                      // 1��
              for (int i = 0; i < currentPanel.y; i++)
                gridBagLayout.rowHeights[i] = BLOCK_SIZE;

              gridBagLayout.rowHeights[currentPanel.y] = 0;

              if (currentPanel.originImgHeight < scroller.getHeight()) {
                gridBagLayout.rowHeights[currentPanel.y] = currentPanel
                    .getHeight()
                    - (currentPanel.originImgHeight * currentPanel.zoom / 100);
              }

              gridBagLayout.columnWeights = new double[] { 0.0,
                  Double.MIN_VALUE };
              gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };

              currentPanel.setLayout(gridBagLayout);

              gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                  .setGridBagLayout(gridBagLayout);

              GridBagConstraints gbc_button = new GridBagConstraints();
              gbc_button.fill = GridBagConstraints.BOTH;

              // Block[][] currentBlock = new Block[currentPanel.y][];
              ArrayList<Block> currentBlock = new ArrayList<Block>();

              for (int i = 0; i < currentPanel.y; i++) {
                // currentBlock[i] = new Block[currentPanel.x];
                for (int j = 0; j < currentPanel.x; j++) {
                  Block block = new Block(i * currentPanel.x + j);

                  currentBlock.add(block);

                  gbc_button.gridx = j + 1; // ���� ��ĭ ����� ��ư ä�� -> �̹�����
                                            // ������ ���� ä�������
                  gbc_button.gridy = i;
                  currentPanel.add(block, gbc_button);
                  gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                      .setBlock(currentBlock);
                }
              }

              mapManagePanelList.get(gui.getCurrentRoomNum() - 1).validate();
              mapManagePanelList.get(gui.getCurrentRoomNum() - 1).repaint();
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
          }
        }
      }

      public void mousePressed(MouseEvent arg0) {
        addMapBtnImg = gui.getAddMapImg_s();
        validate();
        repaint();
      }

      public void mouseReleased(MouseEvent arg0) {
        addMapBtnImg = gui.getAddMapImg();
        validate();
        repaint();
      }
    });

    zoomInBtn.setBorderPainted(false); // �׵θ�
    zoomInBtn.setFocusPainted(false);

    zoomOutBtn.setBorderPainted(false); // �׵θ�
    zoomOutBtn.setFocusPainted(false);

    addWorkBtn.setBorderPainted(false); // �׵θ�
    addWorkBtn.setFocusPainted(false);

    addNodeBtn.setBorderPainted(false); // �׵θ�
    addNodeBtn.setFocusPainted(false);

    getContentPane().add(panel, BorderLayout.SOUTH);
    getContentPane().add(tabPanel, BorderLayout.NORTH);
    getContentPane().add(scroller, BorderLayout.CENTER);

    panel.add(filePath_lbl, BorderLayout.CENTER);
    panel2.add(addMapBtn, BorderLayout.WEST);
    panel2.add(saveBtn, BorderLayout.CENTER);
    panel2.add(backBtn, BorderLayout.EAST);

    panel.add(panel2, BorderLayout.EAST);

    for (int i = 0; i < gui.getRoomCnt(); i++) {
      MapManagePanel temp = new MapManagePanel(gui.getRoomDataList().get(i));
      mapManagePanelList.add(temp);
      RoomButton roomBtn = new RoomButton(i + 1,
          gui.getRoomDataList().get(i).getName());
      roomBtnList.add(roomBtn);
      tabPanel.add(roomBtn);
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

    System.out.println("currentRoomNum : " + num);

    remove(scroller);

    scroller = new JScrollPane(mapManagePanelList.get(num - 1));
    mapManagePanelList.get(num - 1)
        .setLayout(gui.getRoomDataList().get(num - 1).getGridBagLayout());

    if (mapManagePanelList.get(num - 1).roomData.getFilePath() != null)
      filePath_lbl
          .setText(mapManagePanelList.get(num - 1).roomData.getFilePath());

    else
      filePath_lbl.setText("   ���� ����");

    scroller.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroller.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    add(scroller, BorderLayout.CENTER);

    viewValid = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see GUI.ManagePage#addPane()
   */
  @Override
  public void addPane(RoomData roomData) {
    // TODO Auto-generated method stub

    MapManagePanel mapManagePanel = new MapManagePanel(roomData);
    mapManagePanelList.add(mapManagePanel);

    if (gui.getDmp() != null)
      gui.getDmp().addPane2(roomData);
  }

  public void addPane2(RoomData roomData) {
    // TODO Auto-generated method stub
    RoomButton roomBtn = new RoomButton(roomData.getRoomNum(),
        roomData.getName());
    tabPanel.add(roomBtn);
    roomBtnList.add(roomBtn);

    MapManagePanel mapManagePanel = new MapManagePanel(roomData);
    mapManagePanelList.add(mapManagePanel);
  }

  /*
   * (non-Javadoc)
   * 
   * @see GUI.ManagePage#refreshView()
   */
  @Override
  public void refreshView() {
    // TODO Auto-generated method stub
    gui.getMmp().validate();
    gui.getMmp().repaint();

    btnVisible(viewValid);
  }

  @Override
  public void removeRoom(int btnNum) {
    // TODO Auto-generated method stub
    gui.roomCntDecrement();

    tabPanel.remove(roomBtnList.get(btnNum - 1));
    mapManagePanelList.remove(btnNum - 1);

    roomBtnList.remove(btnNum - 1);

    for (int i = 0; i < gui.getRoomDataList().get(btnNum - 1).getWorkCnt(); i++)
      gui.getWorkHashMap().remove(
          gui.getRoomDataList().get(btnNum - 1).getWorkDataList().get(i));
    gui.getRoomDataList().remove(btnNum - 1);

    if (gui.getDmp() != null)
      gui.getDmp().removeRoom2(btnNum);

    for (int i = btnNum - 1; i < gui.getRoomCnt(); i++) {
      roomBtnList.get(i).setRoomNum(i + 1);
      roomBtnList.get(i).mouseListenerRefresh();
      gui.getRoomDataList().get(i).setRoomNum(i + 1);
      System.out.println();
    }

    viewValid = false;
    remove(scroller);
    refreshView();
  }

  public void removeRoom2(int btnNum) {
    // TODO Auto-generated method stub
    tabPanel.remove(roomBtnList.get(btnNum - 1));
    mapManagePanelList.remove(btnNum - 1);
    roomBtnList.remove(btnNum - 1);

    for (int i = btnNum - 1; i < gui.getRoomCnt(); i++) {
      roomBtnList.get(i).setRoomNum(i + 1);
      roomBtnList.get(i).mouseListenerRefresh();
      gui.getRoomDataList().get(i).setRoomNum(i + 1);
      System.out.println();
    }
    remove(scroller);
    refreshView();
  }

  public SelectDataPage getPopUpPage() {
    return popUpPage;
  }

  public void addTempBlock(Block block) {
    for (int i = 0; i < tempBlock.size(); i++) {
      if (tempBlock.get(i).equals(block))
        return;
    }
    tempBlock.add(block);
  }

  public void btnVisible(boolean b) {
    zoomInBtn.setVisible(b);
    zoomOutBtn.setVisible(b);
    addWorkBtn.setVisible(b);
    addNodeBtn.setVisible(b);
    addShareBlockBtn.setVisible(b);
  }

  /*
   * Ȯ�� ���, â ũ�� ��ȭ�ɶ� ���� �׸���
   */
  public void zoomRefresh() {

    MapManagePanel currentPanel = mapManagePanelList
        .get(gui.getCurrentRoomNum() - 1);
    GridBagLayout gbl = (GridBagLayout) currentPanel.getLayout();

    if (currentPanel.originImgWidth * currentPanel.zoom / 100 < scroller
        .getWidth()) {
      gbl.columnWidths[0] = (scroller.getWidth()
          - (currentPanel.originImgWidth * currentPanel.zoom / 100)) / 2;
      gbl.columnWidths[currentPanel.x + 1] = (scroller.getWidth()
          - (currentPanel.originImgWidth * currentPanel.zoom / 100)) / 2;
    } else {
      gbl.columnWidths[0] = 0;
      gbl.columnWidths[currentPanel.x + 1] = 0;
    }

    if (currentPanel.originImgHeight * currentPanel.zoom / 100 < scroller
        .getHeight())
      gbl.rowHeights[currentPanel.y] = scroller.getHeight()
          - (currentPanel.originImgHeight * currentPanel.zoom / 100);
    else
      gbl.rowHeights[currentPanel.y] = 0;

    for (int i = 0; i < currentPanel.x; i++)
      gbl.columnWidths[i + 1] = BLOCK_SIZE * currentPanel.zoom / 100;

    for (int i = 0; i < currentPanel.y; i++)
      gbl.rowHeights[i] = BLOCK_SIZE * currentPanel.zoom / 100;

    currentPanel.setLayout(gbl);

    GridBagConstraints gbc_button = new GridBagConstraints();
    gbc_button.fill = GridBagConstraints.BOTH;

  }

  public ArrayList<MapManagePanel> getMapManagePanelList() {
    return mapManagePanelList;
  }

  public void setMapManagePanelList(
      ArrayList<MapManagePanel> mapManagePanelList) {
    this.mapManagePanelList = mapManagePanelList;
  }

  /*
   * 
   */

  private class MapManagePanel extends JPanel {
    private RoomData roomData;
    private String filePath = "";

    private int zoom = 100; //
    private int x = 0, y = 0; // ��� ��, �� ����

    private Image img = null;

    private int originImgWidth, originImgHeight;

    public MapManagePanel(RoomData roomData) {
      this.roomData = roomData;

      if (roomData.getMap() != null)
        img = roomData.getMap();
    }

    public MapManagePanel() {
      tempBlock = new ArrayList<Block>();
    }

    public RoomData getRoomData() {
      return roomData;
    }

    public void setRoomData(RoomData roomData) {
      this.roomData = roomData;
    }

    public Image getImg() {
      return img;
    }

    public void setImg(Image img) {
      this.img = img;
      originImgWidth = img.getWidth(null);
      originImgHeight = img.getHeight(null);
      x = (int) (originImgWidth / BLOCK_SIZE) + 1;
      y = (int) (originImgHeight / BLOCK_SIZE) + 1;

      GridBagLayout gridBagLayout = new GridBagLayout();
      gridBagLayout.columnWidths = new int[x + 2]; // ���� �¿� 2��

      for (int i = 1; i < x + 1; i++)
        gridBagLayout.columnWidths[i] = BLOCK_SIZE;

      gridBagLayout.columnWidths[0] = 0;
      gridBagLayout.columnWidths[x + 1] = 0;

      if (originImgWidth < scroller.getWidth()) {
        gridBagLayout.columnWidths[0] = (getWidth()
            - (originImgWidth * zoom / 100)) / 2;
        gridBagLayout.columnWidths[x
            + 1] = (getWidth() - (originImgWidth * zoom / 100)) / 2;
      }

      gridBagLayout.rowHeights = new int[y + 1]; // ���� �� 1��
      for (int i = 0; i < y; i++)
        gridBagLayout.rowHeights[i] = BLOCK_SIZE;

      gridBagLayout.rowHeights[y] = 0;

      if (originImgHeight < scroller.getHeight()) {
        gridBagLayout.rowHeights[y] = getHeight()
            - (originImgHeight * zoom / 100);
      }

      gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
      gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };

    }

    @Override
    public void paintComponent(Graphics g) {
      if (img == null) {
        if (roomData.getMap() != null)
          img = roomData.getMap();
      }

      if (img != null) {
        if (originImgWidth * zoom / 100 < getWidth()) {
          g.drawImage(img, (getWidth() - (originImgWidth * zoom / 100)) / 2, 0,
              originImgWidth * zoom / 100, originImgHeight * zoom / 100, null);
        } else
          g.drawImage(img, 0, 0, originImgWidth * zoom / 100,
              originImgHeight * zoom / 100, null);

        setOpaque(false);
      } else
        super.paintComponent(g);

      if (img != null)
        zoomRefresh();
    }

  }

  /*
   * 
   */

  public class Block extends JButton implements ActionListener {
    private int blockNum;
    private int workNum = -1;
    private int realWorkNum = -1;
    private int roomNum;
    private int type = NONE;
    private int mouseState = NONE;

    private int shareRoomNum = -1;
    private int shareBlockNum = -1;

    private JPopupMenu popupMenu;
    private JMenuItem add, select;

    public Block(int num) {

      popupMenu = new JPopupMenu();
      add = new JMenuItem("���ù� �߰�");
      select = new JMenuItem("���ù� ����");

      popupMenu.add(add);
      popupMenu.add(select);

      add.addActionListener(this);
      select.addActionListener(this);

      roomNum = gui.getCurrentRoomNum();
      blockNum = num;
      setOpaque(false);

      addMouseMotionListener(new MouseAdapter() {
        public void mouseDragged(MouseEvent e) {
          // if(e.getButton() == MouseEvent.BUTTON1)
          if (editState == WORK) {
            drag = true;
            mouseState = MOUSE_PRESSED;

            mapManagePanelList.get(gui.getCurrentRoomNum() - 1).validate();
            mapManagePanelList.get(gui.getCurrentRoomNum() - 1).repaint();
          } else if (editState == NODE) {
            drag = true;
            mouseState = MOUSE_PRESSED;
          }
        }
      });

      BlockMouseListener listener = new BlockMouseListener();
      addMouseListener(listener);

      addKeyListener(new BlockKeyListener());
    }

    public void setType(int type) {
      this.type = type;
    }

    public int getType() {
      return type;
    }

    public void setMouseState(int state) {
      this.mouseState = state;
    }

    public int getMouseState() {
      return mouseState;
    }

    public int getNum() {
      return blockNum;
    }

    public void setNum(int num) {
      this.blockNum = num;
    }

    public int getWorkNum() {
      return workNum;
    }

    public void setWorkNum(int workNum) {
      this.workNum = workNum;
    }

    public int getRealWorkNum() {
      return realWorkNum;
    }

    public void setRealWorkNum(int realWorkNum) {
      this.realWorkNum = realWorkNum;
    }

    public WorkData addWork(String str) {
      WorkData workData = new WorkData();
      workData.setRealNum(gui.realWorkNumIncrement());

      realWorkNum = workData.getRealNum();

      workData.setWorkNum(gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
          .getWorkDataList().size() + 1);
      workData.setRoomNum(gui.getCurrentRoomNum());
      workData.setTitle(str);
      gui.getWorkHashMap().put(workData.getRealNum(), workData);
      gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1).getWorkDataList()
          .add(workData);
      gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1).workCntIncrement();

      return workData;
    }

    public void addToolTip(WorkData workData) {
      String preView = "";

      preView += "<html><center><h1>" + workData.getTitle() + "</h1>";
      preView += "<br/>";

      if (workData.getImage() != null && workData.getImage().size() != 0) {
        preView += "<img src=";
        preView += workData.getImageScr().get(0);
        preView += "width=\"100\" height=\"200\">";
      }

      preView += "<br/>";
      preView += workData.getSimpleContents();

      setToolTipText(preView);
    }

    public void addToolTip() {
      String preView = "";

      preView += "<html><center><h1>" + "���� ���" + "</h1>";
      preView += "<br/>";

      preView += gui.getRoomDataList().get(shareRoomNum - 1).getName();
      preView += "<br/>";
      preView += shareBlockNum;
      preView += " �� ���";

      setToolTipText(preView);
    }

    /*
     * ��Ͽ� �Ҵ�� ��ǰ ����
     */
    public void initBlock(Block block) {
      gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1).getWorkDataList()
          .get(block.workNum).setAssigned(false);
      block.workNum = -1;
      block.realWorkNum = -1;
      block.type = NONE;
      block.mouseState = NONE;
      block.validate();
      block.repaint();
    }

    /*
     * ��� Ŭ�� -> Ŭ�� ����
     * ��� ���, ��ǰ ���� �� ����
     */
    public void initValue() {
      drag = false;
      editState = NONE;
      for (int i = 0; i < tempBlock.size(); i++) {
        tempBlock.get(i).mouseState = NONE;
        tempBlock.get(i).workNum = -1;
        tempBlock.get(i).realWorkNum = -1;
      }
      tempBlock.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     * �� ��� ��ư�� �׸��� �Լ�
     */
    public void paint(Graphics g) {

      Graphics gButton = null;
      BufferedImage img = null;

      // ��Ͽ� �ƹ��͵� �������� �ʾ��� ��
      if (type == NONE) {
        if (mouseState == NONE) {
          img = blockImage;
          gButton = blockImage.getGraphics();
        } else if (mouseState == MOUSE_ENTERED || mouseState == MOUSE_PRESSED
            || mouseState == MOUSE_EXITED || mouseState == MOUSE_CLICKED) {
          img = enterBlockImage;
          gButton = enterBlockImage.getGraphics();
        }
      }

      // ����� ��ǰ���� ���� �Ǿ��� ��
      else if (type == WORK || type == SHARE) {
        if (mouseState == NONE) {
          img = workBlockImage;
          gButton = workBlockImage.getGraphics();
        } else if (mouseState == MOUSE_ENTERED || mouseState == MOUSE_PRESSED
            || mouseState == MOUSE_EXITED || mouseState == MOUSE_CLICKED) {
          img = enterWorkBlockImage;
          gButton = enterWorkBlockImage.getGraphics();
        }
      }
      // ����� ���� �����Ǿ�����
      else if (type == NODE) {
        if (mouseState == NONE) {
          img = nodeBlockImage;
          gButton = nodeBlockImage.getGraphics();
        } else if (mouseState == MOUSE_ENTERED || mouseState == MOUSE_PRESSED
            || mouseState == MOUSE_EXITED || mouseState == MOUSE_CLICKED) {
          img = enterNodeBlockImage;
          gButton = enterNodeBlockImage.getGraphics();
        }
      }

      gButton.setClip(g.getClip());

      Graphics2D g2d = (Graphics2D) g;

      AlphaComposite newComposite;
      //

      if (mouseState == NONE)
        newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f);

      else if (mouseState == MOUSE_CLICKED)
        newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f);

      else
        newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f);

      g2d.setComposite(newComposite);

      g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }

    public void actionPerformed(ActionEvent e) {
      if (!drag) {
        if (e.getSource() == add) {
          String str = null;
          str = JOptionPane.showInputDialog("��ǰ �̸�");

          // ��� ��������
          if (str == "" || str == null) {
            initValue();
            return;
          }
          WorkData workData = addWork(str);
          workData.setAssigned(true);
          addToolTip(workData);

          workNum = workData.getWorkNum();
          realWorkNum = workData.getRealNum();
        } else if (e.getSource() == select) {
          Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

          popUpPage.refresh();
          popUpPage.setLocation((d.width - popUpPage.getWidth()) / 2,
              (d.height - popUpPage.getHeight()) / 2);
          popUpPage.setVisible(true);

          System.out
              .println("���õ� �ε���(��ǰ��ȣ) : " + popUpPage.getSelectedIdx());

          if (popUpPage.getSelectedIdx() == -1) {
            initValue();
            return;
          }
          // ����� ���� ��ǰ���� ������ ��
          WorkData workData = gui.getRoomDataList()
              .get(gui.getCurrentRoomNum() - 1).getWorkDataList()
              .get(popUpPage.getSelectedIdx());
          workData.setAssigned(true);
          addToolTip(workData);

          workNum = workData.getWorkNum();
          realWorkNum = workData.getRealNum();
        }
        type = WORK;
      } else if (drag) {
        if (e.getSource() == add) {

          String str = null;
          str = JOptionPane.showInputDialog("���ý� �̸�");

          if (str == "" || str == null) {// ��� ��������
            initValue();
            return;
          }

          WorkData workData = addWork(str);
          workData.setAssigned(true);
          for (int i = 0; i < tempBlock.size(); i++) {
            tempBlock.get(i).setType(WORK);
            tempBlock.get(i).setMouseState(NONE);
            tempBlock.get(i).workNum = workData.getWorkNum();
            tempBlock.get(i).realWorkNum = workData.getRealNum();
            tempBlock.get(i).addToolTip(workData);
          }
        } else if (e.getSource() == select) {
          Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

          popUpPage.setLocation((d.width - popUpPage.getWidth()) / 2,
              (d.height - popUpPage.getHeight()) / 2);
          popUpPage.refresh();
          popUpPage.setVisible(true);

          WorkData workData = gui.getRoomDataList()
              .get(gui.getCurrentRoomNum() - 1).getWorkDataList()
              .get(popUpPage.getSelectedIdx());
          workData.setAssigned(true);
          for (int i = 0; i < tempBlock.size(); i++) {
            tempBlock.get(i).setType(WORK);
            tempBlock.get(i).setMouseState(NONE);
            tempBlock.get(i).workNum = workData.getWorkNum();
            tempBlock.get(i).realWorkNum = workData.getRealNum();
            tempBlock.get(i).addToolTip(workData);
          }

          tempBlock.clear();
        }
      }

      drag = false;
      editState = NONE;

      addWorkBtn.validate();
      addWorkBtn.repaint();

      tempBlock.clear();
      refreshView();
    }

    class BlockKeyListener extends KeyAdapter {
      public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());

        switch (e.getKeyCode()) {
        case 127: // delete
          if (mouseState == MOUSE_CLICKED) {
            if (type == NODE) {
              initBlock((Block) e.getComponent());
            } else if (type == WORK) {
              System.out.println("��ǰ ��ȣ(�ε���) : " + workNum);
              gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                  .getWorkDataList().get(workNum).setAssigned(false);

              for (int i = 0; i < gui.getRoomDataList()
                  .get(gui.getCurrentRoomNum() - 1).getBlock().size(); i++) {
                if (gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                    .getBlock().get(i).workNum == workNum) {
                  gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                      .getBlock().get(i).mouseState = MOUSE_CLICKED;
                  gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                      .getBlock().get(i).type = NONE;
                  gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                      .getBlock().get(i).mouseState = NONE;
                  gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                      .getBlock().get(i).validate();
                  gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                      .getBlock().get(i).repaint();
                }
              }
            }
          }
          break;
        case 113: // F2
        case 27: // esc
          ((Block) e.getComponent()).mouseState = NONE;
        }
      }
    }

    class BlockMouseListener implements MouseListener {
      public void mouseClicked(MouseEvent e) {
        // Ŭ���� �巡�װ� �ƴ� ������!
        if (e.getButton() == MouseEvent.BUTTON1)
          if (!drag) {
            // ���콺 ����
            switch (editState) {
            case NONE: // ���콺�� �Ϲ� �����̸鼭
              switch (type) {
              case NONE: // �Ϲ� ����� ��
                // �ƹ� �ϵ� �Ͼ�� �ʴ´�...
                break;
              case WORK: // ���ù� ����� ��
                for (int i = 0; i < gui.getRoomDataList()
                    .get(gui.getCurrentRoomNum() - 1).getBlock().size(); i++) {
                  if (gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                      .getBlock().get(i).workNum == workNum)
                    gui.getRoomDataList().get(gui.getCurrentRoomNum() - 1)
                        .getBlock().get(i).mouseState = MOUSE_CLICKED;
                }
                mouseState = MOUSE_CLICKED; // ����� ���õȴ�. �� ��ȭ�� �ʿ�
                break;
              case NODE: // ��� ����� ��
                mouseState = MOUSE_CLICKED; // ����� ���õȴ�. �� ��ȭ�� �ʿ�
                break;
              }
              break;

            case WORK: // ���콺�� ���ù� ���� �����϶�
              switch (type) {
              case NONE: // �Ϲ� ����� ��
                popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
                break;
              case WORK:

              case NODE:
                break;
              }
              break;
            case NODE: // ���콺�� ��� ���� �����̸鼭
              switch (type) {
              case NONE: // �Ϲ� ����� ��
                type = NODE;
                editState = NONE;
                validate();
                repaint();
                addNodeBtn.validate();
                addNodeBtn.repaint();
                break;
              case WORK:
                break;
              case NODE:
                // ��� ���� ��� �ձ�....
              }
              break;
            case SHARE: // ���콺�� ���� ��� ���� �����̸鼭
              switch (type) {
              case NONE:
                if (shareEdit) {
                  System.out.println("tempBlock ���� : " + tempBlock.size());
                  tempBlock.get(0).shareBlockNum = tempBlock.get(1).blockNum;
                  tempBlock.get(0).shareRoomNum = tempBlock.get(1).roomNum;
                  tempBlock.get(0).type = SHARE;
                  tempBlock.get(1).shareBlockNum = tempBlock.get(0).blockNum;
                  tempBlock.get(1).shareRoomNum = tempBlock.get(0).roomNum;
                  tempBlock.get(1).type = SHARE;

                  tempBlock.get(0).addToolTip();
                  tempBlock.get(1).addToolTip();

                  tempBlock.get(0).validate();
                  tempBlock.get(0).repaint();
                  tempBlock.get(1).validate();
                  tempBlock.get(1).repaint();

                  System.out.print(tempBlock.get(0).roomNum + "�� ���ý��� "
                      + tempBlock.get(0).blockNum + " �� �����");
                  System.out.println(tempBlock.get(0).shareRoomNum
                      + "�� ���ý��� " + tempBlock.get(0).shareBlockNum
                      + " �� ����� �����Ѵ�");
                  System.out.print(tempBlock.get(1).roomNum + "�� ���ý��� "
                      + tempBlock.get(1).blockNum + " �� �����");
                  System.out.println(tempBlock.get(1).shareRoomNum
                      + "�� ���ý��� " + tempBlock.get(1).shareBlockNum
                      + " �� ����� �����Ѵ�");

                  editState = NONE;
                  shareEdit = false;

                  setView(tempBlock.get(0).roomNum);
                  tempBlock.clear();

                  System.out.println("���� ��� ���� �Ϸ�");
                  return;
                }

                shareEdit = true;

                String[] list = new String[gui.getRoomCnt() - 1];

                for (int i = 0, j = 0; i < gui.getRoomCnt(); i++) {
                  if (i == gui.getCurrentRoomNum() - 1)
                    continue;

                  list[j++] = gui.getRoomDataList().get(i).getName();
                }

                String selectValue = (String) JOptionPane.showInputDialog(null,
                    "���ý� �̸�", "���� ����� �ִ� ���ý� ����",
                    JOptionPane.INFORMATION_MESSAGE, null, list, list[0]);

                for (int i = 0; i < gui.getRoomCnt(); i++)
                  if (gui.getRoomDataList().get(i).getName()
                      .compareTo(selectValue) == 0) {
                    setView(gui.getRoomDataList().get(i).getRoomNum());
                    return;
                  }
                break;
              }
            }
          }
      }

      public void mousePressed(MouseEvent e) {
        System.out
            .println(((Block) e.getComponent()).getNum() + " �� ��� ���콺 ����");
        addTempBlock((Block) e.getComponent());
        mouseState = MOUSE_PRESSED;
      }

      public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
          if (drag && editState == WORK) {
            drag = false;
            popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
          }
      }

      public void mouseEntered(MouseEvent e) {
        // System.out.println(((Block)e.getComponent()).getNum() + " �� ���
        // ����");
        // �巡�� ���� ��
        if (drag) {
          mouseState = MOUSE_ENTERED; // �巡�� �� ��Ͽ� ���� (���¸� ������ ���·�)

          if (type == NONE) // �Ϲ� ����� ��
            for (int i = 0; i < tempBlock.size(); i++) {
              // �巡�׵� ��Ͽ� ���Ե��ִ��� �˻�. ������ �׳� ����
              if (tempBlock.get(i).getNum() == blockNum)
                return;
            }
          // �巡�׵� ��Ͽ� ���� ����̸� �߰�
          addTempBlock((Block) e.getComponent());
        } else {
          mouseState = MOUSE_ENTERED;
        }
      }

      public void mouseExited(MouseEvent e) {
        if (drag) {
          if (mouseState == MOUSE_ENTERED)
            mouseState = MOUSE_EXITED;
        } else {
          mouseState = NONE;
        }
      }
    }
  }
}
