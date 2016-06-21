package datatype;

public class Link extends Node
{
	private Area destArea;
	private int destBlockNum;
	
	
	public Link(Area destArea, int destBlockNum)
	{
		this.destArea = destArea;
		this.destBlockNum = destBlockNum;
	}
	
	@Override
	public TYPE getType() { return TYPE.LINK; }
	
	public Area	getDestinationArea()		{ return destArea; }
	public int	getDestinationBlockNumber()	{ return destBlockNum; }
}
