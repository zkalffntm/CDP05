package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import datatype.Area;
import datatype.Exhibition;
import datatype.Node;
import datatype.Node.TYPE;
import pathsearch.Dijkstra;
import tools.DynamicLoader;
import tools.LocationTracker;

class ExhibitionListInnerFragment extends Fragment
{
	private List<Exhibition> exhibitionList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<CustomAdapter.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    
    public ExhibitionListInnerFragment(Area area)
    {
    	super();
    	//frameMap.put(area.getName(), this);
    	exhibitionList = new ArrayList<Exhibition>();

		int rowCount = area.getRowCount();
		int columnCount = area.getColumnCount();
		for(int i = 0; i < rowCount; i++)
		{
			for(int j = 0; j < columnCount; j++)
			{
	    		Node e = area.getNode(i, j);
	    		if(e != null && e.getType() == TYPE.EXHIBITION)
	    			exhibitionList.add((Exhibition)e);
			}
			
		}
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.recycler_view, container, false);
    	
    	// Set Recycler View
    	layoutManager = new LinearLayoutManager(getActivity());
    	recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
    	recyclerView.setLayoutManager(layoutManager);
    	adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

    	return v;
    }
    
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
    {
    	public class ViewHolder extends RecyclerView.ViewHolder
        {
    		public View view;
            public ImageView imagePhoto;
            public TextView txtName;
            public TextView txtArtist;
            public TextView txtContent;

            public ViewHolder(View view)
            {
                super(view);
                this.view = view;
                imagePhoto = (ImageView)view.findViewById(R.id.photo);
                txtName = (TextView)view.findViewById(R.id.name);
                txtArtist = (TextView)view.findViewById(R.id.artist);
                txtContent = (TextView)view.findViewById(R.id.content);
            }
        }

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
	        View item = LayoutInflater.from(parent.getContext())
	                               .inflate(R.layout.exhibition_item, parent, false);
	        ViewHolder holder = new ViewHolder(item);
	        return holder;
	    }

		@Override
		public void onBindViewHolder(ViewHolder holder, int position)
		{
			Exhibition e = exhibitionList.get(position);
			final int tag = e.getTag();
			DynamicLoader.startExhibitionImage(holder.imagePhoto, e.getId());
			holder.view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					new Thread()
					{
						public void run()
						{
							int dist = Dijkstra.calculatePath(LocationTracker.getInstance().
									getCurrentExhibition().getTag(), tag);
							final List<Integer> path = Dijkstra.getPath();
							
							getActivity().runOnUiThread(new Thread()
							{
								public void run()
								{
									MapFragment.getInstance().setRoute(path, 0);
									MainActivity.getInstatnce().showFragment(MapFragment.getInstance(), -1);
								}
							});
						}
					}.start();
				}});
			
	        holder.txtName.setText(getResources().getString(R.string.exhibition_name) + " : " + e.getName());
	        holder.txtArtist.setText(getResources().getString(R.string.author) + " : " + 
	        		(e.getAuthor().isEmpty() ? getResources().getString(R.string.unknown) : e.getAuthor()));
	        holder.txtContent.setText(e.getSummary());
		}

		@Override
		public int getItemCount()
		{ return exhibitionList.size(); }
    }
}