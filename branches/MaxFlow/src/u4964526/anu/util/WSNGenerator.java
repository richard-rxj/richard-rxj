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
import java.util.logging.Logger;

public class WSNGenerator {

	private int gLength=100;                             //m
	private int gWidth=100;                              //m
	private double gRadius=10;                           //m
	private double gInitialEnergy=50;                      //J
	private double gETx=0.0000144;                      //J/b
	private double gERx=0.00000576;                      //J/b
	private double[] gMaxRate={76800,57600,38400};                     //bps
	private double[] gWeight={1,0.7,0.5};
	private double[] gBudgetEnergy={0.01365,0.00653};
	private int gNodeNum=100;
	
	
	private int gDataNum=100;
	private int gDataGroup=10;
	private int gSelfStep=2;
	private int gSelfGroup=10;
	private int gNeigStep=1;
	private double minSeed=14;
	private double maxSeed=20;
	
	
	public int getgLength() {
		return gLength;
	}







	public void setgLength(int gLength) {
		this.gLength = gLength;
	}







	public int getgWidth() {
		return gWidth;
	}







	public void setgWidth(int gWidth) {
		this.gWidth = gWidth;
	}







	public double getgRadius() {
		return gRadius;
	}







	public void setgRadius(double gTransRange) {
		this.gRadius = gTransRange;
	}







	public double getgInitialEnergy() {
		return gInitialEnergy;
	}







	public void setgInitialEnergy(double gBaseNodeEnergy) {
		this.gInitialEnergy = gBaseNodeEnergy;
	}







	public double getgETx() {
		return gETx;
	}







	public void setgETx(double gFactorSend) {
		this.gETx = gFactorSend;
	}







	public double getgERx() {
		return gERx;
	}







	public void setgERx(double gFactorRecv) {
		this.gERx = gFactorRecv;
	}







	public double[] getgMaxRate() {
		return gMaxRate;
	}







	public void setgMaxRate(double[] gBaseMaxRate) {
		this.gMaxRate = gBaseMaxRate;
	}







	public double[] getgWeight() {
		return gWeight;
	}







	public void setgWeight(double[] gBaseWeight) {
		this.gWeight = gBaseWeight;
	}







	public int getgNodeNum() {
		return gNodeNum;
	}







	public void setgNodeNum(int gNodeNum) {
		this.gNodeNum = gNodeNum;
	}







	public double[] getgBudgetEnergy() {
		return gBudgetEnergy;
	}







	public void setgBudgetEnergy(double[] gBaseEdgeCapacity) {
		this.gBudgetEnergy = gBaseEdgeCapacity;
	}

	
	public int getgDataNum() {
		return gDataNum;
	}







	public void setgDataNum(int gDataNum) {
		this.gDataNum = gDataNum;
	}







	public int getgDataGroup() {
		return gDataGroup;
	}







	public void setgDataGroup(int gDataGroup) {
		this.gDataGroup = gDataGroup;
	}







	public int getgSelfStep() {
		return gSelfStep;
	}







	public void setgSelfStep(int gSelfStep) {
		this.gSelfStep = gSelfStep;
	}







	public int getgSelfGroup() {
		return gSelfGroup;
	}







	public void setgSelfGroup(int gSelfGroup) {
		this.gSelfGroup = gSelfGroup;
	}







	public int getgNeigStep() {
		return gNeigStep;
	}







	public void setgNeigStep(int gNeigStep) {
		this.gNeigStep = gNeigStep;
	}







	public double getMinSeed() {
		return minSeed;
	}







	public void setMinSeed(double minSeed) {
		this.minSeed = minSeed;
	}







	public double getMaxSeed() {
		return maxSeed;
	}







	public void setMaxSeed(double maxSeed) {
		this.maxSeed = maxSeed;
	}







