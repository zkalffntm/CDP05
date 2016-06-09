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
import datatype.Museum;

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
	private Museum museum;
	
	public DynamicLoader(Context context)
	{
		cacheDirStr = context.getCacheDir().getAbsolutePath();
		this.museum = Museum.getSelectedMuseum();
	}
	
	public void setAreaImage(ImageView view, int areaNum)
	{
		loadAsync(view, cacheDirStr + "/" + museum.getMajor() + "/a" + areaNum,
				PacketLiteral.REQ_AREA_IMAGE, areaNum, null);
	}
	
	public void setAreaImage(ImageView view, int areaNum, Handler handler)
	{
		loadAsync(view, cacheDirStr + "/" + museum.getMajor() + "/a" + areaNum,
				PacketLiteral.REQ_AREA_IMAGE, areaNum, handler);
	}

	public void setExhibitionImage(ImageView view, int imageId)
	{
		loadAsync(view, cacheDirStr + "/" + museum.getMajor() + "/e" + imageId,
				PacketLiteral.REQ_ITEM_IMAGE, imageId, null);
	}
	
	public void setRecommandationImage(ImageView view, int recommandationNum)
	{
		loadAsync(view, cacheDirStr + "/" + museum.getMajor() + "/r" + recommandationNum,
				PacketLiteral.REQ_RECOMMAND_IMAGE, recommandationNum, null);
	}
	
	public void setMuseumImage(ImageView view, int major)
	{
		loadAsync(view, cacheDirStr + "/m" + museum.getMajor(),
				PacketLiteral.REQ_PROVIDER_IMAGE, major, null);
	}
	
	
	private void loadAsync(ImageView view, String localPath, String remoteRequestKey, 
			Integer imageId, Handler handler)
	{ new AsyncLoader().execute(view, localPath, remoteRequestKey, imageId, handler); }
	
	
	/**
	 * Asynchronous ImageView Loader
	 * 
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
			view = (ImageView)params[0];
			handler = (Handler)params[4];
			Bitmap bitmap = BitmapFactory.decodeFile((String)params[1]);
			
			if(bitmap == null)
			{
				try
				{
					JSONTransactionClient jsonClient = 
							JSONTransactionClient.getClient(museum.getIP(), museum.getPort());
					
					jsonClient.requestImage((String)params[2], params[3], (String)params[1]);
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
