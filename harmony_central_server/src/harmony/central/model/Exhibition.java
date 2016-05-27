package harmony.central.model;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class Exhibition {
  private int num = 0;
  private String name = "";
  private int beaconMajor = 0;
  private String ip = "";
  private int port = 0;
  private String image = "";
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

  public int getBeaconMajor() {
    return this.beaconMajor;
  }

  public void setBeaconMajor(int beaconMajor) {
    this.beaconMajor = beaconMajor;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public int getPort() {
    return this.port;
  }

  public void setPort(int port) {
    this.port = port;
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
