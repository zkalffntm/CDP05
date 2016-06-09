package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import datatype.Area;
import datatype.Exhibition;
import datatype.Museum;
import datatype.Node;
import tools.LocationTracker;

class MapOverlay extends View
{
	private static final int BLOCK_SIZE = 30;
	private static final float SPOT_SIZE = 20;
	
	private Area area;
	private List<Point> pointList;
	private List<Line> lineList;
	private Point selectedPoint;
	
	private Bitmap selectedSpot;
	private Bitmap deSelectedSpot;
	private float density;
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
		selectedSpot = BitmapFactory.decodeResource(getResources(), R.drawable.spot_selected);
		deSelectedSpot = BitmapFactory.decodeResource(getResources(), R.drawable.spot);
		density = getResources().getDisplayMetrics().density;
		setLocation(LocationTracker.getInstance().getCurrentExhibition());
	}
	
	public void setLocation(Exhibition exhibition)
	{
		this.area = exhibition.getArea();
		pointList = new ArrayList<Point>();
		lineList = new ArrayList<Line>();
		selectedPoint = null;
		
		SparseArray<Node> placementList = area.getPlacements();
		for(int i = 0; i < placementList.size(); i++)
		{
			Node e = placementList.valueAt(i);
			if(e.getType() == Node.TYPE.EXHIBITION)
			{
				int id = ((Exhibition)e).getId();
				pointList.add(new Point(id % columnCount, id / columnCount));
			}
		}
			
		invalidate();
	}
	
	public void setMapWidth(int imageWidth)
	{
		mapWidth = imageWidth;
		columnCount = (int)Math.ceil((double)mapWidth / BLOCK_SIZE);
	}
	
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		this.getWidth();
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		
		new RectF();
		for(Point e : pointList)
			canvas.drawBitmap(deSelectedSpot, null, dst, paint);
		canvas.drawRect(50, 100, 50+100, 100+100, paint);
		
		/*
		Paint paint3 = new Paint();
		paint3.setColor(Color.BLACK);
		
		canvas.drawLine(200, 500, 500, 600, paint3);
		*/
	}
	
	class Point
	{
		int row, col;
		
		public Point(int row, int col)
		{
			this.row = row;
			this.col = col;
		}
	}
	
	class Line
	{
		int rowFrom, colFrom;
		int rowTo, colTo;
		
		public Line(int rowFrom, int colFrom, int rowTo, int colTo)
		{
			this.rowFrom = rowFrom;
			this.colFrom = colFrom;
			this.rowTo = rowTo;
			this.colTo = colTo;
		}
	}
}