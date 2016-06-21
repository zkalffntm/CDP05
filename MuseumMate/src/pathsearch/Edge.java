package pathsearch;

/** One edge of the graph (only used by Graph constructor) */
public class Edge
{
   public final int v1, v2;
   public final int dist;
   public Edge(int v1, int v2, int dist)
   {
      this.v1 = v1;
      this.v2 = v2;
      this.dist = dist;
   }
}