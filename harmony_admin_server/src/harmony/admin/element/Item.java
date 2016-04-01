package harmony.admin.element;

/**
 * item 테이블 레코드 클래스.
 * 
 * @author Seongjun Park
 * @since 2016/4/1
 * @version 2016/4/1
 */
public class Item {
	private int num;
	private int mapNum;
	private int blockNum;
	private String title;
	private String artist;
	private String image;
	private String content;

	public int getNum() {
		return this.num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getMapNum() {
		return this.mapNum;
	}

	public void setMapNum(int mapNum) {
		this.mapNum = mapNum;
	}

	public int getBlockNum() {
		return this.blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return this.artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
