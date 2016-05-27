package harmony.museummate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MapFragment extends Fragment
{
	private PhotoView map;
	private PhotoViewAttacher attacher;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.map, container, false);
    	map = (PhotoView)v.findViewById(R.id.mapview);
    	map.setImageResource(R.drawable.map_sample);
    	attacher = new PhotoViewAttacher(map);
    	return v;
    }
}
