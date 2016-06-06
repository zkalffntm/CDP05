package datatype;

import android.net.Uri;

public class Exhibition	extends Item
{
	public Exhibition(	int id, String name, String author,
						String summary, String description, String image)
	{
		this.id = id;
		this.name = name;
		this.author = author;
		this.summary = summary;
		this.description = description;
		this.image = image;
	}

	private int id;
	private String name;
	private String author;
	private String summary;
	private String description;
	private String image;
	
	
	@Override
	public TYPE getType() { return TYPE.EXHIBITION; }
	
	public int		getId()				{ return id; }
	public String	getName()			{ return name; }
	public String	getAuthor()			{ return author; }
	public String	getSummary()		{ return summary; }
	public String	getDescription()	{ return description; }
	public String	getImage()			{ return image; }
}
