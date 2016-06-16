package tools;

import java.io.IOException;

import org.json.JSONException;

import com.andrewson.mapview.MapView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import datatype.CentralServer;
import datatype.Museum;
import harmony.museummate.R;

/**
 * Used when reading dynamically managed data in cache space.
 * (means that always removable by android kernel)
 * If cannot be read from the cache space,
 * try to connect to and receive from the museum admin server.
 * 
 * @author Kyuho
 *
 */
public class DynamicLoader
{
	private String cacheDirStr;
	
	public DynamicLoader(Context context)
	{ cacheDirStr = context.getCacheDir().getAbsolutePath(); }
	
	public static void startAreaImage(View view, int areaNum)
	{
		// Test Code
		if(areaNum == 0)
		{
			((MapView)view).initialize(BitmapFactory.decodeResource(
					view.getContext().getResources(), R.drawable.map_sample));
			return;
		}
		
		// Real Operation
		DynamicLoader instance = new DynamicLoader(view.getContext());
		Museum museum = Museum.getSelectedMuseum();
		instance.startAsync(museum.getName(), museum.getPort(),
							view, instance.cacheDirStr + "/" + 
							museum.getMajor() + "/a" + areaNum,
							PacketLiteral.REQ_AREA_IMAGE, areaNum, null);
	}
	
	public static void startAreaImage(View view, int areaNum, Handler handler)
	{
		// Test Code
		if(areaNum == 0)
		{
			((MapView)view).initialize(BitmapFactory.decodeResource(
					view.getContext().getResources(), R.drawable.map_sample));
			
			Message msg = new Message();
			msg.what = CustomMsg.SUCCESS;
			handler.sendMessage(msg);
			return;
		}
		
		// Real Operation
		DynamicLoader instance = new DynamicLoader(view.getContext());
		Museum museum = Museum.getSelectedMuseum();
		instance.startAsync(museum.getName(), museum.getPort(),
							view, instance.cacheDirStr + "/" + 
							museum.getMajor() + "/a" + areaNum,
							PacketLiteral.REQ_AREA_IMAGE, areaNum, handler);
	}

	public static void startExhibitionImage(ImageView view, int imageId)
	{
		// Test Code
		if(imageId == 0)
		{
			view.setImageResource(R.drawable.exhibition_sample);
			return;
		}
		
		// Real Operation
		DynamicLoader instance = new DynamicLoader(view.getContext());
		Museum museum = Museum.getSelectedMuseum();
		instance.startAsync(museum.getName(), museum.getPort(),
							view, instance.cacheDirStr + "/" + 
							museum.getMajor() + "/e" + imageId,
							PacketLiteral.REQ_ITEM_IMAGE, imageId, null);
	}
	
	public static void startRecommendationImage(ImageView view, int recommendationNum)
	{
		// Test Code
		if(recommendationNum == 0)
		{
			view.setImageResource(R.drawable.recommendation_sample);
			return;
		}
		
		// Real Operation
		DynamicLoader instance = new DynamicLoader(view.getContext());
		Museum museum = Museum.getSelectedMuseum();
		instance.startAsync(museum.getName(), museum.getPort(),
							view, instance.cacheDirStr + "/" + 
							museum.getMajor() + "/r" + recommendationNum,
							PacketLiteral.REQ_RECOMMEND_IMAGE, recommendationNum, null);
	}
	
	public static void startMuseumImage(ImageView view, int major)
	{
		// Test Code
		if(major == 0)
		{
			view.setImageResource(R.drawable.visited_sample);
			return;
		}
		
		// Real Operation
		DynamicLoader instance = new DynamicLoader(view.getContext());
		Museum museum = Museum.getSelectedMuseum();
		instance.startAsync(CentralServer.IP, CentralServer.PORT,
							view, instance.cacheDirStr + "/m/" + major,
							PacketLiteral.REQ_PROVIDER_IMAGE, major, null);
	}
	
	
	private void startAsync(String ip, Integer port, View view, String localPath, String remoteRequestKey, 
			Integer imageId, Handler handler)
	{ new AsyncLoader().execute(ip, port, view, localPath, remoteRequestKey, imageId, handler); }
	
	
	/**
	 * Asynchronous ImageView Loader
	 * 
	 * @param String ip
	 * @param Integer port
	 * @param View view
	 * @param String localPath
	 * @param String remoteRequestKey	Should be PacketLiteral.blah~~~
	 * @param Integer imageId
	 * @param Handler handler that would receive an message with size data after the task
	 * 
	 * @author Kyuho
	 *
	 */
	class AsyncLoader extends AsyncTask<Object, Integer, Bitmap>
	{
		private Handler handler;
		private View view;
		
		@Override
		protected Bitmap doInBackground(Object... params)
		{
			view = (View)params[2];
			handler = (Handler)params[6];
			Bitmap bitmap = BitmapFactory.decodeFile((String)params[3]);
			
			if(bitmap == null)
			{
				try
				{
					JSONTransactionClient jsonClient = 
							JSONTransactionClient.getClient((String)params[0], (Integer)params[1]);
					
					jsonClient.requestImage((String)params[4], params[5], (String)params[3]);
				}
				catch (IOException e) { Log.e("test", "error", e); } 
				catch (JSONException e) { Log.e("test", "error", e); }
				
				bitmap = BitmapFactory.decodeFile((String)params[1]);
			}
			
			return bitmap;
		}

		@Override
        protected void onPostExecute(Bitmap bitmap)
		{
			if(handler != null)
			{
				if(view != null) ((MapView)view).initialize(bitmap);
				
				Message msg = new Message();
				msg.what = (bitmap != null) ? CustomMsg.SUCCESS : CustomMsg.FAILED;
				msg.arg1 = bitmap.getWidth();
				msg.arg2 = bitmap.getHeight();
				handler.sendMessage(msg);
			}
			else if(view != null) ((ImageView)view).setImageBitmap(bitmap);
		}
	}
}