	private double[] subData(double[] master)
	{
		double[] result=new double[this.gDataNum*this.gDataGroup];
		
		if(master==null)
		{
			double tSeed=this.minSeed+Math.random()*(this.maxSeed-this.minSeed);
			double tSeed0=tSeed;
			double tStep=this.gSelfStep;
			int tGroup=0;
			for(int i=0;i<result.length;i++)
			{
				tGroup++;
				if(tGroup>this.gDataNum)
				{
					tGroup=tGroup-this.gDataNum;
					tSeed=tSeed0;
				}
				
				if(tGroup%this.gSelfGroup==0)
				{
					tSeed=tSeed+tStep;
				}
				result[i]=tSeed+Math.random()*tStep;
			}
		}
		else
		{
			for(int i=0;i<result.length;i++)
			{
				result[i]=master[i]+(2*Math.random()-1)*this.gNeigStep;
			}
			
		}
		return result;
	}
	
    
	public Graph generateGraph(String f) throws FileNotFoundException
	{
		Graph g=new Graph();
		Random r=new Random();
		PrintWriter pwVertex=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/topology/vertex_"+f+".txt")));
		PrintWriter pwEdge=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/topology/edge_"+f+".txt")));
		//PrintWriter pwWFEdge=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/topology/wfedge_"+f+".txt")));
		
		int i=0;
		Vertex s=new Vertex(String.valueOf(0));
		s.setMaxRate(this.getgMaxRate()[r.nextInt(this.getgMaxRate().length)]);
		s.setWeight(this.getgWeight()[r.nextInt(this.getgWeight().length)]);
		s.setBudgetEnergy(this.gBudgetEnergy[1]+r.nextInt(10)*1.0/10*(this.gBudgetEnergy[0]-this.gBudgetEnergy[1]));
		s.setxLabel(50);
		s.setyLabel(50);
		g.addVertex(s);
		g.addSink(s);
		s.setWasSink(true);
		pwVertex.println(s+" "+s.getxLabel()+" "+s.getyLabel()+" "+s.getWeight()+" "+s.getMaxRate()+" "+s.getBudgetEnergy());
		pwVertex.flush();
		i++;	
		
		/*
		for(int m=0; m<=Math.floor(100.0/this.gRadius);m++)
		{
			for(int n=0;n<=Math.floor(100.0/this.gRadius);n++)
			{
				Vertex s2=new Vertex(String.valueOf(i+1));
				s2.setMaxRate(this.getgMaxRate()[r.nextInt(this.getgMaxRate().length)]);
				s2.setWeight(this.getgWeight()[r.nextInt(this.getgWeight().length)]);
				s2.setBudgetEnergy(this.gBudgetEnergy[1]+r.nextInt(10)*1.0/10*(this.gBudgetEnergy[0]-this.gBudgetEnergy[1]));
				s2.setxLabel(m*this.gRadius);
				s2.setyLabel(n*this.gRadius);
				g.addVertex(s2);
				g.addSource(s2);
				s2.setWasSource(true);
				pwVertex.println(s2+" "+s2.getxLabel()+" "+s2.getyLabel()+" "+s2.getWeight()+" "+s2.getMaxRate()+" "+s2.getBudgetEnergy());
				pwVertex.flush();
				i=i+1;
			}
		}
		*/
		
		double[][] gData=new double[this.gNodeNum][this.gDataGroup*this.gDataNum];
		for(int m=0;m<this.gNodeNum;m++)
		{
			for(int n=0;n<this.gDataGroup*this.gDataNum;n++)
			{
				gData[m][n]=-1;
			}
		}
		
		
		
		for(;i<this.getgNodeNum();i++)
		{
			Vertex v=new Vertex(String.valueOf(i));
			v.setMaxRate(this.getgMaxRate()[r.nextInt(this.getgMaxRate().length)]);
			v.setWeight(this.getgWeight()[r.nextInt(this.getgWeight().length)]);
			//v.setBudgetEnergy(this.getgBaseEdgeCapacity()[r.nextInt(this.getgBaseEdgeCapacity().length)]);
			v.setBudgetEnergy(this.gBudgetEnergy[1]+r.nextInt(10)*1.0/10*(this.gBudgetEnergy[0]-this.gBudgetEnergy[1]));
			v.setxLabel(this.getgWidth()*r.nextDouble());
			v.setyLabel(this.getgLength()*r.nextDouble());
			g.addVertex(v);
			g.addSource(v);
			v.setWasSource(true);
			pwVertex.println(v+" "+v.getxLabel()+" "+v.getyLabel()+" "+v.getWeight()+" "+v.getMaxRate()+" "+v.getBudgetEnergy());
			pwVertex.flush();
		}
		
		
		ArrayList<Vertex> vSet=g.getVertexList();
		Iterator<Vertex> slowI=vSet.iterator();
		Iterator<Vertex> fastI;
		while(slowI.hasNext())
		{
			Vertex v1=slowI.next();
			if(gData[v1.getVerValue()][0]==-1)
			{
				gData[v1.getVerValue()]=this.subData(null);
			}
				
			fastI=vSet.iterator();
			
			while(fastI.next()!=v1)
			{
				
			}
			
			Vertex v2;
			while(fastI.hasNext())
			{
				v2=fastI.next();
				double tRange=this.validTransRangePlusEdgeCapacity(v1, v2,this.gRadius);
				if(tRange>0)
				{
					//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
					Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
					g.addEdge(e1);
					pwEdge.println(v1+" "+v2+" "+e1.getCapacity());
					//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
					Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
					g.addEdge(e2);
					pwEdge.println(v2+" "+v1+" "+e2.getCapacity());
					pwEdge.flush();
					
					gData[v2.getVerValue()]=this.subData(gData[v1.getVerValue()]);
				}
				
				/*
				double tWFRange=this.validTransRangePlusEdgeCapacity(v1, v2,this.gRadius*v1.getBudgetEnergy()/this.gBudgetEnergy[1]);
				if(tWFRange>0)
				{
					//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
					Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
					g.addEdge(e1);
					pwWFEdge.println(v1+" "+v2+" "+e1.getCapacity());
					
				}
				
				tWFRange=this.validTransRangePlusEdgeCapacity(v1, v2,this.gRadius*v2.getBudgetEnergy()/this.gBudgetEnergy[1]);
				if(tWFRange>0)
				{
					//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
					Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
					g.addEdge(e2);
					pwWFEdge.println(v2+" "+v1+" "+e2.getCapacity());
					pwWFEdge.flush();
				}
				*/
				
				
			}
						
		}
		
		boolean w=true;
		double tRadius=this.gRadius;
		while(w)
		{
			tRadius=tRadius+1;
			w=false;
			g.getShortPathAndDSNode(g.getSinkList().get(0));
			for(int m=0;m<g.getSourceList().size();m++)
			{
				Vertex v1=g.getSourceList().get(m);
				if(!v1.isWasConnected())
				{
					
					v1.setWasConnected(true);
					w=true;
					slowI=vSet.iterator();
					while(slowI.hasNext())
					{
						Vertex v2=slowI.next();
						double tRange=this.validTransRangePlusEdgeCapacity(v1, v2,tRadius);
						if(tRange>0)
						{
							//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
							Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
							g.addEdge(e1);
							pwEdge.println(v1+" "+v2+" "+e1.getCapacity());
							//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
							Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
							g.addEdge(e2);
							pwEdge.println(v2+" "+v1+" "+e2.getCapacity());
							pwEdge.flush();
						}
					}
				}
			}
		}
		pwVertex.close();
		pwEdge.close();
		//pwWFEdge.close();
		
		
		
		for(int m=0;m<this.gNodeNum;m++)
		{
			for(int n=0;n<this.gDataNum*this.gDataGroup;n++)
			{
				PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/topology/data_"+f+"_"+n/this.gDataNum+".txt",true)));
				pw1.print(gData[m][n]+" ");
				if((n+1)%this.gDataNum==0)
				{
					pw1.println();
				}
				pw1.flush();
				pw1.close();
			}
			
			
		}
		

		
		
		
		return g;
	}
	
