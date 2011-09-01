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
import java.util.Calendar;
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
	private static double eTx=0.00003;  //dB
	private static double eRx=0.00002;  //dB
	private static double apprFactor=0.2;

	
	public static double getApprFactor() {
		return apprFactor;
	}

	public static void setApprFactor(double apprFactor) {
		TestMaxFlow.apprFactor = apprFactor;
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
	
	private static void initRealData(String fileName1, String fileName2, String fileName3, Graph g, int wOption)
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
			   v1.setMaxRate(TestMaxFlow.getVertexMaxRate());
			   v1.setRate(0);
			   g.addVertex(v1);
			   if(Integer.parseInt(b[0])==4)
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
			   double c1=TestMaxFlow.getEdgeCapacity();
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
	
	private static void mainTaskGrag(Graph g, PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		GragMaxFlow mFlow=new GragMaxFlow();
		mFlow.setMaxG(g);
				      
        
        mFlow.setApprFactor(TestMaxFlow.getApprFactor());
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
		logger.info(String.valueOf(mFlow.getMaxG()));
        /*
         * end of debug info
         */
      
        for(int i=0;i<mFlow.getMaxG().getSourceList().size();i++)
        {
        	Vertex tVertex=mFlow.getMaxG().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
        	pw.flush();
        }
	}
	
	private static int mainTaskConcurrent(Graph g,PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		GragMaxFlow mFlow=new GragMaxFlow();
		mFlow.setMaxG(g);
				      
        
        mFlow.setApprFactor(TestMaxFlow.getApprFactor());
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
      
        for(int i=0;i<mFlow.getMaxG().getSourceList().size();i++)
        {
        	Vertex tVertex=mFlow.getMaxG().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
        	pw.flush();
        }
        
        return result;
	}
	
	private static void mainTaskWF(Graph g,PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		WfMaxFlow mFlow=new WfMaxFlow();
    	mFlow.setMaxG(g);		
		
  
        
        mFlow.setApprFactor(TestMaxFlow.getApprFactor());
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
		logger.info("!!!!!!!!!!!!!!!"+String.valueOf(mFlow.getMaxG()));
        //pwDebug.flush();
        /*
         * end of debug info
         */
      
        for(int i=0;i<mFlow.getMaxG().getSourceList().size();i++)
        {
        	Vertex tVertex=mFlow.getMaxG().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
        	pw.flush();
        }  
        
	}
	
	private static int mainTaskDWF(Graph g,PrintWriter pw) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		WfMaxFlow mFlow=new WfMaxFlow();
    	mFlow.setMaxG(g);		
		
  
        
        mFlow.setApprFactor(TestMaxFlow.getApprFactor());
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
		logger.info("!!!!!!!!!!!!!!!"+String.valueOf(mFlow.getMaxG()));
        //pwDebug.flush();
        /*
         * end of debug info
         */
      
        for(int i=0;i<mFlow.getMaxG().getSourceList().size();i++)
        {
        	Vertex tVertex=mFlow.getMaxG().getSourceList().get(i);
        	pw.println(tVertex.getVerValue()+" "+tVertex.getRate()+" "+tVertex.getMaxRate()+" "+tVertex.getWeight());
        	pw.flush();
        }  
        
        return result;
	}
	
	
	private void storeRoom() throws SecurityException, IOException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		FileHandler fh=new FileHandler("m.log");
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(Level.INFO);
		logger.addHandler(fh);
		
		
		/*
        *     real data
        *
		
		String fileName1="/home/u4964526/workspace/MaxFlow/input/RealData/coordinate.txt";
		String fileName2="/home/u4964526/workspace/MaxFlow/input/RealData/neighbor.txt";
		String fileName3="/home/u4964526/workspace/MaxFlow/input/RealData/weight.txt";
		
		
		PrintWriter pwGrag=new PrintWriter(new OutputStreamWriter(new FileOutputStream("/home/u4964526/workspace/MaxFlow/output/Grag_Real_rate1.txt")));  
		Graph gGrag=new Graph();
		TestMaxFlow.initRealData(fileName1, fileName2, fileName3, gGrag, pwDebug,1);
		TestMaxFlow.mainTaskGrag(gGrag, pwGrag, pwDebug);
	
		PrintWriter pwWf=new PrintWriter(new OutputStreamWriter(new FileOutputStream("/home/u4964526/workspace/MaxFlow/output/WF_Real_rate1.txt")));  
		Graph gWf=new Graph();
		TestMaxFlow.initRealData(fileName1, fileName2, fileName3, gWf, pwDebug,1);
		TestMaxFlow.mainTaskWF(gWf, pwWf, pwDebug);
		/*
		 * 
		 */
		
		/*
         *     random data
         */
		int[] gNodeSet={6,10,15,20,50,100,150};
		double[] gApprFactorSet={0.2,0.1,0.05};
		//Calendar calendar=Calendar.getInstance();
		String filePre="test/RandomNoWeight/";
		PrintWriter pwGragRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePre+"Grag_Random_Run.txt")));
		PrintWriter pwWfRun=new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePre+"Wf_Random_Run.txt")));
		long startTime=0;
		long endTime=0;
		int tLoop=0;
		
		for(int i=0;i<gNodeSet.length;i++)
		{
			
			int gNode=gNodeSet[i];
			String fileName1="test/topology/vertex_"+gNode+".txt";
			String fileName2="test/topology/edge_"+gNode+".txt";
			
			/*
			 *  begin of matlab solve
			 *
			Graph gMatlab=new Graph();
			TestMaxFlow.initRandomData(fileName1, fileName2, gMatlab,1);
			MatlabMaxFlow matlabMaxFlow=new MatlabMaxFlow();
			matlabMaxFlow.seteRx(eRx);
			matlabMaxFlow.seteTx(eTx);
			matlabMaxFlow.setMaxG(gMatlab);
			matlabMaxFlow.computeLPMatlab("test/RandomNoWeight/");
            /*
             *  end of matlab solve
             */
			
			
			/*
			 * 
			 */
			for(int j=0;j<gApprFactorSet.length;j++)
			{
				//calendar.setTime(new Date());
				int gApprFactor=(int)(gApprFactorSet[j]*1000);
				TestMaxFlow.setApprFactor(gApprFactorSet[j]);


			    /*
				Graph gGrag=new Graph();
				TestMaxFlow.initRandomData(fileName1, fileName2, gGrag,1);
				PrintWriter pwGrag=new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePre+"Grag_Random_rate_"+gNode+"_"+gApprFactor+".txt")));
				TestMaxFlow.mainTaskGrag(gGrag, pwGrag);
		        */
				
				Graph gCon=new Graph();
				TestMaxFlow.initRandomData(fileName1, fileName2, gCon,1);
				PrintWriter pwCon=new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePre+"Concurrent_Random_rate_"+gNode+"_"+gApprFactor+".txt")));
				startTime=System.currentTimeMillis();
				tLoop=TestMaxFlow.mainTaskConcurrent(gCon, pwCon);
				endTime=System.currentTimeMillis();
				pwGragRun.println(gNode+" "+gApprFactor+" "+tLoop+" "+(endTime-startTime));
				pwGragRun.flush();
				
			    /*
				Graph gWf=new Graph();
				TestMaxFlow.initRandomData(fileName1, fileName2, gWf,1);
				PrintWriter pwWf=new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePre+"WF_Random_rate_"+gNode+"_"+gApprFactor+".txt")));
				TestMaxFlow.mainTaskWF(gWf, pwWf);
				*/
				
				
				Graph gWf=new Graph();
				TestMaxFlow.initRandomData(fileName1, fileName2, gWf,1);
				PrintWriter pwWf=new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePre+"DWF_Random_rate_"+gNode+"_"+gApprFactor+".txt")));
				startTime=System.currentTimeMillis();
				tLoop=TestMaxFlow.mainTaskDWF(gWf, pwWf);
				endTime=System.currentTimeMillis();
				pwWfRun.println(gNode+" "+gApprFactor+" "+tLoop+" "+(endTime-startTime)+" ");
				pwWfRun.flush();
			}
			/*
			 * 
			 */
		}
		/*
		 * 
		 */
		
		
		
	}
	
	
	public static void main(String[] args) {
		
		try
		{
			double[] apprFactorSet={0.2,0.1,0.05};
			for(int i=0;i<apprFactorSet.length;i++)
			{
				apprFactor=apprFactorSet[i];
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/performance_"+apprFactorSet[i]+".txt")));
				for(int j=0;j<20;j++)
				{
					String fileName1="test/topology/vertex_"+j+".txt";
					String fileName2="test/topology/edge_"+j+".txt";
					GragMaxFlow gGrag=new GragMaxFlow();
					Graph g1=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, g1, 1);
					pw.print(g1.getSourceList().size()+" ");
					pw.flush();
					gGrag.setMaxG(g1);
					gGrag.seteRx(eRx);
					gGrag.seteTx(eTx);
					gGrag.setApprFactor(apprFactor);
					gGrag.computeConcurrentFlow();
					pw.print(gGrag.getMaxG().getSourceList().get(0).getRate()+" ");
					pw.flush();
					WfMaxFlow gWf=new WfMaxFlow();
					Graph g2=new Graph();
					TestMaxFlow.initRandomData(fileName1, fileName2, g2, 1);
					gWf.setMaxG(g2);
					gWf.seteRx(eRx);
					gWf.seteTx(eTx);
					gWf.setApprFactor(apprFactor);
					gWf.computeDWFFLow();
					pw.println(gWf.getMaxG().getSourceList().get(0).getRate());
					pw.flush();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
}
