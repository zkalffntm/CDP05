package tools;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
	
	public DynamicLoader(Context context, Museum museum)
	{
		cacheDirStr = context.getCacheDir().getAbsolutePath();
		this.museum = museum;
	}

	public void setExhibitionImage(ImageView view, int imageId)
	{
		loadAsync(view, cacheDirStr + "/" + museum.getMajor() + "/e" + imageId,
				PacketLiteral.REQ_ITEM_IMAGE, imageId);
	}
	
	public void setAreaImage(ImageView view, int areaNum)
	{
		loadAsync(view, cacheDirStr + "/" + museum.getMajor() + "/a" + areaNum,
				PacketLiteral.REQ_AREA_IMAGE, areaNum);
	}
	
	public void setRecommandationImage(ImageView view, int recommandationNum)
	{
		loadAsync(view, cacheDirStr + "/" + museum.getMajor() + "/r" + recommandationNum,
				PacketLiteral.REQ_RECOMMAND_IMAGE, recommandationNum);
	}
	
	public void setMuseumImage(ImageView view, int major)
	{
		loadAsync(view, cacheDirStr + "/m" + museum.getMajor(),
				PacketLiteral.REQ_PROVIDER_IMAGE, major);
	}
	
	
	private void loadAsync(ImageView view, String localPath, String remoteRequestKey, Integer imageId)
	{ new AsyncLoader().execute(view, localPath, remoteRequestKey, imageId); }
	
	
	/**
	 * Asynchronous ImageView Loader
	 * 
	 * @param ImageView view
	 * @param String localPath
	 * @param String remoteRequestKey	Should be PacketLiteral.blah~~~
	 * @param Integer imageId
	 * 
	 * @author Kyuho
	 *
	 */
	class AsyncLoader extends AsyncTask<Object, Integer, Bitmap>
	{
		private ImageView view;
		private Bitmap bitmap;
		
		@Override
		protected Bitmap doInBackground(Object... params)
		{
			view = (ImageView)params[0];
			bitmap = BitmapFactory.decodeFile((String)params[1]);
			
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
		{ if(view != null) view.setImageBitmap(bitmap); }
	}
}
