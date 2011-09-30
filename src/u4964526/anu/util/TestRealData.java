package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestRealData {

	/**
	 * @param args
	 */
	private static double eTx=0.0000144;  //dB
	private static double eRx=0.00000576;  //dB
	private static double epsilon=0.1;
	private static double[] gBaseBudgetEnergy={0.00653*8,0.00653*10,0.00653*12};
	private static double[] gBaseMaxRate={76800, 57600, 38400};
	private static double transRange=25;

	
	public static double getEpsilon() {
		return epsilon;
	}

	public static void setEpsilon(double apprFactor) {
		epsilon = apprFactor;
	}

	private static double getBudgetEnergy()
	{
		return gBaseBudgetEnergy[new Random().nextInt(gBaseBudgetEnergy.length)];
	}

   private static double getVertexMaxRate()
   {
		return gBaseMaxRate[new Random().nextInt(gBaseMaxRate.length)];
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
			   v1.setWeight(1);
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
		if(wOption==0)
		{
			try
			{
			   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName3)));
			   int lineNum=0;
			   while((tempString=reader.readLine())!=null&&lineNum<g.getVertexList().size())
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
					   v1.setWeight(Double.parseDouble(b[1]));   
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
			   v1.setMaxRate(Double.parseDouble(b[4]));
			   //v1.setMaxRate(TestRealData.getVertexMaxRate());
			   v1.setBudgetEnergy(Double.parseDouble(b[5]));
			   //v1.setBudgetEnergy(TestRealData.getBudgetEnergy());
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
		   while((tempString=reader.readLine())!=null&&lineNum<g.getVertexList().size())
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
				   if(Double.parseDouble(b[1])<0.95)
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
	
   public static void realTestOne() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Logger log=Logger.getLogger("TestReal");
		String fileName1="test/real/topology-20.txt";
	    String fileName2="test/real/neighbor-20.txt";
	    String fileName3="test/real/weight-1.txt";
		
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
	    
       /*
	    PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/real/rate-0.txt")));
	    Graph g=new Graph();
		TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1);
		String fVertex="test/real/vertex.txt";
		String fEdge="test/real/edge.txt";
		g.outputFile(fVertex, fEdge);
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
	    */
	    
	    
	    double[] rouSet={0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1};
	    int[] nodeSet={100};
	    int[] thresholdSet={9};
	    int rMax=20;
	    int gDataSum=100;
	    
	    //begin of weightComputing
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	for(int r=0;r<rMax;r++)
		    	{
		    		Graph g=new Graph();
			    	fileName1="test/real/topology/vertex_"+100+"_0.txt";
			    	fileName2="test/real/topology/edge_"+100+"_0.txt";
			    	fileName3="";
					TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1);
					
					String tFileName="test/real/one/"+gNode+"/"+gThreshold+"/";
		    		File tf=new File(tFileName);
		    		if(!tf.exists())
		    		{
		    			tf.mkdirs();
		    		}
					
					String fVertex=tFileName+"vertex.txt";
					String fEdge=tFileName+"edge.txt";
					String fWeight=tFileName+"weight-"+r+".txt";
					g.outputFile(fVertex, fEdge);
					g.outputWeightFile(fWeight, 0.6, 0.1);
					String fData=tFileName+"data-"+r+".txt";
					NodeDataGenerator dGenerator= new NodeDataGenerator();
					dGenerator.setdThreshold(gThreshold*1.0/10);
					dGenerator.setDataSum(gDataSum);
					dGenerator.setNodeSum(gNode);
					dGenerator.dataGenerator(fWeight, fData);
		    	}
		    }
	    }
	    //end of weightComputing
	    
	    //begin of rateComputing
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	for(int r=0;r<rMax;r++)
			    {
			    	Graph g=new Graph();
			    	fileName1="test/real/topology/vertex_"+gNode+"_0.txt";
			    	fileName2="test/real/topology/edge_"+gNode+"_0.txt";
			    	fileName3="";
					TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1);
					String fVertex="test/real/vertex.txt";
					String fEdge="test/real/edge.txt";
					g.outputFile(fVertex, fEdge);
					
			    	for (int rr=0;rr<rouSet.length;rr++)
				    {
			    		double rou=rouSet[rr];
			    		String tFileName="test/real/one/"+gNode+"/"+gThreshold+"/";
			    		
				    	PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tFileName+"rate-"+r+"-"+(int)(rou*10)+".txt")));
				    	g=new Graph();
				    	fileName1="test/real/vertex.txt";
				    	fileName2="test/real/edge.txt";
				    	fileName3=tFileName+"weight-"+r+".txt";
				    	if(rou>0.95)
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 1,rou);
				    	}
				    	else
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 0,rou);
				    	}
				        WfMaxFlow wFlow=new WfMaxFlow();
				    	wFlow.setTopology(g);
					    wFlow.seteRx(eRx);
					    wFlow.seteTx(eTx);
					    wFlow.setEpsilon(epsilon);
				    	log.fine(String.valueOf(wFlow.getTopology()));
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
	    }
	    //end of rateComputing
	    
	    //begin of MSEComputing
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	String fRReal="test/real/one/"+gNode+"/"+"1Real-"+gThreshold+".txt";
				PrintWriter pwR=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRReal,true)));
		    	for(int rr=0;rr<rouSet.length-1;rr++)
		    	{
		    		double rou=rouSet[rr];	
		    		double tRFresh=0;
		    		double tROld=0;
		    		for (int r=0;r<rMax;r++)
				    {
			    				
						String tFileName="test/real/one/"+gNode+"/"+gThreshold+"/";
			    		
						String fData=tFileName+"data-"+r+".txt";
						String fWeight=tFileName+"weight-"+r+".txt";
						String fRate=tFileName+"rate-"+r+"-"+(int)(rou*10)+".txt";
						String fOldRate=tFileName+"rate-"+r+"-10.txt";
						DataQuality dq=new DataQuality();
						dq.setDataSum(gDataSum);
						dq.setNodeSum(gNode);
						
						String fReal=tFileName+"1Real-"+(int)(rou*10)+".txt";
						PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fReal,true)));
						double tFresh=dq.computeMSE(fData, fRate, 0, fWeight);
						tRFresh=tRFresh+tFresh;
						double tOld=dq.computeMSE(fData, fOldRate, 1, fWeight);
						pw.print(tFresh+" "+tOld+" ");
						tROld=tROld+tOld;
						if(tFresh<tOld)
						{
							pw.print("*");
						}
						pw.println();
						pw.flush();
						pw.close();
				    }
		    		pwR.println(rou+" "+tRFresh/rMax+" "+tROld/rMax);
		    		pwR.flush();
		    	}
		    	pwR.close();
		    }
	    }
	    //end of MSEComputing
	}
   
   
   
   public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			Logger logger=Logger.getLogger("MaxFlow");
			logger.setLevel(Level.WARNING);
			
			TestRealData.realTestOne();
			
			
			
		}
		catch(Exception e)
		{
			
		}
   }
}

