package harmony.admin.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/17
 * @version 2016/5/17
 */
public class ShareBlock {
  private int num = 0;
  private int blockNum1 = 0;
  private int blockNum2 = 0;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public int getBlockNum1() {
    return this.blockNum1;
  }

  public void setBlockNum1(Integer blockNum1) {
    if (blockNum1 == null) {
      this.blockNum1 = 0;
    } else {
      this.blockNum1 = blockNum1;
    }
  }

  public int getBlockNum2() {
    return this.blockNum2;
  }

  public void setBlockNum2(Integer blockNum2) {
    if (blockNum2 == null) {
      this.blockNum2 = 0;
    } else {
      this.blockNum2 = blockNum2;
    }
  }
}
