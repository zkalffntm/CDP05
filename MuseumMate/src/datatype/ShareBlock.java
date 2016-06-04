package datatype;

public class ShareBlock implements Item
{
	private Area area;
	private int blockNum;
	
	
	public ShareBlock(Area area, int blockNum)
	{
		this.area = area;
		this.blockNum = blockNum;
	}
	
	@Override
	public TYPE getType() { return TYPE.SHAREBLOCK; }
	
	public Area getArea()		{ return area; }
	public int getBlockNumber()	{ return blockNum; }
}
