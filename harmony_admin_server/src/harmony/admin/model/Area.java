package harmony.admin.model;

/**
 * 
 * @author Seongjun park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class Area {
  private int num;
  private String name;
  private String image;
  private boolean imageEdited = false;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
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
