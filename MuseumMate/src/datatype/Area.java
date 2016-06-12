package datatype;

import java.io.Serializable;

import android.util.SparseArray;

public class Area implements Serializable
{
	private int num;
	private String name;
	private String image;
	
	transient private SparseArray<Node> nodeList;
	
	
	public Area(int num, String name)
	{
		this.num = num;
		this.name = name;
		nodeList = new SparseArray<Node>();
	}
	
	public int		getNumber()			{ return num; }
	public String	getName()			{ return name; }
	public String	getImage()			{ return image; }
	
	public void setNumber(int num)		{ this.num = num; }
	public void	setName(String name)	{ this.name = name; }
	public void	setImage(String image)	{ this.image = image; }
	
	public void addNode(Node node)
	{ nodeList.append(node.getBlockNumber(), node); }
	
	public SparseArray<Node> getNodes() { return nodeList; }
}
