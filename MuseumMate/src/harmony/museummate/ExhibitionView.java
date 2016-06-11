package harmony.museummate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import datatype.Exhibition;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ExhibitionView extends ImageView implements OnClickListener
{
	private static final int BLOCK_SIZE = 100;	// pixel scale
	private static final float SPOT_SIZE = 40;	// dp scale (also used in click event)
	
	private static Bitmap selectedSpot;
	private static Bitmap deSelectedSpot;
	private static int radius = 0;	// Correspond to SPOT_SIZE
	
	private Exhibition exhibition;
	private PhotoViewAttacher mapAttatcher;
	private int mapIntrinsicWidth;
	private PointF relCoordinate;
	private Point absCoordinate;
	
	public ExhibitionView(Exhibition exhibition, PhotoViewAttacher mapAttatcher)
	{
		super(mapAttatcher.getImageView().getContext());

		// Static Value Configuration
    	if(radius == 0)
    	{
    		// Load Resource
        	selectedSpot = BitmapFactory.decodeResource(getResources(), R.drawable.spot_selected);
        	deSelectedSpot = BitmapFactory.decodeResource(getResources(), R.drawable.spot);
        	
        	// Get density
    		DisplayMetrics metrics = new DisplayMetrics();
    		WindowManager windowManager = (WindowManager)mapAttatcher.getImageView().
    				getContext().getSystemService(Context.WINDOW_SERVICE);
    		windowManager.getDefaultDisplay().getMetrics(metrics);
    		radius = (int)(SPOT_SIZE * metrics.density) / 2;
    	}
    	
    	// Set Resource
    	setImageBitmap(deSelectedSpot);
    	
		// Set Size of the Spot
		LayoutParams params = new LayoutParams(radius * 2, radius * 2);
		setLayoutParams(params);
		
		// Caculate Location of the Spot
		mapIntrinsicWidth = mapAttatcher.getImageView().getDrawable().getIntrinsicWidth();
		int columnCount = (int)Math.ceil((double)mapIntrinsicWidth / BLOCK_SIZE);
		absCoordinate = new Point();
		relCoordinate = new PointF();
		relCoordinate.x = (exhibition.getBlockNumber() % columnCount + 0.5f) * BLOCK_SIZE;
		relCoordinate.y = (exhibition.getBlockNumber() / columnCount + 0.5f) * BLOCK_SIZE;
		
		// Other Initialization
		this.exhibition = exhibition;
		this.mapAttatcher = mapAttatcher;
		setOnClickListener(this);
	}
	
	public void update(RectF mapRect)
	{
		float magnification = mapRect.width() / mapIntrinsicWidth;

		Log.i("test", "" + mapIntrinsicWidth);
		LayoutParams params = (LayoutParams)getLayoutParams();
		absCoordinate.x = (int)(mapRect.left + relCoordinate.x * magnification);
		absCoordinate.y = (int)(mapRect.top + relCoordinate.y * magnification);
		params.leftMargin = absCoordinate.x - radius;
		params.topMargin = absCoordinate.y - radius;
		setLayoutParams(params);
	}
	
	public Point getAbsoluteCoordinate()
	{ return new Point(absCoordinate); }

	@Override
	public void onClick(View v)
	{
		
	}
	
	public Exhibition getExhibition() { return exhibition; }
}