package tools;

import android.os.Handler;

public class Loader extends Thread
{
	private Handler handler;
	
	public Loader(Handler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public void run()
	{
		// Å×½ºÆ®
		try { Thread.sleep(2000); } catch(Exception e) {}
		
		handler.sendEmptyMessage(0);
	}
}
