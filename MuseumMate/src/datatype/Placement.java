package datatype;

import java.io.Serializable;

public abstract class Placement implements Serializable
{
	public enum TYPE { EXHIBITION, LINK };
	
	private Area area;
	private int blockNum;
	
	public abstract TYPE getType();
	
	public void place(Area area, int blockNum)
	{
		this.area = area;
		this.blockNum = blockNum;
		area.addPlacement(this);
	}
	
	public Area	getArea()			{ return area; }
	public int	getBlockNumber()	{ return blockNum; }
}
