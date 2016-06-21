package com.andrewson.mapview;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;

public class MapView extends View
{
	private static final float DEFAULT_MIN_MAGNIFICATION = 0.5f;
	private static final float DEFAULT_MAX_MAGNIFICATION = 10.0f;
	private static final float DEFAULT_MAGNIFICATION = 0.5f;

	public interface OnCoordinateClickListener
	{ public abstract void onCoordinateClick(float x, float y, MotionEvent event); }
	
	public interface OnPixelClickListener
	{ public abstract void onPixelClick(int x, int y, MotionEvent event); }
	
	public interface OnDrawablePointClickListener
	{ public abstract void onDrawablePointClick(DrawablePoint clickedPoint, MotionEvent event); }

	public interface OnInvalidateListener
	{ public abstract void onInvalidate(); }
	
	// Listeners
	private ScaleGestureDetector			scaleGestureDetector;
	private GestureDetector					gestureDetector;
	private OnCoordinateClickListener		onCoordinateClickListener;
	private OnPixelClickListener			onPixelClickListener;
	private OnInvalidateListener			onInvalidateListener;
	private OnDrawablePointClickListener	onDrawablePointClickListener;
	
	// Resources
	private Bitmap 				mapImage;
	private List<DrawablePoint>	pointList;
	private List<DrawableLine>	lineList;
	
	// Drawing Objects
	private Paint paint;
	
	// Device Properties
	private float density;
	
	// Drawing Variables
	private int		sourceWidth;
	private int		sourceHeight;
	private Rect	sourceRect;
	private Rect	destinationRect;
	private RectF	targetBoundRect;
	private float	magnification;
	private float	minMagnification;
	private float	maxMagnification;
	private PointF	lookingCenterInSource;
	

	public MapView(Context context)
	{ super(context); }
	
	
	public MapView(Context context, AttributeSet attrs)
	{ super(context, attrs); }
	
	
	public MapView(Context context, AttributeSet attrs, int defStyleAttr)
	{ super(context, attrs, defStyleAttr); }
	
	
	public void initialize(Bitmap mapImage)
	{
		// Initialize Listeners
		scaleGestureDetector = new ScaleGestureDetector(getContext(), onScaleGestureListener);
		gestureDetector = new GestureDetector(getContext(), onGestureListener);
		
		// Initialize Resources
		this.mapImage	= mapImage;
		pointList 		= new ArrayList<DrawablePoint>();
		lineList 		= new ArrayList<DrawableLine>();
	    
		// Initialize Device Properties
		density = getResources().getDisplayMetrics().density;

		// Initialize Drawing Objects
		paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setFilterBitmap(true);
	    paint.setDither(true);
	    paint.setStrokeWidth(density * 3);
	    paint.setColor(Color.BLUE);
	    
	    // Initialize Drawing Variables
		sourceWidth 			= mapImage.getWidth();
		sourceHeight			= mapImage.getHeight();
		sourceRect 				= new Rect();
		destinationRect 		= new Rect();
		targetBoundRect			= new RectF();
		magnification			= DEFAULT_MAGNIFICATION;
		maxMagnification		= DEFAULT_MAX_MAGNIFICATION;
		minMagnification		= DEFAULT_MIN_MAGNIFICATION;
		lookingCenterInSource 	= new PointF(sourceWidth * 0.5f, sourceHeight * 0.5f);
		
		// Show View
		invalidate();
	}
	
	public int getSourceWidth() { return sourceWidth; }
	public int getSourceHeight() { return sourceHeight; }
	public void setLookingCenterInSource(float xInSource, float yInSource)
	{
		lookingCenterInSource = new PointF(xInSource, yInSource);
		invalidate();
	}
	
	
	public void setOnPixelClickListener(OnPixelClickListener l)
	{ onPixelClickListener = l; }

	
	public void setOnCoordinateClickListener(OnCoordinateClickListener l)
	{ onCoordinateClickListener = l; }
	

