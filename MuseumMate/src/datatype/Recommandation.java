package datatype;

import java.io.Serializable;

public class Recommandation implements Serializable
{
	private int num;
	private String summary;
	private int[] route;
	
	public Recommandation(int num, String summary, int[] route)
	{
		this.num = num;
		this.summary = summary;
		this.route = route;
	}
	
	public int		getNumber()		{ return num; }
	public String	getSummary()	{ return summary; }
	public int[]	getRoute()		{ return route; }
}