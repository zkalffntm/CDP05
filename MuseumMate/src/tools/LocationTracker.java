package tools;

import datatype.Exhibition;
import datatype.Museum;

public class LocationTracker
{
	// Singleton /////////////////////////////////////////////
	private static volatile LocationTracker self;			//
	public static LocationTracker getInstance()				//
	{														//
		synchronized(LocationTracker.class)					//
		{ if(self == null) self = new LocationTracker(); }	//
		return self;										//
	}														//
	//////////////////////////////////////////////////////////
	
	private LocationTracker() {}
	
	public Exhibition getCurrentExhibition()
	{
		// test code
		return Museum.getSelectedMuseum().getExhibitionList().get(0);
	}
}
