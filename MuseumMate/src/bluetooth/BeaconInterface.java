package bluetooth;

import android.app.Activity;
import android.bluetooth.*;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
 * @author Kyuho
 *
 */
public class BeaconInterface
{
	// Singleton
	private static volatile BeaconInterface self;
	public static BeaconInterface getInstance()
	{
		synchronized(BeaconInterface.class)
		{
			if(self == null) self = new BeaconInterface();
			return self;
		}
	}
	
	private BeaconInterface() {}

	private BluetoothAdapter ba;
	private LeScanCallback cb;
	
	/**
	 * Turn on Bluetooth
	 * 
	 * @param	activity
	 * @return	if the bluetooth was on
	 */
	public boolean checkOn(Activity activity)
	{
		// get adapter
		if(ba == null)
		{
			final BluetoothManager bm = 
					(BluetoothManager)activity.getSystemService(Context.BLUETOOTH_SERVICE);
			ba = bm.getAdapter();
		}
		
		// turn on if is off
		if(!ba.isEnabled())
		{
		    Intent btin = 
		    		new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    activity.startActivity(btin);
		    return false;
		}
		
		return true;
	}
	
	public void startScan(LeScanCallback cb)
	{
		if(this.cb != null) return;
		this.cb = cb;
		
        ba.startLeScan(cb);
	}
	
	public void stopScan()
	{
		if(cb == null) return;
		cb = null;
		
        ba.stopLeScan(cb);
	}
	
	/**
	 * Identify properties with the record bytes
	 * 
	 * @param record	BLE GATT record bytes
	 * @return 			detected beacon's property
	 */
	public static BeaconProp parseRecord(byte[] record)
	{
		BeaconProp prop = null;
		
		for(int ptr = 2; ptr <= 5; ptr++)
		{
			if(((int)record[ptr+2] & 0xff) == 0x02 &&
			   ((int)record[ptr+3] & 0xff) == 0x15)
			{
				// relocate bytes
				byte[] uuidBytes = new byte[16];
		        System.arraycopy(record, ptr+4, uuidBytes, 0, 16);
		        String hexString = bytesToHex(uuidBytes);

		        // get uuid
		        String uuid =  hexString.substring(0,8) + "-" + 
		                hexString.substring(8,12) + "-" + 
		                hexString.substring(12,16) + "-" + 
		                hexString.substring(16,20) + "-" + 
		                hexString.substring(20,32);
		        
		        // Major, Minor
		        int major = (record[ptr+20] & 0xff) * 0x100 + (record[ptr+21] & 0xff);
		        int minor = (record[ptr+22] & 0xff) * 0x100 + (record[ptr+23] & 0xff);

				prop = new BeaconProp();
				prop.setUUID(uuid);
				prop.setMajor(major);
				prop.setMinor(minor);
				break;
			}
		}
		
		return prop;
	}
	
	/**
	 * bytesToHex method
	 * found on stackoverflow
	 * 
	 * @author maybeWeCouldStealAVan
	 * @see http://stackoverflow.com/a/9855338
	 */
	private static String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
