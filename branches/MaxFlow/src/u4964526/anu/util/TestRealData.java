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
	private static double[] gBaseBudgetEnergy={0.00653,0.01365};  //0.01365,0.01221,0.01079,0.00937,0.00795,0.00653
	private static double[] gBaseMaxRate={76800,69120,61440};   //76800,69120,61440
	private static double transRange=25;

	
	public static double getEpsilon() {
		return epsilon;
	}

	public static void setEpsilon(double apprFactor) {
		epsilon = apprFactor;
	}

	private static double getBudgetEnergy()
	{
		return gBaseBudgetEnergy[0]+Math.random()*(gBaseBudgetEnergy[1]-gBaseBudgetEnergy[0]);  //7(100,150)  1.2(real)  19(300)  3(50) 12(200)
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
   
   
   private static void initRealData(String fileName1, String fileName2, String fileName3, Graph g, int wOption,double eOption) throws FileNotFoundException
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
			   v1.setBudgetEnergy(getBudgetEnergy()*eOption);
			   v1.setRate(0);
			   v1.setWeight(1);
			   g.addVertex(v1);
			   if(Integer.parseInt(b[0])==0)
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
			   Vertex s1=g.getVertexList().get(Integer.parseInt(b[0]));
			   Vertex t1=g.getVertexList().get(Integer.parseInt(b[1]));
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
			   while((tempString=reader.readLine())!=null&&lineNum<g.getSourceList().size())
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
				   Vertex v1=g.getSourceList().get(lineNum-1);
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
			   v1.setWeight(1);
			   g.addVertex(v1);
			   if(Integer.parseInt(b[0])==0)
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
			   Vertex s1=g.getVertexList().get(Integer.parseInt(b[0]));
			   Vertex t1=g.getVertexList().get(Integer.parseInt(b[1]));
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
		   if(wOption!=1)
		   {
				reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName3)));
			   int lineNum=0;
			   while((tempString=reader.readLine())!=null&&lineNum<g.getSourceList().size())
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
					   if(Double.parseDouble(b[1])<0.99)
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
		
	   	    
	    
	    double[] rouSet={1,0,0.2,0.4,0.6,0.8};  //0,0.2,0.4,0.6,0.8,1
	    int[] nodeSet={50,100,150,200,250,300};      //50,100,150,200,300
	    int[] gDataSumSet={100,100,100,100,100,100}; //100,100,100,100,100
	    double[] gEISet={1,1,1,1,1,1};         //3,7,9,12,19   0.7,3,3,3,3,11
	    int[] thresholdSet={4,5,6,7,8,9};  //9,8,7,6,5,4,3
	    double[][] gPairSet=new double[nodeSet.length][thresholdSet.length];
	    double gRateIndicator=60;
	    int rMax=15;    // 
	    int rDataMax=10;  // number of time slots
	    
	    /*
	     * //begin of weightComputing
	     */
    
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    double gEI=gEISet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	double gPair=0;
		    	
		    	String tTopologyFileName="test/real/one/"+gNode+"/topology/";
	    		File tf=new File(tTopologyFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    		String tStarTopologyFileName="test/real/star/"+gNode+"/topology/";
	    		tf=new File(tStarTopologyFileName);
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
			    	fileName1="test/topology/vertex_"+gNode+"_"+r+".txt";
			    	fileName2="test/topology/edge_"+gNode+"_"+r+".txt";
			    	fileName3="test/topology/data_"+gNode+"_"+r+"_0.txt";
					TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1,gEI);
					
					
					String fVertex=tTopologyFileName+"vertex-"+r+".txt";
					String fEdge=tTopologyFileName+"edge-"+r+".txt";
					String fStarVertex=tStarTopologyFileName+"vertex-"+r+".txt";
					String fStarEdge=tStarTopologyFileName+"edge-"+r+".txt";
					g.outputFile(fVertex, fEdge);
					g.outputFile(fStarVertex, fStarEdge);
					
					String fData="test/topology/data_"+gNode+"_"+r+"_0.txt";
					
					String fWeight=tWeightFileName+"weight-"+r+".txt";
					RealDataHandler rdh=new RealDataHandler();
					rdh.setDataSum(gDataSum);
					rdh.setNodeSum(gNode);
					gPair=gPair+rdh.outputWeightFile2(g, fData, fWeight, gThreshold*1.0/10, 0.03);
					
					
		    	}
		    	gPairSet[gn][gt]=gPair/rMax;
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
		    	
		    	String tTopologyFileName="test/real/one/"+gNode+"/topology/";	    			    		
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
					
			    	double rateIndicator=1;
			    	
			    	for (int rr=0;rr<1;rr++)
				    {
			    		double rou=rouSet[rr];
			    		
			    		
				    	PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt")));
				    	Graph g=new Graph();
				    	
				    	if(rou>0.95)      //rou==1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 1,rou);
				    	}
				    	else if(rou<0.05)  //rou==0
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
				    	//System.out.println(r+"-"+gThreshold+"-"+rou);
				    	wFlow.computeDWFFLow();
				    
				    	rateIndicator=gRateIndicator/wFlow.getTopology().getSourceList().get(0).getRate();
				    	
				    	for(int i=0;i<wFlow.getTopology().getSourceList().size();i++)
				    	{
				    		Vertex tVertex=wFlow.getTopology().getSourceList().get(i);
				    		pw.println(tVertex.getVerValue()+" "+tVertex.getRate()*rateIndicator+" "+tVertex.getWeight());
				    		pw.flush();
				    	}
				    }
			    	
			    	
			    	for (int rr=1;rr<rouSet.length;rr++)
				    {
			    		double rou=rouSet[rr];
			    		
			    		
				    	PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt")));
				    	Graph g=new Graph();
				    	
				    	if(rou>0.95)      //rou==1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 1,rou);
				    	}
				    	else if(rou<0.05)  //rou==0
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
				    	//System.out.println(r+"-"+gThreshold+"-"+rou);
				    	wFlow.computeDWFFLow();
				    
				    	for(int i=0;i<wFlow.getTopology().getSourceList().size();i++)
				    	{
				    		Vertex tVertex=wFlow.getTopology().getSourceList().get(i);
				    		pw.println(tVertex.getVerValue()+" "+tVertex.getRate()*rateIndicator+" "+tVertex.getWeight());
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
		    	double gPair=gPairSet[gn][gt];
		    	String fRReal="test/real/one/"+"Rho-"+gNode+"-"+gThreshold+".txt";
				PrintWriter pwR=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRReal,true)));
				
				
				
				String tFileName="test/real/one/"+gNode+"/"+gThreshold+"/";
	    		String tWeightFileName="test/real/one/"+gNode+"/"+gThreshold+"/weight/";

	    		String tRateFileName="test/real/one/"+gNode+"/"+gThreshold+"/rate/";
				
		    	for(int rr=1;rr<rouSet.length;rr++)
		    	{
		    				    		
		    		
		    		double rou=rouSet[rr];	//tou=0
		    		double tRatio=0;
		    		double tgFresh=0;
		    		double tgOld=0;
		    		int tSum=0;
		    		double tgPFresh=0;
		    		double tgPOld=0;
		    		double tPRatio=0;
		    		int tPSum=0;
		    		
		    		String fNode="test/real/one/Node-"+gThreshold+"-"+(int)(rou*10)+".txt";
					PrintWriter pwN=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fNode,true)));
					
					String fThreshold="test/real/one/Threshold-"+gNode+"-"+(int)(rou*10)+".txt";
					PrintWriter pwT=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fThreshold,true)));

		    		
		    		for (int r=0;r<rMax;r++)
				    {
			    		double tFresh=0;
		    			double tOld=0;
		    			double tPFresh=0;
		    			double tPOld=0;
			    		String fReal=tFileName+"1Real-"+(int)(rou*10)+".txt";
						PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fReal,true)));
			    		for(int rData=0;rData<rDataMax;rData++)         //10 time slots
			    		{
							String fData="test/topology/data_"+gNode+"_"+r+"_"+rData+".txt";
							String fWeight=tWeightFileName+"weight-"+r+".txt";
							String fRate=tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt";  
							String fOldRate=tRateFileName+"rate-"+r+"-10.txt";
							DataQuality dq=new DataQuality();
							dq.setDataSum(gDataSum);
							dq.setNodeSum(gNode);
							
							if(rData==0)                //training period
							{
								tFresh=tFresh+dq.computeMSE2(fData, fOldRate, 1, fWeight,0,null)/gNode;
							}
							else                        //
							{
								tFresh=tFresh+dq.computeMSE2(fData, fRate, 0, fWeight,0,null)/gNode;
							}
								
							tOld=tOld+dq.computeMSE2(fData, fOldRate, 1, fWeight,0,null)/gNode;
							
							
							
							if(rData==0)
							{
								tPFresh=tPFresh+dq.computeMSE2(fData, fOldRate, 1, fWeight,1,null)/gNode;
							}
							else
							{
								tPFresh=tPFresh+dq.computeMSE2(fData, fRate, 0, fWeight,1,null)/gNode;
							}
								
							tPOld=tPOld+dq.computeMSE2(fData, fOldRate, 1, fWeight,1,null)/gNode;
							
							
			    		}
			    		tgFresh=tgFresh+tFresh/rDataMax;		
			    		tgOld=tgOld+tOld/rDataMax;
			    		pw.print(df.format(tFresh)+" "+df.format(tOld)+" ");
			    		if(tFresh<tOld)
						{
							pw.print("* ");
							tRatio=tRatio+tFresh/tOld;
							tSum++;
						}
			    		tgPFresh=tgPFresh+tPFresh/rDataMax;
			    		tgPOld=tgPOld+tPOld/rDataMax;
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
		    		pwN.println(gNode+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwN.flush();
		    		pwN.close();
		    		
		    		pwT.println(gThreshold+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwT.flush();
		    		pwT.close();
		    		
		    		pwR.println(rou+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
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
			
			
			
			
						
			//TestRealData.testSPTGK(50);
			//TestRealData.testSPTGK(100);
			//TestRealData.testSPTGK(200);
			
	
			
			//TestRealData.realTestOne();
	
			TestRealData.testIntervalSet(100);
			System.out.println("100 complete");
			TestRealData.testIntervalSet(200);
			System.out.println("200 complete");
			TestRealData.testIntervalSet(50);
			System.out.println("50 complete");
			
				
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
   }
   
   
   // need control energy manual!!!
   public static void testSPTGK(int gAppr) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Logger log=Logger.getLogger("TestReal");
		String fileName1="test/real/topology-20.txt";
	    String fileName2="test/real/neighbor-20.txt";
	    String fileName3="test/real/weight-1.txt";
	    DecimalFormat df=new DecimalFormat("#.0000");
	   	    
	    
	    double[] rouSet={1,0.4,0.8};  //0,0.2,0.4,0.6,0.8,1
	    int[] nodeSet={50,100,150,200};      //50,100,150,200,300
	    int[] gDataSumSet={100,100,100,100,100,100}; //100,100,100,100,100
	    double[] gEISet={1,1,1,1,1,1};         //3,7,9,12,19   0.7,3,3,3,3,11
	    int[] thresholdSet={8};  //9,8,7,6,5,4,3
	    double[][] gPairSet=new double[nodeSet.length][thresholdSet.length];
	    double gRateIndicator=60;
	    int rMax=15;
	    int rDataMax=10;
	   
	    /*
	     * //begin of weightComputing
	     */
   
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    double gEI=gEISet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	double gPair=0;
		    	
		    	String tTopologyFileName="test/real/sptgk/"+gNode+"/topology/";
	    		File tf=new File(tTopologyFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
	    		
	    				
	    		
	    		String tWeightFileName="test/real/sptgk/"+gNode+"/"+gThreshold+"/weight/";
	    		tf=new File(tWeightFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
		    	
	    		if(gAppr==50)
	    		{
			    	for(int r=0;r<rMax;r++)
			    	{
			    		Graph g=new Graph();
				    	fileName1="test/topology/vertex_"+gNode+"_"+r+".txt";
				    	fileName2="test/topology/edge_"+gNode+"_"+r+".txt";
				    	fileName3="test/topology/data_"+gNode+"_"+r+"_0.txt";
						TestRealData.initRealData(fileName1, fileName2, fileName3, g, 1,gEI);
						
						
						String fVertex=tTopologyFileName+"vertex-"+r+".txt";
						String fEdge=tTopologyFileName+"edge-"+r+".txt";
						g.outputFile(fVertex, fEdge);
						
						
						String fData="test/topology/data_"+gNode+"_"+r+"_0.txt";
						
						String fWeight=tWeightFileName+"weight-"+r+".txt";
						RealDataHandler rdh=new RealDataHandler();
						rdh.setDataSum(gDataSum);
						rdh.setNodeSum(gNode);
						gPair=gPair+rdh.outputWeightFile2(g, fData, fWeight, gThreshold*1.0/10, 0.03);
						
						
			    	}
			    	gPairSet[gn][gt]=gPair/rMax;
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
		    	
		    	String tTopologyFileName="test/real/sptgk/"+gNode+"/topology/";	    			    		
	    		String tWeightFileName="test/real/sptgk/"+gNode+"/"+gThreshold+"/weight/";		    	
	    		String tRateFileName="test/real/sptgk/"+gAppr+"/"+gNode+"/"+gThreshold+"/rate/";
	    		File tf=new File(tRateFileName);
	    		if(!tf.exists())
	    		{
	    			tf.mkdirs();
	    		}
		    	
		    	double[] gGRateIndicatorSet=new double[rMax];
		    	double[] gWRateIndicatorSet=new double[rMax];
		    	
		    	
		    	/*
		    	 * 
		    	 */
		    	for(int r=0;r<rMax;r++)
			    {

		    		fileName1=tTopologyFileName+"vertex-"+r+".txt";
			    	fileName2=tTopologyFileName+"edge-"+r+".txt";
			    	fileName3=tWeightFileName+"weight-"+r+".txt";
			    	
			    	
					
			    	double rateIndicator=1;
			    	
			    	for (int rr=0;rr<1;rr++)
				    {
			    		double rou=1;
			    		
			    		
				    	
				    	Graph g=new Graph();
				    	
				    	if(rou>0.95)      //rou==1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 1,rou);
				    	}
				    	else if(rou<0.05)  //rou==0
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 2,rou);
				    	}
				    	else
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g, 0,rou);
				    	}
				        GragMaxFlow wFlow=new GragMaxFlow();
				    	wFlow.setTopology(g);
					    wFlow.seteRx(eRx);
					    wFlow.seteTx(eTx);
					    wFlow.setEpsilon(0.1);
				    	log.fine(String.valueOf(wFlow.getTopology()));
				    	//System.out.println(r+"-"+gThreshold+"-"+rou);
				    	wFlow.computeConcurrentFlow();
				    
				    	rateIndicator=gRateIndicator/wFlow.getTopology().getSourceList().get(5).getRate();
				    	gGRateIndicatorSet[r]=rateIndicator;
				    	
				    	
				    	
				    	Graph tg=new Graph();
				    	
				    	if(rou>0.95)      //rou==1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, tg, 1,rou);
				    	}
				    	else if(rou<0.05)  //rou==0
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, tg, 2,rou);
				    	}
				    	else
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, tg, 0,rou);
				    	}
				        WfMaxFlow twFlow=new WfMaxFlow();
				    	twFlow.setTopology(g);
					    twFlow.seteRx(eRx);
					    twFlow.seteTx(eTx);
					    twFlow.setEpsilon(0.1);
				    	log.fine(String.valueOf(wFlow.getTopology()));
				    	//System.out.println(r+"-"+gThreshold+"-"+rou);
				    	twFlow.computeDWFFLow();
				    
				    	rateIndicator=gRateIndicator/twFlow.getTopology().getSourceList().get(0).getRate();
				    	gWRateIndicatorSet[r]=rateIndicator;
				    	
				    	
				    	
				    }
			    	
			    	
			    	
			    }
		    	/*
		    	 * 
		    	 */
		    	
		    	//!!!!!!!!!!!!!!!!!
		    	for (int rr=0;rr<rouSet.length;rr++)
			    {
		    		double rou=rouSet[rr];
		    		
		    		String fRun="test/real/sptgk/Run-"+gThreshold+"-"+(int)(rou*10)+"-"+gAppr+".txt";
		    		PrintWriter pwGRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRun,true)));
		    		
		    		double pRatio=0;
					double rRatio=0;
					double gRunTime=0;
					double gFlowData=0;
					double wfRunTime=0;
					double wfFlowData=0;
		    		
		    		for(int r=0;r<rMax;r++)
		    		{
		    			
		    			fileName1=tTopologyFileName+"vertex-"+r+".txt";
				    	fileName2=tTopologyFileName+"edge-"+r+".txt";
				    	fileName3=tWeightFileName+"weight-"+r+".txt";
		    			
			    		double tGRateIndicator=1;
			    		
			    		
			    		PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tRateFileName+"grate-"+r+"-"+(int)(rou*10)+".txt")));
				    	Graph g1=new Graph();
				    	
				    	if(rou>0.99)      //rou==1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g1, 1,rou);
				    	}
				    	else if(rou<0.01)  //rou==0
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g1, 2,rou);
				    	}
				    	else
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g1, 0,rou);
				    	}
				        
				    	double tGTime=0;
				    	double tGFlow=0;
						double tGRate=0;
				    	GragMaxFlow gFlow=new GragMaxFlow();
				    	gFlow.setTopology(g1);
					    gFlow.seteRx(eRx);
					    gFlow.seteTx(eTx);
					    gFlow.setEpsilon(gAppr*1.0/1000);
				    	log.fine(String.valueOf(gFlow.getTopology()));
				    	//System.out.println(r+"-"+gThreshold+"-"+rou);
				    	tGTime=gFlow.computeConcurrentFlow();
				        
				    	if(rou>0.99)
				    	{
				    		//tGRateIndicator=gRateIndicator/gFlow.getTopology().getSourceList().get(0).getRate();
				    		//gGRateIndicatorSet[r]=tGRateIndicator;
				    		tGRateIndicator=gGRateIndicatorSet[r];
				    	}
				    	else
				    	{
				    		tGRateIndicator=gGRateIndicatorSet[r];
				    	}
				    	
				    	
				    	for(int i=0;i<gFlow.getTopology().getSourceList().size();i++)
				    	{
				    		Vertex tVertex=gFlow.getTopology().getSourceList().get(i);
				    		pw1.println(tVertex.getVerValue()+" "+tVertex.getRate()*tGRateIndicator+" "+tVertex.getWeight()+" "+tVertex.getRate());
				    		pw1.flush();
				    		tGFlow=tGFlow+tVertex.getRate()*(eRx+eTx);
							tGRate=tGRate+tVertex.getRate();
				    	}
				    	pw1.flush();
				    	pw1.close();
				    	gRunTime=gRunTime+tGTime;
			            gFlowData=gFlowData+tGRate;
			    		
			    		
			    		
			            double tWRateIndicator=1;
				    	PrintWriter pw2=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt")));
				    	Graph g2=new Graph();
				    	
				    	if(rou>0.99)      //rou==1
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g2, 1,rou);
				    	}
				    	else if(rou<0.01)  //rou==0
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g2, 2,rou);
				    	}
				    	else
				    	{
				    		TestRealData.initRealDataRou(fileName1, fileName2, fileName3, g2, 0,rou);
				    	}
				    	
				    	double tWTime=0;
				    	double tWFlow=0;
						double tWRate=0;
				        WfMaxFlow wFlow=new WfMaxFlow();
				    	wFlow.setTopology(g2);
					    wFlow.seteRx(eRx);
					    wFlow.seteTx(eTx);
					    wFlow.setEpsilon(gAppr*1.0/1000);
				    	log.fine(String.valueOf(wFlow.getTopology()));
				    	//System.out.println(r+"-"+gThreshold+"-"+rou);
				    	tWTime=wFlow.computeDWFFLow();
				    	
				    	if(rou>0.99)
				    	{
				    		//tWRateIndicator=gRateIndicator/wFlow.getTopology().getSourceList().get(0).getRate();
				    		//gWRateIndicatorSet[r]=tWRateIndicator;
				    		tWRateIndicator=gWRateIndicatorSet[r];
				    	}
				    	else
				    	{
				    		tWRateIndicator=gWRateIndicatorSet[r];
				    	}
				    	
				    
				    	for(int i=0;i<wFlow.getTopology().getSourceList().size();i++)
				    	{
				    		Vertex tVertex=wFlow.getTopology().getSourceList().get(i);
				    		pw2.println(tVertex.getVerValue()+" "+tVertex.getRate()*tWRateIndicator+" "+tVertex.getWeight()+" "+tVertex.getRate());
				    		pw2.flush();
				    		tWFlow=tWFlow+tVertex.getRate()*(eRx+eTx);
							tWRate=tWRate+tVertex.getRate();
				    	}
				    	pw2.flush();
				    	pw2.close();
				    	
				    	wfRunTime=wfRunTime+tWTime;
						wfFlowData=wfFlowData+tWRate;
						
						pRatio=pRatio+tWRate/tGRate;
						rRatio=rRatio+tWTime/tGTime;
		    		}
		    		
		    		pwGRun.println(gNode+" "+df.format(gRunTime/rMax)+" "+df.format(gFlowData/rMax)+" "+df.format(wfRunTime/rMax)+" "+df.format(wfFlowData/rMax)+" "+df.format(pRatio/rMax)+" "+df.format(rRatio/rMax));

		    		
		    		pwGRun.flush();
		    		pwGRun.close();
			    }
		    }
	    }
	    /*
	     *  //end of rateComputing
	     */
	    
	    
	    /*
	     * //begin of MSEComputing
	     */
	    
	    for(int gn=0;gn<nodeSet.length;gn++)
	    {
		    int gNode=nodeSet[gn];
		    int gDataSum=gDataSumSet[gn];
		    for(int gt=0;gt<thresholdSet.length;gt++)
		    {
		    	int gThreshold=thresholdSet[gt];
		    	double gPair=gPairSet[gn][gt];
		    	String fRReal="test/real/sptgk/"+"Rho-"+gNode+"-"+gThreshold+"-"+gAppr+".txt";
				PrintWriter pwR=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRReal,true)));
				
				
				
				String tFileName="test/real/sptgk/"+gAppr+"/"+gNode+"/"+gThreshold+"/";
	    		String tWeightFileName="test/real/sptgk/"+gNode+"/"+gThreshold+"/weight/";

	    		String tRateFileName="test/real/sptgk/"+gAppr+"/"+gNode+"/"+gThreshold+"/rate/";
				
		    	for(int rr=0;rr<rouSet.length;rr++)
		    	{
		    				    		
		    		
		    		double rou=rouSet[rr];	//tou=0
		    		double tRatio=0;
		    		double tgFresh=0;
		    		double tgOld=0;
		    		int tSum=0;
		    		double tgPFresh=0;
		    		double tgPOld=0;
		    		double tPRatio=0;
		    		int tPSum=0;
		    		
		    		String fNode="test/real/sptgk/Node-"+gThreshold+"-"+(int)(rou*10)+"-"+gAppr+".txt";
					PrintWriter pwN=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fNode,true)));
					
					String fThreshold="test/real/sptgk/Threshold-"+gNode+"-"+(int)(rou*10)+"-"+gAppr+".txt";
					PrintWriter pwT=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fThreshold,true)));

		    		
		    		for (int r=0;r<rMax;r++)
				    {
			    		double tFresh=0;
		    			double tOld=0;
		    			double tPFresh=0;
		    			double tPOld=0;
			    		String fReal=tFileName+"1Real-"+(int)(rou*10)+".txt";
						PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fReal,true)));
			    		for(int rData=0;rData<rDataMax;rData++)
			    		{
							String fData="test/topology/data_"+gNode+"_"+r+"_"+rData+".txt";
							String fWeight=tWeightFileName+"weight-"+r+".txt";
							String fRate=tRateFileName+"rate-"+r+"-"+(int)(rou*10)+".txt";  
							String fOldRate=tRateFileName+"rate-"+r+"-10.txt";
							String fGRate=tRateFileName+"grate-"+r+"-"+(int)(rou*10)+".txt";  
							String fGOldRate=tRateFileName+"grate-"+r+"-10.txt";
							
							DataQuality dq=new DataQuality();
							dq.setDataSum(gDataSum);
							dq.setNodeSum(gNode);
							
							
							if(rData==0)                //training period
							{
								tFresh=tFresh+dq.computeMSE2(fData, fOldRate, 1, fWeight,0,null)/gNode;
							}
							else                        //
							{
								tFresh=tFresh+dq.computeMSE2(fData, fRate, 0, fWeight,0,null)/gNode;
							}
								
							tOld=tOld+dq.computeMSE2(fData, fOldRate, 1, fWeight,0,null)/gNode;
							
							
							
							if(rData==0)
							{
								tPFresh=tPFresh+dq.computeMSE2(fData, fOldRate, 1, fWeight,1,null)/gNode;
							}
							else
							{
								tPFresh=tPFresh+dq.computeMSE2(fData, fRate, 0, fWeight,1,null)/gNode;
							}
								
							tPOld=tPOld+dq.computeMSE2(fData, fOldRate, 1, fWeight,1,null)/gNode;
							
							
			    		}
			    		tgFresh=tgFresh+tFresh/rDataMax;		
			    		tgOld=tgOld+tOld/rDataMax;
			    		pw.print(df.format(tFresh)+" "+df.format(tOld)+" ");
			    		if(tFresh<tOld)
						{
							pw.print("* ");
							tRatio=tRatio+tFresh/tOld;
							tSum++;
						}
			    		tgPFresh=tgPFresh+tPFresh/rDataMax;
			    		tgPOld=tgPOld+tPOld/rDataMax;
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
		    		pwN.println(gNode+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwN.flush();
		    		pwN.close();
		    		
		    		pwT.println(gThreshold+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwT.flush();
		    		pwT.close();
		    		
		    		pwR.println(rou+" "+df.format(tgFresh/tgOld)+" "+df.format(tgFresh/rMax)+" "+df.format(tgOld/rMax)+" "+df.format(tgPFresh/tgPOld)+" "+df.format(tgPFresh/rMax)+" "+df.format(tgPOld/rMax));
		    		pwR.flush();
		    	}
		    	pwR.close();
		    }
	    }
	    /*
	     *  //end of MSEComputing
	     */
	}
   
   public static void testIntervalSet(int gAppr) throws FileNotFoundException
   {
	   DecimalFormat df=new DecimalFormat("#.0000");
  	    
	    
	    int[] gRouSet={100,110,0,20,40,80};  //0,0.2,0.4,0.6,0.8,1
	    int[] gNodeSet={50,100,150,200};      //50,100,150,200,300
	    int[] gDataSumSet={100,100,100,100,100,100}; //100,100,100,100,100
	    double[] gEISet={1,1,1,1,1,1};         //3,7,9,12,19   0.7,3,3,3,3,11
	    int[] gCThresholdSet={9,8,7,6,5,4};  //9,8,7,6,5,4,3
	    double[][] gPairSet=new double[gNodeSet.length][gCThresholdSet.length];
	    double gRateIndicator=60;
	    int topologySum=15;
	    int intervalSum=10;
	    //0--SPTtime 1--SPTFlow 2--SPTMSE  3--GKtime  4--GKFlow  5--GKMSE
	    double[][][][] gResultSet=new double[gNodeSet.length][gCThresholdSet.length][gRouSet.length][6];
	    
	    
	    
	    
	    for(int gI=0;gI<intervalSum;gI++)
	    {
	    	double[][][][] tResultSet=new double[gNodeSet.length][gCThresholdSet.length][gRouSet.length][6];
	    	
	    	for(int gT=0;gT<topologySum;gT++)
		    {
	    		for(int gN=0;gN<gNodeSet.length;gN++)
		    	{
		    		int gNode=gNodeSet[gN];
		    		int gDataSum=gDataSumSet[gN];
		    		double gEI=gEISet[gN];
		    		
		    		String tTopologyFileName="test/real/testinterval/topology/I"+gI+"/";
    	    		File tf=new File(tTopologyFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		String tWeightFileName="test/real/testinterval/weight/I"+gI+"/";
    	    		tf=new File(tWeightFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    				
    	    		String tgkRateFileName="test/real/testinterval/gkrate/I"+gI+"/";
    	    		tf=new File(tgkRateFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		String twfRateFileName="test/real/testinterval/wfrate/I"+gI+"/";
    	    		tf=new File(twfRateFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		String tgkSDataFileName="test/real/testinterval/gksdata/I"+gI+"/";
    	    		tf=new File(tgkSDataFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}		
    	    		
    	    		String twfSDataFileName="test/real/testinterval/wfsdata/I"+gI+"/";
    	    		tf=new File(twfSDataFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		Graph g=new Graph();
    	    		String fileName1="test/topology/vertex_"+gNode+"_"+gT+".txt";
			    	String fileName2="test/topology/edge_"+gNode+"_"+gT+".txt";
			    	String fRData="test/topology/data_"+gNode+"_"+gT+"_"+gI+".txt";
    	    		
    	    		
    	    		//initial energy and maxrate
    	    		
			    	TestRealData.initRealData(fileName1, fileName2, "1", g, 1,gEI);
					String fVertex=tTopologyFileName+"vertex-N"+gNode+"-T"+gT+"-I"+gI+".txt";
					String fEdge=tTopologyFileName+"edge-N"+gNode+"-T"+gT+"-I"+gI+".txt";
					g.outputFile(fVertex, fEdge);
					
					
					g=new Graph();
					TestRealData.initRealDataRou(fVertex, fEdge, "", g, 1,1*1.0/100);
					double tWRateFactor=0;
					WfMaxFlow twFlow=new WfMaxFlow();
			    	twFlow.setTopology(g);
				    twFlow.seteRx(eRx);
				    twFlow.seteTx(eTx);
				    twFlow.setEpsilon(0.1);
			    	twFlow.computeDWFFLow();
			    	tWRateFactor=gRateIndicator/twFlow.getTopology().getSourceList().get(0).getRate();
					
    	    		
			    	g=new Graph();
					TestRealData.initRealDataRou(fVertex, fEdge, "", g, 1,1*1.0/100);
					double tGRateFactor=0;
					GragMaxFlow tgFlow=new GragMaxFlow();
			    	tgFlow.setTopology(g);
				    tgFlow.seteRx(eRx);
				    tgFlow.seteTx(eTx);
				    tgFlow.setEpsilon(0.1);
			    	tgFlow.computeConcurrentFlow();
			    	tGRateFactor=gRateIndicator/tgFlow.getTopology().getSourceList().get(0).getRate();
			    	
			    	
		    		
		    		
		    		for(int gC=0;gC<gCThresholdSet.length;gC++)
		    		{
		    			int gCThreshold=gCThresholdSet[gC];
		    		    
		    			
						
		    			
		    			for(int gR=0;gR<gRouSet.length;gR++)
		    			{
		    				int gRou=gRouSet[gR];
		    				
		    				
		    				//calculate weight
		    				String fWData="test/topology/data_"+gNode+"_"+gT+"_"+(gI-1)+".txt";
			    			String fWeight=tWeightFileName+"weight-N"+gNode+"-T"+gT+"-I"+gI+"-C"+gCThreshold+"-R"+gRou+".txt";
							
			    			
			    			g=new Graph();
							TestRealData.initRealDataRou(fVertex, fEdge, "", g, 1,1*1.0/100);
			    			RealDataHandler rdh=new RealDataHandler();
							rdh.setDataSum(gDataSum);
							rdh.setNodeSum(gNode);
							if(gI==0)
							{
								rdh.outputWeightFile2(g, null, fWeight, gCThreshold*1.0/10, 0.03);      //first interval
							}
							else
							{
								rdh.outputWeightFile2(g, fWData, fWeight, gCThreshold*1.0/10, 0.03);
							}
		    				
		    				
		    				
							
							//GK rate allocation
					    	String fGRate=tgkRateFileName+"grate-N"+gNode+"-T"+gT+"-I"+gI+"-C"+gCThreshold+"-R"+gRou+".txt";		    		
				    		
				    		
				            
					    	PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fGRate)));
					    	Graph g1=new Graph();
					    	
					    	if(gRou==100)      //rou==1
					    	{
					    		TestRealData.initRealDataRou(fVertex, fEdge, fWeight, g1, 1,gRou*1.0/100);
					    	}
					    	else if(gRou==110)  //rou==varible
					    	{
					    		TestRealData.initRealDataRou(fVertex, fEdge, fWeight, g1, 2,gRou*1.0/100);
					    	}
					    	else
					    	{
					    		TestRealData.initRealDataRou(fVertex, fEdge, fWeight, g1, 0,gRou*1.0/100);
					    	}
					    	
					    	double tGTime=0;
					    	double tGFlow=0;
							double tGRate=0;
					        GragMaxFlow gFlow=new GragMaxFlow();
					    	gFlow.setTopology(g1);
						    gFlow.seteRx(eRx);
						    gFlow.seteTx(eTx);
						    gFlow.setEpsilon(gAppr*1.0/1000);
					    	tGTime=gFlow.computeConcurrentFlow();
					    	
					    	
					    	//if(gRou==100)
					    	//{
					    		//tGRateFactor=gRateIndicator/tgFlow.getTopology().getSourceList().get(0).getRate();
					    	//}
					    	
					    	
					    
					    	for(int i=0;i<gFlow.getTopology().getSourceList().size();i++)
					    	{
					    		Vertex tVertex=gFlow.getTopology().getSourceList().get(i);
					    		pw1.println(tVertex.getVerValue()+" "+tVertex.getRate()*tGRateFactor+" "+tVertex.getWeight()+" "+tVertex.getRate());
					    		pw1.flush();
					    		tGFlow=tGFlow+tVertex.getRate()*(eRx+eTx);
								tGRate=tGRate+tVertex.getRate();
					    	}
					    	pw1.flush();
					    	pw1.close();
					    	
					    	tResultSet[gN][gC][gR][3]=tResultSet[gN][gC][gR][3]+tGTime;
					    	tResultSet[gN][gC][gR][4]=tResultSet[gN][gC][gR][4]+tGRate;
							
							
					    	
					    	//GKMSE calculate
					    	String fSData1=tgkSDataFileName+"gdata-N"+gNode+"-T"+gT+"-I"+(gI+1)+"-R"+gRou+".txt";      //data save for next interval          
		    				DataQuality dq=new DataQuality();
							dq.setDataSum(gDataSum);
							dq.setNodeSum(gNode);
							double gkTemp=dq.computeMSE2(fRData, fGRate, 0, fWeight,0,fSData1)/gNode/100;
							if(gRou==100)
							{
								gkTemp=dq.computeMSE2(fRData, fGRate, 1, fWeight,0,fSData1)/gNode/100;
							}
		    				tResultSet[gN][gC][gR][5]=tResultSet[gN][gC][gR][5]+gkTemp;
		    				
							
							
							
							//WFrate allocation
		    			    String fRate=twfRateFileName+"rate-N"+gNode+"-T"+gT+"-I"+gI+"-C"+gCThreshold+"-R"+gRou+".txt";		    		
				    		
				    		
				            
					    	PrintWriter pw2=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRate)));
					    	Graph g2=new Graph();
					    	
					    	if(gRou==100)      //rou==1
					    	{
					    		TestRealData.initRealDataRou(fVertex, fEdge, fWeight, g2, 1,gRou*1.0/100);
					    	}
					    	else if(gRou==110)  //rou==0
					    	{
					    		TestRealData.initRealDataRou(fVertex, fEdge, fWeight, g2, 2,gRou*1.0/100);
					    	}
					    	else
					    	{
					    		TestRealData.initRealDataRou(fVertex, fEdge, fWeight, g2, 0,gRou*1.0/100);
					    	}
					    	
					    	double tWTime=0;
					    	double tWFlow=0;
							double tWRate=0;
					        WfMaxFlow wFlow=new WfMaxFlow();
					    	wFlow.setTopology(g2);
						    wFlow.seteRx(eRx);
						    wFlow.seteTx(eTx);
						    wFlow.setEpsilon(gAppr*1.0/1000);
					    	tWTime=wFlow.computeDWFFLow();
					    	
					    	/*
					    	if(gRou==100)
					    	{
					    		tWRateFactor=gRateIndicator/twFlow.getTopology().getSourceList().get(0).getRate();
					    		
					    	}
					    	*/
					    	
					    
					    	for(int i=0;i<wFlow.getTopology().getSourceList().size();i++)
					    	{
					    		Vertex tVertex=wFlow.getTopology().getSourceList().get(i);
					    		pw2.println(tVertex.getVerValue()+" "+tVertex.getRate()*tWRateFactor+" "+tVertex.getWeight()+" "+tVertex.getRate());
					    		pw2.flush();
					    		tWFlow=tWFlow+tVertex.getRate()*(eRx+eTx);
								tWRate=tWRate+tVertex.getRate();
					    	}
					    	pw2.flush();
					    	pw2.close();
					    	
					    	tResultSet[gN][gC][gR][0]=tResultSet[gN][gC][gR][0]+tWTime;
					    	tResultSet[gN][gC][gR][1]=tResultSet[gN][gC][gR][1]+tWRate;
							
							
		    				
		    				
					    	
					    	
		    				
		    	    		
					    	
		    				
		    				
		    				//WFMSE calculate
					    	String fSData2=twfSDataFileName+"data-N"+gNode+"-T"+gT+"-I"+(gI+1)+"-R"+gRou+".txt";      //data save for next interval          
		    				dq=new DataQuality();
							dq.setDataSum(gDataSum);
							dq.setNodeSum(gNode);
							double wfTemp=dq.computeMSE2(fRData, fRate, 0, fWeight,0,fSData2)/gNode/100;
							if(gRou==100)
							{
								wfTemp=dq.computeMSE2(fRData, fRate, 1, fWeight,0,fSData2)/gNode/100;
							}
		    				tResultSet[gN][gC][gR][2]=tResultSet[gN][gC][gR][2]+wfTemp;
		    			
		    			}
		    		}
		    	}
		    }
	    	
	    	for(int i=0;i<gNodeSet.length;i++)
	    	{
	    		for(int j=0;j<gCThresholdSet.length;j++)
	    		{
	    			for(int k=0;k<gRouSet.length;k++)
	    			{
	    				gResultSet[i][j][k][0]=gResultSet[i][j][k][0]+tResultSet[i][j][k][0]/topologySum;
	    				gResultSet[i][j][k][1]=gResultSet[i][j][k][1]+tResultSet[i][j][k][1]/topologySum;
	    				gResultSet[i][j][k][2]=gResultSet[i][j][k][2]+tResultSet[i][j][k][2]/topologySum;
	    				gResultSet[i][j][k][3]=gResultSet[i][j][k][3]+tResultSet[i][j][k][3]/topologySum;
	    				gResultSet[i][j][k][4]=gResultSet[i][j][k][4]+tResultSet[i][j][k][4]/topologySum;
	    				gResultSet[i][j][k][5]=gResultSet[i][j][k][5]+tResultSet[i][j][k][5]/topologySum;
	    			}
	    		}
	    	}
	    	
	    	
	    	//output result
		    String tResultFileName="test/real/testinterval/result/I"+gI+"/";
			File tf=new File(tResultFileName);
			if(!tf.exists())
			{
				tf.mkdirs();
			}
		    for(int i=0;i<gNodeSet.length;i++)
	    	{
	    		for(int j=0;j<gCThresholdSet.length;j++)
	    		{
	    			for(int k=1;k<gRouSet.length;k++)
	    			{
	    				String fNode=tResultFileName+"Node-C"+gCThresholdSet[j]+"-R"+gRouSet[k]+"-A"+gAppr+".txt";
	    				PrintWriter pwN=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fNode,true)));
	    				double tTime=gResultSet[i][j][k][0]/intervalSum;
	    				double tMSE=gResultSet[i][j][k][2]/intervalSum;
	    				double t1MSE=gResultSet[i][j][0][2]/intervalSum;
	    				double tGTime=gResultSet[i][j][k][3]/intervalSum;
	    				double tGMSE=gResultSet[i][j][k][5]/intervalSum;
	    				double t1GMSE=gResultSet[i][j][0][5]/intervalSum;
	    				pwN.println(gNodeSet[i]+" "+df.format(tMSE/t1MSE)+" "+df.format(tMSE)+" "+df.format(t1MSE)+" "+df.format(tTime)+" "+df.format(tGMSE/t1GMSE)+" "+df.format(tGMSE)+" "+df.format(t1GMSE)+" "+df.format(tGTime));
	    				pwN.flush();
	    				pwN.close();
	    			}
	    		}
	    	}
	    	
	    	
	    	
	    }
	    
	    
	    
   }
   
   
}