	public void setOnDrawablePointClickListener(OnDrawablePointClickListener l)
	{ onDrawablePointClickListener = l; }
	
	
	public void setOnMapTransformListener(OnInvalidateListener l)
	{ onInvalidateListener = l; }
	
	
	public void addDrawablePoint(DrawablePoint drawablePoint)
	{ pointList.add(drawablePoint); }
	
	public void clearDrawablePoint()
	{ pointList.clear(); }

	public void addLine(Point startInSource, Point endInSource)
	{ lineList.add(new DrawableLine(startInSource, endInSource)); }

	public void clearLine()
	{ lineList.clear(); }
	
	public DrawablePoint getDrawablePoint(int xPixel, int yPixel)
	{
		for(DrawablePoint e : pointList)
			if(e.getDestinationRect().contains(xPixel, yPixel))
					return e;
		
		return null;
	}
	
	
	@Override
	public void invalidate()
	{
		if(mapImage != null)
		{
			limitLookingCenterBound();
			calculateTargetBound();
			calculateDrawablePointsDestination();
			calculateDrawableLinesDestination();
		}
		super.invalidate();
		
		if(onInvalidateListener != null)
			onInvalidateListener.onInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if(mapImage == null) return;
		
		// Set Source Rect
		sourceRect.left		= (targetBoundRect.left < 0) ? 0 : (int)targetBoundRect.left;
		sourceRect.top		= (targetBoundRect.top < 0) ? 0 : (int)targetBoundRect.top;
		sourceRect.right	= (targetBoundRect.right > sourceWidth) ? 
				sourceWidth : (int)targetBoundRect.right;
		sourceRect.bottom	= (targetBoundRect.bottom > sourceHeight) ? 
				sourceHeight : (int)targetBoundRect.bottom;
		
		// Set Destination Rect
		destinationRect.left	= (targetBoundRect.left < 0) ? 
				(int)(-targetBoundRect.left * magnification) : 0;
		destinationRect.top		= (targetBoundRect.top < 0) ? 
				(int)(-targetBoundRect.top * magnification) : 0;
		destinationRect.right	= (targetBoundRect.right > sourceWidth) ? 
				(int)(getWidth() - (targetBoundRect.right - sourceWidth) * magnification) : getWidth();
		destinationRect.bottom	= (targetBoundRect.bottom > sourceHeight) ? 
				(int)(getHeight() - (targetBoundRect.bottom - sourceHeight) * magnification) : getHeight();

		// Draw Map
		canvas.drawBitmap(mapImage, sourceRect, destinationRect, paint);

		// Draw Points
		for(DrawablePoint e : pointList)
			canvas.drawBitmap(e.getPointImage(), null, e.getDestinationRect(), paint);
			
		// Draw Lines
		for(DrawableLine e : lineList)
		{
			canvas.drawLine(e.getDestinationStart().x, e.getDestinationStart().y, 
							e.getDestinationEnd().x, e.getDestinationEnd().y, paint);
		}
	}
	
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(mapImage != null)
		{
			scaleGestureDetector.onTouchEvent(event);
			gestureDetector.onTouchEvent(event);
		}
		
