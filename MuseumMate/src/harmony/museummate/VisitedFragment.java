package harmony.museummate;

import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import datatype.User;
import datatype.Visited;
import tools.DynamicLoader;

public class VisitedFragment extends Fragment
{
	// Singleton /////////////////////////////////////////////////////
	private static VisitedFragment self;							//
	public static VisitedFragment getInstance()						//
	{ if(self == null) self = new VisitedFragment(); return self; }	//
	private VisitedFragment() {}									//
	//////////////////////////////////////////////////////////////////
	
	private List<Visited> visitedList;
	
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
        visitedList = User.getCurrentUser().getVisitedList();
    	return v;
    }
    
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
    {
    	public class ViewHolder extends RecyclerView.ViewHolder
        {
            public ImageView imagePhoto;
            public TextView txtName;

            public ViewHolder(View view)
            {
                super(view);
                imagePhoto = (ImageView)view.findViewById(R.id.photo);
                txtName = (TextView)view.findViewById(R.id.name);
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
			Visited e = visitedList.get(position);
			DynamicLoader.startMuseumImage(holder.imagePhoto, e.getMajor());
	        holder.txtName.setText(e.getName());
		}

		@Override
		public int getItemCount()
		{ return visitedList.size(); }
    }
}
