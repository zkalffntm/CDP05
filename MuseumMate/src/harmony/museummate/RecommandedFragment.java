package harmony.museummate;

import java.util.ArrayList;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommandedFragment extends Fragment
{
	class ItemData
	{
		public ItemData(Uri photo, String name, String summary)
		{
			this.photo = photo;
			this.name = name;
			this.summary = summary;
		}
		
		Uri photo;
		String name;
		String summary;
	}
	
	private ArrayList<ItemData> itemdata = new ArrayList<ItemData>();
	
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
    	return v;
    }
    
    public void addItem(Uri photo, String name, String summary)
    { itemdata.add(new ItemData(photo, name, summary)); }
    
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
    {
    	public class ViewHolder extends RecyclerView.ViewHolder
        {
            public ImageView imagePhoto;
            public TextView txtName;
            public TextView txtSummary;

            public ViewHolder(View view)
            {
                super(view);
                imagePhoto = (ImageView)view.findViewById(R.id.photo);
                txtName = (TextView)view.findViewById(R.id.name);
                txtSummary = (TextView)view.findViewById(R.id.summary);
            }
        }
    	

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
	        View item = LayoutInflater.from(parent.getContext())
	                               .inflate(R.layout.recommanded_item, parent, false);
	        ViewHolder holder = new ViewHolder(item);
	        return holder;
	    }

		@Override
		public void onBindViewHolder(ViewHolder holder, int position)
		{
	        holder.imagePhoto.setImageURI(itemdata.get(position).photo);
	        holder.txtName.setText(itemdata.get(position).name);
	        holder.txtSummary.setText(itemdata.get(position).summary);
		}

		@Override
		public int getItemCount()
		{ return itemdata.size(); }
    }
}
