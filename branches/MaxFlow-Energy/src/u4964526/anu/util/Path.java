package u4964526.anu.util;

import java.util.ArrayList;

public class Path {
	
	private final ArrayList<Vertex> vertexs;
	private final ArrayList<Edge> edges;
	private int nStep=0;
	private double bottleNeck=Double.POSITIVE_INFINITY;
	private double length=0;
	private double wLength=0;
	private double weight=1;
	private Vertex start;
	private Vertex end;

	
	public double getBottleNeck() {
		return bottleNeck;
	}

	public Path() {
		// TODO Auto-generated constructor stub
		vertexs=new ArrayList<Vertex>();
		edges=new ArrayList<Edge>();
	}
	
	public void addVertex(Vertex v)
	{
		vertexs.add(v);
	}
	
    public ArrayList<Vertex> getVertexs() {
		return vertexs;
	}

	public void addEdge(Edge e)
    {
    	if(e!=null)
    	{
    	  edges.add(e);
    	  vertexs.add(e.getSource());
    	  ++nStep;
    	  this.length=this.length+e.getLength();
    	  this.wLength=this.length/this.getWeight();
    	  if (this.bottleNeck > e.getCapacity())
    	  {
             this.bottleNeck=e.getCapacity();
    	  }
    	}
    	else
    	{
    		this.length=Double.POSITIVE_INFINITY;
    		this.wLength=Double.POSITIVE_INFINITY;
    	}
    }


	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public double getLength() {
		return length;
	}

	public double getwLength() {
		return wLength;
	}

	public double getwULength() {
		/*
		 * only for concurrent flow
		 */
		double wULength=0;
		for(int i=0;i<edges.size();i++)
		{
			Edge e=edges.get(i);
			wULength=wULength+e.getCapacity()*e.getLength();
		}
		return wULength;
		
	}

	public void setStart(Vertex start) {
		this.start = start;
	}

	public Vertex getStart() {
		return start;
	}

	public void setEnd(Vertex end) {
		this.end = end;
	}

	public Vertex getEnd() {
		return end;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void updateLength(double add)
	{
		for (int i=0;i<edges.size();i++)
		{
			Edge e=edges.get(i);
			double l=e.getLength()*(1+add/e.getCapacity());
			e.setLength(l);
		}
	}
	
	public void updateRealCap(double add)
	{
		for (int i=0;i<edges.size();i++)
		{
			Edge e=edges.get(i);
			double realCap=e.getRealCap()+add;
			e.setRealCap(realCap);
		}
	}
	
	public Path getCopy()
	{
		Path pCopy=new Path();
		pCopy.setStart(start);
		pCopy.setEnd(end);
		for(int i=0;i<vertexs.size();i++)
		{
			pCopy.addVertex(vertexs.get(i));
		}
		for(int i=0;i<edges.size();i++)
		{
			pCopy.addEdge(edges.get(i));
		}
		
		return pCopy;
	}
	
	
	public String toString()
	{
		String result="@p<"+this.start+"-"+this.end+"-l"+this.length+"-wl"+this.weight+"-c"+this.bottleNeck+"> : \n\t@Edges: ";
		for(int i=0;i<edges.size();i++)
		{
			result=result+edges.get(i)+"  ";
		}
		result=result+"\n\t@Vertexs:";
		for(int i=0;i<vertexs.size();i++)
		{
			result=result+vertexs.get(i)+" ";
		}
		
		return result;
	}
	
	public static void main(String[] args)
	{
		Path p=new Path();
		Vertex aV=new Vertex("A");
        Vertex bV=new Vertex("B");
        //Vertex cV=new Vertex("C");
        //Vertex dV=new Vertex("D");
        //Vertex tV=new Vertex("T");
        //Vertex kV=new Vertex("K");
        Vertex sV=new Vertex("S");
        Edge asE=new Edge(aV,sV,2);
        Edge bsE=new Edge(bV,sV,2);
        
        p.addEdge(asE);
        System.out.println("asE"+p.getEdges().contains(asE));
        System.out.println("bsE"+p.getEdges().contains(bsE));
		
		
	}
	
	
}
