package harmony.museummate;

import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import datatype.Museum;
import datatype.Recommandation;
import tools.DynamicLoader;

public class RecommandationFragment extends Fragment
{
	// Singleton /////////////////////////////////////////////////////////////
	private static RecommandationFragment self;								//
	public static RecommandationFragment getInstance()						//
	{ if(self == null) self = new RecommandationFragment(); return self; }	//
	private RecommandationFragment() {}										//
	//////////////////////////////////////////////////////////////////////////
		
	private List<Recommandation> recommandationList;
	
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
        recommandationList = Museum.getSelectedMuseum().getRecommandationList();
    	return v;
    }
    
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
			Recommandation e = recommandationList.get(position);
	        DynamicLoader.startRecommandationImage(holder.imagePhoto, e.getNumber());
	        holder.txtName.setText(e.getName());
	        holder.txtSummary.setText(e.getSummary());
		}

		@Override
		public int getItemCount()
		{ return recommandationList.size(); }
    }
}
