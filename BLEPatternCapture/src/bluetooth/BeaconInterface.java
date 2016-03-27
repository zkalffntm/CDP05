package bluetooth;

import android.app.Activity;
import android.bluetooth.*;
import android.bluetooth..BluetoothAdapter.*;
import android.content.Context;
import android.content.Intent;

public class BeaconInterface
{
	/******************* Singleton *******************/
	private BeaconInterface self;
	public BeaconInterface getInstance()
	{
		if(self == null) self = new BeaconInterface();
		return self;
	}
	private BeaconInterface() {}
	/*************************************************/
	
	private BluetoothAdapter ba;
	
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
	
}
