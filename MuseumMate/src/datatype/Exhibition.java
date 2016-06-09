package datatype;

public class Exhibition	extends Node
{
	public Exhibition(	int id, String name, String author,
						String summary, String description,
						int[] imageIds)
	{
		this.id = id;
		this.imageIds = imageIds;
		this.name = name;
		this.author = author;
		this.summary = summary;
		this.description = description;
	}

	private int id;
	private String name;
	private String author;
	private String summary;
	private String description;
	private int[] imageIds;
	
	
	@Override
	public TYPE getType() { return TYPE.EXHIBITION; }
	
	public int		getId()				{ return id; }
	public String	getName()			{ return name; }
	public String	getAuthor()			{ return author; }
	public String	getSummary()		{ return summary; }
	public String	getDescription()	{ return description; }
	public int[]	getImageIds()		{ return imageIds; }
}
