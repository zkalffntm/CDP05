package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/18
 */
public class Block {
  private int num = 0;
  private int seq = 0;
  private int itemNum = 0;
  private int areaNum = 0;

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

  public int getItemNum() {
    return this.itemNum;
  }

  public void setItemNum(Integer itemNum) {
    if (itemNum == null) {
      this.itemNum = 0;
    } else {
      this.itemNum = itemNum;
    }
  }

  public int getAreaNum() {
    return this.areaNum;
  }

  public void setAreaNum(Integer areaNum) {
    if (areaNum == null) {
      this.areaNum = 0;
    } else {
      this.areaNum = areaNum;
    }
  }
}
