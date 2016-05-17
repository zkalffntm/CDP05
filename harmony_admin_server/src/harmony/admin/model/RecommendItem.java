package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class RecommendItem {
  private int num = 0;
  private int seq = 0;
  private int recommendNum = 0;
  private int itemNum = 0;

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

  public int getRecommendNum() {
    return this.recommendNum;
  }

  public void setRecommendNum(int recommendNum) {
    this.recommendNum = recommendNum;
  }

  public int getItemNum() {
    return this.itemNum;
  }

  public void setItemNum(int itemNum) {
    this.itemNum = itemNum;
  }
}
