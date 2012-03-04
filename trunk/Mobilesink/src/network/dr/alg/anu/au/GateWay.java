package network.dr.alg.anu.au;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import dr.alg.anu.au.TourDesign;

public class GateWay implements Comparable<GateWay> {

	private int id;
	private int name; 
	private double X=0;
	private double Y=0;
	private double benefit = 0;
	private double utility=0;
	private double throughput=0;
	private ArrayList<Node> neighborNodes;
	private ArrayList<Double> eConSet;
	private ArrayList<Node> activeNodes;
	private double sojournTime =0;
	private double movingTime=0;
	
	
	public GateWay(int id) {
		this.id = id;
		this.name = id;
		this.neighborNodes =new ArrayList<Node>();
		this.activeNodes =new ArrayList<Node>();
		this.eConSet =new ArrayList<Double>(); 
	}
	
	
	public GateWay(GateWay g) {             //copy and create a new gateway
		this.id = g.id;
		this.name = g.name;
		this.X = g.X;
		this.Y = g.Y;
		this.benefit = g.benefit;
		this.utility = g.utility;
		this.throughput = g.throughput;
		this.sojournTime=g.sojournTime;
		this.movingTime=g.movingTime;
		this.neighborNodes =new ArrayList<Node>();
		this.activeNodes =new ArrayList<Node>();
		this.eConSet =new ArrayList<Double>();
		for(int i=0; i<g.neighborNodes.size();i++)
		{
			this.neighborNodes.add(g.neighborNodes.get(i));
		}
		for(int i=0; i<g.activeNodes.size();i++)
		{
			this.activeNodes.add(g.activeNodes.get(i));
		}

		for(int i=0; i<g.eConSet.size();i++)
		{
			this.eConSet.add(g.eConSet.get(i));
		}
		
	}
	
	
	public int addNeighborNode(Node v)
	{
		this.neighborNodes.add(v);
		double eTemp =TourDesign.beta*Math.pow(Math.sqrt(Math.pow((v.getX()-this.X), 2)+Math.pow((v.getY()-this.Y), 2)),3);
		this.eConSet.add(Double.valueOf(eTemp));
		
		return 1;
	}
	
	
	
	public int addActiveNode(Node v)
	{
		this.activeNodes.add(v);
		return 1;
	}
	

	
	@Override
	public int compareTo(GateWay arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public double getBenefit() {
		return benefit;
	}

	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}

	

	public double getUtility() {
		return utility;
	}


	public void setUtility(double utility) {
		this.utility = utility;
	}


