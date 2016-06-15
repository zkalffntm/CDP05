package datatype;

import java.util.List;

import android.net.Uri;

public class User
{
	private static User currentUser;
	private String name;
	private String email;
	private String id;
	private Uri photoUri;
	private List<Visited> visitedList;
	
	public User(String name, String email, String id, Uri photoUri)
	{
		this.name = name;
		this.email = email;
		this.id = id;
		this.photoUri = photoUri;
	}

	public String 	getName()		{ return name; }
	public String 	getEmail()		{ return email; }
	public String 	getId()			{ return id; }
	public Uri 		getPhotoUri()	{ return photoUri; }


	public List<Visited> getVisitedList()					{ return visitedList; }
	public void setVisitedList(List<Visited> visitedList)	{ this.visitedList = visitedList; }
	
	public static User getCurrentUser()					{ return currentUser; }
	public static void setCurrentUser(User currentUser) { User.currentUser = currentUser; }
}
