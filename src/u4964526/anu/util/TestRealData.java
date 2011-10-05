package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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
	private static double[] gBaseBudgetEnergy={0.01365,0.01221,0.01079,0.00937,0.00795,0.00653};
	private static double[] gBaseMaxRate={76800,69120,61440};
	private static double transRange=25;

	
	public static double getEpsilon() {
		return epsilon;
	}

	public static void setEpsilon(double apprFactor) {
		epsilon = apprFactor;
	}

	private static double getBudgetEnergy()
	{
		return gBaseBudgetEnergy[new Random().nextInt(gBaseBudgetEnergy.length)]*7;
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
			   Vertex v1=g.getSourceList().get(lineNum-1);   //!!!!!!
			   if(wOption==1)
			   {
				   v1.setWeight(1);
			   }
			   else if(wOption==2)
			   {
				   v1.setWeight(Double.parseDouble(b[1]));
			   }
			   else
			   {
				   if(Double.parseDouble(b[1])<0.95)
				   {
					   v1.setWeight(rou);
				   }
				   else
				   {
					   v1.setWeight(1);
				   }
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
	    
	    
	    double[] rouSet={0,0.1,0.3,0.5,0.7,0.9,1};
	    int[] nodeSet={100};      //50,100,200
	    int[] gDataSumSet={150}; //250,100,50
	    int[] thresholdSet={9,8,7,6,5};
	    double[] pairRatio={0.1,0.2,0.3,0.4,0.5};
	    int rMax=100;
	   
	    /*
	     * //begin of weightComputing
	     */
    
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	double gPairRatio=pairRatio[gt];
		    	
		    	String tTopologyFileName="test/real/one/"+gNode+"/"+gThreshold+"/topology/";
	    		File tf=new File(tTopologyFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tStarTopologyFileName="test/real/star/"+gNode+"/"+gThreshold+"/topology/";
	    		tf=new File(tStarTopologyFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
				
	    		String tMatchFileName="test/real/one/"+gNode+"/"+gThreshold+"/match/";
	    		tf=new File(tMatchFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tDataFileName="test/real/one/"+gNode+"/"+gThreshold+"/data/";
	    		tf=new File(tDataFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tStarMatchFileName="test/real/star/"+gNode+"/"+gThreshold+"/match/";
	    		tf=new File(tStarMatchFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tWeightFileName="test/real/one/"+gNode+"/"+gThreshold+"/weight/";
	    		tf=new File(tWeightFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
		    	
		    	for(int r=0;r<rMax;r++)
		    	{
		    		Graph g=new Graph();
			    	fileName1="test/real/topology/vertex_"+gNode+"_0.txt";
			    	fileName2="test/real/topology/edge_"+gNode+"_0.txt";
			    	fileName3="";
					TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1);
					
					
					String fVertex=tTopologyFileName+"vertex-"+r+".txt";
					String fEdge=tTopologyFileName+"edge-"+r+".txt";
					String fStarVertex=tStarTopologyFileName+"vertex-"+r+".txt";
					String fStarEdge=tStarTopologyFileName+"edge-"+r+".txt";
					String fMatch=tMatchFileName+"match-"+r+".txt";
					String fStarMatch=tStarMatchFileName+"Smatch-"+r+".txt";
					g.outputFile(fVertex, fEdge);
					g.outputFile(fStarVertex, fStarEdge);
					g.outputMatchFile(fMatch, 0.6, gPairRatio);
					g.outputStarMatchFile(fStarMatch, 0.6, gPairRatio);
					String fData=tDataFileName+"data-"+r;
					NodeDataGenerator dGenerator= new NodeDataGenerator();
					dGenerator.setdThreshold(gThreshold*1.0/10);
					dGenerator.setDataSum(gDataSum);
					dGenerator.setNodeSum(gNode);
					String fWeight=tWeightFileName+"weight-"+r+".txt";
					dGenerator.dataWeightGenerator(fMatch, fData,fWeight);
					
					
		    	}
		    }
	    }
	    /*
	     * //end of weightComputing
	     */

	    
	    /*
	     *  //begin of rateComputing
	     */
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	
		    	String tTopologyFileName="test/real/one/"+gNode+"/"+gThreshold+"/topology/";	    			    		
	    		String tWeightFileName="test/real/one/"+gNode+"/"+gThreshold+"/weight/";		    	
	    		String tRateFileName="test/real/one/"+gNode+"/"+gThreshold+"/rate/";
	    		File tf=new File(tRateFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
		    	
		    	
		    	
		    	for(int r=0;r<rMax;r++)
			    {

		    		fileName1=tTopologyFileName+"vertex-"+r+".txt";
			    	fileName2=tTopologyFileName+"edge-"+r+".txt";
			    	fileName3=tWeightFileName+"weight-"+r+".txt";
					
			    	for (int rr=0;rr<rouSet.length;rr++)
				    {
			    		double rou=rouSet[rr];
			    		
			    		
				    	PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt")));
				    	Graph g=new Graph();
				    	
				    	if(rou>0.95)      //rou==1�����Ȩֵ��Ϊ1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 1,rou);
				    	}
				    	else if(rou<0.05)  //rou==0, ��?ͬȨֵ
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 2,rou);
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
	    /*
	     *  //end of rateComputing
	     */
	    
	    
	    /*
	     * //begin of MSEComputing
	     */
	    DecimalFormat df=new DecimalFormat("#.0000");
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	String fRReal="test/real/one/"+gNode+"/"+"1Real-"+gThreshold+".txt";
				PrintWriter pwR=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRReal,true)));
				
				String tFileName="test/real/one/"+gNode+"/"+gThreshold+"/";
	    		String tWeightFileName="test/real/one/"+gNode+"/"+gThreshold+"/weight/";
	    		String tDataFileName="test/real/one/"+gNode+"/"+gThreshold+"/data/";
	    		String tRateFileName="test/real/one/"+gNode+"/"+gThreshold+"/rate/";
				
		    	for(int rr=0;rr<rouSet.length-1;rr++)
		    	{
		    		double rou=rouSet[rr];	//tou=0��?ͬȨֵ
		    		double tRatio=0;
		    		double tgFresh=0;
		    		double tgOld=0;
		    		int tSum=0;
		    		double tgPFresh=0;
		    		double tgPOld=0;
		    		double tPRatio=0;
		    		int tPSum=0;
		    		for (int r=0;r<rMax;r++)
				    {
			    				
			    		
						String fData=tDataFileName+"data-"+r+"-2.txt";
						String fWeight=tWeightFileName+"weight-"+r+".txt";
						String fRate=tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt";  //ͳһweight
						String fOldRate=tRateFileName+"rate-"+r+"-10.txt";
						DataQuality dq=new DataQuality();
						dq.setDataSum(gDataSum);
						dq.setNodeSum(gNode);
						
						String fReal=tFileName+"1Real-"+(int)(rou*10)+".txt";
						PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fReal,true)));
						double tFresh=dq.computeMSE(fData, fRate, 0, fWeight,0);
						tgFresh=tgFresh+tFresh;
						double tOld=dq.computeMSE(fData, fOldRate, 1, fWeight,0);
						tgOld=tgOld+tOld;
						pw.print(df.format(tFresh)+" "+df.format(tOld)+" ");
						if(tFresh<tOld)
						{
							pw.print("* ");
							tRatio=tRatio+tFresh/tOld;
							tSum++;
						}
						
						double tPFresh=dq.computeMSE(fData, fRate, 0, fWeight,1);
						tgPFresh=tgPFresh+tPFresh;
						double tPOld=dq.computeMSE(fData, fOldRate, 1, fWeight,1);
						tgPOld=tgPOld+tPOld;
						pw.print(df.format(tPFresh)+" "+df.format(tPOld)+" ");
						if(tPFresh<tPOld)
						{
							pw.print("*");
							tPRatio=tPRatio+tPFresh/tPOld;
							tPSum++;
						}
						
												
						pw.println();
						pw.flush();
						pw.close();
				    }
		    		pwR.println(rou+" "+tSum+" "+df.format(tRatio/tSum)+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+tPSum+" "+df.format(tPRatio/tPSum)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwR.flush();
		    	}
		    	pwR.close();
		    }
	    }
	    /*
	     *  //end of MSEComputing
	     */
	}
   
   public static void realTestStar() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Logger log=Logger.getLogger("TestReal");
		String fileName1;
	    String fileName2;
	    String fileName3;
		
	    	    
	    
	    double[] rouSet={0,0.1,0.3,0.5,0.7,0.9,1};
	    int[] nodeSet={100};      //50,100,200
	    int[] gDataSumSet={150}; //250,100,50
	    int[] thresholdSet={9,8,7,6,5};
	    double[] pairRatio={0.1,0.2,0.3,0.4,0.5};
	    int rMax=100;
	   
	    /*
	     * //begin of weightComputing
	     */
   
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	double gPairRatio=pairRatio[gt];
		    	
		    	String tTopologyFileName="test/real/star/"+gNode+"/"+gThreshold+"/topology/";
	    		File tf=new File(tTopologyFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
				
	    		String tMatchFileName="test/real/star/"+gNode+"/"+gThreshold+"/match/";
	    		tf=new File(tMatchFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tDataFileName="test/real/star/"+gNode+"/"+gThreshold+"/data/";
	    		tf=new File(tDataFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tWeightFileName="test/real/star/"+gNode+"/"+gThreshold+"/weight/";
	    		tf=new File(tWeightFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
		    	
		    	for(int r=0;r<rMax;r++)
		    	{
		    							
					

					String fStarMatch=tMatchFileName+"Smatch-"+r+".txt";
					
					String fData=tDataFileName+"data-"+r;
					NodeDataGenerator dGenerator= new NodeDataGenerator();
					dGenerator.setdThreshold(gThreshold*1.0/10);
					dGenerator.setDataSum(gDataSum);
					dGenerator.setNodeSum(gNode);
					String fWeight=tWeightFileName+"weight-"+r+".txt";
					dGenerator.dataWeightGenerator(fStarMatch, fData,fWeight);
					
					
		    	}
		    }
	    }
	    /*
	     * //end of weightComputing
	     */

	    
	    /*
	     *  //begin of rateComputing
	     */
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	
		    	String tTopologyFileName="test/real/star/"+gNode+"/"+gThreshold+"/topology/";	    			    		
	    		String tWeightFileName="test/real/star/"+gNode+"/"+gThreshold+"/weight/";
	    		
		    	
	    		String tRateFileName="test/real/star/"+gNode+"/"+gThreshold+"/rate/";
	    		File tf=new File(tRateFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		
		    	for(int r=0;r<rMax;r++)
			    {
		    		fileName1=tTopologyFileName+"vertex-"+r+".txt";
			    	fileName2=tTopologyFileName+"edge-"+r+".txt";
			    	fileName3=tWeightFileName+"weight-"+r+".txt";
					
			    	for (int rr=0;rr<rouSet.length;rr++)
				    {
			    		double rou=rouSet[rr];
			    		
			    		
				    	PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt")));
				    	Graph g=new Graph();
				    	
				    	if(rou>0.95)      //rou==1�����Ȩֵ��Ϊ1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 1,rou);
				    	}
				    	else if(rou<0.05)  //rou==0, ��?ͬȨֵ
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 2,rou);
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
	    /*
	     *  //end of rateComputing
	     */
	    
	    
	    /*
	     * //begin of MSEComputing
	     */
	    DecimalFormat df=new DecimalFormat("#.0000");
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	String fRReal="test/real/star/"+gNode+"/"+"1Real-"+gThreshold+".txt";
				PrintWriter pwR=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRReal,true)));
		    	
				String tFileName="test/real/star/"+gNode+"/"+gThreshold+"/";
	    		String tWeightFileName="test/real/star/"+gNode+"/"+gThreshold+"/weight/";
	    		String tDataFileName="test/real/star/"+gNode+"/"+gThreshold+"/data/";
	    		String tRateFileName="test/real/star/"+gNode+"/"+gThreshold+"/rate/";

				
				for(int rr=0;rr<rouSet.length-1;rr++)
		    	{
		    		double rou=rouSet[rr];	//tou=0��?ͬȨֵ
		    		double tRatio=0;
		    		double tgFresh=0;
		    		double tgOld=0;
		    		int tSum=0;
		    		double tPRatio=0;
		    		double tgPFresh=0;
		    		double tgPOld=0;
		    		int tPSum=0;
		    		for (int r=0;r<rMax;r++)
				    {
			    				
									    		
						String fData=tDataFileName+"data-"+r+"-2.txt";
						String fWeight=tWeightFileName+"weight-"+r+".txt";
						String fRate=tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt";  //ͳһweight
						String fOldRate=tRateFileName+"rate-"+r+"-10.txt";
						DataQuality dq=new DataQuality();
						dq.setDataSum(gDataSum);
						dq.setNodeSum(gNode);
						
						String fReal=tFileName+"1Real-"+(int)(rou*10)+".txt";
						PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fReal,true)));
						double tFresh=dq.computeMSE(fData, fRate, 0, fWeight,0);
						tgFresh=tgFresh+tFresh;
						double tOld=dq.computeMSE(fData, fOldRate, 1, fWeight,0);
						tgOld=tgOld+tOld;
						pw.print(df.format(tFresh)+" "+df.format(tOld)+" ");
						if(tFresh<tOld)
						{
							pw.print("* ");
							tRatio=tRatio+tFresh/tOld;
							tSum++;
						}
						
						double tPFresh=dq.computeMSE(fData, fRate, 0, fWeight,1);
						tgPFresh=tgPFresh+tPFresh;
						double tPOld=dq.computeMSE(fData, fOldRate, 1, fWeight,1);
						tgPOld=tgPOld+tPOld;
						pw.print(df.format(tPFresh)+" "+df.format(tPOld)+" ");
						if(tPFresh<tPOld)
						{
							pw.print("*");
							tPRatio=tPRatio+tPFresh/tPOld;
							tPSum++;
						}
						
												
						pw.println();
						pw.flush();
						pw.close();
				    }
		    		pwR.println(rou+" "+tSum+" "+df.format(tRatio/tSum)+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+tPSum+" "+df.format(tPRatio/tPSum)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwR.flush();
		    	}
		    	pwR.close();
		    }
	    }
	    /*
	     *  //end of MSEComputing
	     */
	}
   
   public static void realDataTest() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Logger log=Logger.getLogger("TestReal");
		String fileName1="test/real/topology-20.txt";
	    String fileName2="test/real/neighbor-20.txt";
	    String fileName3="test/real/weight-1.txt";
		
	   
	    
	    double[] rouSet={0,0.1,0.3,0.5,0.7,0.9,1};
	    int[] nodeSet={50};      //50,100,200
	    int[] gDataSumSet={100}; //250,100,50
	    int[] thresholdSet={9,8,7,6,5};
	    double[] pairSumSet={0,0,0,0,0};
	    int rMax=18;
	   
	    /*
	     * //begin of weightComputing
	     */
   
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	double gPair=pairSumSet[gt];
		    	
		    	String tTopologyFileName="test/real/labData/"+gNode+"/"+gThreshold+"/topology/";
	    		File tf=new File(tTopologyFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    						
	    		String tMatchFileName="test/real/labData/"+gNode+"/"+gThreshold+"/match/";
	    		tf=new File(tMatchFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tDataFileName="test/real/labData/"+gNode+"/data/";
	    		tf=new File(tDataFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    			    		
	    		String tWeightFileName="test/real/labData/"+gNode+"/"+gThreshold+"/weight/";
	    		tf=new File(tWeightFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
		    	
		    	for(int r=0;r<rMax;r++)
		    	{
		    		Graph g=new Graph();
			    	fileName1="test/real/fVertex.txt";
			    	fileName2="test/real/fEdge.txt";
			    	fileName3="";
					TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1);
					
					
					String fVertex=tTopologyFileName+"vertex-"+r+".txt";
					String fEdge=tTopologyFileName+"edge-"+r+".txt";
				    String fWeight=tWeightFileName+"weight-"+r+".txt";
				    String fData=tDataFileName+"data-"+r+"-1.txt";
					
					g.outputFile(fVertex, fEdge);
					RealDataHandler rdh=new RealDataHandler();
					gPair=gPair+rdh.outputWeightFile(g, fData, fWeight, gThreshold*1.0/10, 0.01);   //��Է�ʽ

					
					
		    	}
		    	pairSumSet[gt]=gPair/rMax;
		    }
	    }
	    /*
	     * //end of weightComputing
	     */

	    
	    /*
	     *  //begin of rateComputing
	     */
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	
		    	String tTopologyFileName="test/real/labData/"+gNode+"/"+gThreshold+"/topology/";	    			    		
	    		String tWeightFileName="test/real/labData/"+gNode+"/"+gThreshold+"/weight/";		    	
	    		String tRateFileName="test/real/labData/"+gNode+"/"+gThreshold+"/rate/";
	    		File tf=new File(tRateFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
		    	
		    	
		    	
		    	for(int r=0;r<rMax;r++)
			    {

		    		fileName1=tTopologyFileName+"vertex-"+r+".txt";
			    	fileName2=tTopologyFileName+"edge-"+r+".txt";
			    	fileName3=tWeightFileName+"weight-"+r+".txt";
			    	Graph tg=new Graph();
			    	TestRealData.initRealData(fileName1, fileName2, fileName3, tg, 1);
					tg.outputFile(fileName1, fileName2);
			    	
			    	for (int rr=0;rr<rouSet.length;rr++)
				    {
			    		double rou=rouSet[rr];
			    		
			    		
				    	PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt")));
				    	Graph g=new Graph();
				    	
				    	if(rou>0.95)      //rou==1�����Ȩֵ��Ϊ1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 1,rou);
				    	}
				    	else if(rou<0.05)  //rou==0, ��?ͬȨֵ
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 2,rou);
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
	    /*
	     *  //end of rateComputing
	     */
	    
	    
	    /*
	     * //begin of MSEComputing
	     */
	    DecimalFormat df=new DecimalFormat("#.0000");
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	double gPair=pairSumSet[gt];
		    	String fRReal="test/real/labData/"+gNode+"/"+"1Real-"+gThreshold+".txt";
				PrintWriter pwR=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRReal,true)));
				
				String tFileName="test/real/labData/"+gNode+"/"+gThreshold+"/";
	    		String tWeightFileName="test/real/labData/"+gNode+"/"+gThreshold+"/weight/";
	    		String tDataFileName="test/real/labData/"+gNode+"/data/";
	    		String tRateFileName="test/real/labData/"+gNode+"/"+gThreshold+"/rate/";
				
		    	for(int rr=0;rr<rouSet.length-1;rr++)
		    	{
		    		double rou=rouSet[rr];	//tou=0��?ͬȨֵ
		    		double tRatio=0;
		    		double tgFresh=0;
		    		double tgOld=0;
		    		int tSum=0;
		    		double tPRatio=0;
		    		double tgPFresh=0;
		    		double tgPOld=0;
		    		int tPSum=0;
		    		for (int r=0;r<rMax;r++)
				    {
			    				
			    		
						String fData=tDataFileName+"data-"+r+"-2.txt";
						String fWeight=tWeightFileName+"weight-"+r+".txt";
						String fRate=tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt";  //ͳһweight
						String fOldRate=tRateFileName+"rate-"+r+"-10.txt";
						DataQuality dq=new DataQuality();
						dq.setDataSum(gDataSum);
						dq.setNodeSum(gNode);
						
						String fReal=tFileName+"1Real-"+(int)(rou*10)+".txt";
						PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fReal,true)));
						//debug
						//System.out.println(fData);
						//System.out.println(fRate);
						//System.out.println(fWeight);
						//debug
						double tFresh=dq.computeMSE(fData, fRate, 0, fWeight,0);
						tgFresh=tgFresh+tFresh;
						double tOld=dq.computeMSE(fData, fOldRate, 1, fWeight,0);
						tgOld=tgOld+tOld;
						pw.print(df.format(tFresh)+" "+df.format(tOld)+" ");
						if(tFresh<tOld)
						{
							pw.print("* ");
							tRatio=tRatio+tFresh/tOld;
							tSum++;
						}
						
						
						double tPFresh=dq.computeMSE(fData, fRate, 0, fWeight,1);
						tgPFresh=tgPFresh+tPFresh;
						double tPOld=dq.computeMSE(fData, fOldRate, 1, fWeight,1);
						tgPOld=tgPOld+tPOld;
						pw.print(df.format(tPFresh)+" "+df.format(tPOld)+" ");
						if(tPFresh<tPOld)
						{
							pw.print("*");
							tPRatio=tPRatio+tPFresh/tPOld;
							tPSum++;
						}
						
												
						pw.println();
						pw.flush();
						pw.close();
				    }
		    		pwR.println(gPair+" "+rou+" "+tSum+" "+df.format(tRatio/tSum)+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+tPSum+" "+df.format(tPRatio/tPSum)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwR.flush();
		    	}
		    	pwR.close();
		    }
	    }
	    /*
	     *  //end of MSEComputing
	     */
	}
   
   public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			Logger logger=Logger.getLogger("MaxFlow");
			logger.setLevel(Level.WARNING);
			
			TestRealData.realTestOne();
			TestRealData.realTestStar();
			//TestRealData.realDataTest();
			
		}
		catch(Exception e)
		{
			
		}
   }
}

