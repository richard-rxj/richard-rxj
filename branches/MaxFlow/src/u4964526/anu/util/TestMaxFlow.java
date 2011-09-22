package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestMaxFlow {

	/**
	 * @param args
	 */
	private static double eTx=0.0000144;  //dB
	private static double eRx=0.00000576;  //dB
	private static double epsilon=0.1;
	private static double maxMaxRate=76800;
	private static double maxBudgetEnergy=0.01365;

	
	public static double getEpsilon() {
		return epsilon;
	}

	public static void setEpsilon(double apprFactor) {
		TestMaxFlow.epsilon = apprFactor;
	}

	private static double getEdgeCapacity()
	{
		int[] result={1000,2000,3000};
		int i=new Random().nextInt(3);
		return result[i];
		
	}

	private static double getBudgetEnergy1()
	{
		return 0.01365;
		
	}
	
	private static double getBudgetEnergy3()
	{
		double[] result={0.01365,0.01009,0.00653};
		double resultB =0.00653+(0.01365-0.00653)*new Random().nextInt(3)/result.length;
		return resultB;
		
	}
	
	private static double getBudgetEnergy6()
	{
		double[] result={0.01365,0.01221,0.01079,0.00937,0.00795,0.00653};
		double resultB =0.00653+(0.01365-0.00653)*new Random().nextInt(6)/result.length;
		return resultB;
		
	}
	
	private static double getBudgetEnergy12()
	{
		double[] result={0.01365,0.01221,0.01079,0.00937,0.00795,0.00653};
		double resultB =0.00653+(0.01365-0.00653)*new Random().nextInt(12)/result.length;
		return resultB;
		
	}
	
	private static double getVertexMaxRate1()
	{
		return 76800;
	}
	
	
	private static double getVertexMaxRate3()
   {
	    double[] result={76800,57600,38400};
		int i=new Random().nextInt(result.length);
		return result[i];
   }
   
   private static double getVertexMaxRate6()
   {
	    double[] result={76800,69120,61440,53760,46080,38400};
		int i=new Random().nextInt(result.length);
		return result[i];
   }
	
	
	
	
	public static void initRandomData(String fileName1, String fileName2, Graph g, int rOption, int eOption, int wOption)
	/*
	 *  set all weight to 1
	 */
	{
		String tempString=null;
		BufferedReader reader=null;
		Logger logger=Logger.getLogger("MaxFlow");
		
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
			   if(wOption>0)
			   {
				   v1.setWeight(1);
			   }
			   else
			   {
				   v1.setWeight(Double.parseDouble(b[3]));
			   }
			   
			   
			   if(eOption==1)
			   {
				   v1.setBudgetEnergy(TestMaxFlow.getBudgetEnergy1());
			   }
			   else if(eOption==3)
			   {
				   v1.setBudgetEnergy(TestMaxFlow.getBudgetEnergy3());
			   }
			   else if(eOption==6)
			   {
				   v1.setBudgetEnergy(TestMaxFlow.getBudgetEnergy6());
			   }
			   else if(eOption==12)
			   {
				   v1.setBudgetEnergy(TestMaxFlow.getBudgetEnergy12());
			   }
			   else
			   {
				   v1.setBudgetEnergy(Double.parseDouble(b[5]));
			   }
			   
			   if(rOption==0)
			   {
				   v1.setMaxRate(v1.getBudgetEnergy()/TestMaxFlow.maxBudgetEnergy*TestMaxFlow.maxMaxRate);
			   }
			   else if(rOption==1)
			   {
				   v1.setMaxRate(TestMaxFlow.getVertexMaxRate1());
			   }
			   else if(rOption==3)
			   {
				   v1.setMaxRate(TestMaxFlow.getVertexMaxRate3());
			   }
			   else if(rOption==6)
			   {
				   v1.setMaxRate(TestMaxFlow.getVertexMaxRate6());
			   }
			   else
			   {
				   v1.setMaxRate(Double.parseDouble(b[4]));
			   }
			   
			  
				   
			   g.addVertex(v1);
			   if(g.getVertexList().size()==1)
			   {
				   g.addSink(v1);
				   v1.setWasSink(true);
			   }
			   else
			   {
				   g.addSource(v1);
				   v1.setWasSource(true);
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
			   logger.info(detail);
			   /*
			    * end of debug info
			    */
			   Vertex s1=g.getVertexList().get(Integer.parseInt(b[0])-1);
			   Vertex t1=g.getVertexList().get(Integer.parseInt(b[1])-1);
			   double c1=Double.parseDouble(b[2]);
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
		
	}
	
	
	
	private static void mainTaskGrag(Graph g, PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		GragMaxFlow mFlow=new GragMaxFlow();
		mFlow.setTopology(g);
		
		logger.info(String.valueOf(mFlow.getTopology()));
        
        mFlow.setEpsilon(TestMaxFlow.getEpsilon());
        mFlow.seteRx(eRx);
        mFlow.seteTx(eTx);
        
       
        mFlow.computeGragFlow();
        
        /*
        *output the flow information
        *
        Map fFlow=mFlow.getMaxFlow();
        Iterator iter=fFlow.entrySet().iterator();
        while(iter.hasNext())
        {
     	   Map.Entry<Vertex,Flow> entry=(Map.Entry<Vertex, Flow>)iter.next();
     	   Vertex v=entry.getKey();
     	   Flow f=entry.getValue();
     	   pw.println(v);
     	   pw.println(f);
     	   pw.flush();
        }
        /*
         * 
         */

        
        /*
		 * begin of debug info
		 */
		logger.info(String.valueOf(mFlow.getTopology()));
        /*
         * end of debug info
         */
      
        for(int i=0;i<mFlow.getTopology().getSourceList().size();i++)
        {
        	Vertex tVertex=mFlow.getTopology().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
        	pw.flush();
        }
	}
	
	private static int mainTaskConcurrent() throws FileNotFoundException
	{
		int[] gNodeSet={100};
		double[] apprFactorSet={0.1};
		Logger logger=Logger.getLogger("MaxFlow");
		for(int m=0;m<gNodeSet.length;m++)
		{
			for(int n=0;n<apprFactorSet.length;n++)
			{
				for(int l=0;l<20;l++)
				{
					PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/simulation/temp/Gragtask_"+gNodeSet[m]+"_"+l+".txt")));
					String fileName1="test/simulation/topology/vertex_"+gNodeSet[m]+"_"+l+".txt";
					String fileName2="test/simulation/topology/edge_"+gNodeSet[m]+"_"+l+".txt";
					Graph g=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, g,0,0,1);
					GragMaxFlow mFlow=new GragMaxFlow();
					mFlow.setTopology(g);
					//logger.info(String.valueOf(mFlow.getTopology()));
			        mFlow.seteRx(eRx);
			        mFlow.seteTx(eTx);		      
			        mFlow.setEpsilon(TestMaxFlow.getEpsilon());
			
			       
			        mFlow.computeConcurrentFlow();
			        
			        /*
			        *output the flow information
			        *
			        Map fFlow=mFlow.getMaxFlow();
			        Iterator iter=fFlow.entrySet().iterator();
			        while(iter.hasNext())
			        {
			     	   Map.Entry<Vertex,Flow> entry=(Map.Entry<Vertex, Flow>)iter.next();
			     	   Vertex v=entry.getKey();
			     	   Flow f=entry.getValue();
			     	   pw.println(v);
			     	   pw.println(f);
			     	   pw.flush();
			        }
			        /*
			         * 
			         */
			
			        
			        /*
					 * begin of debug info
					 *
					logger.info(String.valueOf(mFlow.getMaxG()));
			        /*
			         * end of debug info
			         */
			      
			        for(int i=0;i<mFlow.getTopology().getSourceList().size();i++)
			        {
			        	Vertex tVertex=mFlow.getTopology().getSourceList().get(i);
			        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
			        	pw.flush();
			        }
			        pw.close();
				}
			}
		}
        return 0;
	}
	
	private static void mainTaskWF(Graph g,PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		WfMaxFlow mFlow=new WfMaxFlow();
    	mFlow.setTopology(g);		
		
  
        
        mFlow.setEpsilon(TestMaxFlow.getEpsilon());
        mFlow.seteRx(eRx);
        mFlow.seteTx(eTx);
        
       
        mFlow.computeWFFLow();
        
        /*
        //output the flow information
        Map fFlow=mFlow.getMaxFlow();
        Iterator iter=fFlow.entrySet().iterator();
        while(iter.hasNext())
        {
     	   Map.Entry<Vertex,Flow> entry=(Map.Entry<Vertex, Flow>)iter.next();
     	   Vertex v=entry.getKey();
     	   Flow f=entry.getValue();
     	   pw.println(v);
     	   pw.println(f);
     	   pw.flush();
        }
        */
        
        /*
		 * begin of debug info
		 */
		logger.info("!!!!!!!!!!!!!!!"+String.valueOf(mFlow.getTopology()));
        //pwDebug.flush();
        /*
         * end of debug info
         */
      
        for(int i=0;i<mFlow.getTopology().getSourceList().size();i++)
        {
        	Vertex tVertex=mFlow.getTopology().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
        	pw.flush();
        }  
        
	}
	
	private static int mainTaskDWF() throws FileNotFoundException
	{
		int[] gNodeSet={450};
		double[] apprFactorSet={0.3};
		Logger logger=Logger.getLogger("MaxFlow");
		for(int m=0;m<gNodeSet.length;m++)
		{
			for(int n=0;n<apprFactorSet.length;n++)
			{
				for(int l=0;l<1;l++)
				{
					PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/simulation/temp/WFtask_"+gNodeSet[m]+"_"+(int)(apprFactorSet[n]*100)+"_"+l+".txt")));
					String fileName1="test/simulation/topology/vertex_"+gNodeSet[m]+"_"+l+".txt";
					String fileName2="test/simulation/topology/edge_"+gNodeSet[m]+"_"+l+".txt";
					Graph g=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, g,0,0,1);
					WfMaxFlow mFlow=new WfMaxFlow();
			    	mFlow.setTopology(g);		
					
			  
			        
			        mFlow.setEpsilon(apprFactorSet[n]);
			        mFlow.seteRx(eRx);
			        mFlow.seteTx(eTx);
			        
			       
			        mFlow.computeDWFFLow();
			        
			        /*
			        //output the flow information
			        Map fFlow=mFlow.getMaxFlow();
			        Iterator iter=fFlow.entrySet().iterator();
			        while(iter.hasNext())
			        {
			     	   Map.Entry<Vertex,Flow> entry=(Map.Entry<Vertex, Flow>)iter.next();
			     	   Vertex v=entry.getKey();
			     	   Flow f=entry.getValue();
			     	   pw.println(v);
			     	   pw.println(f);
			     	   pw.flush();
			        }
			        */
			        
			        /*
					 * begin of debug info
					 */
					logger.info("!!!!!!!!!!!!!!!"+String.valueOf(mFlow.getTopology()));
			        //pwDebug.flush();
			        /*
			         * end of debug info
			         */
			      
			        for(int i=0;i<mFlow.getTopology().getSourceList().size();i++)
			        {
			        	Vertex tVertex=mFlow.getTopology().getSourceList().get(i);
			        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
			        	pw.flush();
			        }  
			        pw.close();
				}
			}
		}
        return 0;
	}
	
	
	public static void runningTask(int rOption,int eOption ) throws SecurityException, IOException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		FileHandler fh=new FileHandler("m.log");
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(Level.WARNING);
		logger.addHandler(fh);
		DecimalFormat df=new DecimalFormat("#.0000");  
				
		/*
         *     random data
         */
		long startTime=0;
		long endTime=0;
		int gNode=0;
		int [] gNodeSet={100,150,200,250,300,350,400,450,500};
		
		double[] apprFactorSet={0.3,0.2,0.1,0.05};
		String tFileAdd="test/simulation/"+rOption+"-"+eOption;
		File tFile=new File(tFileAdd);
		if(!tFile.exists())
		{
			tFile.mkdirs();
		}
		
		for(int j=0;j<apprFactorSet.length;j++)
		{
			PrintWriter pwGRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tFileAdd+"/running_"+(int)(apprFactorSet[j]*100)+".txt")));
			for(int i=0;i<gNodeSet.length;i++)
			{
				double pRatio=0;
				double rRatio=0;
				double pRRatio=0;
				double rRRatio=0;
				double gRunTime=0;
				double gFlowData=0;
				double wfRunTime=0;
				double wfFlowData=0;
				for(int l=0;l<20;l++)
				{
					String rFileAdd="test/simulation/running/"+rOption+"-"+eOption;
					File rFile=new File(rFileAdd);
					if(!rFile.exists())
					{
						rFile.mkdirs();
					}
					
					
					PrintWriter pwRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream(rFileAdd+"/running_"+(int)(apprFactorSet[j]*100)+"_"+l+".txt",true)));

					String fileName1="test/simulation/topology/vertex_"+gNodeSet[i]+"_"+l+".txt";
					String fileName2="test/simulation/topology/edge_"+gNodeSet[i]+"_"+l+".txt";
					String fileName3="test/simulation/topology/wfedge_"+gNodeSet[i]+"_"+l+".txt";
					Graph gGraph=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, gGraph, rOption, eOption, 1);
					fileName1=tFileAdd+"/vertex.txt";
					fileName2=tFileAdd+"/edge.txt";
					gGraph.outputFile(fileName1,fileName2);
					
					
					    
					
						
						Graph gGrag=new Graph();
						double tGTime=0;
						TestMaxFlow.initRandomData(fileName1, fileName2, gGrag,100,100,1);
						GragMaxFlow mFlow=new GragMaxFlow();
						mFlow.setTopology(gGrag);
						mFlow.seteRx(eRx);
						mFlow.seteTx(eTx);
						mFlow.setEpsilon(apprFactorSet[j]);
						startTime=System.currentTimeMillis();
						tGTime=mFlow.computeConcurrentFlow();
						endTime=System.currentTimeMillis();
						
						pwRun.print(gNodeSet[i]+" "+df.format(tGTime)+" ");
						pwRun.flush();
						
						double tGFlow=0;
						double tGRate=0;
						for(int tS=0;tS<mFlow.getTopology().getSourceList().size();tS++)
						{
							Vertex tV=mFlow.getTopology().getSourceList().get(tS);
							tGFlow=tGFlow+tV.getRate()*(eRx+eTx);
							tGRate=tGRate+tV.getRate();
						}
						pwRun.print(df.format(tGFlow)+" "+df.format(tGRate)+" "+df.format(mFlow.getTopology().getSinkList().get(0).getMaxRate())+" ");
						pwRun.flush();
		                gRunTime=gRunTime+tGTime;
		                gFlowData=gFlowData+tGRate;
						
		                
		                
						
						Graph gWf=new Graph();
						double tWTime=0;
						TestMaxFlow.initRandomData(fileName1, fileName2, gWf,100,100,1);
						WfMaxFlow wFlow=new WfMaxFlow();
						wFlow.setTopology(gWf);
						wFlow.seteRx(eRx);
						wFlow.seteTx(eTx);
						wFlow.setEpsilon(apprFactorSet[j]);
						startTime=System.currentTimeMillis();
						tWTime=wFlow.computeDWFFLow();
						endTime=System.currentTimeMillis();
						
						pwRun.print(df.format(tWTime)+" ");
						pwRun.flush();
					
						double tWFlow=0;
						double tWRate=0;
						for(int tS=0;tS<wFlow.getTopology().getSourceList().size();tS++)
						{
							Vertex tV=wFlow.getTopology().getSourceList().get(tS);
							tWFlow=tWFlow+tV.getRate()*(eRx+eTx);
							tWRate=tWRate+tV.getRate();
						}
						pwRun.print(df.format(tWFlow)+" "+df.format(tWRate)+" "+df.format(wFlow.getTopology().getSinkList().get(0).getMaxRate())+" ");
						pwRun.flush();
						wfRunTime=wfRunTime+tWTime;
						wfFlowData=wfFlowData+tWRate;
						/*
						 * begin of changable radius
						 *
						Graph gRWf=new Graph();
						TestMaxFlow.initRandomData(fileName1, fileName3, gRWf,rOption,eOption,1);
						WfMaxFlow wRFlow=new WfMaxFlow();
						wRFlow.setTopology(gRWf);
						wRFlow.seteRx(eRx);
						wRFlow.seteTx(eTx);
						wRFlow.setEpsilon(apprFactorSet[j]);
						startTime=System.currentTimeMillis();
						wRFlow.computeDWFFLow();
						endTime=System.currentTimeMillis();
						double tRWTime=endTime-startTime;
						pwRun.print(df.format(tRWTime)+" ");
						pwRun.flush();
					
						double tRWFlow=0;
						double tRWRate=0;
						for(int tS=0;tS<wRFlow.getTopology().getSourceList().size();tS++)
						{
							Vertex tV=wRFlow.getTopology().getSourceList().get(tS);
							tRWFlow=tRWFlow+tV.getRate()*(eRx+eTx);
							tRWRate=tRWRate+tV.getRate();
						}
						pwRun.print(df.format(tRWFlow)+" "+df.format(tRWRate)+" ");
						pwRun.flush();
						/*
						 * end of changable radius
						 */
						
						
						pRatio=pRatio+tWRate/tGRate;
						rRatio=rRatio+tWTime/tGTime;
						//pRRatio=pRRatio+tRWRate/tGRate;
						//rRRatio=rRRatio+tRWTime/tGTime;
						//pwRun.println(df.format(tWRate/tGRate)+" "+df.format(tWTime/tGTime)+" "+df.format(tRWRate/tGRate)+" "+df.format(tRWTime/tGTime));
						
						pwRun.println(df.format(tWRate/tGRate)+" "+df.format(tWTime/tGTime));
						pwRun.flush();
						pwRun.close();
						
				}
				pwGRun.println(gNodeSet[i]+" "+df.format(gRunTime/20)+" "+df.format(gFlowData/20)+" "+df.format(wfRunTime/20)+" "+df.format(wfFlowData/20)+" "+df.format(pRatio/20)+" "+df.format(rRatio/20)+" "+df.format(pRRatio/20)+" "+df.format(rRRatio/20));
				pwGRun.flush();
				
			}
			pwGRun.close();
		}
		/*
		 * 
		 */
		
		
		
	}
	
	private static void includeMatlabTask(int rOption, int eOption)
	{
				
		try
		{
			Logger logger=Logger.getLogger("MaxFlow");
			DecimalFormat df=new DecimalFormat("#.0000");
			
			
			double[] apprFactorSet={0.3,0.2,0.1,0.05};
			int [] gNodeSet={10,15};
			
			long startTime=0;
			long endTime=0;
			String tFileAdd="test/simulation/matlab/"+rOption+"-"+eOption;;
			File tFile=new File(tFileAdd);
			if(!tFile.exists())
			{
				tFile.mkdirs();
			}
			
			for(int i=0;i<gNodeSet.length;i++)
			{
				for(int l=0;l<1;l++)
				{
					String fileName1="test/simulation/topology/vertex_"+gNodeSet[i]+"_"+l+".txt";
					String fileName2="test/simulation/topology/edge_"+gNodeSet[i]+"_"+l+".txt";
					
					
					
					String mFileAdd="test/simulation/matlab/"+rOption+"-"+eOption+"/"+gNodeSet[i];;
					
					
					Graph gMatlab=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, gMatlab,rOption,eOption,1);
					//logger.warning(String.valueOf(gMatlab));
					MatlabMaxFlow matlabFlow=new MatlabMaxFlow();
					matlabFlow.setMaxG(gMatlab);
					matlabFlow.seteRx(eRx);
					matlabFlow.seteTx(eTx);
					matlabFlow.computeLPMatlab(mFileAdd);
				}
			}
			
			for(int j=0;j<apprFactorSet.length;j++)
			{
				PrintWriter pwGRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tFileAdd+"/running_"+(int)(apprFactorSet[j]*100)+".txt")));
				for(int i=0;i<gNodeSet.length;i++)
				{
					double pRatio=0;
					double rRatio=0;
					double pRRatio=0;
					double rRRatio=0;
					double gRunTime=0;
					double gFlowData=0;
					double wfRunTime=0;
					double wfFlowData=0;
					int lMax=1;
					for(int l=0;l<lMax;l++)
					{
						String rFileAdd="test/simulation/matlab/running/"+rOption+"-"+eOption;
						File rFile=new File(rFileAdd);
						if(!rFile.exists())
						{
							rFile.mkdirs();
						}
						
						
						PrintWriter pwRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream(rFileAdd+"/running_"+(int)(apprFactorSet[j]*100)+"_"+l+".txt",true)));

						String fileName1="test/simulation/topology/vertex_"+gNodeSet[i]+"_"+l+".txt";
						String fileName2="test/simulation/topology/edge_"+gNodeSet[i]+"_"+l+".txt";
							
						
						    
						
							
							Graph gGrag=new Graph();
							double tGTime=0;
							TestMaxFlow.initRandomData(fileName1, fileName2, gGrag,rOption,eOption,1);
							GragMaxFlow mFlow=new GragMaxFlow();
							mFlow.setTopology(gGrag);
							mFlow.seteRx(eRx);
							mFlow.seteTx(eTx);
							mFlow.setEpsilon(apprFactorSet[j]);
							startTime=System.currentTimeMillis();
							tGTime=mFlow.computeConcurrentFlow();
							endTime=System.currentTimeMillis();
							
							pwRun.print(gNodeSet[i]+" "+df.format(tGTime)+" ");
							pwRun.flush();
							
							double tGFlow=0;
							double tGRate=0;
							for(int tS=0;tS<mFlow.getTopology().getSourceList().size();tS++)
							{
								Vertex tV=mFlow.getTopology().getSourceList().get(tS);
								tGFlow=tGFlow+tV.getRate()*(eRx+eTx);
								tGRate=tGRate+tV.getRate();
							}
							pwRun.print(df.format(tGFlow)+" "+df.format(tGRate)+" ");
							pwRun.flush();
			                gRunTime=gRunTime+tGTime;
			                gFlowData=gFlowData+tGRate;
							
			                
			                
							
							Graph gWf=new Graph();
							double tWTime=0;
							TestMaxFlow.initRandomData(fileName1, fileName2, gWf,rOption,eOption,1);
							WfMaxFlow wFlow=new WfMaxFlow();
							wFlow.setTopology(gWf);
							wFlow.seteRx(eRx);
							wFlow.seteTx(eTx);
							wFlow.setEpsilon(apprFactorSet[j]);
							startTime=System.currentTimeMillis();
							tWTime=wFlow.computeDWFFLow();
							endTime=System.currentTimeMillis();
							
							pwRun.print(df.format(tWTime)+" ");
							pwRun.flush();
						
							double tWFlow=0;
							double tWRate=0;
							for(int tS=0;tS<wFlow.getTopology().getSourceList().size();tS++)
							{
								Vertex tV=wFlow.getTopology().getSourceList().get(tS);
								tWFlow=tWFlow+tV.getRate()*(eRx+eTx);
								tWRate=tWRate+tV.getRate();
							}
							pwRun.print(df.format(tWFlow)+" "+df.format(tWRate)+" ");
							pwRun.flush();
							wfRunTime=wfRunTime+tWTime;
							wfFlowData=wfFlowData+tWRate;
							/*
							 * begin of changable radius
							 *
							Graph gRWf=new Graph();
							TestMaxFlow.initRandomData(fileName1, fileName3, gRWf,rOption,eOption,1);
							WfMaxFlow wRFlow=new WfMaxFlow();
							wRFlow.setTopology(gRWf);
							wRFlow.seteRx(eRx);
							wRFlow.seteTx(eTx);
							wRFlow.setEpsilon(apprFactorSet[j]);
							startTime=System.currentTimeMillis();
							wRFlow.computeDWFFLow();
							endTime=System.currentTimeMillis();
							double tRWTime=endTime-startTime;
							pwRun.print(df.format(tRWTime)+" ");
							pwRun.flush();
						
							double tRWFlow=0;
							double tRWRate=0;
							for(int tS=0;tS<wRFlow.getTopology().getSourceList().size();tS++)
							{
								Vertex tV=wRFlow.getTopology().getSourceList().get(tS);
								tRWFlow=tRWFlow+tV.getRate()*(eRx+eTx);
								tRWRate=tRWRate+tV.getRate();
							}
							pwRun.print(df.format(tRWFlow)+" "+df.format(tRWRate)+" ");
							pwRun.flush();
							/*
							 * end of changable radius
							 */
							
							
							pRatio=pRatio+tWRate/tGRate;
							rRatio=rRatio+tWTime/tGTime;
							//pRRatio=pRRatio+tRWRate/tGRate;
							//rRRatio=rRRatio+tRWTime/tGTime;
							//pwRun.println(df.format(tWRate/tGRate)+" "+df.format(tWTime/tGTime)+" "+df.format(tRWRate/tGRate)+" "+df.format(tRWTime/tGTime));
							
							pwRun.println(df.format(tWRate/tGRate)+" "+df.format(tWTime/tGTime));
							pwRun.flush();
							pwRun.close();
							
					}
					pwGRun.println(gNodeSet[i]+" "+df.format(gRunTime/lMax)+" "+df.format(gFlowData/lMax)+" "+df.format(wfRunTime/lMax)+" "+df.format(wfFlowData/lMax)+" "+df.format(pRatio/lMax)+" "+df.format(rRatio/lMax)+" "+df.format(pRRatio/lMax)+" "+df.format(rRRatio/lMax));
					pwGRun.flush();
					
				}
				pwGRun.close();
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	

	
	
	public static void main(String[] args) throws SecurityException, IOException {
		
		Logger logger=Logger.getLogger("MaxFlow");
		FileHandler fh=new FileHandler("m.log");
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(Level.WARNING);
		logger.addHandler(fh);
		//TestMaxFlow.performanceTask();
		//TestMaxFlow.runningTask();
		TestMaxFlow.mainTaskDWF();
		//TestMaxFlow.mainTaskConcurrent();
		/*
		TestMaxFlow.includeMatlabTask(1, 1);
		TestMaxFlow.includeMatlabTask(1, 3);
		TestMaxFlow.includeMatlabTask(1, 6);
		TestMaxFlow.includeMatlabTask(0, 3);
		TestMaxFlow.includeMatlabTask(0, 6);
		TestMaxFlow.includeMatlabTask(3, 1);
		TestMaxFlow.includeMatlabTask(6, 1);
		TestMaxFlow.includeMatlabTask(3, 3);
		TestMaxFlow.includeMatlabTask(6, 6);
	    */
	}
}
