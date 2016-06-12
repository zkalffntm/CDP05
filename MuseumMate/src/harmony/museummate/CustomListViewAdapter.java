package harmony.museummate;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<ListViewItem>
{	
	private Context context;
	private int resourceId;

	public CustomListViewAdapter(Context context, int resourceId, List<ListViewItem> items)
	{
	    super(context, resourceId, items);
	    this.resourceId = resourceId;
	    this.context = context;
	}

	private class ViewHolder
	{
	    ImageView imageView;
	    TextView txtTitle;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
	    ViewHolder holder = null;
	    ListViewItem rowItem = getItem(position);

	    LayoutInflater mInflater = (LayoutInflater)context.
	    		getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    
	    if(convertView == null)
	    {
	        convertView = mInflater.inflate(resourceId, null);
	        holder = new ViewHolder();
	        holder.imageView = (ImageView)convertView.findViewById(R.id.item_icon);
	        holder.txtTitle = (TextView)convertView.findViewById(R.id.item_name);
	        convertView.setTag(holder);
	    }
	    else holder = (ViewHolder)convertView.getTag();

	    holder.imageView.setImageResource(rowItem.getIcon());
	    holder.txtTitle.setText(rowItem.getTitle());
	
	    return convertView;
	}
}