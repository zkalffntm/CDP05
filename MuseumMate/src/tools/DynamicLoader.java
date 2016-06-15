package tools;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	
	public static void startAreaImage(ImageView view, int areaNum)
	{
		// Test Code
		if(areaNum == 0)
		{
			view.setImageResource(R.drawable.map_sample);
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
	
	public static void startAreaImage(ImageView view, int areaNum, Handler handler)
	{
		// Test Code
		if(areaNum == 0)
		{
			view.setImageResource(R.drawable.map_sample);
			
			Message msg = new Message();
			msg.what = CustomMsg.SUCCESS;
			msg.arg1 = view.getDrawable().getIntrinsicWidth();
			msg.arg2 = view.getDrawable().getIntrinsicHeight();
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
	
	public static void startRecommandationImage(ImageView view, int recommandationNum)
	{
		// Test Code
		if(recommandationNum == 0)
		{
			view.setImageResource(R.drawable.recommandation_sample);
			return;
		}
		
		// Real Operation
		DynamicLoader instance = new DynamicLoader(view.getContext());
		Museum museum = Museum.getSelectedMuseum();
		instance.startAsync(museum.getName(), museum.getPort(),
							view, instance.cacheDirStr + "/" + 
							museum.getMajor() + "/r" + recommandationNum,
							PacketLiteral.REQ_RECOMMAND_IMAGE, recommandationNum, null);
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
	
	
	private void startAsync(String ip, Integer port, ImageView view, String localPath, String remoteRequestKey, 
			Integer imageId, Handler handler)
	{ new AsyncLoader().execute(ip, port, view, localPath, remoteRequestKey, imageId, handler); }
	
	
	/**
	 * Asynchronous ImageView Loader
	 * 
	 * @param String ip
	 * @param Integer port
	 * @param ImageView view
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
		private ImageView view;
		
		@Override
		protected Bitmap doInBackground(Object... params)
		{
			view = (ImageView)params[2];
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
			if(view != null) view.setImageBitmap(bitmap);
			
			if(handler != null)
			{
				Message msg = new Message();
				msg.what = (bitmap != null) ? CustomMsg.SUCCESS : CustomMsg.FAILED;
				msg.arg1 = bitmap.getWidth();
				msg.arg2 = bitmap.getHeight();
				handler.sendMessage(msg);
			}
		}
	}
}
