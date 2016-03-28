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
	
	// ������� �ѱ�
	// ret : true (������� ���� ����), false (������� ���� �־��� �Ѷ�� �ȳ�����)
	public boolean checkOn(Activity a)
	{
		// ����� �ޱ�
		if(ba == null)
		{
			final BluetoothManager bm = 
					(BluetoothManager)a.getSystemService(Context.BLUETOOTH_SERVICE);
			ba = bm.getAdapter();
		}
		
		// ���� ������ �ѱ�
		if(!ba.isEnabled())
		{
		    Intent btin = 
		    		new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    a.startActivity(btin);
		    return false;
		}
		
		return true;
	}
	
	// ��ĵ ����
	public void startScan(LeScanCallback cb)
	{
		if(this.cb != null) return;
		this.cb = cb;
		
        ba.startLeScan(cb);
	}
	
	// ��ĵ ����
	public void stopScan()
	{
		if(cb == null) return;
		cb = null;
		
        ba.stopLeScan(cb);
	}
}
