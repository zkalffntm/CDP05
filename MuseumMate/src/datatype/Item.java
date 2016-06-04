package datatype;

interface Item
{
	public enum TYPE { EXHIBITION, SHAREBLOCK };
	public abstract TYPE getType();
}
