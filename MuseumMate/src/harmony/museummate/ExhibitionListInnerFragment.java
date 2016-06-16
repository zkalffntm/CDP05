package harmony.museummate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import datatype.Area;
import datatype.Exhibition;
import datatype.Node;
import datatype.Node.TYPE;
import tools.DynamicLoader;

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
    	
    	for(int i = 0; i < area.getNodes().size(); i++)
    	{
    		Node e = area.getNodes().valueAt(i);
    		if(e.getType() == TYPE.EXHIBITION)
    			exhibitionList.add((Exhibition)e);
    	}
    	Log.i("test", "¸¶¤Ó¤¤µé¾îÁü");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	Log.i("test", "111");
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
            public ImageView imagePhoto;
            public TextView txtName;
            public TextView txtArtist;
            public TextView txtContent;

            public ViewHolder(View view)
            {
                super(view);
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
			DynamicLoader.startExhibitionImage(holder.imagePhoto, e.getId());
	        holder.txtName.setText(e.getName());
	        holder.txtArtist.setText(e.getAuthor());
	        holder.txtContent.setText(e.getSummary());
		}

		@Override
		public int getItemCount()
		{ return exhibitionList.size(); }
    }
}