	private double validTransRangePlusEdgeCapacity(Vertex v1, Vertex v2, double tRadius)
	{
		double result=0;
		double c1=Math.pow((v1.getxLabel()-v2.getxLabel()),2);
		double c2=Math.pow((v1.getyLabel()-v2.getyLabel()),2);
		double o=Math.pow(tRadius,2);
		if(c1+c2<=o)
		{
			result=c1+c2;
		}
		
		return result;
	}
	
	
	public Graph generateSyntheticData(String fileNode, String fileData) throws FileNotFoundException
	{
		Graph g=new Graph();
		
		
		double[][] gData=new double[this.gNodeNum][this.gDataGroup*this.gDataNum];
		for(int m=0;m<this.gNodeNum;m++)
		{
			for(int n=0;n<this.gDataGroup*this.gDataNum;n++)
			{
				gData[m][n]=-1;
			}
		}
		
		
		Logger logger=Logger.getLogger("MaxFlow");
		String tempString=null;
		BufferedReader reader=null;
		
		/*
		 * begin of initial Vertices
		 */
		try
		{
		   reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileNode)));
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
			if(gData[v1.getVerValue()][0]==-1)
			{
				gData[v1.getVerValue()]=this.subData(null);
			}
			
			fastI=vSet.iterator();
			
			while(fastI.next()!=v1)
			{
				
			}
			
			Vertex v2;
			while(fastI.hasNext())
			{
				v2=fastI.next();
				double tRange=validTransRangePlusEdgeCapacity(v1, v2,this.gRadius);
				if(tRange>0)
				{
					//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
					Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
					g.addEdge(e1);
					
					//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
					Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
					g.addEdge(e2);
					
					gData[v2.getVerValue()]=this.subData(gData[v1.getVerValue()]);
				}
			}
			
		}
		
		
		
		
		
		
		
		for(int m=0;m<this.gNodeNum;m++)
		{
			for(int n=0;n<this.gDataNum*this.gDataGroup;n++)
			{
				PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileData+"_"+n/this.gDataNum+".txt",true)));
				pw1.print(gData[m][n]+" ");
				if((n+1)%this.gDataNum==0)
				{
					pw1.println();
				}
				pw1.flush();
				pw1.close();
			}
			
			
		}
		

		
		
		
		return g;
	}
	
	
	
	
	public static void main(String[] args)
	{
		try
		{
			 int[] gNodeSet={50,100,200,300,400,500};      //50,100,200,300,400,500,600,700,800,900,1000
			 double[] gTransSet={39,26,18.5,14.5,12.5,11,10,9,8.5,8,7.5};    //24,24,24,24,24,24
			 int[] gDataSumSet={100,100,100,100,100,100,100,100,100,100,100}; //100,100,100,100,100
			
			 for(int i=0;i<10;i++)
			 {
				 for(int gN=0;gN<gNodeSet.length;gN++)
			    	{
			    		int gNode=gNodeSet[gN];
			    		double gTrans=gTransSet[gN];
			    		int gDataSum=gDataSumSet[gN];
			    		
			    		String fileNode="test/topologySynthetic/node-"+gNode+"-"+i+".txt";
			    		String fileData="test/topologySynthetic/data_"+gNode+"_"+i;
			    		
			    		WSNGenerator tGene=new WSNGenerator();
			    		tGene.setgDataNum(gDataSum);
			    		tGene.setgNodeNum(gNode);
			    		tGene.setgRadius(gTrans);
			    		
			    		tGene.generateSyntheticData(fileNode, fileData);
			    	}
			 }
			 
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
