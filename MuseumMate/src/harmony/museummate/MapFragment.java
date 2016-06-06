package harmony.museummate;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import datatype.Area;
import datatype.Exhibition;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MapFragment extends Fragment
{
	private static final float FLOAT_BIAS = 0.01f;
	
	private static Area area;
	
	private PhotoView photoMap;
	private PhotoViewAttacher attacher;
	private MapOverlay canvas;
	private RelativeLayout canvasLayout;
	private RectObserver observer;
	
	private RectF mapRect;
	
	
	//public MapFragment(Area area)
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.map, container, false);
    	canvas = (MapOverlay)v.findViewById(R.id.canvas);
    	photoMap = (PhotoView)v.findViewById(R.id.mapview);
    	canvasLayout = (RelativeLayout)v.findViewById(R.id.canvas_layout);
    	
    	// initialize overlay
    	canvas.initialize(area);
    	
    	// get informed on bitmap and set image
    	try
    	{
    		ContentResolver cr = getActivity().getContentResolver();
    		Bitmap bm = Images.Media.getBitmap(cr, area.getImage());
        	photoMap.setImageBitmap(bm);
    	} catch(Exception e) { Log.i("test", e.getMessage()); }
    	
    	attacher = new PhotoViewAttacher(photoMap);
    	observer = new RectObserver();
    	observer.start();
    	return v;
    }

    public void updateCanvas()
    {
    	Activity currentActivity = getActivity();
    	if(currentActivity != null) currentActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				canvas.setLeft((int)mapRect.left);
				canvas.setRight((int)mapRect.right);
				canvas.setTop((int)mapRect.top);
				canvas.setBottom((int)mapRect.bottom);
				canvasLayout.bringToFront();
				canvas.invalidate();
			}
		});
    }
    
    class RectObserver extends Thread
    {
    	@Override
    	public void run()
    	{
    		RectF mapRectPrev = new RectF(attacher.getDisplayRect());

    		while(true)
        	{
        		try{ Thread.sleep(8); } catch(Exception e) {}
        		
        		mapRect = new RectF(attacher.getDisplayRect());
        		
        		if(	Math.abs(mapRect.left 	- mapRectPrev.left) 	> FLOAT_BIAS ||
        			Math.abs(mapRect.right 	- mapRectPrev.right) 	> FLOAT_BIAS ||
        			Math.abs(mapRect.top 	- mapRectPrev.top) 		> FLOAT_BIAS ||
        			Math.abs(mapRect.bottom - mapRectPrev.bottom) 	> FLOAT_BIAS)
        			updateCanvas();

            	if(!isVisible()) break;
        	}
    	}
    }
}
