package u4964526.anu.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class WSNGenerator {

	private int gLength=100;                             //m
	private int gWidth=100;                              //m
	private double gRadius=10;                           //m
	private double gInitialEnergy=50;                      //J
	private double gETx=0.0000144;                      //J/b
	private double gERx=0.00000576;                      //J/b
	private double[] gMaxRate={100000,75000,50000};                     //bps
	private double[] gWeight={1,0.7,0.5};
	private double[] gBudgetEnergy={0.01365,0.00653};
	private int gNodeNum=100;
	
	
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

    
	public Graph generateGraph(String f) throws FileNotFoundException
	{
		Graph g=new Graph();
		Random r=new Random();
		PrintWriter pwVertex=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/topology/vertex_"+f+".txt")));
		PrintWriter pwEdge=new PrintWriter(new OutputStreamWriter(new FileOutputStream("test/topology/edge_"+f+".txt")));
		int i=0;
		
		Vertex s=new Vertex(String.valueOf(1));
		s.setMaxRate(this.getgMaxRate()[r.nextInt(this.getgMaxRate().length)]);
		s.setWeight(this.getgWeight()[r.nextInt(this.getgWeight().length)]);
		s.setBudgetEnergy(this.gBudgetEnergy[1]+r.nextDouble()*(this.gBudgetEnergy[0]-this.gBudgetEnergy[1]));
		s.setxLabel(1);
		s.setyLabel(1);
		g.addVertex(s);
		g.addSink(s);
		s.setWasSink(true);
		pwVertex.println(s+" "+s.getxLabel()+" "+s.getyLabel()+" "+s.getWeight()+" "+s.getMaxRate()+" "+s.getBudgetEnergy());
		pwVertex.flush();
		i=i+1;	
		
		Vertex s1=new Vertex(String.valueOf(2));
		s1.setMaxRate(this.getgMaxRate()[r.nextInt(this.getgMaxRate().length)]);
		s1.setWeight(this.getgWeight()[r.nextInt(this.getgWeight().length)]);
		s1.setBudgetEnergy(this.gBudgetEnergy[1]+r.nextDouble()*(this.gBudgetEnergy[0]-this.gBudgetEnergy[1]));
		s1.setxLabel(5);
		s1.setyLabel(5);
		g.addVertex(s1);
		g.addSource(s1);
		s1.setWasSource(true);
		pwVertex.println(s1+" "+s1.getxLabel()+" "+s1.getyLabel()+" "+s1.getWeight()+" "+s1.getMaxRate()+" "+s1.getBudgetEnergy());
		pwVertex.flush();
		i=i+1;
		
		for(int m=0; m<=Math.floor(100.0/this.gRadius);m++)
		{
			for(int n=0;n<=6;n++)
			{
				Vertex s2=new Vertex(String.valueOf(i+1));
				s2.setMaxRate(this.getgMaxRate()[r.nextInt(this.getgMaxRate().length)]);
				s2.setWeight(this.getgWeight()[r.nextInt(this.getgWeight().length)]);
				s2.setBudgetEnergy(this.gBudgetEnergy[1]+r.nextDouble()*(this.gBudgetEnergy[0]-this.gBudgetEnergy[1]));
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
		
		
		
		
		for(;i<this.getgNodeNum();i++)
		{
			Vertex v=new Vertex(String.valueOf(i+1));
			v.setMaxRate(this.getgMaxRate()[r.nextInt(this.getgMaxRate().length)]);
			v.setWeight(this.getgWeight()[r.nextInt(this.getgWeight().length)]);
			//v.setBudgetEnergy(this.getgBaseEdgeCapacity()[r.nextInt(this.getgBaseEdgeCapacity().length)]);
			v.setBudgetEnergy(this.gBudgetEnergy[1]+r.nextDouble()*(this.gBudgetEnergy[0]-this.gBudgetEnergy[1]));
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
			fastI=vSet.iterator();
			
			while(fastI.next()!=v1)
			{
				
			}
			
			Vertex v2;
			while(fastI.hasNext())
			{
				v2=fastI.next();
				double tRange=this.validTransRangePlusEdgeCapacity(v1, v2);
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
		
		return g;
	}
	
	private double validTransRangePlusEdgeCapacity(Vertex v1, Vertex v2)
	{
		double result=0;
		double c1=Math.pow((v1.getxLabel()-v2.getxLabel()),2);
		double c2=Math.pow((v1.getyLabel()-v2.getyLabel()),2);
		double o=Math.pow(this.getgRadius(),2);
		if(c1+c2<=o)
		{
			result=c1+c2;
		}
		
		return result;
	}
	
	public static void main(String[] args)
	{
		try
		{
			//int[] tNodeSet={50,60,70,80,90,100,110,120,130,140,150,200,250,300};
			int[] tNodeSet={100,150,200,250,300,350,400,450,500};
			double[] tRadiusSet={15,13,10.5,10,9,8.5,8,7.5,7};
			//int[] tXSet={100,100,100};
			//int[] tYSet={100,100,100};
			//int[] tRangeSet={25,25,25};
			//Random r=new Random();
			
			for(int j=0;j<20;j++)
			{
				for(int i=0;i<tNodeSet.length;i++)
				{
					WSNGenerator tGenerator=new WSNGenerator();
					tGenerator.setgNodeNum(tNodeSet[i]);
					tGenerator.setgRadius(tRadiusSet[i]);
					tGenerator.generateGraph(String.valueOf(tNodeSet[i])+"_"+j);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
