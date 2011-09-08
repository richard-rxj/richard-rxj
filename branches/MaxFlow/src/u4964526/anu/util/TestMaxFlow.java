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

   private static double getVertexMaxRate()
   {
	    double[] result={1000};
		int i=new Random().nextInt(3);
		return result[i];
   }
	
	
	private static void initTest(Graph add)
	{
		Logger logger=Logger.getLogger("MaxFlow");
		Graph g=add;
		Vertex aV=new Vertex("A");
        Vertex bV=new Vertex("B");
        Vertex cV=new Vertex("C");
        Vertex dV=new Vertex("D");
        Vertex tV=new Vertex("T");
        Vertex kV=new Vertex("K");
        Vertex sV=new Vertex("S");
        Edge asE=new Edge(aV,sV,2);
        Edge bsE=new Edge(bV,sV,2);
        Edge tsE=new Edge(tV,sV,2);
        Edge ksE=new Edge(kV,sV,2);
        Edge atE=new Edge(aV,tV,2);
        Edge btE=new Edge(bV,tV,2);
        Edge ctE=new Edge(cV,tV,2);
        Edge ckE=new Edge(cV,kV,2);
        Edge dkE=new Edge(dV,kV,2);
        asE.setLength(10);
        bsE.setLength(5);
        tsE.setLength(2);
        ksE.setLength(1);
        atE.setLength(2);
        btE.setLength(4);
        ctE.setLength(4);
        ckE.setLength(3);
        dkE.setLength(1);
        g.addVertex(aV);
        g.addVertex(bV);
        g.addVertex(cV);
        g.addVertex(dV);
        g.addVertex(tV);
        g.addVertex(kV);
        g.addVertex(sV);
        g.addEdge(asE);
        g.addEdge(bsE);
        g.addEdge(tsE);
        g.addEdge(ksE);
        g.addEdge(atE);
        g.addEdge(btE);
        g.addEdge(ctE);
        g.addEdge(ckE);
        g.addEdge(dkE);
        g.addSource(aV);
        g.addSource(bV);
        g.addSource(cV);
        g.addSource(dV);
        g.addSink(sV);
        
        logger.info("initial complete!");
	}
	
	private static void initRandomData(String fileName1, String fileName2, Graph g, int wOption)
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
			   v1.setMaxRate(Double.parseDouble(b[4]));
			   v1.setBudgetEnergy(Double.parseDouble(b[5]));
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
			   Edge e1=new Edge(s1,t1,c1);
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
	
	private static int mainTaskConcurrent(Graph g,PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		GragMaxFlow mFlow=new GragMaxFlow();
		mFlow.setTopology(g);
				      
        
        mFlow.setEpsilon(TestMaxFlow.getEpsilon());
        mFlow.seteRx(eRx);
        mFlow.seteTx(eTx);
        
       
        int result=mFlow.computeConcurrentFlow();
        
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
        
        return result;
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
	
	private static int mainTaskDWF(Graph g,PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		WfMaxFlow mFlow=new WfMaxFlow();
    	mFlow.setTopology(g);		
		
  
        
        mFlow.setEpsilon(TestMaxFlow.getEpsilon());
        mFlow.seteRx(eRx);
        mFlow.seteTx(eTx);
        
       
        int result=mFlow.computeDWFFLow();
        
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
        
        return result;
	}
	
	
	public static void runningTask() throws SecurityException, IOException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		FileHandler fh=new FileHandler("m.log");
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(Level.INFO);
		//logger.addHandler(fh);
		
				
		/*
         *     random data
         */
		long startTime=0;
		long endTime=0;
		int gNode=0;
		int[] gNodeSet={50,60,70,80,90,100,110,110,120,130,140,150};
		
		double[] apprFactorSet={0.3,0.25,0.2,0.15,0.1};
		for(int j=0;j<apprFactorSet.length;j++)
		{
			PrintWriter pwRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/running_"+apprFactorSet[j]*100+".txt")));
			for(int i=0;i<gNodeSet.length;i++)
			{
				
				String fileName1="test/topology/Rvertex_"+gNodeSet[i]+".txt";
				String fileName2="test/topology/Redge_"+gNodeSet[i]+".txt";
				
				
				
				
				
					
					Graph gCon=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, gCon,1);
					GragMaxFlow mFlow=new GragMaxFlow();
					mFlow.setTopology(gCon);
					mFlow.seteRx(eRx);
					mFlow.seteTx(eTx);
					mFlow.setEpsilon(apprFactorSet[j]);
					startTime=System.currentTimeMillis();
					mFlow.computeConcurrentFlow();
					endTime=System.currentTimeMillis();
					pwRun.print(gNodeSet[i]+" "+(endTime-startTime));
					pwRun.flush();
					
	
					
					
					Graph gWf=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, gWf,1);
					WfMaxFlow wFlow=new WfMaxFlow();
					wFlow.setTopology(gWf);
					wFlow.seteRx(eRx);
					wFlow.seteTx(eTx);
					wFlow.setEpsilon(apprFactorSet[j]);
					startTime=System.currentTimeMillis();
					wFlow.computeDWFFLow();
					endTime=System.currentTimeMillis();
					pwRun.println(" "+(endTime-startTime)+" ");
					pwRun.flush();
				
			}
		}
		/*
		 * 
		 */
		
		
		
	}
	
	private static void performanceTask()
	{
		class Performance
		{
			int nodeNum;
		    double gragRate;
		    double wfRate;
		}
		
		try
		{
			double[] apprFactorSet={0.3,0.25,0.2,0.15,0.1};
			PrintWriter pw=null;
			for(int j=50;j<201;)
			{
				String fileName1="test/topology/Rvertex_"+j+".txt";
				String fileName2="test/topology/Redge_"+j+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/performance_"+j+".txt")));
				j=j+50;
				
				for(int i=0;i<apprFactorSet.length;i++)
				{
					epsilon=apprFactorSet[i];
					GragMaxFlow gGrag=new GragMaxFlow();
					Graph g1=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, g1, 1);
					pw.print(apprFactorSet[i]+" ");
					//pw.print(g1.getSourceList().size()+" ");
					pw.flush();
					//gP.nodeNum=g1.getSourceList().size();
					gGrag.setTopology(g1);
					gGrag.seteRx(eRx);
					gGrag.seteTx(eTx);
					gGrag.setEpsilon(epsilon);
					gGrag.computeConcurrentFlow();
					double tFlow=0;
					double tRate=0;
					for(int tS=0;tS<gGrag.getTopology().getSourceList().size();tS++)
					{
						Vertex tV=gGrag.getTopology().getSourceList().get(tS);
						tFlow=tFlow+tV.getRate()*(eRx+eTx);
						tRate=tRate+tV.getRate();
					}
					pw.print(tFlow+" "+tRate+" ");
					pw.flush();
					//gP.gragRate=gGrag.getMaxG().getSourceList().get(0).getRate();
					WfMaxFlow gWf=new WfMaxFlow();
					Graph g2=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, g2, 1);
					gWf.setTopology(g2);
					gWf.seteRx(eRx);
					gWf.seteTx(eTx);
					gWf.setEpsilon(epsilon);
					gWf.computeDWFFLow();
					tFlow=0;
					tRate=0;
					for(int tS=0;tS<gWf.getTopology().getSourceList().size();tS++)
					{
						Vertex tV=gWf.getTopology().getSourceList().get(tS);
						tFlow=tFlow+tV.getRate()*(eRx+eTx);
						tRate=tRate+tV.getRate();
					}
					pw.println(tFlow+" "+tRate);
					pw.flush();
					
				}
				
			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	

	
	
	public static void main(String[] args) throws SecurityException, IOException {
		
		TestMaxFlow.performanceTask();
		TestMaxFlow.runningTask();
	}
}
