package harmony.admin.gui.probremdomain;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

public class WorkData implements Serializable {
  private ArrayList<Image> image = null;
  private ArrayList<String> imageScr = null;

  private String title = null;
  private String artist = null;;
  private String simpleContents = null;;
  private String contents = null;;

  private int roomNum = 0;
  private int workNum = 0;
  private int realNum = 0;
  private boolean assigned = false;
  private boolean edited = false;

  public WorkData() {

  }

  public WorkData(ArrayList<Image> image, String title, String artist,
      String simpleContents, String contents, int roomNum, int workNum) {

    this.image = image;
    this.title = title;
    this.artist = artist;
    this.simpleContents = simpleContents;
    this.contents = contents;
    this.roomNum = roomNum;
    this.workNum = workNum;
  }

  public void setWorkData(ArrayList<Image> image, ArrayList<String> imageScr,
      String title, String artist, String simpleContents, String contents,
      int roomNum, int workNum) {

    this.image = image;
    this.imageScr = imageScr;
    this.title = title;
    this.artist = artist;
    this.simpleContents = simpleContents;
    this.contents = contents;
    this.roomNum = roomNum;
    this.workNum = workNum;
  }

  public ArrayList<String> getImageScr() {
    return imageScr;
  }

  public void setImageScr(ArrayList<String> imageScr) {
    this.imageScr = imageScr;
  }

  public int getRealNum() {
    return realNum;
  }

  public void setRealNum(int realNum) {
    this.realNum = realNum;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ArrayList<Image> getImage() {
    return image;
  }

  public void setImage(ArrayList<Image> image) {
    this.image = image;
  }

  public String getTitle() {
    return title;
  }

  public void setName(String title) {
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getSimpleContents() {
    return simpleContents;
  }

  public void setSimpleContents(String simpleContents) {
    this.simpleContents = simpleContents;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public int getRoomNum() {
    return roomNum;
  }

  public void setRoomNum(int roomNum) {
    this.roomNum = roomNum;
  }

  public int getWorkNum() {
    return workNum;
  }

  public void setWorkNum(int workNum) {
    this.workNum = workNum;
  }

  public boolean getAssigned() {
    return assigned;
  }

  public void setAssigned(boolean assigned) {
    this.assigned = assigned;
  }

  public boolean isEdited() {
    return edited;
  }

  public void setEdited(boolean edited) {
    this.edited = edited;
  }
}
