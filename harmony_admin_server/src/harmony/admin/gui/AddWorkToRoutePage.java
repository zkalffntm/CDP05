package harmony.admin.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import harmony.admin.gui.probremdomain.WorkData;

public class AddWorkToRoutePage extends JDialog implements ActionListener {

  private GridBagConstraints constraints;
  private JPanel btnPanel;
  private JPanel mainPanel;
  private JScrollPane scroller;
  private JButton cancelBtn;
  private JButton okBtn;

  private WorkData workData;
  private ArrayList<SimpleDataPanel> simplePanelList;
  private ArrayList<WorkData> workList;

  private GUI_console gui;

  private int selectedRoomIdx = 0;
  private JPanel panel;
  private JComboBox comboBox;
  private Label label;

  private String[] roomNameList;
  private JButton btnNewButton;

  private int routeNum;

  public AddWorkToRoutePage(JFrame frame, String title) {
    super(frame, title, true);

    simplePanelList = new ArrayList<SimpleDataPanel>();

    setSize(500, 800);
    setResizable(false);

    gui = gui.getInstance();

    roomNameList = new String[gui.getRoomCnt()];

    for (int i = 0; i < gui.getRoomCnt(); i++) {
      roomNameList[i] = gui.getRoomDataList().get(i).getName();
    }

    btnPanel = new JPanel();
    getContentPane().add(btnPanel, BorderLayout.SOUTH);
    btnPanel.setLayout(new BorderLayout(0, 0));

    mainPanel = new JPanel();

    constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;

    GridBagLayout gbl_dataPanel = new GridBagLayout();

    gbl_dataPanel.columnWidths = new int[] { 0 };
    gbl_dataPanel.rowHeights = new int[] { 0 };
    gbl_dataPanel.columnWeights = new double[] { Double.MIN_VALUE };
    gbl_dataPanel.rowWeights = new double[] { Double.MIN_VALUE };

    mainPanel.setLayout(gbl_dataPanel);

    scroller = new JScrollPane(mainPanel);

    btnNewButton = new JButton("New button");
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
    gbc_btnNewButton.gridx = 0;
    gbc_btnNewButton.gridy = 0;
    mainPanel.add(btnNewButton, gbc_btnNewButton);
    scroller.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scroller.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    getContentPane().add(scroller, BorderLayout.CENTER);

    cancelBtn = new JButton("���");
    cancelBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        workList.clear();

        gui.getRmp().getPopUpPage().setVisible(false);
      }
    });
    btnPanel.add(cancelBtn, BorderLayout.EAST);

    okBtn = new JButton("Ȯ��");
    okBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        gui.getRmp().getPopUpPage().setVisible(false);

        gui.getRouteList().get(routeNum - 1).setWorkDataList(workList);

        System.out.println(routeNum + " �� ��õ���");
        for (int i = 0; i < gui.getRouteList().get(routeNum - 1)
            .getWorkDataList().size(); i++) {
          System.out.print(gui.getRouteList().get(routeNum - 1)
              .getWorkDataList().get(i).getRoomNum() + "�� �� : ");
          System.out.println(gui.getRouteList().get(routeNum - 1)
              .getWorkDataList().get(i).getTitle());
        }
      }
    });
    btnPanel.add(okBtn, BorderLayout.CENTER);

    panel = new JPanel();
    panel.setLayout(new BorderLayout());

    getContentPane().add(panel, BorderLayout.NORTH);

    label = new Label();
    label.setFont(new Font("���ý� ����", Font.BOLD, 20));
    label.setText("���ý� ����");

    comboBox = new JComboBox(roomNameList);
    comboBox.addActionListener(this);

    panel.add(comboBox, BorderLayout.CENTER);
    panel.add(label, BorderLayout.WEST);
  }

  public int getRouteNum() {
    return routeNum;
  }

  public void setRouteNum(int routeNum) {
    this.routeNum = routeNum;
  }

  public void refreshList() {
    if (gui.getRouteList().size() > routeNum - 1)
      workList = (ArrayList<WorkData>) (gui.getRouteList().get(routeNum - 1)
          .getWorkDataList().clone());

    else
      workList = new ArrayList<WorkData>();
  }

  public void refresh() {
    simplePanelList.clear();
    mainPanel.removeAll();

    constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;

    GridBagLayout gbl_dataPanel = new GridBagLayout();

    gbl_dataPanel.columnWidths = new int[] { SimpleDataPanel.WIDTH };
    gbl_dataPanel.rowHeights = new int[] { SimpleDataPanel.HEIGHT };
    gbl_dataPanel.columnWeights = new double[] { Double.MIN_VALUE };
    gbl_dataPanel.rowWeights = new double[] { Double.MIN_VALUE };

    mainPanel.setLayout(gbl_dataPanel);

    if (gui.getRoomCnt() > 0) {
      for (int i = 0; i < gui.getRoomDataList().get(selectedRoomIdx)
          .getWorkCnt(); i++) {
        constraints.gridy = i;
        SimpleDataPanel temp = new SimpleDataPanel(gui.getRoomDataList()
            .get(selectedRoomIdx).getWorkDataList().get(i));

        if (workList != null)
          for (int j = 0; j < workList.size(); j++) {
            if (workList.get(j).equals(gui.getRoomDataList()
                .get(selectedRoomIdx).getWorkDataList().get(i))) {
              temp.setSelected(true);
              temp.validate();
              temp.repaint();
            }
          }

        temp.setWorkIdx(i);

        temp.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (temp.isSelected()) {
              workList.remove((WorkData) (gui.getWorkHashMap()
                  .get((int) (temp.getWorkData().getHashNum()))));
              temp.setSelected(false);
            } else {
              workList.add((WorkData) (gui.getWorkHashMap()
                  .get((int) (temp.getWorkData().getHashNum()))));
              temp.setSelected(true);
            }
            temp.validate();
            temp.repaint();

            for (int i = 0; i < workList.size(); i++) {
              System.out.println("��õ��� ��ǰ�� : " + workList.size());
              System.out.println(workList.get(i).getTitle());
            }
          }
        });
        simplePanelList.add(temp);
        mainPanel.add(temp, constraints);
      }
      validate();
      repaint();
    }
  }

  public void paintComponents(Graphics g) {

  }

  public void actionPerformed(ActionEvent e) {
    JComboBox cb = (JComboBox) e.getSource();
    selectedRoomIdx = cb.getSelectedIndex();

    refresh();
  }
}
