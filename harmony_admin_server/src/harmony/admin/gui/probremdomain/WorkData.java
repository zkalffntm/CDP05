package harmony.admin.gui.probremdomain;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

public class WorkData implements Serializable {
  private ArrayList<Integer> imageRealNum = null;
  private ArrayList<Image> image = null;
  private ArrayList<String> imageScr = null;

  private String title = "";
  private String artist = "";
  private String simpleContents = "";
  private String contents = "";

  private int roomNum = 0;
  private int workNum = 0;
  private int hashNum = 0;
  private boolean assigned = false;
  private boolean edited = false;

  private int realNum = 0;

  public WorkData() {
    this.imageRealNum = new ArrayList<Integer>();
    this.image = new ArrayList<Image>();
    this.imageScr = new ArrayList<String>();
  }

  public void setWorkData(ArrayList<Integer> imageRealNum,
      ArrayList<Image> image, ArrayList<String> imageScr, String title,
      String artist, String simpleContents, String contents, int roomNum,
      int workNum) {
    this.imageRealNum = imageRealNum;
    this.image = image;
    this.imageScr = imageScr;
    this.title = title;
    this.artist = artist;
    this.simpleContents = simpleContents;
    this.contents = contents;
    this.roomNum = roomNum;
    this.workNum = workNum;
  }

  public ArrayList<Integer> getImageRealNum() {
    return this.imageRealNum;
  }
  
  public void setImageRealNum(ArrayList<Integer> imageRealNum) {
    this.imageRealNum = imageRealNum;
  }
  
  public ArrayList<String> getImageScr() {
    return imageScr;
  }

  public void setImageScr(ArrayList<String> imageScr) {
    this.imageScr = imageScr;
  }

  public int getHashNum() {
    return hashNum;
  }

  public void setHashNum(int hashNum) {
    this.hashNum = hashNum;
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

  public int getRealNum() {
    return this.realNum;
  }

  public void setRealNum(int realNum) {
    this.realNum = realNum;
  }
}
