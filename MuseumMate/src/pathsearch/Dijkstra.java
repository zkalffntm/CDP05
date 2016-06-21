package pathsearch;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import datatype.Area;
import datatype.Link;
import datatype.Node;

public class Dijkstra
{
	private static final int WEIGHT_ADJACENT = 12;
	private static final int WEIGHT_DIAGONAL = 17;
	private static final int WEIGHT_LINK = 20;

	private static List<Integer> path;
	private static Edge[] adjacencyList;
	private static int nodeListSize;
	
	public static void makeGraph(List<Area> areaList, List<Node> nodeList)
	{
		nodeListSize = nodeList.size();
		ArrayList<Edge> edgeList = new ArrayList<Edge>();

		Log.i("test", "arealist : " + areaList.size());
		Log.i("test", "nodelist : " + nodeList.size());
		
		for(int i = 0; i < nodeList.size(); i++)
		{
			nodeList.get(i).setTag(i);
			
			//Log.i("test", nodeList.get(i).getTag() + " : "+ nodeList.get(i).getArea().getNumber() + "." + 
			//				nodeList.get(i).getBlockNumber());
		}

		for(Area e : areaList)
		{
			int rowCount = e.getRowCount();
			int columnCount = e.getColumnCount();

			for (int i = 0; i < rowCount; i++)
			{
				for (int j = 0; j < columnCount; j++)
				{
					Node currentNode = e.getNode(i, j);
					if(currentNode == null) continue;
					
					if(currentNode.getType() == Node.TYPE.LINK)
					{
						Area dstArea = ((Link)currentNode).getDestinationArea();
						int dstBlockNum = ((Link)currentNode).getDestinationBlockNumber();
						int colCnt = dstArea.getColumnCount();
						Node dstNode = dstArea.getNode(dstBlockNum / colCnt, dstBlockNum % colCnt);
						edgeList.add(new Edge(currentNode.getTag(), dstNode.getTag(), WEIGHT_LINK));
					}
						
					
					if(i + 1 < rowCount) // Not bottom row
					{
						if(j > 0)
						{
							Node leftlowerNode = e.getNode(i + 1, j - 1);
							if(leftlowerNode != null)
								edgeList.add(new Edge(currentNode.getTag(), leftlowerNode.getTag(), 
										WEIGHT_DIAGONAL));
						}

						Node lowerNode = e.getNode(i + 1, j);
						if(lowerNode != null)
							edgeList.add(new Edge(currentNode.getTag(), lowerNode.getTag(), 
									WEIGHT_ADJACENT));
						
						if(j + 1 < columnCount)
						{
							Node rightlowerNode = e.getNode(i + 1, j + 1);
							if(rightlowerNode != null)
								edgeList.add(new Edge(currentNode.getTag(), rightlowerNode.getTag(), 
										WEIGHT_DIAGONAL));

							Node rightNode = e.getNode(i, j + 1);
							if(rightNode != null)
								edgeList.add(new Edge(currentNode.getTag(), rightNode.getTag(), 
										WEIGHT_ADJACENT));
						}
					}
					else
					{
						if(j + 1 < columnCount)
						{
							Node rightNode = e.getNode(i, j + 1);
							if(rightNode != null)
								edgeList.add(new Edge(currentNode.getTag(), rightNode.getTag(), 
										WEIGHT_ADJACENT));
						}
					}
				}
			}
		}

		adjacencyList = new Edge[edgeList.size()];
		edgeList.toArray(adjacencyList);
		
		//for(Edge e : adjacencyList)
		//	Log.i("test", nodeList.get(e.v1).getArea().getNumber() + "." + 
		//					nodeList.get(e.v1).getBlockNumber() + " -> " + 
		//					nodeList.get(e.v2).getArea().getNumber() + "." + 
		//					nodeList.get(e.v2).getBlockNumber() + " : " + e.dist);
		//Log.i("test", "len : " + adjacencyList.length);
	}
	
	public static int calculatePath(int start, int end)
	{
		Graph graph = new Graph(adjacencyList, nodeListSize);
		
		Vertex.totalDist = 0;
		graph.dijkstra(start);
		path = graph.getPath(end);
		
		return Vertex.totalDist;
	}

	public static List<Integer> getPath()
	{ return path; }
}
