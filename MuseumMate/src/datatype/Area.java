package datatype;

import java.io.Serializable;
import java.util.List;

import android.util.Log;
import android.util.SparseArray;

public class Area implements Serializable
{
	private int num;
	private String name;
	private String image;
	private int rowCount;
	private int columnCount;
	
	private Node[][] nodes; // [row][col]
	
	
	public Area(int num, String name, int rowCount, int columnCount)
	{
		this.num = num;
		this.name = name;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		
		nodes = new Node[rowCount][columnCount];
	}
	
	public int		getNumber()			{ return num; }
	public String	getName()			{ return name; }
	public int		getRowCount()		{ return rowCount; }
	public int		getColumnCount()	{ return columnCount; }
	public String	getImage()			{ return image; }
	
	public void	setImage(String image)	{ this.image = image; }
	
	public void addNode(Node node)
	{
		int row = node.getBlockNumber() / columnCount;
		int col = node.getBlockNumber() % columnCount;
		nodes[row][col] = node;
	}
	
	public Node getNode(int row, int col) { return nodes[row][col]; }
}
