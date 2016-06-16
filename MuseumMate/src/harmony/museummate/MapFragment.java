package harmony.museummate;

import com.andrewson.mapview.DrawablePoint;
import com.andrewson.mapview.MapView;
import com.andrewson.mapview.MapView.OnDrawablePointClickListener;
import com.andrewson.mapview.MapView.OnMapTransformListener;
import com.nhaarman.supertooltips.ToolTip;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import datatype.Area;
import datatype.Exhibition;
import datatype.Museum;
import datatype.Node;
import tools.CustomMsg;
import tools.DynamicLoader;
import tools.LocationTracker;

public class MapFragment extends Fragment
{	
	// Singleton /////////////////////////////////////////////////
	private static MapFragment self;							//
	public static MapFragment getInstance()						//
	{ if(self == null) self = new MapFragment(); return self; }	//
	private MapFragment() {}									//
	//////////////////////////////////////////////////////////////

	private static final int BLOCK_SIZE = 100;	// pixel scale
	private static final int SPOT_SIZE = 40;	// dp scale (also used in click event)
	
	private MapView mapView;
	private FrameLayout frameLayout;
	private ProgressBar progressBar;
	private SummaryRelativeLayout summaryRelativeLayout;
	
	private ToolTip toolTip;
    private SummaryView summaryView;
    private View dummyView;
    private ExhibitionPoint clickedPoint;
	
    private Bitmap bitmapSelected;
    private Bitmap bitmapDeselected;
    
	private MapLoadingHandler mapLoadingHandler;
	
	private Area area;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.map, container, false);
    	mapView = (MapView)v.findViewById(R.id.mapview);
    	frameLayout = (FrameLayout)v.findViewById(R.id.map_frame);
    	progressBar = (ProgressBar)v.findViewById(R.id.progress);
        summaryRelativeLayout = (SummaryRelativeLayout)v.findViewById(R.id.tooltipRelativeLayout);
	    
	    // Get Resources
	    bitmapSelected = BitmapFactory.decodeResource(
	    		getContext().getResources(), R.drawable.spot_selected);
	    bitmapDeselected = BitmapFactory.decodeResource(
	    		getContext().getResources(), R.drawable.spot);
    	
        // Get Current location
        lookAt(LocationTracker.getInstance().getCurrentExhibition());

		// Set ToopTip
	    toolTip = new ToolTip().withShadow().withColor(Color.parseColor("#F0E9E9"))
				                .withAnimationType(ToolTip.AnimationType.NONE);
	    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(SPOT_SIZE, SPOT_SIZE);
	    dummyView = new View(getContext());
	    dummyView.setLayoutParams(params);
	    frameLayout.addView(dummyView);
    	
    	return v;
    }
    
    private void placeExhibitions()
    {
		SparseArray<Node> nodeList = area.getNodes();
		for(int i = 0; i < nodeList.size(); i++)
		{
			Node e = nodeList.valueAt(i);
			if(e.getType() == Node.TYPE.EXHIBITION)
			{
				// Caculate Location of the Spot
				int columnCount = (int)Math.ceil((double)mapView.getSourceWidth() / BLOCK_SIZE);
				int xInSource = (int)((e.getBlockNumber() % columnCount + 0.5f) * BLOCK_SIZE);
				int yInSource = (int)((e.getBlockNumber() / columnCount + 0.5f) * BLOCK_SIZE);
				
				int exhibitionNumber = Museum.getSelectedMuseum().getExhibitionList().indexOf((Exhibition)e);
				ExhibitionPoint drawablePoint = new ExhibitionPoint(exhibitionNumber,
						bitmapDeselected, SPOT_SIZE, SPOT_SIZE, xInSource, yInSource);
				mapView.addDrawablePoint(drawablePoint);
			}
		}
    }
    
    
    public void lookAt(Exhibition exhibition)
    {
    	// get informed on bitmap and set image
    	progressBar.setVisibility(View.VISIBLE);
        if(area != exhibition.getArea());
        {
            mapLoadingHandler = new MapLoadingHandler();
    		DynamicLoader.startAreaImage(mapView, area.getNumber(), mapLoadingHandler);
        }
    }
    
    
    public void showSummaryToolTip(View targetView, Exhibition exhibition)
    {
    	if(summaryView != null && summaryView.isShown()) summaryView.remove();
    	
    	summaryView = summaryRelativeLayout.showToolTipForView(toolTip, targetView);
    	summaryView.setExhibition(exhibition);
    	summaryView.addRemoveButton();
    }
    
    
	class MapLoadingHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			try { Thread.sleep(500); } catch(Exception e) {} // test code
			progressBar.setVisibility(View.INVISIBLE);
			
			switch(msg.what)
			{
			case CustomMsg.SUCCESS:
				mapView.setOnDrawablePointClickListener(exhibitionClickListerner);
				mapView.setOnMapTransformListener(onMapTransitionListener);
		    	placeExhibitions();
				mapView.invalidate();
				break;
				
			case CustomMsg.FAILED:
				Toast.makeText(getActivity(), R.string.unable_to_connect, Toast.LENGTH_LONG).show();
			}
			
			super.handleMessage(msg);
		}
	}
	
	private OnDrawablePointClickListener exhibitionClickListerner = new OnDrawablePointClickListener()
	{
		@Override
		public void onDrawablePointClick(DrawablePoint clickedPoint, MotionEvent event)
		{
			MapFragment.this.clickedPoint = (ExhibitionPoint)clickedPoint;
			Rect viewRect = clickedPoint.getDestinationRect();
			dummyView.setLeft(viewRect.left);
			dummyView.setTop(viewRect.top);
			dummyView.setRight(viewRect.right);
			dummyView.setBottom(viewRect.bottom);
			Exhibition exhibition = Museum.getSelectedMuseum().getExhibitionList().
					get(((ExhibitionPoint)clickedPoint).getId());
			showSummaryToolTip(dummyView, exhibition);
		}
	};
	
	private OnMapTransformListener onMapTransitionListener = new OnMapTransformListener()
	{
		@Override
		public void onMapTransform()
		{
			if(summaryView != null && summaryView.isShown())
			{
				Rect viewRect = clickedPoint.getDestinationRect();
				dummyView.setLeft(viewRect.left);
				dummyView.setTop(viewRect.top);
				dummyView.setRight(viewRect.right);
				dummyView.setBottom(viewRect.bottom);
				summaryView.update();
			}
		}
	};
	
	class ExhibitionPoint extends DrawablePoint
	{
		private int id;
		
		public ExhibitionPoint(int id, Bitmap pointImage, 
				int drawWidth, int drawHeight, int xInSource, int yInSource)
		{
			super(pointImage, drawWidth, drawHeight, xInSource, yInSource);
			this.id = id;
		}
		
		public int getId() { return id; }
	}
}
