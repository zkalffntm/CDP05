package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class ItemImage {
  private int num = 0;
  private int seq = 0;
  private String image = "";
  private boolean main = false;
  private int itemNum = 0;
  private boolean imageEdited = false;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public int getSeq() {
    return this.seq;
  }

  public void setSeq(int seq) {
    this.seq = seq;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.imageEdited = !this.image.equals(image);
    this.image = image;
  }

  public boolean isMain() {
    return this.main;
  }

  public void setMain(boolean main) {
    this.main = main;
  }

  public int getItemNum() {
    return this.itemNum;
  }

  public void setItemNum(int itemNum) {
    this.itemNum = itemNum;
  }

  public boolean isImageEdited() {
    return this.imageEdited;
  }

  public void setImageEdited(boolean imageEdited) {
    this.imageEdited = imageEdited;
  }
}
