package harmony.beacon.blepatterncapture;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import bluetooth.BeaconInterface;
import bluetooth.BeaconProp;

public class MainActivity extends Activity
{
	private boolean 	scanning;
	private int[]		avgRssi;
	private TextView[]	txtStatus;
	private Button		btnScan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtStatus = new TextView[4];
		txtStatus[0] = (TextView)findViewById(R.id.textView1);
		txtStatus[1] = (TextView)findViewById(R.id.textView2);
		txtStatus[2] = (TextView)findViewById(R.id.textView3);
		txtStatus[3] = (TextView)findViewById(R.id.textView4);
		btnScan = (Button)findViewById(R.id.btnScan);
		avgRssi = new int[4];
		for(int iter : avgRssi) iter = 0;
	}

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
	
	
	public void btnScanOnClick(View v)
	{
		BeaconInterface bi = BeaconInterface.getInstance();

		if(scanning)
		{
			bi.stopScan();
			scanning = false;
			btnScan.setText("½ºÄµ ½ÃÀÛ");
		}
		else if(bi.checkOn(this))
		{
			LeScanCallback cb = new LeScanCallback()
			{
				@Override
				public void onLeScan(BluetoothDevice device, int rssi, byte[] record)
				{
					BeaconProp prop = BeaconInterface.parseRecord(record);
					
					Log.i("test", prop.getUUID());
					if(prop.getUUID().equals("78751DE4-040B-11E6-B512-3E1D05DEFE78"))
					{
						final int minor = prop.getMinor();
						if(avgRssi[minor] == 0) avgRssi[minor] = rssi;
						else avgRssi[minor] = (int)(avgRssi[minor] * 0.8 + rssi * 0.2);
						
						if(minor >= 0 || minor < 4)
							runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{ txtStatus[minor].setText(minor + " : " + avgRssi[minor]); }
							});
					}
				}
			};
			bi.startScan(cb);
			scanning = true;
			btnScan.setText("½ºÄµ ÁßÁö");
		}
	}
}
