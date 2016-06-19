package pathsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Graph
{
	public final Vertex[] graph;

	public Graph(Edge[] edges, int vertexCount)
	{
		graph = new Vertex[vertexCount];

		// one pass to find all vertices
		for(Edge e : edges)
		{
			if(graph[e.v1] == null) graph[e.v1] = new Vertex(e.v1);
			if(graph[e.v2] == null) graph[e.v2] = new Vertex(e.v2);
		}

		// another pass to set neighbouring vertices
		for(Edge e : edges)
		{
			graph[e.v1].neighbours.put(graph[e.v2], e.dist);
			if(e.dist != 20) graph[e.v2].neighbours.put(graph[e.v1], e.dist);
		}
	}

	/** Runs dijkstra using a specified source vertex */
	public void dijkstra(int startName)
	{
		if(startName >= graph.length || graph[startName] == null)
		{
			System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
			return;
		}

		final Vertex source = graph[startName];
		NavigableSet<Vertex> q = new TreeSet<>();

		// set-up vertices
		for(Vertex v : graph)
		{
			if(v == null) continue;
			v.previous = v == source ? source : null;
			v.dist = v == source ? 0 : Integer.MAX_VALUE;
			q.add(v);
		}

		dijkstra(q);
	}

	/** Implementation of dijkstra's algorithm using a binary heap. */
	private void dijkstra(final NavigableSet<Vertex> q)
	{
		Vertex u, v;
		while (!q.isEmpty())
		{
			u = q.pollFirst(); // vertex with shortest distance (first iteration
								// will return source)
			if (u.dist == Integer.MAX_VALUE)
				break; // we can ignore u (and any other remaining vertices)
						// since they are unreachable

			// look at distances to each neighbour
			for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet())
			{
				v = a.getKey(); // the neighbour in this iteration

				final int alternateDist = u.dist + a.getValue();
				if (alternateDist < v.dist)
				{ // shorter path to neighbour found
					q.remove(v);
					v.dist = alternateDist;
					v.previous = u;
					q.add(v);
				}
			}
		}
	}

	/** Prints a path from the source to the specified vertex */
	public List<Integer> getPath(int endName)
	{
		if (endName >= graph.length || graph[endName] == null)
		{ return null; }

		ArrayList<Integer> path = new ArrayList<Integer>();
		graph[endName].getPath(path);
		return path;
	}
}