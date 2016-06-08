package datatype;

import java.io.Serializable;

public class Recommandation implements Serializable
{
	private String summary;
	private int[] route;
	
	public Recommandation(String summary, int[] route)
	{
		this.summary = summary;
		this.route = route;
	}
	
	public String	getSummary()	{ return summary; }
	public int[]	getRoute()		{ return route; }
}