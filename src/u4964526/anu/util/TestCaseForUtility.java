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

public class TestCaseForUtility {

	/**
	 * @param args
	 */
	private static double eTx=0.0000144;  //dB
	private static double eRx=0.00000576;  //dB
	private static double epsilon=0.1; 
	private static double[] gBaseBudgetEnergy={0.00653,0,00653};  //{0.00653,0.01365}  0.01365,0.01221,0.01079,0.00937,0.00795,0.00653
	private static double[] gBaseMaxRate={10000};   //{100,80,60}  76800,61440,46080
	//private static double transRange=25;

	
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
	
   private static double validTransRangePlusEdgeCapacity(Vertex v1, Vertex v2,double transRange)
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
   
   
   private static void initRealData(String fileName1, String fileName2, String fileName3, Graph g, int wOption,double eOption, double transRange) throws FileNotFoundException
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
			   Vertex v1=new Vertex(""+lineNum);
			   v1.setxLabel(Double.parseDouble(b[1]));
			   v1.setyLabel(Double.parseDouble(b[2]));
			   v1.setMaxRate(getVertexMaxRate());
			   v1.setBudgetEnergy(getBudgetEnergy()*eOption);
			   v1.setRate(0);
			   v1.setWeight(1);
			   g.addVertex(v1);
			   if(lineNum==0)
			   {
				   g.addSink(v1);
			   }
			   else
			   {
				   g.addSource(v1);
			   }
			   ++lineNum;
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
		 */
		ArrayList<Vertex> vSet=g.getVertexList();
		Iterator<Vertex> slowI=vSet.iterator();
		Iterator<Vertex> fastI;
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
				double tRange=validTransRangePlusEdgeCapacity(v1, v2,transRange);
				if(tRange>0)
				{
					//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
					Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
					g.addEdge(e1);
					
					//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
					Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
					g.addEdge(e2);
					
				}
			}
			
		}
		/*
		 *  end of generate edges
		 */
		
		/*
		 * begin of initial Edges
		 */
