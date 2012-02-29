package network.dr.alg.anu.au;

import java.util.ArrayList;
import java.util.Arrays;

import dr.alg.anu.au.TourDesign;

public class GateWay implements Comparable<GateWay> {

	private int id;
	private int name; 
	private double X=0;
	private double Y=0;
	private double lBenefit = 0;
	private double fBenefit = 0;
	private ArrayList<Node> neighborNodes;
	private ArrayList<Double> eConSet;
	private ArrayList<Node> lActiveNodes;
	private ArrayList<Node> fActiveNodes;
	private double lSojournTime =0;
	private double fSojournTime =0;
	private double tMovingTime=0;
	
	
	public GateWay(int id) {
		this.id = id;
		this.name = id;
		this.neighborNodes =new ArrayList<Node>();
		this.lActiveNodes =new ArrayList<Node>();
		this.fActiveNodes =new ArrayList<Node>();
		this.eConSet =new ArrayList<Double>(); 
	}
	
	
	public GateWay(GateWay g) {             //copy and create a new gateway
		this.id = g.id;
		this.name = g.name;
		this.X = g.X;
		this.Y = g.Y;
		this.lBenefit =g.lBenefit;
		this.fBenefit =g.fBenefit;
		this.lSojournTime=g.lSojournTime;
		this.fSojournTime=g.fSojournTime;
		this.tMovingTime=g.tMovingTime;
		this.neighborNodes =new ArrayList<Node>();
		this.lActiveNodes =new ArrayList<Node>();
		this.fActiveNodes =new ArrayList<Node>();
		this.eConSet =new ArrayList<Double>();
		for(int i=0; i<g.neighborNodes.size();i++)
		{
			this.neighborNodes.add(g.neighborNodes.get(i));
		}
		for(int i=0; i<g.lActiveNodes.size();i++)
		{
			this.lActiveNodes.add(g.lActiveNodes.get(i));
		}
		for(int i=0; i<g.fActiveNodes.size();i++)
		{
			this.fActiveNodes.add(g.fActiveNodes.get(i));
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
	
	
	
	public int addLActiveNode(Node v)
	{
		this.lActiveNodes.add(v);
		return 1;
	}
	
	public int addFActiveNode(Node v)
	{
		this.fActiveNodes.add(v);
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

	public double getlBenefit() {
		return lBenefit;
	}

	public void setlBenefit(double lBenefit) {
		this.lBenefit = lBenefit;
	}

	public double getfBenefit() {
		return fBenefit;
	}

	public void setfBenefit(double fBenefit) {
		this.fBenefit = fBenefit;
	}

	public ArrayList<Node> getNeighborNodes() {
		return neighborNodes;
	}





	public ArrayList<Double> geteConSet() {
		return eConSet;
	}


	public double getlSojournTime() {
		return lSojournTime;
	}


	public void setlSojournTime(double lSojournTime) {
		this.lSojournTime = lSojournTime;
	}


	public double getfSojournTime() {
		return fSojournTime;
	}


	public void setfSojournTime(double fSojournTime) {
		this.fSojournTime = fSojournTime;
	}


	public double gettMovingTime() {
		return tMovingTime;
	}


	public void settMovingTime(double tMovingTime) {
		this.tMovingTime = tMovingTime;
	}


	public ArrayList<Node> getlActiveNodes() {
		return lActiveNodes;
	}


	public ArrayList<Node> getfActiveNodes() {
		return fActiveNodes;
	}

	
	public double calcLBenefit(double lossPSec,double timeLimit)                   //linear policy
	{
		
		
		
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
		double tTimeLimit=timeLimit-this.tMovingTime;
        if(tTimeLimit<=0)
        {
        	this.lBenefit=Double.NEGATIVE_INFINITY;
        	return this.lBenefit;
        }
		
		
		for (int i=0;i<this.neighborNodes.size();i++)
		{
			this.neighborNodes.get(i).calcUploadTime(this.tMovingTime,this.eConSet.get(i));
		}
		Object[] nSet=this.neighborNodes.toArray();
		NodeUploadTimeComparator nCom=new NodeUploadTimeComparator(false);
		Arrays.sort(nSet, nCom);
		this.lActiveNodes.clear();
		
		for(int i=0;i<nSet.length;i++)
		{
			Node t=(Node)nSet[i];
			this.lActiveNodes.add(t);
			double tSojournTime=t.getUploadTime();
			if(tSojournTime>tTimeLimit)
			{
				tSojournTime=tTimeLimit;
			}
			
			
			double tBenefit=0;
			for(int j=0;j<this.lActiveNodes.size();j++)
			{
				tBenefit=tBenefit+this.lActiveNodes.get(i).calcLBenefit(tSojournTime);
			}
			this.lBenefit=tBenefit-tSojournTime*lossPSec;
			this.lSojournTime=tSojournTime;
			
			GateWay tGateWay=new GateWay(this);
			resultGateWays.add(tGateWay);			
		}
		
		Object[] gSet=resultGateWays.toArray();
		GateWayLBenefitComparator gCom=new GateWayLBenefitComparator(false);
		Arrays.sort(gSet,gCom);
		GateWay result=(GateWay)gSet[0];
		
		
		//update the chosen result
		this.lBenefit=result.lBenefit;
		this.lSojournTime=result.lSojournTime;
		this.lActiveNodes.clear();
		for(int i=0;i<result.lActiveNodes.size();i++)
		{
			this.lActiveNodes.add(result.lActiveNodes.get(i));
		}
		
		return result.lBenefit;
	}
	
	
	
	public double calcFBenefit(double lossPSec,double timeLimit)                   //fairness policy
	{
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
        double tTimeLimit=timeLimit-this.tMovingTime;
        if(tTimeLimit<=0)
        {
        	this.fBenefit=Double.NEGATIVE_INFINITY;
        	return this.fBenefit;
        }
		
		for (int i=0;i<this.neighborNodes.size();i++)
		{
			this.neighborNodes.get(i).calcUploadTime(this.tMovingTime,this.eConSet.get(i));
		}
		Object[] nSet=this.neighborNodes.toArray();
		NodeUploadTimeComparator nCom=new NodeUploadTimeComparator(false);
		Arrays.sort(nSet, nCom);
		this.fActiveNodes.clear();
		
		for(int i=0;i<nSet.length;i++)
		{
			Node t=(Node)nSet[i];
			this.fActiveNodes.add(t);
			double tSojournTime=t.getUploadTime();
			
			if(tSojournTime>tTimeLimit)
			{
				tSojournTime=tTimeLimit;
			}
			
			double tBenefit=0;
			for(int j=0;j<this.fActiveNodes.size();j++)
			{
				tBenefit=tBenefit+this.fActiveNodes.get(i).calcFBenefit(tSojournTime);
			}
			this.fBenefit=tBenefit-tSojournTime*lossPSec;
			this.fSojournTime=tSojournTime;
			
			GateWay tGateWay=new GateWay(this);
			resultGateWays.add(tGateWay);			
		}
		
		Object[] gSet=resultGateWays.toArray();
		GateWayFBenefitComparator gCom=new GateWayFBenefitComparator(false);
		Arrays.sort(gSet,gCom);
		GateWay result=(GateWay)gSet[0];
		
		
		//update the chosen result
		this.fBenefit=result.fBenefit;
		this.fSojournTime=result.fSojournTime;
		this.fActiveNodes.clear();
		for(int i=0;i<result.fActiveNodes.size();i++)
		{
			this.fActiveNodes.add(result.fActiveNodes.get(i));
		}
		
		return result.fBenefit;
	}
	
	
	

	public String toString() {
		return "gateway " + id;
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
