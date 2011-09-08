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
		return 0.003;
		
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
			   //v1.setBudgetEnergy(Double.parseDouble(b[5]));
			   v1.setBudgetEnergy(TestMaxFlow.getEdgeCapacity());
			   v1.setRate(0);
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
			   //double c1=Double.parseDouble(b[2]);
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
	
	
	
	public static void main(String[] args) throws SecurityException, IOException {
		
		int j=50;
		PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/performance-all.txt")));
		String fileName1="test/topology/vertex_"+j+".txt";
		String fileName2="test/topology/edge_"+j+".txt";
		Graph g1=new Graph();
		TestMaxFlow.initRandomData(fileName1, fileName2, g1, 1);
		TestMaxFlow.mainTaskConcurrent(g1, pw);

	}
}
