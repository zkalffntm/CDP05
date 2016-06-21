package pathsearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** One vertex of the graph, complete with mappings to neighbouring vertices */
public class Vertex implements Comparable<Vertex>
{
	public static int totalDist;
	
	public final int id;
	public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
	public Vertex previous = null;
	public final Map<Vertex, Integer> neighbours = new HashMap<>();

	public Vertex(int id)
	{ this.id = id; }

	public void getPath(List<Integer> path)
	{
		if(this == previous) path.add(id);
		else if(previous == null) path.add(id);
		else
		{
			previous.getPath(path);
			path.add(id);
			totalDist += neighbours.get(previous);
		}
	}

	public int compareTo(Vertex other)
	{ return dist - other.dist; }
}