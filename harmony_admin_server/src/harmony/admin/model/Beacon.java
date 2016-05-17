package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class Beacon {
  private int minor = 0;
  private String comment = "";
  private int itemNum = 0;

  public int getMinor() {
    return this.minor;
  }

  public void setMinor(int minor) {
    this.minor = minor;
  }

  public String getComment() {
    return this.comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public int getItemNum() {
    return this.itemNum;
  }

  public void setItemNum(int itemNum) {
    this.itemNum = itemNum;
  }
}
