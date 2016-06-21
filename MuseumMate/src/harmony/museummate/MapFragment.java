package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import com.andrewson.mapview.DrawablePoint;
import com.andrewson.mapview.MapView;
import com.andrewson.mapview.MapView.OnDrawablePointClickListener;
import com.andrewson.mapview.MapView.OnInvalidateListener;
import com.nhaarman.supertooltips.ToolTip;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import datatype.Area;
import datatype.Exhibition;
import datatype.Link;
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
	
	// View elements
	private MapView mapView;
	private FrameLayout frameLayout;
	private ProgressBar progressBar;
	private LinearLayout layoutBottom;
	private ImageButton buttonPrev;
	private ImageButton buttonStart;
	
	// About ToolTip
	private SummaryRelativeLayout summaryRelativeLayout;
	private ToolTip toolTip;
    private SummaryView summaryView;
    private View dummyView;
    private NodePoint clickedPoint;
	
    // Resources
    private Bitmap bitmapSelected;
    private Bitmap bitmapDeselected;
    private Bitmap bitmapLink;
    
    // Control Module
	private MapLoadingHandler mapLoadingHandler;
	private TranslateAnimation showAnimation;
	private TranslateAnimation hideAnimation;
	
	// Control Variable
	private Area area;
	private List<NodePoint> nodePointList;
	private Node lookAtNode;
	private List<Integer> route;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.map, container, false);
    	mapView = (MapView)v.findViewById(R.id.mapview);
    	frameLayout = (FrameLayout)v.findViewById(R.id.map_frame);
    	progressBar = (ProgressBar)v.findViewById(R.id.progress);
        summaryRelativeLayout = (SummaryRelativeLayout)v.findViewById(R.id.tooltipRelativeLayout);
    	layoutBottom = (LinearLayout)v.findViewById(R.id.layout_bottom);
    	buttonPrev = (ImageButton)v.findViewById(R.id.button_prev);
    	buttonStart = (ImageButton)v.findViewById(R.id.button_start);
    	
	    // Get Resources
	    bitmapSelected = BitmapFactory.decodeResource(
	    		getContext().getResources(), R.drawable.spot_selected);
	    bitmapDeselected = BitmapFactory.decodeResource(
	    		getContext().getResources(), R.drawable.spot);
	    bitmapLink = BitmapFactory.decodeResource(
	    		getContext().getResources(), R.drawable.ic_call_made_black_24dp);
        
        // Get Current location
        area = LocationTracker.getInstance().getCurrentExhibition().getArea();

	    // Setup Control Module
        mapLoadingHandler = new MapLoadingHandler();
		DynamicLoader.startAreaImage(mapView, area.getNumber(), mapLoadingHandler);
		DisplayMetrics metrics = v.getContext().getApplicationContext().getResources().getDisplayMetrics();
		int screenHeight = metrics.heightPixels;
		float density = metrics.density;
		showAnimation = new TranslateAnimation(0.0f, 0.0f, screenHeight, screenHeight - 60 * density);
		showAnimation.setDuration(1000);
		showAnimation.setFillAfter(true);
		hideAnimation = new TranslateAnimation(0.0f, 0.0f, screenHeight - 60 * density, screenHeight);
		hideAnimation.setDuration(1000);
		hideAnimation.setFillAfter(true);
		
		
		// Set ToopTip
	    toolTip = new ToolTip().withShadow().withColor(Color.parseColor("#F0E9E9"))
				                .withAnimationType(ToolTip.AnimationType.NONE);
	    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(SPOT_SIZE, SPOT_SIZE);
	    dummyView = new View(getContext());
	    dummyView.setLayoutParams(params);
	    frameLayout.addView(dummyView);
    	
    	return v;
    }
    
    public void setRoute(List<Integer> route, int fromWhichFragment)
    {
    	if(route == null || route.size() == 1) return;
    	
		this.route = route;
		final int toGet = fromWhichFragment;

    	buttonStart.setOnClickListener(new OnClickListener()
    	{
			@Override
			public void onClick(View v){}
			//{ //layoutBottom.startAnimation(hideAnimation); }
    	});
    			
    	buttonPrev.setOnClickListener(new OnClickListener()
    	{
			@Override
			public void onClick(View v)
			{
				if(toGet == 0)
					MainActivity.getInstatnce().
						showFragment(ExhibitionListFragment.getInstance(), 0);
				
				else if(toGet == 1)
					MainActivity.getInstatnce().
						showFragment(RecommendationFragment.getInstance(), 1);
			}
    	});
    	
    	//layoutBottom.startAnimation(showAnimation);
    }
    
    public void accessExhibition(Exhibition currentExhibition)
    {
    	Log.i("test", currentExhibition.getName());
    	
    	if(route == null) return;
    	List<Node> nodeList = Museum.getSelectedMuseum().getNodeList();
    	int i;
    	for(i = 1; i < route.size(); i++)
    	{
    		if(route.get(i) == currentExhibition.getTag())
    		{
    			for(int j = 0; j < i; j++) route.remove(0);
        		DescriptionDialog.showExhibitionDialog(getContext(), currentExhibition);
        		
    			if(route.size() == 1)
    			{
    				route = null;
    				mapView.clearLine();
    	        	mapView.invalidate();
    	        	return;
    			}
    			else updateRoute();
    		}
    		
    		if(nodeList.get(route.get(i)).getType() == Node.TYPE.EXHIBITION) break;
    	}
    }
    
    private void updateRoute()
    {
    	if(route != null)
    	{
    		//Log.i("test", "" + route);
    		mapView.clearLine();
    		List<Node> nodeList = Museum.getSelectedMuseum().getNodeList();
    		
    		int i;

    		for(i = 0; i < route.size(); i++)
    			if(nodeList.get(route.get(i)).getArea() == area) break;
    		
    		if(i >= route.size()) return;
			Node node = nodeList.get(route.get(i));
			int columnCount = node.getArea().getColumnCount();
			int row = node.getBlockNumber() / columnCount;
			int col = node.getBlockNumber() % columnCount;
    		Point previousPoint = new Point((int)((col + 0.5f) * BLOCK_SIZE), 
    				(int)((row + 0.5f) * BLOCK_SIZE));
    		
    		for(; i < route.size(); i++)
    		{
    			if(nodeList.get(route.get(i)).getArea() != node.getArea()) break;
    			
    			node = nodeList.get(route.get(i));
    			columnCount = node.getArea().getColumnCount();
    			row = node.getBlockNumber() / columnCount;
    			col = node.getBlockNumber() % columnCount;
				Point currentPoint = new Point((int)((col + 0.5f) * BLOCK_SIZE), 
						(int)((row + 0.5f) * BLOCK_SIZE));
    			mapView.addLine(previousPoint, currentPoint);
            	//Log.i("test", previousPoint + " -> " + currentPoint);
    			previousPoint = currentPoint;
    		}
        	
        	mapView.invalidate();
    	}
    }
    
    private void placeExhibitions()
    {
		nodePointList = new ArrayList<NodePoint>();
		
		int rowCount = area.getRowCount();
		int columnCount = area.getColumnCount();
		for(int i = 0; i < rowCount; i++)
		{
			for(int j = 0; j < columnCount; j++)
			{
				Node e = area.getNode(i, j);
				if(e != null)
				{
					int xInSource, yInSource;
					
					switch(e.getType())
					{
					case EXHIBITION:
						// Caculate Location of the Spot
						xInSource = (int)((j + 0.5f) * BLOCK_SIZE);
						yInSource = (int)((i + 0.5f) * BLOCK_SIZE);
					
						NodePoint exhibitionPoint = new NodePoint(e, bitmapDeselected,
								SPOT_SIZE, SPOT_SIZE, xInSource, yInSource);
						mapView.addDrawablePoint(exhibitionPoint);
						nodePointList.add(exhibitionPoint);
						break;
						
					case LINK:
						// Caculate Location of the Spot
						xInSource = (int)((j + 0.5f) * BLOCK_SIZE);
						yInSource = (int)((i + 0.5f) * BLOCK_SIZE);
						
						NodePoint linkPoint = new NodePoint(e, bitmapLink,
								SPOT_SIZE, SPOT_SIZE, xInSource, yInSource);
						mapView.addDrawablePoint(linkPoint);
						nodePointList.add(linkPoint);
						break;
					}
				}
			}
		}
    }
    
    public void lookAt(Node node)
    {
    	// get informed on bitmap and set image
        if(area != node.getArea())
        {
        	progressBar.setVisibility(View.VISIBLE);
        	area = node.getArea();
    		DynamicLoader.startAreaImage(mapView, area.getNumber(), mapLoadingHandler);
    		lookAtNode = node;
        }
        else lookAtInArea(node);
    }
    
    private void lookAtInArea(Node node)
    {
    	if(node == null) return;
    	
    	int columnCount = area.getColumnCount();
    	int blockNumber = node.getBlockNumber();
    	float xInSource = (blockNumber % columnCount + 0.5f) * BLOCK_SIZE;
    	float yInSource = (blockNumber / columnCount + 0.5f) * BLOCK_SIZE;
		mapView.setLookingCenterInSource(xInSource, yInSource);
    }
    
    
    public void showSummaryToolTip(View targetView, Exhibition exhibition)
    {
    	if(summaryView != null && summaryView.isShown()) summaryView.remove();
    	
    	summaryView = summaryRelativeLayout.showToolTipForView(toolTip, targetView);
    	summaryView.setExhibition(exhibition);
    	//summaryView.addRemoveButton();
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
				mapView.setOnMapTransformListener(onInvalidateListener);
		    	placeExhibitions();
		    	updateRoute();
		    	lookAtInArea(lookAtNode);
				mapView.invalidate();
				if(summaryView != null && summaryView.isShown()) summaryView.remove();
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
			Node node = ((NodePoint)clickedPoint).getNode();
			if(node instanceof Exhibition)
			{
				MapFragment.this.clickedPoint = (NodePoint)clickedPoint;
				Rect viewRect = clickedPoint.getDestinationRect();
				dummyView.setLeft(viewRect.left);
				dummyView.setTop(viewRect.top);
				dummyView.setRight(viewRect.right);
				dummyView.setBottom(viewRect.bottom);
				showSummaryToolTip(dummyView, (Exhibition)node);
			}
			else if(node instanceof Link)
			{
				Area dstArea = ((Link)node).getDestinationArea();
				int dstBlockNum = ((Link)node).getDestinationBlockNumber();
				int columnCount = dstArea.getColumnCount();
				lookAt(dstArea.getNode(dstBlockNum / columnCount, dstBlockNum % columnCount));
			}
		}
	};
	
	private OnInvalidateListener onInvalidateListener = new OnInvalidateListener()
	{
		@Override
		public void onInvalidate()
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
	
	class NodePoint extends DrawablePoint
	{
		private Node node;
		
		public NodePoint(Node node, Bitmap pointImage, 
				int drawWidth, int drawHeight, int xInSource, int yInSource)
		{
			super(pointImage, drawWidth, drawHeight, xInSource, yInSource);
			this.node = node;
		}
		
		public Node getNode() { return node; }
	}
}
