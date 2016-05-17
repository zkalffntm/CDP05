package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class Block {
  private int num = 0;
  private int itemNum = 0;
  private int areaNum = 0;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public int getItemNum() {
    return this.itemNum;
  }

  public void setItemNum(int itemNum) {
    this.itemNum = itemNum;
  }

  public int getArea() {
    return this.areaNum;
  }

  public void setAreaNum(int areaNum) {
    this.areaNum = areaNum;
  }
}
