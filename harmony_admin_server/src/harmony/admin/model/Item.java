package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class Item {
  private int num = 0;
  private String title = "";
  private String artist = "";
  private String simpleContent = "";
  private String detailContent = "";
  private String size = "";
  private int areaNum = 0;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return this.artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getSimpleContent() {
    return this.simpleContent;
  }

  public void setSimpleContent(String simpleContent) {
    this.simpleContent = simpleContent;
  }

  public String getDetailContent() {
    return this.detailContent;
  }

  public void setDetailContent(String detailContent) {
    this.detailContent = detailContent;
  }

  public String getSize() {
    return this.size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public int getAreaNum() {
    return this.areaNum;
  }

  public void setAreaNum(int areaNum) {
    this.areaNum = areaNum;
  }
}
