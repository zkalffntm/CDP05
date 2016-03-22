package element;

/**
 * Exhibition 테이블 레코드 클래스
 * 
 * @author Seongjun Park
 * @since 2016/3/22
 * @version 2016/3/22
 */
public class Exhibition {
	private int num;
	private String name;
	private String ip;
	private double gpsX;
	private double gpsY;

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

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public double getGpsX() {
		return this.gpsX;
	}

	public void setGpsX(double gpsX) {
		this.gpsX = gpsX;
	}

	public double getGpsY() {
		return this.gpsY;
	}

	public void setGpsY(double gpsY) {
		this.gpsY = gpsY;
	}
}
