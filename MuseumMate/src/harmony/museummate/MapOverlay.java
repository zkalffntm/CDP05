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
import android.util.AttributeSet;
import android.view.View;

class MapOverlay extends View
{
	private static final int BLOCK_SIZE = 30;
	private static final float SPOT_SIZE = 20;
	
	private List<Point> pointList;
	private List<Line> lineList;
	
	private Bitmap selectedSpot;
	private Bitmap deSelectedSpot;
	private float density;
	
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
	
	private void initialize()
	{
		pointList = new ArrayList<Point>();
		lineList = new ArrayList<Line>();
		selectedSpot = BitmapFactory.decodeResource(getResources(), R.drawable.spot_selected);
		deSelectedSpot = BitmapFactory.decodeResource(getResources(), R.drawable.spot);
		density = getResources().getDisplayMetrics().density;
	}

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
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
		int itemNum, row, col;
		
		public Point(int itemNum, int row, int col)
		{
			this.itemNum = itemNum;
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