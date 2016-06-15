package datatype;

import java.io.Serializable;

public class Recommandation implements Serializable
{
	private int num;
	private String name;
	private String summary;
	private int[] route;
	
	public Recommandation(int num, String name, String summary, int[] route)
	{
		this.num = num;
		this.name = name;
		this.summary = summary;
		this.route = route;
	}
	
	public int		getNumber()		{ return num; }
	public String	getName()		{ return name; }
	public String	getSummary()	{ return summary; }
	public int[]	getRoute()		{ return route; }
}