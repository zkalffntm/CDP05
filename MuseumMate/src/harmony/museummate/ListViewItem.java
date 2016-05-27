package harmony.museummate;

public class ListViewItem
{
	private int		icon;
	private String	title;
	
	public ListViewItem(String title, int icon)
	{
		this.icon = icon;
		this.title = title;
	}
	
	public int		getIcon()	{ return icon; }
	public String	getTitle()	{ return title; }
}
