package harmony.admin.gui.probremdomain;

import java.awt.Image;
import java.util.ArrayList;

public class RouteData {

  private int realNum = 0;
  private int routeNum; // index
  private String title;
  private ArrayList<WorkData> workDataList;
  private Image img;
  private String imgScr;

  public RouteData(String title, int routeNum) {
    this.routeNum = routeNum;
    this.title = title;

    workDataList = new ArrayList<WorkData>();
  }

  public int getRealNum() {
    return this.realNum;
  }
  
  public void setRealNum(int realNum) {
    this.realNum = realNum;
  }
  
  public int getRouteNum() {
    return routeNum;
  }

  public void setRouteNum(int routeNum) {
    this.routeNum = routeNum;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ArrayList<WorkData> getWorkDataList() {
    return workDataList;
  }

  public void setWorkDataList(ArrayList<WorkData> workDataList) {
    this.workDataList = workDataList;
  }

  public Image getImg() {
    return img;
  }

  public void setImg(Image img) {
    this.img = img;
  }
  
  public String getImgScr() {
    return this.imgScr;
  }
  
  public void setImgScr(String imgScr) {
    this.imgScr = imgScr;
  }
}
