package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class Recommend {
  private int num = 0;
  private String content = "";
  private String image = "";
  private boolean imageEdited = false;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.imageEdited = !this.image.equals(image);
    this.image = image;
  }

  public boolean isImageEdited() {
    return this.imageEdited;
  }

  public void setImageEdited(boolean imageEdited) {
    this.imageEdited = imageEdited;
  }
}
