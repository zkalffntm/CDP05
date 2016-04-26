package harmony.museummate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.support.v7.widget.Toolbar;

public class MainActivity extends Activity
{
	private ProgressBar progBar;
	private Toolbar		toolbar
	
	private LoadingHandler handler;
	
	private boolean loaded;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		progBar = new ProgressBar(this);
		RelativeLayout layoutLoading = (RelativeLayout)findViewById(R.id.layout_loading);
		LayoutParams lParamProgBar = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lParamProgBar.addRule(RelativeLayout.CENTER_IN_PARENT);
		layoutLoading.addView(progBar, lParamProgBar);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar); //툴바설정
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		setSupportActionBar(toolbar);
		
		handler = new LoadingHandler();
		
		loaded = false;
		new tools.Loader(handler).start();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		//progBar.setVisibility(View.VISIBLE);
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
	
	class LoadingHandler extends Handler
	{
		public static final int
		SUCCESS = 0,
		FAILED = -1;
		
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case SUCCESS:
				loaded = true;
				runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{ setContentView(R.layout.activity_main); }
						});
				break;
				
			case FAILED:
				
			}
		}
	}
}
