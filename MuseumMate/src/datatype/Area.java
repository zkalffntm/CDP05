package datatype;

import android.net.Uri;
import android.util.SparseArray;

public class Area
{
	private int num;
	private String name;
	private Uri image;
	
	private SparseArray<Item> itemList;
	
	public Area(int num, String name)
	{
		this.num = num;
		this.name = name;
		itemList = new SparseArray<Item>();
	}
	
	public int		getNumber()	{ return num; }
	public String	getName()	{ return name; }
	public Uri		getImage()	{ return image; }
	public void setNumber(int num)		{ this.num = num; }
	public void	setName(String name)	{ this.name = name; }
	public void	setImage(Uri image)		{ this.image = image; }
	
	public SparseArray<Item> getItemList() { return itemList; }
}
