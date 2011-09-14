package u4964526.anu.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Set;

public class Graph {

	private static int MAX_VERTS=1000;
	private ArrayList<Vertex> vertexList;
	private Edge adjMat[][];
	private ArrayList<Edge> edgeList;
	private int nVerts;
	private ArrayList<Vertex> sourceList; //specified for Weighted Max-Flow problem
	private ArrayList<Vertex> sinkList;   //specified for Weighted Max-Flow problem
	
	public Graph() {
		// TODO Auto-generated constructor stub
		vertexList=new ArrayList<Vertex>();
		edgeList=new ArrayList<Edge>();
		adjMat=new Edge[MAX_VERTS][MAX_VERTS];
		nVerts=0;
		
		sourceList=new ArrayList<Vertex>();      //specified for Weighted Max-Flow problem
		sinkList=new ArrayList<Vertex>();        //specified for Weighted Max-Flow problem
	}
	

	public ArrayList<Vertex> getVertexList() {
		return vertexList;
	}


	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}


	public void setSourceList(ArrayList<Vertex> sourceList) {
		this.sourceList = sourceList;
	}


	public ArrayList<Vertex> getSourceList() {
		return sourceList;
	}

	public ArrayList<Vertex> getSinkList() {
		return sinkList;
	}
	
	
	
	public void addVertex(Vertex add)
	{
		add.setVerValue(nVerts);
		vertexList.add(add);
		nVerts++;
	}
	
	public void addEdge(Edge add)
	{
		adjMat[add.getSource().getVerValue()][add.getTarget().getVerValue()]=add;
        edgeList.add(add);
	}
	
	public void addSource(Vertex add)
	{
		sourceList.add(add);
	}
	
	public void addSink(Vertex add)
	{
		sinkList.add(add);
	}
	
	public HashMap<Vertex,Path> getShortPath(Vertex sink)
	//specified for Weighted Max-Flow problem
	{
		int s=sink.getVerValue();
		double tempMat[][]=new double [this.nVerts][this.nVerts];
		for(int i=0;i<nVerts;i++)
			for(int j=0; j<nVerts;j++)
			{
				if(adjMat[j][i]==null)
					tempMat[i][j]=Double.POSITIVE_INFINITY;
				else
					tempMat[i][j]=adjMat[j][i].getLength();
			}
		
		Dist sDist[]=new Dist[nVerts];
		int sSet[]=new int[nVerts];
		int tSet[]=new int[nVerts];
		for (int i=0;i<nVerts;i++)
		{
			sDist[i]=new Dist();
			sDist[i].setPreviousVer(s);
			sDist[i].setShortDist(Double.POSITIVE_INFINITY);
			sSet[i]=0;
			tSet[i]=1;
		}
		sSet[s]=1;
		tSet[s]=0;
		for (int i=0;i<nVerts;i++)
		{
			if(Double.compare(Double.POSITIVE_INFINITY, tempMat[s][i])>0)
			{
				sDist[i].setPreviousVer(s);
				sDist[i].setShortDist(tempMat[s][i]);
			}
			
		}
		
		int loopNum=0;
		
		while (checkSet(tSet))
		{
			
			int m=getMin(sDist,tSet);
			tSet[m]=0;
			
			/*
			 * begin of debug info
			 *
			++loopNum;
			System.out.println("loopNum---"+loopNum);
			System.out.println("m---------"+m);
			*
			 * end of debug info
			 */
			
			/*
			 * begin of update sDist
			 */
			double sm=sDist[m].getShortDist();
			for(int i=0;i<sDist.length;i++)
			{
				double mi=tempMat[m][i];
				if((mi+sm)<sDist[i].getShortDist())
				{
					sDist[i].setPreviousVer(m);
					sDist[i].setShortDist(sm+mi);
				}
				
			}
			/*
			 * end of update sDist
			 */
		}
		
		/*
		 *  begin of Path Creation
		 */
		HashMap<Vertex,Path> result=new HashMap<Vertex,Path>();
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s1=sourceList.get(i);
			Path p1=new Path();
			p1.setStart(s1);
			p1.setEnd(sink);
			p1.setWeight(s1.getWeight());
			int se1=s1.getVerValue();
			while(se1!=sink.getVerValue())
			{
				int j=sDist[se1].getPreviousVer();
				p1.addEdge(adjMat[se1][j]);
				se1=j;
			}
			result.put(s1, p1);
			
			/*
			 * begin of debug info
			 *
			System.out.println(p1);
			*
			 * end of debug info
			 */
		}
		
		return result;
		/*
		 *  end of Path Creation
		 */
	}
	
	
	private boolean checkSet(int[] a)
	{
		boolean result=false;
		for(int i=0;i<a.length;i++)
		{
			if(a[i]>0)
				return true;
		}
		return result;
	}
	
	private int getMin(Dist[] a,int[] t)
	{
	    int result=0;
	    double min=0;
	    min=Double.POSITIVE_INFINITY;
	    for (int i=0;i<a.length;i++)
	    {
	    	if((t[i]>0)&&(min>=a[i].getShortDist()))
	    	{
	    		min=a[i].getShortDist();
	    		result=i;
	    	}
	    }
	    return result;
	}
	
	public Path getSingleShortPath(Vertex source, Vertex sink)
	{
		HashMap<Vertex,Path> pSet=this.getShortPathAndDSNode(sink);
		Path p=pSet.get(source);
		return p;
	}
	
	public void transit()
	{
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s1=sourceList.get(i);//--------------------output 
			Vertex s2=new Vertex(s1.getName()+"1");//---------input
			s2.setWeight(s1.getWeight());
			s2.setMaxRate(s1.getMaxRate());
			s2.setBudgetEnergy(s1.getBudgetEnergy());
			s2.setxLabel(s1.getxLabel());
			s2.setyLabel(s1.getyLabel());
			this.addVertex(s2);
			Edge e1=new Edge(s1,s2,s1.getBudgetEnergy()); 
			//System.out.println("@@@"+e1);
			e1.setWasFaked(true);

			
			for(int j=0;j<edgeList.size();j++)
			{
				Edge eTemp=edgeList.get(j);
				eTemp.setCapacity(eTemp.getSource().getBudgetEnergy());
				Vertex tTemp=eTemp.getSource();
				adjMat[eTemp.getSource().getVerValue()][eTemp.getTarget().getVerValue()]=null;
				if(tTemp==s1)
				{
					eTemp.setSource(s2);
				}
				adjMat[eTemp.getSource().getVerValue()][eTemp.getTarget().getVerValue()]=eTemp;
			}
			
			this.addEdge(e1);
			
		}
	}
	
	public String toString()
	{
		String result="The Graph information:\n";
		result=result+"********1.Vertex:********\n";
		for (int i=0;i<vertexList.size();i++)
		{
			Vertex v1=vertexList.get(i);
			result=result+v1.getName()+"<"+"(x-"+v1.getxLabel()+")"+"(y-"+v1.getyLabel()+")"+"(weight-"+v1.getWeight()+")"+"(Maxrate-"+v1.getMaxRate()+")"+"(rate-"+v1.getRate()+")"+">\n";
		}
		
		result=result+"********2.Edge********\n";
		for(int i=0;i<edgeList.size();i++)
		{
			Edge e1=edgeList.get(i);
			//result=result+"<"+e1.getSource()+"---"+e1.getTarget()+">  <"+"(capacity-"+e1.getCapacity()+")"+"(length-"+e1.getLength()+")>\n";
			result=result+e1+"\n";
		}
		
		result=result+"********3.Source********\n";
		for (int i=0;i<sourceList.size();i++)
		{
			Vertex v1=sourceList.get(i);
			result=result+v1.getName()+"Connected"+v1.isWasConnected()+"<"+"(x-"+v1.getxLabel()+")"+"(y-"+v1.getyLabel()+")"+"(weight-"+v1.getWeight()+")"+"(rate-"+v1.getRate()+")"+">\n";
		}
		
		result=result+"********4.Sink********\n";
		for (int i=0;i<sinkList.size();i++)
		{
			Vertex v1=sinkList.get(i);
			result=result+v1.getName()+"<"+"(x-"+v1.getxLabel()+")"+"(y-"+v1.getyLabel()+")"+"(weight-"+v1.getWeight()+")"+"(rate-"+v1.getRate()+")"+">\n";
		}
		
		return result;
	}
	
	public HashMap<Vertex,Path> getShortPathAndDSNode(Vertex sink)
	//specified for Weighted Max-Flow problem
	{
		int s=sink.getVerValue();
		double tempMat[][]=new double [this.nVerts][this.nVerts];
		for(int i=0;i<nVerts;i++)
			for(int j=0; j<nVerts;j++)
			{
				if(adjMat[j][i]==null)
					tempMat[i][j]=Double.POSITIVE_INFINITY;
				else
					tempMat[i][j]=adjMat[j][i].getLength();
			}
		
		Dist sDist[]=new Dist[nVerts];
		int sSet[]=new int[nVerts];
		int tSet[]=new int[nVerts];
		for (int i=0;i<nVerts;i++)
		{
			sDist[i]=new Dist();
			sDist[i].setPreviousVer(s);
			sDist[i].setShortDist(Double.POSITIVE_INFINITY);
			sSet[i]=0;
			tSet[i]=1;
		}
		
		
		sSet[s]=1;
		tSet[s]=0;
		for (int i=0;i<nVerts;i++)
		{
			if(Double.compare(Double.POSITIVE_INFINITY, tempMat[s][i])>0)
			{
				sDist[i].setPreviousVer(s);
				sDist[i].setShortDist(tempMat[s][i]);
			}
			
		}
		
		int loopNum=0;
		
		while (checkSet(tSet))
		{
			
			int m=getMin(sDist,tSet);
			tSet[m]=0;
			
			/*
			 * begin of debug info
			 *
			++loopNum;
			System.out.println("loopNum---"+loopNum);
			System.out.println("m---------"+m);
			/*
			 * end of debug info
			 */
			
			/*
			 * begin of update sDist
			 */
			double sm=sDist[m].getShortDist();
			for(int i=0;i<sDist.length;i++)
			{
				double mi=tempMat[m][i];
				if((mi+sm)<sDist[i].getShortDist())
				{
					sDist[i].setPreviousVer(m);
					sDist[i].setShortDist(sm+mi);
				}
				
			}
			/*
			 * end of update sDist
			 */
		}
		
		/*
		 *  begin of Path Creation
		 */
		HashMap<Vertex,Path> result=new HashMap<Vertex,Path>();
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s1=sourceList.get(i);
			Path p1=new Path();
			p1.setStart(s1);
			p1.setEnd(sink);
			p1.setWeight(s1.getWeight());
			int se1=s1.getVerValue();
			while(se1!=sink.getVerValue())
			{
				int j=sDist[se1].getPreviousVer();
				if(sDist[se1].getShortDist()>1000000)
				{
					s1.setWasConnected(false);
				}
				if(adjMat[se1][j]!=null)
				{
					p1.addEdge(adjMat[se1][j]);
					adjMat[se1][j].setWasTreed(true);
					adjMat[se1][j].addWfNode(s1);
					
				}
				se1=j;
			}
			
			result.put(s1, p1);
			
			/*
			 * begin of debug info
			 *
			Logger logger=Logger.getLogger("MaxFlow");
			logger.info(String.valueOf(p1));
			/*
			 * end of debug info
			 */
		}
		
		/*
		 * begin of debug info
		 *
		for(int i=0;i<edgeList.size();i++)
		{
			Edge te=edgeList.get(i);
			ArrayList<Vertex> tvSet=te.getWfNodeSet();
			if(tvSet.size()>0)
			{
				System.out.println(te+"\n"+tvSet);
			}
		}
		/*
		 * end of debug info
		 */
		
		return result;
		/*
		 *  end of Path Creation
		 */
	}
	
	private void getSubPath(Path p, ArrayList<Path> pSet, double[][] tempMat)
	{
		ArrayList<Vertex> pV=p.getVertexs();
		int pS=p.getStart().getVerValue();
		for(int i=0;i<nVerts;i++)
		{
			if(tempMat[pS][i]>0.5)
			{
				Edge te=adjMat[i][pS];
				if(pV.contains(te.getSource()))
				{
					
				}
				else{
					Path pCopy=p.getCopy();
					pCopy.addEdge(te);
					pCopy.addVertex(te.getSource());
					pCopy.setStart(te.getSource());
					pSet.add(pCopy);
					getSubPath(pCopy,pSet,tempMat);
				}
			}
		}
		
	}
	
	public ArrayList<Path> getAllPath(Vertex sink)
	{
		ArrayList<Path> pSet=new ArrayList<Path>();
		int s=sink.getVerValue();
		double tempMat[][]=new double [this.nVerts][this.nVerts];
		for(int i=0;i<nVerts;i++)
		{
			for(int j=0; j<nVerts;j++)
			{
				if(adjMat[j][i]==null)
					tempMat[i][j]=0;
				else
					tempMat[i][j]=1;
			}
		}
		
		for(int i=0;i<nVerts;i++)
		{
			if(tempMat[s][i]>0.5)
			{
				Edge e1=adjMat[i][s];
				Path p1=new Path();
				p1.setEnd(sink);
				p1.setStart(e1.getSource());
				p1.addEdge(e1);
				p1.addVertex(e1.getSource());
				p1.addVertex(sink);
				pSet.add(p1);
				this.getSubPath(p1, pSet, tempMat);
			}
		}
		
		return pSet;
	}
	
	public void outputFile(String fVertex, String fEdge) throws FileNotFoundException
	{
		PrintWriter pwVertex=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fVertex)));
		PrintWriter pwEdge=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fEdge)));
		for(int i=0;i<this.getVertexList().size();i++)
		{
			Vertex v=this.getVertexList().get(i);
			pwVertex.println(v+" "+v.getxLabel()+" "+v.getyLabel()+" "+v.getWeight()+" "+v.getMaxRate()+" "+v.getBudgetEnergy());
			pwVertex.flush();
		}
		for(int i=0;i<this.getEdgeList().size();i++)
		{
			Edge e=this.getEdgeList().get(i);
			pwEdge.println(e.getSource()+" "+e.getTarget()+" "+e.getCapacity());
			pwEdge.flush();
		}
	}
	
	
	public static void main(String[] args) {
		
		String fileName1="test/topology/vertex_450_0.txt";
		String fileName2="test/topology/edge_450_0.txt";
		
		
		
		
		
			
	    Graph g=new Graph();
	    TestMaxFlow.initRandomData(fileName1, fileName2, g,1);
		
		
		
		System.out.println(g);
		g.transit();
		System.out.println(g);
		//g.getShortPathAndDSNode(g.getSinkList().get(0));
		System.out.println(g);
	}
	
}
