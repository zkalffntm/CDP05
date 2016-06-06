package datatype;

public class Link extends Item
{
	private Area area;
	private int blockNum;
	
	
	public Link(Area area, int blockNum)
	{
		this.area = area;
		this.blockNum = blockNum;
	}
	
	@Override
	public TYPE getType() { return TYPE.LINK; }
	
	public Area	getArea()			{ return area; }
	public int	getBlockNumber()	{ return blockNum; }
}
