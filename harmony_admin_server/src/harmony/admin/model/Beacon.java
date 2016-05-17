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
  private Item item = null;

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

  public Item getItem() {
    return this.item;
  }

  public void setItem(Item item) {
    this.item = item;
  }
}
