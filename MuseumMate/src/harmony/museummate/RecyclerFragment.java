package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import datatype.Area;
import datatype.Exhibition;
import datatype.Node;
import datatype.Node.TYPE;

class RecyclerFragment extends Fragment
{	
	private List<Exhibition> exhibitionList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<CustomAdapter.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    
    public RecyclerFragment(Area area)
    {
    	super();
    	
    	exhibitionList = new ArrayList<Exhibition>();
    	
    	for(int i = 0; i < area.getNodes().size(); i++)
    	{
    		Node e = area.getNodes().valueAt(i);
    		if(e.getType() == TYPE.EXHIBITION)
    			exhibitionList.add((Exhibition)e);
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
			// To do : dynamic Load
	        //holder.imagePhoto.setImageURI(exhibitionList.get(position).getSummary());
			// test code
	        holder.imagePhoto.setImageResource(R.drawable.exhibition_sample);
	        holder.txtName.setText(exhibitionList.get(position).getName());
	        holder.txtArtist.setText(exhibitionList.get(position).getAuthor());
	        holder.txtContent.setText(exhibitionList.get(position).getSummary());
		}

		@Override
		public int getItemCount()
		{ return exhibitionList.size(); }
    }
}