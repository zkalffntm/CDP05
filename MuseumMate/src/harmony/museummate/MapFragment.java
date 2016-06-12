package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import com.nhaarman.supertooltips.ToolTip;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import datatype.Area;
import datatype.Exhibition;
import datatype.Node;
import tools.CustomMsg;
import tools.LocationTracker;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MapFragment extends Fragment
{
	private static final float FLOAT_BIAS = 0.01f;
	
	private PhotoView photoMap;
	private PhotoViewAttacher attacher;
	private MapOverlay canvas;
	private FrameLayout frameLayout;
	private ProgressBar progressBar;
	private List<ExhibitionView> pointList;
	private SummaryRelativeLayout summaryRelativeLayout;
	private ToolTip toolTip;
    private SummaryView summaryView;
    
	private RectObserver observer;
	private MapLoadingHandler handler;
	
	private Area area;

	
	public MapFragment()
	{
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.map, container, false);
    	canvas = (MapOverlay)v.findViewById(R.id.canvas);
    	photoMap = (PhotoView)v.findViewById(R.id.mapview);
    	frameLayout = (FrameLayout)v.findViewById(R.id.map_frame);
    	progressBar = (ProgressBar)v.findViewById(R.id.progress);
        summaryRelativeLayout = (SummaryRelativeLayout)v.findViewById(R.id.tooltipRelativeLayout);

		//new DynamicLoader(getActivity()).setAreaImage(photoMap, area.getNumber(), handler);
    	// get informed on bitmap and set image
		
    	try
    	{
    		ContentResolver cr = getActivity().getContentResolver();
    		Bitmap bm = Images.Media.getBitmap(cr, 
    				Uri.parse("android.resource://harmony.museummate/" + R.drawable.map_sample));
        	photoMap.setImageBitmap(bm);
        	canvas.setMapWidth(bm.getWidth());
    	} catch(Exception e) { Log.i("test", e.getMessage()); }
    	
    	// Set Views
    	attacher = new PhotoViewAttacher(photoMap);
    	observer = new RectObserver();
    	observer.start();
    	canvas.setLocation(LocationTracker.getInstance().getCurrentExhibition());
    	area = LocationTracker.getInstance().getCurrentExhibition().getArea();
    	placeExhibitions();
		progressBar.setVisibility(View.INVISIBLE);
		
		// Set ToopTip
	    toolTip = new ToolTip().withShadow()
				                .withAnimationType(ToolTip.AnimationType.NONE);
    	return v;
    }

    public void updateViews(final RectF mapRect)
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
				frameLayout.bringToFront();
				summaryRelativeLayout.invalidate();
				canvas.invalidate();
				
				for(ExhibitionView e : pointList)
				{
					e.update(mapRect);
					e.bringToFront();
				}
				
				if(summaryView != null)
					try { summaryView.update(); } catch(Exception e) {}
			}
		});
    }
    
    private void placeExhibitions()
    {
    	pointList = new ArrayList<ExhibitionView>();
		SparseArray<Node> nodeList = area.getNodes();
		for(int i = 0; i < nodeList.size(); i++)
		{
			Node e = nodeList.valueAt(i);
			if(e.getType() == Node.TYPE.EXHIBITION)
			{
				ExhibitionView point = new ExhibitionView((Exhibition)e, this);
				frameLayout.addView(point);
				pointList.add(point);
			}
		}
    }
    
    public void showSummaryToolTip(ExhibitionView targetView)
    {
    	if(summaryView != null && summaryView.isShown())
    	{
    		if(!targetView.equals(summaryView.getExhibitionView()))
    		{
    			summaryView.remove();
    			summaryView = summaryRelativeLayout.showToolTipForView(toolTip, targetView);
    		}
    		else
    		{
    			summaryView.remove();
    			summaryView = null;
    		}
    	}
    	else summaryView = summaryRelativeLayout.showToolTipForView(toolTip, targetView);
    	summaryView.addRemoveButton();
    }
    
    public PhotoViewAttacher getMapAttacher()
    { return attacher; }
    
    class RectObserver extends Thread
    {
    	@Override
    	public void run()
    	{
    		RectF mapRectPrev = new RectF(attacher.getDisplayRect());
			updateViews(mapRectPrev);

			while(!isVisible()) try{ Thread.sleep(16); } catch(Exception e) {}
			
    		while(true)
        	{
        		try{ Thread.sleep(16); } catch(Exception e) {}

        		RectF mapRect = new RectF(attacher.getDisplayRect());

        		if(	Math.abs(mapRect.left 	- mapRectPrev.left) 	> FLOAT_BIAS ||
        			Math.abs(mapRect.right 	- mapRectPrev.right) 	> FLOAT_BIAS ||
        			Math.abs(mapRect.top 	- mapRectPrev.top) 		> FLOAT_BIAS ||
        			Math.abs(mapRect.bottom - mapRectPrev.bottom) 	> FLOAT_BIAS)
        		{
        			mapRectPrev = mapRect;
        			updateViews(mapRect);
        		}

            	if(!isVisible()) break;
        	}
    	}
    }
	
	class MapLoadingHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			progressBar.setVisibility(View.INVISIBLE);
			
			switch(msg.what)
			{
			case CustomMsg.SUCCESS:
				canvas.setMapWidth(msg.arg1);
		    	attacher = new PhotoViewAttacher(photoMap);
		    	observer = new RectObserver();
		    	observer.start();
				break;
				
			case CustomMsg.FAILED:
				Toast.makeText(getActivity(), R.string.unable_to_connect, Toast.LENGTH_LONG).show();
			}
			
			super.handleMessage(msg);
		}
	}
}
