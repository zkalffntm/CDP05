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

public class MainActivity extends Activity
{
	private boolean 	scanning;
	private TextView	txtStatus;
	private Button		btnScan;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtStatus = (TextView)findViewById(R.id.txtStatus);
		btnScan = (Button)findViewById(R.id.btnScan);
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
					Log.i("BLETest", device.toString() + " -> "
							+ device.getName() + " ~ " + rssi);
				}
			};

			bi.startScan(cb);
			scanning = true;
			btnScan.setText("½ºÄµ ÁßÁö");
		}
	}
}
