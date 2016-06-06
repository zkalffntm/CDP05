package datatype;

public class Location
{
	private Area area;
	private int blockNum;
	private Item item;
	
	
	public Location(Area area, int blockNum, Item item)
	{
		this.area = area;
		this.blockNum = blockNum;
		this.item = item;
	}
	
	public Area	getArea()			{ return area; }
	public int	getBlockNumber()	{ return blockNum; }
	public Item	getItem()			{ return item; }
}
