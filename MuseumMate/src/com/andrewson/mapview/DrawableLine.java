package com.andrewson.mapview;

import android.graphics.Point;

public class DrawableLine
{
	private Point startInSource;
	private Point endInSource;
	private Point destinationStart;
	private Point destinationEnd;

	public DrawableLine(Point startInSource, Point endInSource)
	{
		this.startInSource = startInSource;
		this.endInSource = endInSource;
		destinationStart = new Point();
		destinationEnd = new Point();
	}
	
	public Point getStartInSource()		{ return startInSource; }
	public Point getEndInSource()		{ return endInSource; }
	public Point getDestinationStart()	{ return destinationStart; }
	public Point getDestinationEnd()	{ return destinationEnd; }

	public void setStartInSource(Point startInSource)		{ this.startInSource = startInSource; }
	public void setEndInSource(Point endInSource)			{ this.endInSource = endInSource; }
	public void setDestinationStart(Point destinationStart)	{ this.destinationStart = destinationStart; }
	public void setDestinationEnd(Point destinationEnd)		{ this.destinationEnd = destinationEnd; }
}
