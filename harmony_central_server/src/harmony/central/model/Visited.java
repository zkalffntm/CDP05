package harmony.central.model;

import java.sql.Timestamp;

/**
 * 
 * @author Seongjun Park
 * @since 2016/6/6
 * @version 2016/6/6
 */
public class Visited {
  private int num;
  private String userId;
  private Timestamp date;
  private int exhibitionNum;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Timestamp getDate() {
    return this.date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public int getExhibitionNum() {
    return this.exhibitionNum;
  }

  public void setExhibitionNum(int exhibitionNum) {
    this.exhibitionNum = exhibitionNum;
  }

}
