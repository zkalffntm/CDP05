package harmony.central.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class Gps {
  private int num = 0;
  private double x = 0.0;
  private double y = 0.0;
  private int coverage = 0;
  private int exhibitionNum = 0;

  public int getNum() {
    return this.num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public double getX() {
    return this.x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return this.y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public int getCoverage() {
    return this.coverage;
  }

  public void setCoverage(int coverage) {
    this.coverage = coverage;
  }

  public int getExhibitionNum() {
    return this.exhibitionNum;
  }

  public void setExhibitionNum(int exhibitionNum) {
    this.exhibitionNum = exhibitionNum;
  }
}
