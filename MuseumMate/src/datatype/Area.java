package datatype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

public class Area implements Serializable
{
	private int num;
	private String name;
	private String image;
	private int columnCount;
	
	transient private List<Location> locationList;
	
	
	public Area(int num, String name, String image)
	{
		this.num = num;
		this.name = name;
		this.image = image;
		locationList = new ArrayList<Location>();
	}
	
	public int		getNumber()			{ return num; }
	public String	getName()			{ return name; }
	public String	getImage()			{ return image; }
	public int		getColumnCount()	{ return columnCount; }
	
	public void setNumber(int num)				{ this.num = num; }
	public void	setName(String name)			{ this.name = name; }
	public void	setImage(String image)			{ this.image = image; }
	public void setColumnCount(int columnCount)	{ this.columnCount = columnCount; }
	
	public List<Location> getLocatedItem() { return locationList; }
}
