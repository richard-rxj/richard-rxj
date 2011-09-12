package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

public class TestRealData {

	/**
	 * @param args
	 */
	private static double eTx=0.0000144;  //dB
	private static double eRx=0.00000576;  //dB
	private static double epsilon=0.1;
	private static double[] gBaseBudgetEnergy={0.01365,0.00653};
	private static double[] gBaseMaxRate={100000};
	private static double transRange=25;

	
	public static double getEpsilon() {
		return epsilon;
	}

	public static void setEpsilon(double apprFactor) {
		epsilon = apprFactor;
	}

	private static double getBudgetEnergy()
	{
		//Random r=new Random();
		//return gBaseBudgetEnergy[1]+r.nextDouble()*(gBaseBudgetEnergy[0]-gBaseBudgetEnergy[1]);
		return 0.00653*1.7;
	}

   private static double getVertexMaxRate()
   {
		return gBaseMaxRate[0];
   }
	
   private static double validTransRangePlusEdgeCapacity(Vertex v1, Vertex v2)
	{
		double result=0;
		double c1=Math.pow((v1.getxLabel()-v2.getxLabel()),2);
		double c2=Math.pow((v1.getyLabel()-v2.getyLabel()),2);
		double o=Math.pow(transRange,2);
		if(c1+c2<o)
		{
			result=c1+c2;
		}
		
		return result;
	}
   
   
   private static void initRealData(String fileName1, String fileName2, String fileName3, Graph g, int wOption) throws FileNotFoundException
	/*
	 *  set all weight according to the weight file
	 */
	{
		Logger logger=Logger.getLogger("MaxFlow");
		String tempString=null;
		BufferedReader reader=null;
		
		/*
		 * begin of initial Vertices
		 */
		try
		{
		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName1)));
		   int lineNum=0;
		   while((tempString=reader.readLine())!=null)
		   {
			   String[] b=tempString.split(" ");
			   /*
			    * begin of debug info
			    */
			   ++lineNum;
			   logger.fine(String.valueOf(lineNum));
			   logger.fine(tempString);
			   String detail="detail:(";
			   for(int i=0;i<b.length;i++)
			   {
				   detail=detail+"<"+i+"-"+b[i]+">";
			   }
			   detail=detail+")\n";
			   logger.fine(detail);
			   /*
			    * end of debug info
			    */
			   Vertex v1=new Vertex(b[0]);
			   v1.setxLabel(Double.parseDouble(b[1]));
			   v1.setyLabel(Double.parseDouble(b[2]));
			   v1.setMaxRate(getVertexMaxRate());
			   v1.setBudgetEnergy(getBudgetEnergy());
			   v1.setRate(0);
			   g.addVertex(v1);
			   if(Integer.parseInt(b[0])==1)
			   {
				   g.addSink(v1);
			   }
			   else
			   {
				   g.addSource(v1);
			   }
			   
		   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e)
				{
				}
			}
		}
		/*
		 * end of initial Vertices
		 */
		
		/*
		 * begin of generate edges
		 *
		ArrayList<Vertex> vSet=g.getVertexList();
		Iterator<Vertex> slowI=vSet.iterator();
		Iterator<Vertex> fastI;
		PrintWriter pwEdge=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/real/edgeGenerate.txt")));
		while(slowI.hasNext())
		{
			Vertex v1=slowI.next();
			fastI=vSet.iterator();
			
			while(fastI.next()!=v1)
			{
				
			}
			
			Vertex v2;
			while(fastI.hasNext())
			{
				v2=fastI.next();
				double tRange=validTransRangePlusEdgeCapacity(v1, v2);
				if(tRange>0)
				{
					//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
					Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
					g.addEdge(e1);
					pwEdge.println(v1+" "+v2+" "+e1.getCapacity());
					//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
					Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
					g.addEdge(e2);
					pwEdge.println(v2+" "+v1+" "+e1.getCapacity());
					pwEdge.flush();
				}
			}
			
		}
		/*
		 *  end of generate edges
		 */
		
		/*
		 * begin of initial Edges
		 */
		try
		{
		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName2)));
		   int lineNum=0;
		   while((tempString=reader.readLine())!=null)
		   {
			   String[] b=tempString.split(" ");
			   /*
			    * begin of debug info
			    */
			   ++lineNum;
			   logger.fine(String.valueOf(lineNum));
			   logger.fine(tempString);
			   String detail="detail:(";
			   for(int i=0;i<b.length;i++)
			   {
				   detail=detail+"<"+i+"-"+b[i]+">";
			   }
			   detail=detail+")\n";
			   logger.fine(detail);
			   /*
			    * end of debug info
			    */
			   Vertex s1=g.getVertexList().get(Integer.parseInt(b[0])-1);
			   Vertex t1=g.getVertexList().get(Integer.parseInt(b[1])-1);
			   Edge e1=new Edge(s1,t1,s1.getBudgetEnergy());
			   g.addEdge(e1);
			   
		   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e)
				{
				}
			}
		}
		/*
		 * end of initial Edges
		 */
		
		
		/*
		 * begin of initial Weights
		 */
		try
		{
		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName3)));
		   int lineNum=0;
		   while((tempString=reader.readLine())!=null)
		   {
			   String[] b=tempString.split(" ");
			   /*
			    * begin of debug info
			    */
			   ++lineNum;
			   logger.fine(String.valueOf(lineNum));
			   logger.fine(tempString);
			   String detail="detail:(";
			   for(int i=0;i<b.length;i++)
			   {
				   detail=detail+"<"+i+"-"+b[i]+">";
			   }
			   detail=detail+")\n";
			   logger.fine(detail);
			   /*
			    * end of debug info
			    */
			   Vertex v1=g.getVertexList().get(lineNum-1);
			   if(wOption>0)
			   {
				   v1.setWeight(1);
			   }
			   else
			   {
				   v1.setWeight(Double.parseDouble(b[0]));   
			   }
			   
			   
		   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e)
				{
				}
			}
		}
		/*
		 * end of initial Weights
		 */
	}
   
   private static void initRealDataRou(String fileName1, String fileName2, String fileName3, Graph g, int wOption, double rou) throws FileNotFoundException
	/*
	 *  set all weight according to the weight file
	 */
	{
		Logger logger=Logger.getLogger("MaxFlow");
		String tempString=null;
		BufferedReader reader=null;
		
		/*
		 * begin of initial Vertices
		 */
		try
		{
		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName1)));
		   int lineNum=0;
		   while((tempString=reader.readLine())!=null)
		   {
			   String[] b=tempString.split(" ");
			   /*
			    * begin of debug info
			    */
			   ++lineNum;
			   logger.fine(String.valueOf(lineNum));
			   logger.fine(tempString);
			   String detail="detail:(";
			   for(int i=0;i<b.length;i++)
			   {
				   detail=detail+"<"+i+"-"+b[i]+">";
			   }
			   detail=detail+")\n";
			   logger.fine(detail);
			   /*
			    * end of debug info
			    */
			   Vertex v1=new Vertex(b[0]);
			   v1.setxLabel(Double.parseDouble(b[1]));
			   v1.setyLabel(Double.parseDouble(b[2]));
			   v1.setMaxRate(getVertexMaxRate());
			   v1.setBudgetEnergy(getBudgetEnergy());
			   v1.setRate(0);
			   g.addVertex(v1);
			   if(Integer.parseInt(b[0])==1)
			   {
				   g.addSink(v1);
			   }
			   else
			   {
				   g.addSource(v1);
			   }
			   
		   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e)
				{
				}
			}
		}
		/*
		 * end of initial Vertices
		 */
		
		/*
		 * begin of generate edges
		 *
		ArrayList<Vertex> vSet=g.getVertexList();
		Iterator<Vertex> slowI=vSet.iterator();
		Iterator<Vertex> fastI;
		PrintWriter pwEdge=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/real/edgeGenerate.txt")));
		while(slowI.hasNext())
		{
			Vertex v1=slowI.next();
			fastI=vSet.iterator();
			
			while(fastI.next()!=v1)
			{
				
			}
			
			Vertex v2;
			while(fastI.hasNext())
			{
				v2=fastI.next();
				double tRange=validTransRangePlusEdgeCapacity(v1, v2);
				if(tRange>0)
				{
					//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
					Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
					g.addEdge(e1);
					pwEdge.println(v1+" "+v2+" "+e1.getCapacity());
					//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
					Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
					g.addEdge(e2);
					pwEdge.println(v2+" "+v1+" "+e1.getCapacity());
					pwEdge.flush();
				}
			}
			
		}
		/*
		 *  end of generate edges
		 */
		
		/*
		 * begin of initial Edges
		 */
		try
		{
		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName2)));
		   int lineNum=0;
		   while((tempString=reader.readLine())!=null)
		   {
			   String[] b=tempString.split(" ");
			   /*
			    * begin of debug info
			    */
			   ++lineNum;
			   logger.fine(String.valueOf(lineNum));
			   logger.fine(tempString);
			   String detail="detail:(";
			   for(int i=0;i<b.length;i++)
			   {
				   detail=detail+"<"+i+"-"+b[i]+">";
			   }
			   detail=detail+")\n";
			   logger.fine(detail);
			   /*
			    * end of debug info
			    */
			   Vertex s1=g.getVertexList().get(Integer.parseInt(b[0])-1);
			   Vertex t1=g.getVertexList().get(Integer.parseInt(b[1])-1);
			   Edge e1=new Edge(s1,t1,s1.getBudgetEnergy());
			   g.addEdge(e1);
			   
		   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e)
				{
				}
			}
		}
		/*
		 * end of initial Edges
		 */
		
		
		/*
		 * begin of initial Weights
		 */
		try
		{
		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName3)));
		   int lineNum=0;
		   while((tempString=reader.readLine())!=null)
		   {
			   String[] b=tempString.split(" ");
			   /*
			    * begin of debug info
			    */
			   ++lineNum;
			   logger.fine(String.valueOf(lineNum));
			   logger.fine(tempString);
			   String detail="detail:(";
			   for(int i=0;i<b.length;i++)
			   {
				   detail=detail+"<"+i+"-"+b[i]+">";
			   }
			   detail=detail+")\n";
			   logger.fine(detail);
			   /*
			    * end of debug info
			    */
			   Vertex v1=g.getVertexList().get(lineNum-1);
			   if(wOption>0)
			   {
				   v1.setWeight(1);
			   }
			   else
			   {
				   if(Double.parseDouble(b[0])<0.95)
					   v1.setWeight(rou);   
			   }
			   
			   
		   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e)
				{
				}
			}
		}
		/*
		 * end of initial Weights
		 */
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Logger log=Logger.getLogger("TestReal");
		String fileName1="test/real/topology-20.txt";
	    String fileName2="test/real/neighbor-20.txt";
	    String fileName3="test/real/weight-20.txt";
		
	    /*
	    PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/real/Grate-0.txt")));
	    Graph g=new Graph();
		TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1);
		GragMaxFlow wFlow=new GragMaxFlow();
	    wFlow.setMaxG(g);
	    wFlow.setApprFactor(apprFactor);
	    wFlow.seteRx(eRx);
	    wFlow.seteTx(eTx);
	    log.info(String.valueOf(wFlow.getMaxG()));
	    wFlow.computeConcurrentFlow();
	    
	    for(int i=0;i<wFlow.getMaxG().getSourceList().size();i++)
        {
        	Vertex tVertex=wFlow.getMaxG().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
        	pw.flush();
        }  
        */
	    

	    PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/real/rate-1.txt")));
	    Graph g=new Graph();
		TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1);
		WfMaxFlow wFlow=new WfMaxFlow();
	    wFlow.setTopology(g);
	    wFlow.seteRx(eRx);
	    wFlow.seteTx(eTx);
	    wFlow.setEpsilon(epsilon);
	    log.info(String.valueOf(wFlow.getTopology()));
	    wFlow.computeDWFFLow();
	    
	    for(int i=0;i<wFlow.getTopology().getSourceList().size();i++)
        {
        	Vertex tVertex=wFlow.getTopology().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getWeight());
        	pw.flush();
        }
	    
	    
	    double[] rouSet={0.9,0.8,0.7,0.6,0.5};
	    
	    for (int r=0;r<rouSet.length;r++)
	    {
	    	double rou=rouSet[r];
	    	pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/real/rate-"+rou+".txt")));
	    	g=new Graph();
	    	TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 0,rou);
	    	//wFlow=new WfMaxFlow();
	    	wFlow.setTopology(g);
	    	//wFlow.seteRx(eRx);
	    	//wFlow.seteTx(eTx);
	    	//wFlow.setEpsilon(apprFactor);
	    	log.info(String.valueOf(wFlow.getTopology()));
	    	wFlow.computeDWFFLow();
	    
	    	for(int i=0;i<wFlow.getTopology().getSourceList().size();i++)
	    	{
	    		Vertex tVertex=wFlow.getTopology().getSourceList().get(i);
	    		pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getWeight());
	    		pw.flush();
	    	}
	    }
	    
	}

}
