package com.andrewson.mapview;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

public class DrawablePoint
{
	private Bitmap	pointImage;
	private int		xInSource;
	private int		yInSource;
	private int 	drawWidth;	// in dp
	private int 	drawHeight; // in dp
	private Rect	destinationRect;
	
	public DrawablePoint(Bitmap pointImage, int drawWidth, int drawHeight, int xInSource, int yInSource)
	{
		this.pointImage	= pointImage;
		this.drawWidth	= drawWidth;
		this.drawHeight = drawHeight;
		this.xInSource	= xInSource;
		this.yInSource	= yInSource;
		destinationRect = new Rect();
	}
	
	public Bitmap	getPointImage()			{ return pointImage; }
	public int		getDrawWidth()			{ return drawWidth; }
	public int		getDrawHeight()			{ return drawHeight; }
	public int		getXInSource()			{ return xInSource; }
	public int		getYInSource()			{ return yInSource; }
	public Rect		getDestinationRect()	{ return destinationRect; }
	
	public void setPointImage(Bitmap pointImage)
	{ this.pointImage = pointImage; }

	public void	setXInSource(int xInSource)	{ this.xInSource = xInSource; }
	public void	setYInSource(int yInSource)	{ this.yInSource = yInSource; }
	public void setDestinationRect(Rect destinationRect)
	{ this.destinationRect = destinationRect; }
}