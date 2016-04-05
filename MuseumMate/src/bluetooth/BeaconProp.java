package bluetooth;

public class BeaconProp // 비콘 등록정보
{
	private String	uuid;
	private int		major;
	private int		minor;
	
	public String 	getUUID()	{ return uuid; }
	public int		getMajor()	{ return major; }	
	public int		getMinor()	{ return minor; }	
	
	public void setUUID(String uuid)	{ this.uuid = uuid; }
	public void setMajor(int major)		{ this.major = major; }
	public void setMinor(int minor)		{ this.minor = minor; }
}
