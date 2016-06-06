package datatype;

import java.io.Serializable;

import android.util.SparseArray;

public class Area implements Serializable
{
	private int num;
	private String name;
	private String image;
	private int columnCount;
	
	transient private SparseArray<Placement> placementList;
	
	
	public Area(int num, String name)
	{
		this.num = num;
		this.name = name;
		placementList = new SparseArray<Placement>();
	}
	
	public int		getNumber()			{ return num; }
	public String	getName()			{ return name; }
	public String	getImage()			{ return image; }
	public int		getColumnCount()	{ return columnCount; }
	
	public void setNumber(int num)				{ this.num = num; }
	public void	setName(String name)			{ this.name = name; }
	public void	setImage(String image)			{ this.image = image; }
	public void setColumnCount(int columnCount)	{ this.columnCount = columnCount; }
	
	public void addPlacement(Placement placement)
	{ placementList.append(placement.getBlockNumber(), placement); }
	
	public SparseArray<Placement> getplacements() { return placementList; }
}
