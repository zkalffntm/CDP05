package bluetooth;

import android.app.Activity;
import android.bluetooth.*;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BeaconInterface
{
	/******************* Singleton *******************/
	private static BeaconInterface self;
	public static BeaconInterface getInstance()
	{
		if(self == null) self = new BeaconInterface();
		return self;
	}
	private BeaconInterface() {}
	/*************************************************/

	private BluetoothAdapter ba;
	private LeScanCallback cb;
	
	/*************************************************/
	
	// 블루투스 켜기
	// ret : true (블루투스 켜져 있음), false (블루투스 꺼져 있었고 켜라고 안내했음)
	public boolean checkOn(Activity a)
	{
		// 어댑터 받기
		if(ba == null)
		{
			final BluetoothManager bm = 
					(BluetoothManager)a.getSystemService(Context.BLUETOOTH_SERVICE);
			ba = bm.getAdapter();
		}
		
		// 꺼져 있으면 켜기
		if(!ba.isEnabled())
		{
		    Intent btin = 
		    		new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    a.startActivity(btin);
		    return false;
		}
		
		return true;
	}
	
	// 스캔 시작
	public void startScan(LeScanCallback cb)
	{
		if(this.cb != null) return;
		this.cb = cb;
		
        ba.startLeScan(cb);
	}
	
	// 스캔 중지
	public void stopScan()
	{
		if(cb == null) return;
		cb = null;
		
        ba.stopLeScan(cb);
	}
	
	// record byte로 properties 식별
	public static BeaconProp parseRecord(byte[] record)
	{
		BeaconProp prop = null;
		
		for(int ptr = 2; ptr <= 5; ptr++)
		{
			if(((int)record[ptr+2] & 0xff) == 0x02 &&
			   ((int)record[ptr+3] & 0xff) == 0x15)
			{
				// 바이트 순서 변환
				byte[] uuidBytes = new byte[16];
		        System.arraycopy(record, ptr+4, uuidBytes, 0, 16);
		        String hexString = bytesToHex(uuidBytes);

		        // uuid 구하기
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
	 * Found on the internet
	 * http://stackoverflow.com/a/9855338
	 */
	static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
