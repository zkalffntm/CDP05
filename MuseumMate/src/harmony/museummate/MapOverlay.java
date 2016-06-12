package harmony.museummate;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import datatype.Area;
import datatype.Exhibition;
import datatype.Node;

class MapOverlay extends View
{
	private static final int BLOCK_SIZE = 100;	// pixel scale
	
	private Area area;
	private List<Point> lineList;
	private Point selectedPoint;
	
	private Paint paint;
	private int mapWidth;
	private int columnCount;

	
	public MapOverlay(Context context)
	{
		super(context);
		initialize();
	}
	
	public MapOverlay(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		initialize();
	}

	public void initialize()
	{
		paint = new Paint();
		paint.setColor(Color.BLUE);
	}
	
	public void setLocation(Exhibition exhibition)
	{
		area = exhibition.getArea();
		selectedPoint = null;
	}
	
	public void setMapWidth(int imageWidth)
	{
		mapWidth = imageWidth;
		columnCount = (int)Math.ceil((double)mapWidth / BLOCK_SIZE);
	}	
	
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		float magnification = (float)getWidth() / mapWidth;
		/*
		for(Point e : lineList)
		{
			float lineStartX	= (BLOCK_SIZE * (e.startX + 0.5f)) * magnification;
			float lineStartY	= (BLOCK_SIZE * (e.startY + 0.5f)) * magnification;
			float lineEndX		= (BLOCK_SIZE * (e.endX + 0.5f)) * magnification;
			float lineEndY		= (BLOCK_SIZE * (e.endY + 0.5f)) * magnification;
			canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, paint);
		}*/
	}
	
	private void onClick(float clickX, float clickY)
	{
		// Get On-Map-Location and Calculate block index on map
		float magnification = (float)getWidth() / mapWidth;
		int blockX = (int)((clickX / magnification) / BLOCK_SIZE);
		int blockY = (int)((clickY / magnification) / BLOCK_SIZE);
		int blockIndex = blockX + blockY * columnCount;
		
		// Get clicked node and process
		Node clickedNode = area.getNodes().get(blockIndex);
		if(clickedNode != null)
		{
			Point newlySelected = new Point(blockX, blockY);
			if(	selectedPoint.x != newlySelected.x ||
				selectedPoint.y != newlySelected.y)
			{
				selectedPoint = newlySelected;
				invalidate();
			}
		}
	}
}