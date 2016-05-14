package harmony.museummate;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceFragment;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import tools.CustomMsg;

public class MainActivity	extends 	AppCompatActivity 
							implements	OnConnectionFailedListener
{
	private static final int SIGN_IN = 0;
	private static final int RESOLUTION = 1;

	private boolean loading;
	
	// Google Account
	private GoogleApiClient client;
	private String name;
	private String email;
	private String id;
	private Uri photo;
	
	// Loading Activity
	private ProgressBar	progBar;
	private LoadingHandler handler;
	
	// Main Activity
	private Toolbar					toolbar;
	private ActionBar				actionbar;
	private DrawerLayout			layoutDrawer;
	private ActionBarDrawerToggle	toggleDrawer;
    private ListView				listViewDrawer;
    private	LinearLayout			drawer;
    private ImageView				imageUserPhoto;
    private TextView				txtUserName;
    private TextView				txtEmail;
	
    String[]		drawerListTitles;
    private int[]	drawerListIcons = { R.drawable.ic_reader,
    									R.drawable.ic_thumb_up,
    									R.drawable.ic_directions_walk,
    									R.drawable.ic_notifications,
    									R.drawable.ic_settings };
	
	// Fragments
    PreferenceFragment fragmentPref;
    
    
    
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
		
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
		client = new GoogleApiClient.Builder(this)
		        .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(!loading)
		{
			startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(client), SIGN_IN);
			loading = true;
		}
	}

	
	@Override
	protected void onStop()
	{
		super.onStop();
		if(client.isConnected()) client.disconnect();
	}


	@Override
	protected void onResume()
	{
		super.onResume();
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
	
	/*************************** Google Account Operations ***************************/
	
	@Override
	public void onConnectionFailed(ConnectionResult result)
	{ 
		if(result.hasResolution())
		{
			try
	        { result.startResolutionForResult(this, RESOLUTION); }
			catch (SendIntentException e)
			{ startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(client), SIGN_IN); }
	    }
		else
		{
			GoogleApiAvailability.getInstance().
				getErrorDialog(this, result.getErrorCode(), 0).show();
		}
	}
		
	
	@Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent)
	{
	    super.onActivityResult(requestCode, responseCode, intent);
	    
		if(requestCode == RESOLUTION && responseCode == RESULT_OK)
		{
			Log.i("test", "retry");
			startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(client), SIGN_IN);
		}
		else if(requestCode == SIGN_IN)
		{
	        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
	        
	        if(result.isSuccess())
	        {
	            GoogleSignInAccount account = result.getSignInAccount();
	        	name = account.getDisplayName();
	        	email = account.getEmail();
	        	id = account.getId();
	        	photo = account.getPhotoUrl();
	    		// start server connection
	    		new tools.Loader(handler).start();
	        }
	        else
	        {
				Log.i("test", result.getStatus().toString());
				Log.i("test", "구글 받아오기 실패");
				// server login failed
	        }
	    }
	}
	

	/*************************** Main Activity Handler ***************************/
	
	class LoadingHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case CustomMsg.SUCCESS:
				runOnUiThread(new Runnable() { public void run() { loadMain(); } });
				break;
				
			case CustomMsg.FAILED:
				Log.i("test", "서버 받아오기 실패");
				// server login failed
			}
		}
	}

	
	/*************************** Main Activity Interface Loader ***************************/
	
	private void loadMain()
	{
		Log.i("test", "loaded");
		setContentView(R.layout.activity_main);
		toolbar 		= (Toolbar)findViewById(R.id.toolbar);
	    layoutDrawer 	= (DrawerLayout)findViewById(R.id.drawer_layout);
	    listViewDrawer 	= (ListView)findViewById(R.id.drawer_list);
	    drawer 			= (LinearLayout)findViewById(R.id.drawer);
	    imageUserPhoto 	= (ImageView)findViewById(R.id.user_photo);
	    txtUserName 	= (TextView)findViewById(R.id.user_name);
	    txtEmail 		= (TextView)findViewById(R.id.user_email);
	    
	    // Update Profile
	    new Thread() { public void run() { loadPhoto(photo); } }.start();
	    txtUserName.setText(name);
	    txtEmail.setText(email);
	    
	    // Setup Drawer
	    List<ListViewItem> drawerListItems = new LinkedList<ListViewItem>();
	    drawerListTitles = getResources().getStringArray(R.array.drawer_titles);
	    for(int i = 0; i < 5; i++)
	    	drawerListItems.add(new ListViewItem(drawerListTitles[i], drawerListIcons[i]));
	    listViewDrawer.setAdapter(new CustomListViewAdapter(this, R.layout.drawerlist, drawerListItems));
	    listViewDrawer.setOnItemClickListener(new DrawerItemClickListener());
	    
	    // Setup Toolbar
		setSupportActionBar(toolbar);
		toolbar.setLogo(R.drawable.ic_launcher);
		actionbar = getSupportActionBar();
		actionbar.setDisplayUseLogoEnabled(true);
	    
		toggleDrawer = new ActionBarDrawerToggle(this, layoutDrawer, toolbar, R.string.app_name, R.string.app_name);
		toggleDrawer.syncState();
		
		callNotice();
	}
	
	private void loadPhoto(Uri uri)
	{
		try
		{ 
            URL url = new URL(uri.toString());
            URLConnection conn = url.openConnection(); 
            conn.connect(); 
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream()); 
            Bitmap before = BitmapFactory.decodeStream(bis); 
            final Bitmap after = tools.BitmapTool.getRoundedCornerBitmap(before, 0xFF460000);
            bis.close();
			runOnUiThread(new Runnable() { public void run() { imageUserPhoto.setImageBitmap(after); } });
        }
		catch (Exception e) { Log.i("test", e.getMessage()); } 
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id)
	    {
	    	switch(position)
	    	{
	    	case 3:
	    		callNotice();
	    		break;
	    		
	    	case 4:
	    		showPreference();
	    		break;
	    	}
			/*
		    // Create a new fragment and specify the planet to show based on position
		    Fragment fragment = new PlanetFragment();
		    fragment.setArguments(args);

		    // Insert the fragment by replacing any existing fragment
		    FragmentManager fragmentManager = getFragmentManager();
		    fragmentManager.beginTransaction()
		                   .replace(R.id.content_frame, fragment)
		                   .commit();
			*/
		    // Highlight the selected item, update the title, and close the drawer
	    	listViewDrawer.setItemChecked(position, true);
		    //setTitle(mPlanetTitles[position]);
	    	layoutDrawer.closeDrawer(drawer);
	    }
	}
	
	
	/*************************** Notice WebView Caller ***************************/
	
	private void callNotice()
	{
		// Load XML
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notice);
        WebView wv 			= (WebView)dialog.findViewById(R.id.webview);
        ImageButton btnExit	= (ImageButton)dialog.findViewById(R.id.btn_exit);
        
        // Set WebView
        wv.setHorizontalScrollBarEnabled(false);
        wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(false);
		wv.setWebViewClient(new WebViewClient()
        {
			@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) 
            {
                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl("http://www.google.com/");

        // Set Exit Button
        btnExit.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{ dialog.dismiss(); }
		});
        
        // Resize and Set Dialog
        WindowManager.LayoutParams lpDialog = new WindowManager.LayoutParams();
        lpDialog.copyFrom(dialog.getWindow().getAttributes());
        lpDialog.width = LayoutParams.MATCH_PARENT;
        lpDialog.height = LayoutParams.MATCH_PARENT;
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setAttributes(lpDialog);
	}
	
	
	/*************************** Preference Fragment Shower ***************************/

	private void showPreference()
	{
	    if(fragmentPref == null)
	    {
	    	fragmentPref = new PreferenceFragment()
		    {
		        @Override
		        public void onCreate(Bundle savedInstanceState)
		        {
		        	super.onCreate(savedInstanceState);
		        	addPreferencesFromResource(R.xml.pref_screen);
		        }     
		    };
	    }
	    
	    FragmentManager fragmentManager = getFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.content_frame, fragmentPref)
	                   .commit();
	}
	
	/*
	@Override
	public void setTitle(CharSequence title)
	{
	    mTitle = title;
	    getActionBar().setTitle(mTitle);
	}*/
}
