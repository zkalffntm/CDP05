package harmony.museummate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends AppCompatActivity
{
	private ProgressBar 			progBar;
	private Toolbar					toolbar;
	private ActionBar				actionbar;
	private DrawerLayout			layoutDrawer;
	private ActionBarDrawerToggle	toggleDrawer;
	
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

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
	    super.onPostCreate(savedInstanceState);
	    
	    if(toggleDrawer != null) toggleDrawer.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
	    super.onConfigurationChanged(newConfig);
	    
	    if(toggleDrawer != null) toggleDrawer.onConfigurationChanged(newConfig);
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
				runOnUiThread(new Runnable() { public void run() { loadMain(); } });
				break;
				
			case FAILED:
				
			}
		}
	}
	
	private void loadMain()
	{
		setContentView(R.layout.activity_main);
		
		toolbar = (Toolbar)findViewById(R.id.toolbar);
	    layoutDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
	    
		setSupportActionBar(toolbar);
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true); 
		actionbar.setHomeButtonEnabled(true); 
		//actionbar.setHomeAsUpIndicator(R.drawable.hamburger_icon);
	    
	    toggleDrawer = new ActionBarDrawerToggle(this, layoutDrawer, toolbar, R.string.app_name, R.string.app_name);
	    layoutDrawer.addDrawerListener(toggleDrawer);
	    toggleDrawer.syncState();
	}
}
