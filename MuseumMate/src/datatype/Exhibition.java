package datatype;

import java.io.Serializable;

import android.net.Uri;

public class Exhibition implements Serializable
{
	public Exhibition(	int id, int mapNum, int blockNum, String name, 
						String author, String description, Uri imageUri)
	{
		this.id = id;
		this.mapNum = mapNum;
		this.blockNum = blockNum;
		this.name = name;
		this.author = author;
		this.description = description;
		this.imageUri = imageUri;
	}
	
	private int id;
	private int mapNum;
	private int blockNum;
	
	private String name;
	private String author;
	private String description;
	private Uri imageUri;
	
	
	public int getId()				{ return id; }
	public int getMapNum()			{ return mapNum; }
	public int getBlockNum()		{ return blockNum; }
	public String getName()			{ return name; }
	public String getAuthor()		{ return author; }
	public String getDescription()	{ return description; }
	public Uri getImageUri()		{ return imageUri; }
}
