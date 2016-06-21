package harmony.museummate;

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

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
import uk.co.senab.photoview.sample.HackyDrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import datatype.Museum;
import datatype.User;
import tools.CustomMsg;
import tools.LocationTracker;
import tools.ProfilePhotoLoader;

public class MainActivity	extends 	AppCompatActivity 
							implements	OnConnectionFailedListener
{
	private static final int SIGN_IN = 0;
	private static final int RESOLUTION = 1;
	
	private static MainActivity self;
	public static MainActivity getInstatnce()
	{ return self; }
	
	private static final int
	MAP				= -1,
	EXHIBITION		= 0,
	RECOMMENDATION	= 1,
	VISITED			= 2,
	NOTICE			= 3,
	SETTING			= 4;
	
	// State Variable
	private boolean loading;
	private int curFragmentNum;
	
	// Google Account
	private GoogleApiClient client;
	
	// Loading Activity
	private ProgressBar	progBar;
	private LoadingHandler handler;
	
	// Main Activity
	private Toolbar					toolbar;
	private ActionBar				actionbar;
	private HackyDrawerLayout		layoutDrawer;
	private ActionBarDrawerToggle	toggleDrawer;
	private ListView				listViewDrawer;
	private	LinearLayout			drawer;
	private ImageView				imageUserPhoto;
	private TextView				txtUserName;
	private TextView				txtEmail;
	private FloatingActionButton	btnFloating;
	
	private String[]				drawerListTitles;
	private static final int[]		drawerListIcons = { R.drawable.ic_reader,
														R.drawable.ic_thumb_up,
														R.drawable.ic_directions_walk,
														R.drawable.ic_notifications,
														R.drawable.ic_settings };
	
	// Fragments
	private PreferenceFragment		fragmentPref;
	private VisitedFragment			fragmentVisited;
	
	    
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
		self = this;
		
		// Google Account Load
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
	protected void onPostCreate(Bundle savedInstanceState)
	{
	    super.onPostCreate(savedInstanceState);
	    if(toggleDrawer != null) toggleDrawer.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
	    super.onConfigurationChanged(newConfig);
	    if(toggleDrawer != null)
	    	toggleDrawer.onConfigurationChanged(newConfig);
	}
 
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    Log.i("test", Integer.toString(item.getItemId()));
	    return toggleDrawer.onOptionsItemSelected(item);
	}
	
	@Override
	public void	onBackPressed()
	{ super.onBackPressed(); }
	
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
	            User.setCurrentUser(new User(	account.getDisplayName(),
	            								account.getEmail(),
	            								account.getId(),
	            								account.getPhotoUrl()));
	    		// start server connection
	    		new tools.InitialLoader(handler).start();
	        }
	        else
	        {
				Log.i("test", result.getStatus().toString());
				Log.i("test", "Failed to load Google Account");
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
				LocationTracker.initialize(MainActivity.this);
				break;
				
			case CustomMsg.FAILED:
				Log.i("test", "Failed to load Account and server data");
				System.runFinalizersOnExit(true);
				System.exit(0);
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
	    layoutDrawer 	= (HackyDrawerLayout)findViewById(R.id.drawer_layout);
	    listViewDrawer 	= (ListView)findViewById(R.id.drawer_list);
	    drawer 			= (LinearLayout)findViewById(R.id.drawer);
	    imageUserPhoto 	= (ImageView)findViewById(R.id.user_photo);
	    txtUserName 	= (TextView)findViewById(R.id.user_name);
	    txtEmail 		= (TextView)findViewById(R.id.user_email);
	    btnFloating		= (FloatingActionButton)findViewById(R.id.btn_floating);
	    
	    // Update Profile
	    User user =  User.getCurrentUser();
	    ProfilePhotoLoader.start(imageUserPhoto, user.getPhotoUri());
	    txtUserName.setText(user.getName());
	    txtEmail.setText(user.getEmail());
	    
	    // Setup Drawer
	    List<ListViewItem> drawerListItems = new LinkedList<ListViewItem>();
	    drawerListTitles = getResources().getStringArray(R.array.drawer_titles);
	    for(int i = 0; i < 5; i++)
	    	drawerListItems.add(new ListViewItem(drawerListTitles[i], drawerListIcons[i]));
	    listViewDrawer.setAdapter(new CustomListViewAdapter(this, R.layout.drawer_item, drawerListItems));
	    listViewDrawer.setOnItemClickListener(new DrawerItemClickListener());
	    
	    // Setup Toolbar
		setSupportActionBar(toolbar);
		toolbar.setLogo(R.drawable.ic_launcher);
		actionbar = getSupportActionBar();
		actionbar.setDisplayUseLogoEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
	    
		toggleDrawer = new ActionBarDrawerToggle(this, layoutDrawer, toolbar, R.string.app_name, R.string.app_name)
				        {
							@Override
				            public void onDrawerClosed(View view)
				            {
				                super.onDrawerClosed(view);
				                invalidateOptionsMenu();
				                syncState();
				            }
							
							@Override
				            public void onDrawerOpened(View view)
							{
				                super.onDrawerOpened(view);
				                invalidateOptionsMenu();
				                syncState();
				            }
				        };
				        
		toggleDrawer.syncState();
		
		// Setup Floating Button
		btnFloating.setBackgroundColor(0xFF460000);
		btnFloating.setOnClickListener(new OnClickListener()
		{ @Override public void onClick(View v) { showFragment(MapFragment.getInstance(), MAP); } });
		
		showFragment(MapFragment.getInstance(), MAP);
		showNotice();
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id)
	    {
	    	switch(position)
	    	{
	    	case 0: showFragment(ExhibitionListFragment.getInstance(),	EXHIBITION);	break;	
	    	case 1: showFragment(RecommendationFragment.getInstance(), RECOMMENDATION);	break;
	    	case 2: showFragment(VisitedFragment.getInstance(), VISITED);	break;
	    	case 3: showNotice();			break;
	    	case 4: showFragment(SettingFragment.getInstance(), SETTING);	break;
	    	}
	    	
	    	listViewDrawer.setItemChecked(position, true);
	    	layoutDrawer.closeDrawer(drawer);
	    }
	}
	
	/*************************** Notice WebView Shower ***************************/
	
	private void showNotice()
	{
		// Load XML
	    final Dialog dialog = new Dialog(this);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(R.layout.notice);
	    WebView wv = (WebView)dialog.findViewById(R.id.webview);
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
	    wv.loadUrl(Museum.getSelectedMuseum().getNoticeUrl());
	
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
	
	/*************************** Fragment Shower ***************************/
	
	public void showFragment(Fragment fragment, int position)
	{
		if(position == curFragmentNum) return;
		
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.content_frame, fragment)
	                   .commit();
	    
	    if(position == -1) actionbar.setTitle(Museum.getSelectedMuseum().getName());
	    else actionbar.setTitle(drawerListTitles[position]);
	    
	    curFragmentNum = position;
	}

}
