package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/6/10
 * @version 2016/6/10
 */
public class Node {
  private int num = 0;
  private int blockNum = 0;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public int getBlockNum() {
    return this.blockNum;
  }

  public void setBlockNum(Integer blockNum) {
    if (blockNum == null) {
      this.blockNum = 0;
    } else {
      this.blockNum = blockNum;
    }
  }
}
