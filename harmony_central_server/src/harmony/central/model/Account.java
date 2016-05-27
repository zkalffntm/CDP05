package harmony.central.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class Account {
  private String id = "";
  private String password = "";
  private int exhibitionNum = 0;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getExhibitionNum() {
    return this.exhibitionNum;
  }

  public void setExhibitionNum(int exhibitionNum) {
    this.exhibitionNum = exhibitionNum;
  }
}
