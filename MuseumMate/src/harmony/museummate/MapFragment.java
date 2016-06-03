package harmony.museummate;

import java.util.List;

import android.app.Fragment;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import datatype.Exhibition;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MapFragment extends Fragment
{
	private static final float FLOAT_BIAS = 0.01f;
	
	private Uri uriMap;
	private List<Exhibition> exhibitionList;
	
	private PhotoView photoMap;
	private PhotoViewAttacher attacher;
	private MapOverlay canvas;
	private RelativeLayout canvasLayout;
	
	
	private RectF mapRect;
	
	public MapFragment(Uri uriMap, List<Exhibition> exhibitionList)
	{
		this.uriMap = uriMap;
		this.exhibitionList = exhibitionList;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.map, container, false);
    	canvas = (MapOverlay)v.findViewById(R.id.canvas);
    	photoMap = (PhotoView)v.findViewById(R.id.mapview);
    	canvasLayout = (RelativeLayout)v.findViewById(R.id.canvas_layout);
    	photoMap.setImageURI(uriMap);
    	attacher = new PhotoViewAttacher(photoMap);
    	new RectObserver().start();
    	return v;
    }
    
    public void updateCanvas()
    {
    	getActivity().runOnUiThread(new Runnable()
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
        		try{ Thread.sleep(16); } catch(Exception e) {}
        		
        		mapRect = new RectF(attacher.getDisplayRect());
        		
        		if(	Math.abs(mapRect.left 	- mapRectPrev.left) 	> FLOAT_BIAS ||
        			Math.abs(mapRect.right 	- mapRectPrev.right) 	> FLOAT_BIAS ||
        			Math.abs(mapRect.top 	- mapRectPrev.top) 		> FLOAT_BIAS ||
        			Math.abs(mapRect.bottom - mapRectPrev.bottom) 	> FLOAT_BIAS)
        			updateCanvas();
        	}
    	}
    }
}
