package harmony.museummate;

import java.util.ArrayList;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class VisitedFragment extends Fragment
{
	class ItemData
	{
		public ItemData(Uri photo, String location, String event, String term)
		{
			this.photo = photo;
			this.location = location;
			this.event = event;
			this.term = term;
		}
		
		Uri photo;
		String location;
		String event;
		String term;
	}
	
	private ArrayList<ItemData> itemdata = new ArrayList<ItemData>();
	
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<CustomAdapter.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.recycler_view, container, false);
    	layoutManager = new LinearLayoutManager(this.getActivity());
    	recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
    	recyclerView.setLayoutManager(layoutManager);
    	adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    	return v;
    }
    
    public void addItem(Uri photo, String location, String event, String term)
    { itemdata.add(new ItemData(photo, location, event, term)); }
    
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
    {
    	public class ViewHolder extends RecyclerView.ViewHolder
        {
            public ImageView imagePhoto;
            public TextView txtLocation;
            public TextView txtEvent;
            public TextView txtTerm;

            public ViewHolder(View view)
            {
                super(view);
                imagePhoto = (ImageView)view.findViewById(R.id.photo);
                txtLocation = (TextView)view.findViewById(R.id.location);
                txtEvent = (TextView)view.findViewById(R.id.event);
                txtTerm = (TextView)view.findViewById(R.id.term);
            }
        }
    	

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
	        View item = LayoutInflater.from(parent.getContext())
	                               .inflate(R.layout.visited_item, parent, false);
	        ViewHolder holder = new ViewHolder(item);
	        return holder;
	    }

		@Override
		public void onBindViewHolder(ViewHolder holder, int position)
		{
	        holder.imagePhoto.setImageURI(itemdata.get(position).photo);
	        holder.txtLocation.setText(itemdata.get(position).location);
	        holder.txtEvent.setText(itemdata.get(position).event);
	        holder.txtTerm.setText(itemdata.get(position).term);
		}

		@Override
		public int getItemCount()
		{ return itemdata.size(); }
    }
}
