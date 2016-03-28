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
}
