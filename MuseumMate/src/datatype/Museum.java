package datatype;

import java.io.Serializable;
import java.util.List;

public class Museum implements Serializable
{
	private static Museum selectedMuseum;
	
	// Basic Informations
	private int major;
	private String name;
	private String ip;
	private int port;

	// Containing Data
	private String noticeUrl;
	private long lastUpdate;
	private List<Area> areaList;
	private List<Exhibition> exhibitionList;
	private List<Node> nodeList;
	private List<Recommendation> recommendationList;
	
	
	public void select()							{ selectedMuseum = this; }
	public static Museum getSelectedMuseum() 		{ return selectedMuseum; }
	
	/********************************* Setters *********************************/
	public void setMajor(int major)		{ this.major = major; }
	public void setName(String name)	{ this.name = name; }
	public void setIP(String ip)		{ this.ip = ip; }
	public void setPort(int port)		{ this.port = port; }
	
	public void setNoticeUrl(String noticeUrl)
	{ this.noticeUrl = noticeUrl; }
	
	public void setLastUpdate(long lastUpdate)
	{ this.lastUpdate = lastUpdate; }
	
	public void setAreaList(List<Area> areaList)
	{ this.areaList = areaList; }
	
	public void setExhibitionList(List<Exhibition> exhibitionList)
	{ this.exhibitionList = exhibitionList; }

	public void setNodeList(List<Node> nodeList)
	{ this.nodeList = nodeList; }
	
	public void setRecommendationList(List<Recommendation> recommendationList)
	{ this.recommendationList = recommendationList; }
	/***************************************************************************/
	
	/********************************* Getters *********************************/
	public int		getMajor()	{ return major; }
	public String	getName()	{ return name; }
	public String	getIP()		{ return ip; }
	public int		getPort()	{ return port; }
	public String				getNoticeUrl()			{ return noticeUrl; }
	public long 				getLastUpdate()			{ return lastUpdate; }
	public List<Area> 			getAreaList()			{ return areaList; }
	public List<Exhibition> 	getExhibitionList()		{ return exhibitionList; }
	public List<Node> 			getNodeList()			{ return nodeList; }
	public List<Recommendation> getRecommendationList()	{ return recommendationList; }
	/***************************************************************************/
}