	public double getThroughput() {
		return throughput;
	}


	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}


	public ArrayList<Node> getNeighborNodes() {
		return neighborNodes;
	}





	public ArrayList<Double> geteConSet() {
		return eConSet;
	}


	public double getSojournTime() {
		return sojournTime;
	}


	public void setSojournTime(double sojournTime) {
		this.sojournTime = sojournTime;
	}





	public double getMovingTime() {
		return movingTime;
	}


	public void setMovingTime(double movingTime) {
		this.movingTime = movingTime;
	}


	public ArrayList<Node> getActiveNodes() {
		return activeNodes;
	}




	
	public double calcLBenefit(double lossPSec,double timeLimit)                   //linear policy
	{
		
		
		
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
		double tTimeLimit=timeLimit-this.movingTime;
        if(tTimeLimit<=0)
        {
        	this.benefit=Double.NEGATIVE_INFINITY;
        	this.utility=0;
        	this.throughput=0;
        	this.sojournTime=0;
        	this.activeNodes.clear();
        	return this.benefit;
        }
		
		
		for (int i=0;i<this.neighborNodes.size();i++)
		{
			this.neighborNodes.get(i).calcUploadTime(this.movingTime,this.eConSet.get(i));
		}
		Object[] nSet=this.neighborNodes.toArray();
		NodeUploadTimeComparator nCom=new NodeUploadTimeComparator(false);
		Arrays.sort(nSet, nCom);
		this.activeNodes.clear();
		
		for(int i=0;i<nSet.length;i++)
		{
			Node t=(Node)nSet[i];
			this.activeNodes.add(t);
			double tSojournTime=t.getUploadTime();
			if(tSojournTime>tTimeLimit)
			{
				tSojournTime=tTimeLimit;
			}
			
			
			double tBenefit=0;
			double tThroughput=0;
			for(int j=0;j<this.activeNodes.size();j++)
			{
				tBenefit=tBenefit+this.activeNodes.get(i).calcLBenefit(tSojournTime);
				tThroughput= tThroughput+this.activeNodes.get(i).gettRate()*tSojournTime;
			}
			this.utility=tBenefit;
			this.throughput=tThroughput;
			this.benefit=tBenefit-this.movingTime*lossPSec;
			this.sojournTime=tSojournTime;
			if(tSojournTime<=TourDesign.minSojournTime)
			{
				this.benefit=Double.NEGATIVE_INFINITY;
			}
			
			
			GateWay tGateWay=new GateWay(this);
			resultGateWays.add(tGateWay);			
		}
		
		Object[] gSet=resultGateWays.toArray();
		GateWayLBenefitComparator gCom=new GateWayLBenefitComparator(false);
		Arrays.sort(gSet,gCom);
		
		if(gSet.length>0)
		{
			GateWay result=(GateWay)gSet[0];
		
		
			//update the chosen result
			this.benefit=result.benefit;
			this.utility=result.utility;
			this.throughput=result.throughput;
			this.sojournTime=result.sojournTime;
			this.activeNodes.clear();
			for(int i=0;i<result.activeNodes.size();i++)
			{
				this.activeNodes.add(result.activeNodes.get(i));
			}
		}
		else
		{
			this.benefit=Double.NEGATIVE_INFINITY;
			this.utility=0;
			this.throughput=0;
			this.sojournTime=0;
			this.activeNodes.clear();
		}
		
		
		return this.benefit;
	}
	
	
	
	public double calcFBenefit(double lossPSec,double timeLimit)                   //fairness policy
	{
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
        double tTimeLimit=timeLimit-this.movingTime;
        if(tTimeLimit<=0)
        {
        	
        	this.benefit=Double.NEGATIVE_INFINITY;
        	this.utility=0;
        	this.throughput=0;
        	this.sojournTime=0;
        	this.activeNodes.clear();
        	return this.benefit;
        }
		
		for (int i=0;i<this.neighborNodes.size();i++)
		{
			this.neighborNodes.get(i).calcUploadTime(this.movingTime,this.eConSet.get(i));
		}
		Object[] nSet=this.neighborNodes.toArray();
		NodeUploadTimeComparator nCom=new NodeUploadTimeComparator(false);
		Arrays.sort(nSet, nCom);
		this.activeNodes.clear();
		
		for(int i=0;i<nSet.length;i++)
		{
			Node t=(Node)nSet[i];
			this.activeNodes.add(t);
			double tSojournTime=t.getUploadTime();
			
			if(tSojournTime>tTimeLimit)
			{
				tSojournTime=tTimeLimit;
			}
			
			double tBenefit=0;
			double tThroughput=0;
			for(int j=0;j<this.activeNodes.size();j++)
			{
				tBenefit=tBenefit+this.activeNodes.get(i).calcFBenefit(tSojournTime);
				tThroughput=tThroughput+this.activeNodes.get(i).gettRate()*tSojournTime;
			}
			this.utility=tBenefit;
			this.throughput=tThroughput;
			this.benefit=tBenefit-tSojournTime*lossPSec;
			this.sojournTime=tSojournTime;
			if(tSojournTime<=TourDesign.minSojournTime)
			{
				this.benefit=Double.NEGATIVE_INFINITY;
			}
			
			
			GateWay tGateWay=new GateWay(this);
			resultGateWays.add(tGateWay);			
		}
		
		Object[] gSet=resultGateWays.toArray();
		GateWayFBenefitComparator gCom=new GateWayFBenefitComparator(false);
		Arrays.sort(gSet,gCom);
		
		if(gSet.length>0)
		{
			GateWay result=(GateWay)gSet[0];
		
		
			//update the chosen result
			this.benefit=result.benefit;
			this.utility=result.utility;
			this.throughput=result.throughput;
			this.sojournTime=result.sojournTime;
			this.activeNodes.clear();
			for(int i=0;i<result.activeNodes.size();i++)
			{
				this.activeNodes.add(result.activeNodes.get(i));
			}
		}
		else
		{
			this.benefit=Double.NEGATIVE_INFINITY;
			this.utility=0;
			this.throughput=0;
			this.sojournTime=0;
			this.activeNodes.clear();
		}
		
		
		return this.benefit;
		
		
	}
	
	
	

	public String toString() {
		DecimalFormat df=new DecimalFormat("#.0000");
		
		return "gateway " + id+"-(m)"+df.format(this.movingTime)+"-(s)"+df.format(this.sojournTime)+"-(active)"+this.activeNodes.toString()+"-(neighbor)"+this.neighborNodes.toString();
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
