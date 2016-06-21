package tools;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.util.Log;
import android.util.SparseIntArray;
import android.bluetooth.BluetoothDevice;
import bluetooth.BeaconInterface;
import bluetooth.BeaconProp;
import datatype.Exhibition;
import datatype.Museum;
import harmony.museummate.MapFragment;

public class LocationTracker
{
	// Singleton /////////////////////////////////////////////////
	private static volatile LocationTracker self;				//
	public static LocationTracker getInstance()	{ return self; }//										//
	//////////////////////////////////////////////////////////////
	
	private Exhibition currentExhibition;
	private LeScanCallback callBack;
	private final SparseIntArray rssiMap;
	
	public static void initialize(Activity activity)
	{ self = new LocationTracker(activity); }
	
	private LocationTracker(Activity activity)
	{
		currentExhibition = Museum.getSelectedMuseum().getExhibitionList().get(0);
		BeaconInterface.getInstance().checkOn(activity);
		rssiMap = new SparseIntArray();
		final Activity handOnActivity = activity;
		callBack = new LeScanCallback()
		{
			@Override
			public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
			{
				BeaconProp prop = BeaconInterface.parseRecord(scanRecord);
				//Log.i("test", prop.getMinor() + " : " + rssi);
				if(prop.getUUID().equals("78751DE4-040B-11E6-B512-3E1D05DEFE78"))
				{
					final int minor = prop.getMinor();
					if(rssiMap.get(minor, -100) < -50 && rssi >= -50)
					{
						currentExhibition = Museum.getSelectedMuseum().
								getExhibitionList().get(minor - 1);
						
						handOnActivity.runOnUiThread(new Thread()
						{
							public void run()
							{
								MapFragment fragmentMap = MapFragment.getInstance();
								
								if(fragmentMap.isVisible())
								{
									fragmentMap.lookAt(currentExhibition);
									fragmentMap.accessExhibition(currentExhibition);
								}
							}
						});
					}
				}
			}
		};
			
		BeaconInterface.getInstance().startScan(callBack);
	}
	
	public Exhibition getCurrentExhibition()
	{
		return currentExhibition;
	}
}
