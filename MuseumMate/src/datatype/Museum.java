package datatype;

import java.io.Serializable;
import java.util.List;

public class Museum implements Serializable
{
	// Basic Informations
	private int major;
	private String name;
	private String ip;
	private int port;

	// Containing Data
	private long lastUpdated;
	private List<Area> areaList;
	private List<Exhibition> exhibitionList;
	private List<Link> linkList;
	private List<Location> locationList;
	
	
	/********************************* Setters *********************************/
	public void setMajor(int major)		{ this.major = major; }
	public void setName(String name)	{ this.name = name; }
	public void setIP(String ip)		{ this.ip = ip; }
	public void setPort(int port)		{ this.port = port; }
	
	public void setLastUpdated(long lastUpdated)
	{ this.lastUpdated = lastUpdated; }
	
	public void setAreaList(List<Area> areaList)
	{ this.areaList = areaList; }
	
	public void setExhibitionList(List<Exhibition> exhibitionList)
	{ this.exhibitionList = exhibitionList; }
	
	public void setLinkList(List<Link> linkList)
	{ this.linkList = linkList; }
	
	public void setLocationList(List<Location> locationList)
	{ this.locationList = locationList; }
	/***************************************************************************/
	
	/********************************* Getters *********************************/
	public int		getMajor()	{ return major; }
	public String	getName()	{ return name; }
	public String	getIP()		{ return ip; }
	public int		getPort()	{ return port; }
	public long getLastUpdated()				{ return lastUpdated; }
	public List<Area> getAreaList()				{ return areaList; }
	public List<Exhibition> getExhibitionList()	{ return exhibitionList; }
	public List<Link> getLinkList()				{ return linkList; }
	public List<Location> getLocationList()		{ return locationList; }
	/***************************************************************************/
}