//		try
//		{
//		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName2)));
//		   int lineNum=0;
//		   while((tempString=reader.readLine())!=null)
//		   {
//			   String[] b=tempString.split(" ");
//			   /*
//			    * begin of debug info
//			    */
//			   ++lineNum;
//			   logger.fine(String.valueOf(lineNum));
//			   logger.fine(tempString);
//			   String detail="detail:(";
//			   for(int i=0;i<b.length;i++)
//			   {
//				   detail=detail+"<"+i+"-"+b[i]+">";
//			   }
//			   detail=detail+")\n";
//			   logger.fine(detail);
//			   /*
//			    * end of debug info
//			    */
//			   Vertex s1=g.getVertexList().get(Integer.parseInt(b[0]));
//			   Vertex t1=g.getVertexList().get(Integer.parseInt(b[1]));
//			   Edge e1=new Edge(s1,t1,s1.getBudgetEnergy());
//			   g.addEdge(e1);
//			   
//		   }
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			if(reader!=null)
//			{
//				try
//				{
//					reader.close();
//				}
//				catch(Exception e)
//				{
//				}
//			}
//		}
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
	
  
   
   
   
   
   
  
   
   
 
   
   public static void testIntervalSet(int gAppr, String fileIn, String fileOut) throws FileNotFoundException
   {
	   DecimalFormat df=new DecimalFormat("#.0000");
  	    
	    
	    int[] gRouSet={100,110,0,20,40,60,80};  //100,110,0,20,40,60,80{0,0.2,0.4,0.6,0.8,1}
	    int[] gNodeSet={20,100};      
	    double[] gTransSet={48,26};    
	    int[] gDataSumSet={100,100,100,100,100,100,100,100,100,100,100}; 
	    double[] gEISet={1,1,1,1,1,1,1,1,1,1,1};         
	    int[] gCThresholdSet={8};  // manual match
	    double[][] gPairSet=new double[gNodeSet.length][gCThresholdSet.length];
	    double gRateIndicator=60;
	    int topologySum=1;     
	    int intervalSum=1;     
	    //0--SPTtime 1--SPTFlow 2--SPTMSE  3--GKtime  4--GKFlow  5--GKMSE
	    double[][][][] gResultSet=new double[gNodeSet.length][gCThresholdSet.length][gRouSet.length][8];
	    
	    
	    
	    
	    for(int gI=0;gI<intervalSum;gI++)
	    {
	    	double[][][][] tResultSet=new double[gNodeSet.length][gCThresholdSet.length][gRouSet.length][8];
	    	
	    	for(int gT=0;gT<topologySum;gT++)
		    {
	    		for(int gN=0;gN<gNodeSet.length;gN++)
		    	{
		    		int gNode=gNodeSet[gN];
		    		double gTrans=gTransSet[gN];
		    		int gDataSum=gDataSumSet[gN];
		    		double gEI=gEISet[gN];
		    		
		    		
		    		
		    		
		    		String tTopologyFileName=fileOut+"testinterval/topology/I"+gI+"/";
    	    		File tf=new File(tTopologyFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		String tWeightFileName=fileOut+"weight/";;
    	    		tf=new File(tWeightFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    				
    	    		String tgkRateFileName=fileOut+"testinterval/gkrate/I"+gI+"/";
    	    		tf=new File(tgkRateFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		String twfRateFileName=fileOut+"testinterval/wfrate/I"+gI+"/";
    	    		tf=new File(twfRateFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		String tgkSDataFileName=fileOut+"testinterval/gksdata/";
    	    		tf=new File(tgkSDataFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}		
    	    		
    	    		String twfSDataFileName=fileOut+"testinterval/wfsdata/";
    	    		tf=new File(twfSDataFileName);
    	    		if(!tf.exists())
    	    		{
    	    			tf.mkdirs();
    	    		}
    	    		
    	    		Graph g=new Graph();
    	    		String fileName1=fileIn+"node-"+gNode+"-"+gT+".txt";
			    	//String fileName2="test/topology/edge_"+gNode+"_"+gT+".txt";
			    	
    	    		
    	    		
    	    		//initial energy and maxrate
    	    		
			    	TestCaseForUtility.initRealData(fileName1, "0", "0", g, 1,gEI,gTrans);
					String fVertex=tTopologyFileName+"vertex-N"+gNode+"-T"+gT+"-I"+gI+".txt";
					String fEdge=tTopologyFileName+"edge-N"+gNode+"-T"+gT+"-I"+gI+".txt";
					g.outputFile(fVertex, fEdge);
					
					
					g=new Graph();
					TestCaseForUtility.initRealDataRou(fVertex, fEdge, "", g, 1,1*1.0/100);
					double tWRateFactor=-1;
					WfMaxFlow twFlow=new WfMaxFlow();
			    	twFlow.setTopology(g);
				    twFlow.seteRx(eRx);
				    twFlow.seteTx(eTx);
				    twFlow.setEpsilon(0.1);
			    	twFlow.computeDWFFLow();
			    	int tWRateFactorI=10;
			    	while(tWRateFactor<0)
			    	{
			    		double tWRate=twFlow.getTopology().getSourceList().get(tWRateFactorI).getMaxRate();
			    		if((tWRate>(gBaseMaxRate[0]*0.95))||(tWRateFactorI==(twFlow.getTopology().getSourceList().size()-1)))
			    		{
			    			tWRateFactor=gRateIndicator/twFlow.getTopology().getSourceList().get(tWRateFactorI).getRate();
			    		}
			    		tWRateFactorI++;
			    		
			    	}
			    	tWRateFactor=1;
    	    		
			    	g=new Graph();
					TestCaseForUtility.initRealDataRou(fVertex, fEdge, "", g, 1,1*1.0/100);
					double tGRateFactor=-1;
					GragMaxFlow tgFlow=new GragMaxFlow();
			    	tgFlow.setTopology(g);
				    tgFlow.seteRx(eRx);
				    tgFlow.seteTx(eTx);
				    tgFlow.setEpsilon(0.1);
			    	tgFlow.computeConcurrentFlow();
//			    	int tGRateFactorI=0;
//			    	while(tGRateFactor==0)
//			    	{
//			    		double tGRate=tgFlow.getTopology().getSourceList().get(tGRateFactorI++).getMaxRate();
//			    		if((tGRate>(gBaseMaxRate[0]*0.95))||(tGRateFactorI==(tgFlow.getTopology().getSourceList().size()-1)))
//			    		{
//			    			tGRateFactor=gRateIndicator/tgFlow.getTopology().getSourceList().get(tGRateFactorI++).getRate();
//			    		}
//			    		tGRateFactorI++;
//			    	}
			    	tGRateFactor=tWRateFactor;
			    	//tGRateFactor=1;
			    	
			    	System.out.println(gAppr+fileOut+"-Interval<"+gI+">---Topology<"+gT+">---Node<"+gNode+">---Initial");
		    		
		    		for(int gC=0;gC<gCThresholdSet.length;gC++)
		    		{
		    			int gCThreshold=gCThresholdSet[gC];
		    		    
		    			
						
		    			
		    			for(int gR=0;gR<gRouSet.length;gR++)
		    			{
		    				int gRou=gRouSet[gR];
		    				
		    				double[][] gResult=new double[gNode+3][2];
		    				
		    				
		    				System.out.println(gAppr+fileOut+"-Interval<"+gI+">---Topology<"+gT+">---Node<"+gNode+">---CThreshold<"+gCThreshold+">---Rou<"+gRou+">");
		    				
		    				
		    				
		    				String fWeight=tWeightFileName+"weight-N"+gNode+".txt";
		    				
		    						
			    			
			    			
		    				
							
							//GK rate allocation
					    	String fGRate=tgkRateFileName+"grate-N"+gNode+"-T"+gT+"-I"+gI+"-C"+gCThreshold+"-R"+gRou+".txt";		    		
				    		
				    		
				            
					    	PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fGRate)));
					    	Graph g1=new Graph();
					    	
					    	if(gRou==100)      //rou==1
					    	{
					    		TestCaseForUtility.initRealDataRou(fVertex, fEdge, fWeight, g1, 1,gRou*1.0/100);
					    	}
					    	else if(gRou==110)  //rou==varible
					    	{
					    		TestCaseForUtility.initRealDataRou(fVertex, fEdge, fWeight, g1, 2,gRou*1.0/100);
					    	}
					    	else
					    	{
					    		TestCaseForUtility.initRealDataRou(fVertex, fEdge, fWeight, g1, 0,gRou*1.0/100);
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
								gResult[i][0]=tVertex.getRate();
								gResult[gNode+1][0]=gResult[gNode+1][0]+tVertex.getRate();
					    	}
					    	pw1.flush();
					    	pw1.close();
					    	
					    	tResultSet[gN][gC][gR][3]=tResultSet[gN][gC][gR][3]+tGTime;
					    	tResultSet[gN][gC][gR][4]=tResultSet[gN][gC][gR][4]+tGRate;
							
					    	System.out.println("GK Rate Done");
					    	
							
					    	
//					    	/*
//					    	 * !!!!!!!!!need to modify for utility
//					    	 */
//					    	String fSData1=tgkSDataFileName+"gdata-N"+gNode+"-T"+gT+"-I"+(gI+1)+"-C"+gCThreshold+"-R"+gRou+".txt";      //data save for next interval          
//		    				
//					    	
//					    	DataQuality dq=new DataQuality();
//							dq.setDataSum(gDataSum);
//							dq.setNodeSum(gNode);
//							
//							double[] gkTemp=dq.computeUtility(fRData, fGRate, 0, fWeight,0,fSData1);
//							if(gRou==100)
//							{
//								gkTemp=dq.computeUtility(fRData, fGRate, 1, fWeight,0,fSData1);
//							}
//		    				tResultSet[gN][gC][gR][5]=tResultSet[gN][gC][gR][5]+gkTemp[0];
//		    				tResultSet[gN][gC][gR][7]=tResultSet[gN][gC][gR][7]+gkTemp[1];
//		    				
//		    				System.out.println("GK Utility Done");
//		    				/*
//		    				 * end of utility
//		    				 */
							
		    				
		    										
			    			
			    			
		    				
							
							//WFrate allocation
		    			    String fRate=twfRateFileName+"rate-N"+gNode+"-T"+gT+"-I"+gI+"-C"+gCThreshold+"-R"+gRou+".txt";		    		
				    		
				    		
				            
					    	PrintWriter pw2=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fRate)));
					    	Graph g2=new Graph();
					    	
					    	if(gRou==100)      //rou==1
					    	{
					    		TestCaseForUtility.initRealDataRou(fVertex, fEdge, fWeight, g2, 1,gRou*1.0/100);
					    	}
					    	else if(gRou==110)  //rou==0
					    	{
					    		TestCaseForUtility.initRealDataRou(fVertex, fEdge, fWeight, g2, 2,gRou*1.0/100);
					    	}
					    	else
					    	{
					    		TestCaseForUtility.initRealDataRou(fVertex, fEdge, fWeight, g2, 0,gRou*1.0/100);
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
								gResult[i][1]=tVertex.getRate();
								gResult[gNode+1][1]=gResult[gNode+1][1]+tVertex.getRate();
					    	}
					    	pw2.flush();
					    	pw2.close();
					    	
					    	tResultSet[gN][gC][gR][0]=tResultSet[gN][gC][gR][0]+tWTime;
					    	tResultSet[gN][gC][gR][1]=tResultSet[gN][gC][gR][1]+tWRate;
							
							
					    	System.out.println("Wf Rate Done");
		    				
					    	
					    	
		    				
		    	    		
					    	
		    				
		    				
//		    				/*
//		    				 * need to modify for  utility  !!!!!!!!!!!!
//		    				 */
//					    	String fSData2=twfSDataFileName+"wdata-N"+gNode+"-T"+gT+"-I"+(gI+1)+"-C"+gCThreshold+"-R"+gRou+".txt";      //data save for next interval          
//		    				dq=new DataQuality();
//							dq.setDataSum(gDataSum);
//							dq.setNodeSum(gNode);
//							double[] wfTemp=dq.computeUtility(fRData, fRate, 0, fWeight,0,fSData2);
//							if(gRou==100)
//							{
//								wfTemp=dq.computeUtility(fRData, fRate, 1, fWeight,0,fSData2);
//							}
//		    				tResultSet[gN][gC][gR][2]=tResultSet[gN][gC][gR][2]+wfTemp[0];
//		    				tResultSet[gN][gC][gR][6]=tResultSet[gN][gC][gR][6]+wfTemp[1];
//		    				
//		    				System.out.println("Wf Utility Done");
//		    			    /*
//		    			     * end of utility !!!!!!!!!!!!!!
//		    			     */
					    	
                            String fResult=fileOut+"result/result-N"+gNode+"-T"+gT+"-I"+gI+"-C"+gCThreshold+"-R"+gRou+".txt";		    		  
					    	PrintWriter pw3=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fResult)));
					    	for(int i=0;i<gResult.length;i++)
					    	{
					    		pw3.println(gResult[i][0]+" "+gResult[i][1]);
					    		pw3.flush();
					    	}
					    	pw3.close();
					    	
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
	    				gResultSet[i][j][k][6]=gResultSet[i][j][k][6]+tResultSet[i][j][k][6]/topologySum;
	    				gResultSet[i][j][k][7]=gResultSet[i][j][k][7]+tResultSet[i][j][k][7]/topologySum;
	    			}
	    		}
	    	}
	    	
	    	
