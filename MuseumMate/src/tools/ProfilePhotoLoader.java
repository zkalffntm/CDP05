package tools;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ProfilePhotoLoader extends AsyncTask<Object, Integer, Bitmap>
{
	public static void start(ImageView view, Uri uri)
	{ new ProfilePhotoLoader().execute(view, uri); }
	
	private ImageView view;
	
	@Override
	protected Bitmap doInBackground(Object... params)
	{
		view = (ImageView)params[0];
		Uri uri = (Uri)params[1];
		Bitmap after = null;
		
		try
		{ 
	        URL url = new URL(uri.toString());
	        URLConnection conn = url.openConnection(); 
	        conn.connect(); 
	        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream()); 
	        Bitmap before = BitmapFactory.decodeStream(bis); 
	        after = tools.BitmapTool.getRoundedCornerBitmap(before, 0xFF460000);
	        bis.close();
	    }
		catch (Exception e) { Log.i("test", e.getMessage()); } 
		
		return after;
	}

	@Override
    protected void onPostExecute(Bitmap bitmap)
	{ if(view != null && bitmap != null) view.setImageBitmap(bitmap); }
}
