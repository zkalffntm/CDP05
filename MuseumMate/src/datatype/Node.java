package datatype;

import java.io.Serializable;

public class Node implements Serializable
{
	public enum TYPE { EXHIBITION, LINK, NODE };
	
	private Area area;
	private int blockNum;
	
	public TYPE getType() { return TYPE.NODE; }
	
	public void place(Area area, int blockNum)
	{
		this.area = area;
		this.blockNum = blockNum;
		area.addNode(this);
	}
	
	public Area	getArea()			{ return area; }
	public int	getBlockNumber()	{ return blockNum; }
}