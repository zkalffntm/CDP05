package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import datatype.Exhibition;
import datatype.Museum;
import datatype.Node;
import datatype.Recommendation;
import pathsearch.Dijkstra;
import tools.DynamicLoader;
import tools.LocationTracker;

public class RecommendationFragment extends Fragment
{
	// Singleton /////////////////////////////////////////////////////////////
	private static RecommendationFragment self;								//
	public static RecommendationFragment getInstance()						//
	{ if(self == null) self = new RecommendationFragment(); return self; }	//
	private RecommendationFragment() {}										//
	//////////////////////////////////////////////////////////////////////////
		
	private List<Recommendation> recommendationList;
	
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<CustomAdapter.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.recycler_view, container, false);
    	layoutManager = new GridLayoutManager(this.getActivity(), 2);
    	recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
    	recyclerView.setLayoutManager(layoutManager);
    	adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
        recommendationList = Museum.getSelectedMuseum().getRecommendationList();
    	return v;
    }
    
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
    {
    	public class ViewHolder extends RecyclerView.ViewHolder
        {
    		public View	view;
            public ImageView imagePhoto;
            public TextView txtName;
            public TextView txtSummary;

            public ViewHolder(View view)
            {
                super(view);
                this.view = view;
                imagePhoto = (ImageView)view.findViewById(R.id.photo);
                txtName = (TextView)view.findViewById(R.id.name);
                txtSummary = (TextView)view.findViewById(R.id.summary);
            }
        }
    	

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
	        View item = LayoutInflater.from(parent.getContext())
	                               .inflate(R.layout.recommended_item, parent, false);
	        ViewHolder holder = new ViewHolder(item);
	        
	        return holder;
	    }

		@Override
		public void onBindViewHolder(ViewHolder holder, int position)
		{
	        Recommendation recommendation = recommendationList.get(position);
	        final int[] route = recommendation.getRoute();
	        
			Recommendation e = recommendationList.get(position);
	        DynamicLoader.startRecommendationImage(holder.imagePhoto, e.getNumber());
	        holder.view.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
					new Thread() 
					{ public void run() { showRecommendationPath(route); } }.start();
				}
	        	
	        });
	        holder.txtName.setText(e.getName());
	        holder.txtSummary.setText(e.getSummary());
		}

		@Override
		public int getItemCount()
		{ return recommendationList.size(); }
    }
    
    private void showRecommendationPath(final int[] route)
    {
        List<Exhibition> exhibitionList = Museum.getSelectedMuseum().getExhibitionList();
        int currentExhibitionTag = LocationTracker.getInstance().
				getCurrentExhibition().getTag();
        int leastDist = Integer.MAX_VALUE;
        int nearestExhibitionTagIndex = 0;
        List<Integer> path = null;
        
        for(int i = 0; i < route.length; i++) route[i]--;
        
        for(int i = 0; i < route.length; i++)
        {
        	int destinationTag = route[i];
        	if(currentExhibitionTag == destinationTag)
        	{
        		path = new ArrayList<Integer>();
        		path.add(currentExhibitionTag);
        		nearestExhibitionTagIndex = i;
        		break;
        	}
        	
			int dist = Dijkstra.calculatePath(currentExhibitionTag, destinationTag);
			if(dist < leastDist)
			{
				leastDist = dist;
				nearestExhibitionTagIndex = i;
				path = Dijkstra.getPath();
			}
        }
        
        final List<Integer> totalPath = path;
        int previousExhibitionTag = route[nearestExhibitionTagIndex];
        Log.i("test", "lastExhibition " + previousExhibitionTag);
        for(int i = nearestExhibitionTagIndex + 1; i < route.length; i++)
        {
			Dijkstra.calculatePath(previousExhibitionTag, route[i]);
        	path = Dijkstra.getPath();
        	path.remove(0);
        	totalPath.addAll(path);
        	previousExhibitionTag = nearestExhibitionTagIndex;
        }

        for(int i = 0; i < nearestExhibitionTagIndex; i++)
        {
			Dijkstra.calculatePath(previousExhibitionTag, route[i]);
        	path = Dijkstra.getPath();
        	path.remove(0);
        	totalPath.addAll(path);
        	previousExhibitionTag = nearestExhibitionTagIndex;
        }

    	//for(Integer e : route) Log.i("test", "route : " + e);
        //for(Integer e : totalPath)
        //{
        	//Node node = Museum.getSelectedMuseum().getNodeList().get(e);
        	//Log.i("test", node.getArea().getNumber() + " : " + node.getBlockNumber());
        //}
        
		getActivity().runOnUiThread(new Thread()
		{
			public void run()
			{
				MapFragment.getInstance().setRoute(totalPath, 1);
				MainActivity.getInstatnce().showFragment(MapFragment.getInstance(), -1);
			}
		});
    	
    }
}
