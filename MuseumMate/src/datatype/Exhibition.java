package datatype;

import java.io.Serializable;

import android.net.Uri;

public class Exhibition	implements Item, Serializable
{
	public Exhibition(	int id, int blockNum, String name, 
						String author, String description, Uri imageUri)
	{
		this.id = id;
		this.blockNum = blockNum;
		this.name = name;
		this.author = author;
		this.description = description;
		this.imageUri = imageUri;
	}
	
	private int id;
	private Area area;
	private int blockNum;
	
	private String name;
	private String author;
	private String description;
	private Uri imageUri;

	
	@Override
	public TYPE getType() { return TYPE.EXHIBITION; }
	
	public int getId()				{ return id; }
	public Area getArea()			{ return area; }
	public int getBlockNum()		{ return blockNum; }
	public String getName()			{ return name; }
	public String getAuthor()		{ return author; }
	public String getDescription()	{ return description; }
	public Uri getImageUri()		{ return imageUri; }
}
