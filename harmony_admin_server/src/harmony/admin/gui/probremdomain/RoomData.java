package harmony.admin.gui.probremdomain;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import harmony.admin.gui.MapManagePage.Block;

public class RoomData implements Serializable {

  private Image map = null;
  private String name = null;
  private String filePath = null;

  private ArrayList<WorkData> workDataList;

  private ArrayList<Block> block;

  private int workCnt, nodeCnt;
  private int roomNum; // GUI���� ���� ��ȣ (index)
  private int realNum; // �ѹ� �������� �ٲ��� ����

  private GridBagLayout gridBagLayout = null;

  public RoomData(String name) {
    this.name = name;
    workCnt = 0;
    nodeCnt = 0;

    workDataList = new ArrayList<WorkData>();
  }

  public Image getMap() {
    if (map == null)
      return null;

    return map;
  }

  public void setMap(Image map) {
    this.map = map;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public ArrayList<WorkData> getWorkDataList() {
    return workDataList;
  }

  public void setWorkDataList(ArrayList<WorkData> workDataList) {
    this.workDataList = workDataList;
  }

  public ArrayList<Block> getBlock() {
    return block;
  }

  public void setBlock(ArrayList<Block> block) {
    this.block = block;
  }

  public int getWorkCnt() {
    return workCnt;
  }

  public void setWorkCnt(int workCnt) {
    this.workCnt = workCnt;
  }

  public int getNodeCnt() {
    return nodeCnt;
  }

  public void setNodeCnt(int nodeCnt) {
    this.nodeCnt = nodeCnt;
  }

  public int getRoomNum() {
    return roomNum;
  }

  public void setRoomNum(int roomNum) {
    this.roomNum = roomNum;
  }

  public GridBagLayout getGridBagLayout() {
    return gridBagLayout;
  }

  public void setGridBagLayout(GridBagLayout gridBagLayout) {
    this.gridBagLayout = gridBagLayout;
  }

  public int getRealNum() {
    return realNum;
  }

  public void setRealNum(int realNum) {
    this.realNum = realNum;
  }

  public void workCntIncrement() {
    workCnt++;
  }

  public void workCntDecrement() {
    workCnt--;
  }
}