		return true;
	}
		
	
	private SimpleOnScaleGestureListener onScaleGestureListener = 
			new SimpleOnScaleGestureListener()
	{
		private float initialMagnification;
		private PointF translation = new PointF();
		private PointF scalingCenter = new PointF();
		
		
		@Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
		{
			initialMagnification = magnification;
			translation.x = getWidth() * 0.5f - detector.getFocusX();
			translation.y = getHeight() * 0.5f - detector.getFocusY();
			scalingCenter.x = lookingCenterInSource.x - translation.x / initialMagnification;
			scalingCenter.y = lookingCenterInSource.y - translation.y / initialMagnification;
			
            return true;
        }

		
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			// Calculate and Limit magnification
			magnification = initialMagnification * detector.getScaleFactor();
			if(magnification > maxMagnification) magnification = maxMagnification;
			else if(magnification < minMagnification) magnification = minMagnification;

			// Calculate lookingCenterInSource
			lookingCenterInSource.x = scalingCenter.x + translation.x / magnification;
			lookingCenterInSource.y = scalingCenter.y + translation.y / magnification;
			
			invalidate();
			
			return false;
		}
	};
	
	
	private SimpleOnGestureListener onGestureListener = new SimpleOnGestureListener()
	{
		@Override
		public boolean onSingleTapUp(MotionEvent event)
		{
			if(onCoordinateClickListener != null)
				onCoordinateClickListener.onCoordinateClick(event.getX(), event.getY(), event);
			
			if(onPixelClickListener != null)
			{
				int xInSource = (int)(lookingCenterInSource.x + (event.getX() - getWidth()) / magnification);
				int yInSource = (int)(lookingCenterInSource.y + (event.getX() - getHeight()) / magnification);
				onPixelClickListener.onPixelClick(xInSource, yInSource, event);
			}
			
			if(onDrawablePointClickListener != null)
			{
				DrawablePoint clickedPoint = getDrawablePoint((int)event.getX(), (int)event.getY());
				if(clickedPoint != null)
					onDrawablePointClickListener.onDrawablePointClick(clickedPoint, event);
			}
			
			return false;
		}

		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			lookingCenterInSource.x += distanceX / magnification;
			lookingCenterInSource.y += distanceY / magnification;
			
			invalidate();
			
			return false;
		}
	};
	
	private void limitLookingCenterBound()
	{			
		if(lookingCenterInSource.x < 0) lookingCenterInSource.x = 0;
		else if(lookingCenterInSource.x >= sourceWidth)
			lookingCenterInSource.x = sourceWidth - 1;
		
		if(lookingCenterInSource.y < 0) lookingCenterInSource.y = 0;
		else if(lookingCenterInSource.y >= sourceHeight)
			lookingCenterInSource.y = sourceHeight - 1;
	}
	
	
	private void calculateTargetBound()
	{
		float halfBoundInSourceX = (getWidth() * 0.5f) / magnification;
		float halfBoundInSourceY = (getHeight() * 0.5f) / magnification;
		targetBoundRect.left 	= lookingCenterInSource.x - halfBoundInSourceX;
		targetBoundRect.top		= lookingCenterInSource.y - halfBoundInSourceY;
		targetBoundRect.right	= lookingCenterInSource.x + halfBoundInSourceX;
		targetBoundRect.bottom	= lookingCenterInSource.y + halfBoundInSourceY;
	}
	

	private void calculateDrawablePointsDestination()
	{
		for(DrawablePoint e : pointList)
		{
			float pixelHalfWidth =  e.getDrawWidth() * 0.5f * density;
			float pixelHalfHeight =  e.getDrawHeight() * 0.5f * density;

			int drawingCenterInViewX = getWidth() / 2 + 
					(int)((e.getXInSource() - lookingCenterInSource.x) * magnification);
			int drawingCenterInViewY = getHeight() / 2 + 
					(int)((e.getYInSource() - lookingCenterInSource.y) * magnification);
			e.getDestinationRect().left 	= (int)(drawingCenterInViewX - pixelHalfWidth);
			e.getDestinationRect().top 		= (int)(drawingCenterInViewY - pixelHalfHeight);
			e.getDestinationRect().right 	= (int)(drawingCenterInViewX + pixelHalfWidth);
			e.getDestinationRect().bottom	= (int)(drawingCenterInViewY + pixelHalfHeight);
		}
	}
	
	private void calculateDrawableLinesDestination()
	{
		for(DrawableLine e : lineList)
		{
			e.getDestinationStart().x = getWidth() / 2 + 
					(int)((e.getStartInSource().x - lookingCenterInSource.x) * magnification);
			e.getDestinationStart().y = getHeight() / 2 + 
					(int)((e.getStartInSource().y - lookingCenterInSource.y) * magnification);
			e.getDestinationEnd().x = getWidth() / 2 + 
					(int)((e.getEndInSource().x - lookingCenterInSource.x) * magnification);
			e.getDestinationEnd().y = getHeight() / 2 + 
					(int)((e.getEndInSource().y - lookingCenterInSource.y) * magnification);
		}
	}
}
