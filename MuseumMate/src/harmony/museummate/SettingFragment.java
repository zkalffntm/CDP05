package harmony.museummate;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

public class SettingFragment extends PreferenceFragment
{
	// Singleton /////////////////////////////////////////////////////
	private static SettingFragment self;							//
	public static SettingFragment getInstance()						//
	{ if(self == null) self = new SettingFragment(); return self; }	//
	private SettingFragment() {}									//
	//////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.xml.pref_screen);
    }    
}