//	    	//output result  and temp result
//		    String tResultFileName=fileOut+"testinterval/result/I"+gI+"/";
//			File tf=new File(tResultFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//		    for(int i=0;i<gNodeSet.length;i++)
//	    	{
//	    		for(int j=0;j<gCThresholdSet.length;j++)
//	    		{
//	    			for(int k=1;k<gRouSet.length;k++)
//	    			{
//	    				String fNode=tResultFileName+"Node-C"+gCThresholdSet[j]+"-R"+gRouSet[k]+"-A"+gAppr+".txt";
//	    				PrintWriter pwN=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fNode,true)));
//	    				double tTime=gResultSet[i][j][k][0]/(gI+1);
//	    				double tMSE=gResultSet[i][j][k][2]/(gI+1);
//	    				double t1MSE=gResultSet[i][j][0][2]/(gI+1);
//	    				double tGTime=gResultSet[i][j][k][3]/(gI+1);
//	    				double tGMSE=gResultSet[i][j][k][5]/(gI+1);
//	    				double t1GMSE=gResultSet[i][j][0][5]/(gI+1);
//	    				double tGUtility=gResultSet[i][j][k][7]/(gI+1);
//	    				double t1GUtility=gResultSet[i][j][0][7]/(gI+1);
//	    				double tUtility=gResultSet[i][j][k][6]/(gI+1);
//	    				double t1Utility=gResultSet[i][j][0][6]/(gI+1);
//	    				pwN.println(gNodeSet[i]+" "+df.format(tUtility/t1Utility)+" "+df.format(tUtility)+" "+df.format(t1Utility)+" "+df.format(tMSE/t1MSE)+" "+df.format(tMSE)+" "+df.format(t1MSE)+" "+df.format(tTime)+" "+df.format(tGUtility/t1GUtility)+" "+df.format(tGUtility)+" "+df.format(t1GUtility)+" "+df.format(tGMSE/t1GMSE)+" "+df.format(tGMSE)+" "+df.format(t1GMSE)+" "+df.format(tGTime));
//	    				pwN.flush();
//	    				pwN.close();
//	    				
//	    				
//	    				
//	    				String tfNode=tResultFileName+"TTTNode-C"+gCThresholdSet[j]+"-R"+gRouSet[k]+"-A"+gAppr+".txt";
//	    				PrintWriter tpwN=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tfNode,true)));
//	    				double ttTime=tResultSet[i][j][k][0]/(gI+1);
//	    				double ttMSE=tResultSet[i][j][k][2]/(gI+1);
//	    				double tt1MSE=tResultSet[i][j][0][2]/(gI+1);
//	    				double ttGTime=tResultSet[i][j][k][3]/(gI+1);
//	    				double ttGMSE=tResultSet[i][j][k][5]/(gI+1);
//	    				double tt1GMSE=tResultSet[i][j][0][5]/(gI+1);
//	    				double ttGUtility=tResultSet[i][j][k][7]/(gI+1);
//	    				double tt1GUtility=tResultSet[i][j][0][7]/(gI+1);
//	    				double ttUtility=tResultSet[i][j][k][6]/(gI+1);
//	    				double tt1Utility=tResultSet[i][j][0][6]/(gI+1);
//	    				tpwN.println(gNodeSet[i]+" "+df.format(ttUtility/tt1Utility)+" "+df.format(ttUtility)+" "+df.format(tt1Utility)+" "+df.format(ttMSE/tt1MSE)+" "+df.format(ttMSE)+" "+df.format(tt1MSE)+" "+df.format(ttTime)+" "+df.format(ttGUtility/tt1GUtility)+" "+df.format(ttGUtility)+" "+df.format(tt1GUtility)+" "+df.format(ttGMSE/tt1GMSE)+" "+df.format(ttGMSE)+" "+df.format(tt1GMSE)+" "+df.format(ttGTime));
//	    				tpwN.flush();
//	    				tpwN.close();
//	    				
//	    				
//	    				
//	    			}
//	    		}
//	    	}
	    	
	    	
	    	
	    }
	    
	    
	    
   }
   
   
   public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			Logger logger=Logger.getLogger("MaxFlow");
			logger.setLevel(Level.WARNING);
			
			

	
			TestCaseForUtility.testIntervalSet(100,"test/testcase/Topology/","test/testcase/");
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
  }
   
   
   
   
   
   
}

