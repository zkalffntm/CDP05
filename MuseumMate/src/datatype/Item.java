package datatype;

import java.io.Serializable;

abstract class Item implements Serializable
{
	private Location location;
	
	public enum TYPE { EXHIBITION, LINK };
	public abstract TYPE getType();
	
	public Location getLocation() { return location; }
	public void setLocation(Location location) { this.location = location; }
	
}
