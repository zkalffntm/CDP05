package harmony.admin.gui.probremdomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Data implements Serializable {
  private ArrayList<RouteData> routeList;
  private ArrayList<RoomData> roomDataList;
  private HashMap workHashMap;

  public Data(ArrayList<RouteData> routeList, ArrayList<RoomData> roomDataList,
      HashMap workHashMap) {
    this.routeList = routeList;
    this.roomDataList = roomDataList;
    this.workHashMap = workHashMap;
  }

  public Data() {

  }

  public ArrayList<RouteData> getRouteList() {
    return routeList;
  }

  public void setRouteList(ArrayList<RouteData> routeList) {
    this.routeList = routeList;
  }

  public ArrayList<RoomData> getRoomDataList() {
    return roomDataList;
  }

  public void setRoomDataList(ArrayList<RoomData> roomDataList) {
    this.roomDataList = roomDataList;
  }

  public HashMap getWorkHashMap() {
    return workHashMap;
  }

  public void setWorkHashMap(HashMap workHashMap) {
    this.workHashMap = workHashMap;
  }

}
