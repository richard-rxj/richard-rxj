package network.dr.alg.anu.au;

import generate.dr.alg.anu.au.NetworkGenerator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import dr.alg.anu.au.ExperimentSetting;


public class GateWay implements Comparable<GateWay> {

	private int id;
	private int name; 
	private double X=0;
	private double Y=0;
	private double utility=0;
	private ArrayList<Node> neighborNodes;
	private ArrayList<Double> eConSet;
	private ArrayList<Node> activeNodes;
	private double sojournTime =0;
	private double movingTime=0;
	private double backTime=0;
	private double utilityGain=0;     //new
	private double unitUtilityGain=0; //new
	private int    feasible=0;        // 0-non feasible 1-feasible
	private double timeStamp=0;
	private double priorityWeight=0;
	private double distance=0;
	
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
		this.utility = g.utility;
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
		this.backTime=g.backTime;
		this.unitUtilityGain=g.unitUtilityGain;
		this.utilityGain=g.utilityGain;
	}
	
	
	public int addNeighborNode(Node v)
	{
		this.neighborNodes.add(v);
		double eTemp =ExperimentSetting.beta*Math.pow(Math.sqrt(Math.pow((v.getX()-this.X), 2)+Math.pow((v.getY()-this.Y), 2)),ExperimentSetting.eComM);  //mΪ2
		if (eTemp*v.gettRate()<ExperimentSetting.minEConsumption)
			eTemp=ExperimentSetting.minEConsumption/v.gettRate();
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


	

	public double getUtility() {
		return utility;
	}


	public void setUtility(double utility) {
		this.utility = utility;
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


	public double getBackTime() {
		return backTime;
	}


	public void setBackTime(double backTime) {
		this.backTime = backTime;
	}




	public double getUtilityGain() {
		return utilityGain;
	}


	public void setUtilityGain(double utilityGain) {
		this.utilityGain = utilityGain;
	}


	public double getUnitUtilityGain() {
		return unitUtilityGain;
	}


	public void setUnitUtilityGain(double unitUtilityGain) {
		this.unitUtilityGain = unitUtilityGain;
	}


	public double getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(double lastVisitTime) {
		this.timeStamp = lastVisitTime;
	}


	public double getPriorityWeight() {
		return priorityWeight;
	}


	public void setPriorityWeight(double priorityWeight) {
		this.priorityWeight = priorityWeight;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}



	public int getFeasible() {
		return feasible;
	}


	public void setFeasible(int feasible) {
		this.feasible = feasible;
	}


	public ArrayList<Node> getActiveNodes() {
		return activeNodes;
	}


	
	public double calcUtilityGain(double lastBackTime, double timeLimit, boolean lastLocation)                   
	//max utility  no time cost
	{
		
		
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
		double tTimeLimit=timeLimit-this.movingTime-this.backTime;     //back to origin spot
        if(tTimeLimit<=0)
        {
        	this.feasible=0;
        	this.utilityGain=Double.NEGATIVE_INFINITY;
        	this.unitUtilityGain=Double.NEGATIVE_INFINITY;
        	this.sojournTime=0;
        	this.activeNodes.clear();
        	return this.utilityGain;
        }
		
        for (int i=0;i<this.neighborNodes.size();i++)
		{
			this.neighborNodes.get(i).calcUploadTime(this.movingTime,this.eConSet.get(i));
		}
		Object[] nSet=this.neighborNodes.toArray();
		NodeUploadTimeComparator nCom=new NodeUploadTimeComparator(false);
		Arrays.sort(nSet, nCom);
		this.activeNodes.clear();
        
        double tSojournTime=((Node)nSet[0]).getUploadTime();
        if(lastLocation)
        {
        	if(tSojournTime>tTimeLimit)
        		tSojournTime=tTimeLimit;
        		
        	double tUtilityGain=0;
			for(int j=0;j<this.neighborNodes.size();j++)
			{
				tUtilityGain= tUtilityGain+this.neighborNodes.get(j).calcUtilityGain(tSojournTime);
			}
			
			
			this.utilityGain=tUtilityGain;
			this.sojournTime=tSojournTime;
			this.unitUtilityGain=this.utilityGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
        }
        
         else
        {   
        	
        	double tUtilityGain=0;
 			for(int j=0;j<this.neighborNodes.size();j++)
 			{
 				tUtilityGain= tUtilityGain+this.neighborNodes.get(j).calcUtilityGain(tSojournTime);
 			}
 			
 			
 			this.utilityGain=tUtilityGain;
 			this.sojournTime=tSojournTime;
 			this.unitUtilityGain=this.utilityGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
        	 
        	 
        	 if(this.sojournTime>tTimeLimit)
				{
					this.feasible=0;
				}
				else
				{
					if(this.sojournTime<ExperimentSetting.minSojournTime)
					{
						this.feasible=0;
					}
					else
					{
						this.feasible=1;
					}
				}
        	 
        	 
        	 
        }
        
        
        
		return this.utilityGain;
	}
	

	
	public double calcUnitUtilityGain(double lastBackTime, double timeLimit, boolean lastLocation)                   //new
	{
		
		
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
		double tTimeLimit=timeLimit-this.movingTime-this.backTime;     //back to origin spot
        if(tTimeLimit<=0)
        {
        	this.feasible=0;
        	this.utilityGain=Double.NEGATIVE_INFINITY;
        	this.unitUtilityGain=Double.NEGATIVE_INFINITY;
        	this.sojournTime=0;
        	this.activeNodes.clear();
        	return this.utilityGain;
        }
		
        if(lastLocation)
        {  //use Uploadtime for GLOBECOM2013
        		
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
				//this.activeNodes.add(t);
				double tSojournTime=t.getUploadTime();
				if(tSojournTime>tTimeLimit)
				{
					tSojournTime=tTimeLimit;
				}
				
				
				double tUtilityGain=0;
				double tBenefitGain=0;
				for(int j=0;j<this.neighborNodes.size();j++)
				{
					tUtilityGain= tUtilityGain+this.neighborNodes.get(j).calcUtilityGain(tSojournTime);
					//tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.utilityGain=tUtilityGain;
				//this.benefitGain=tBenefitGain;
				this.sojournTime=tSojournTime;
				this.unitUtilityGain=this.utilityGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
	//			if(tSojournTime<=TourDesign.minSojournTime)
	//			{
	//				this.benefit=Double.NEGATIVE_INFINITY;
	//			}
				
				
				GateWay tGateWay=new GateWay(this);
				resultGateWays.add(tGateWay);			
			}
			
			Object[] gSet=resultGateWays.toArray();
			GateWayUnitUtilityGainComparator gCom=new GateWayUnitUtilityGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.utilityGain=result.utilityGain;
				this.unitUtilityGain=result.unitUtilityGain;
				this.sojournTime=result.sojournTime;
//				this.activeNodes.clear();
//				for(int i=0;i<result.activeNodes.size();i++)
//				{
//					this.activeNodes.add(result.activeNodes.get(i));
//				}
			
				//update the chosen result
				if(this.sojournTime<ExperimentSetting.minSojournTime)
				{
					this.feasible=0;
				}
				else
				{
					this.feasible=1;
				}
				
				
				
			}
			else
			{
				this.feasible=0;
	        	this.utilityGain=0;
	        	this.unitUtilityGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        else
        {   //use Uploadtime for GLOBECOM2013
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
//				this.activeNodes.add(t);
				double tSojournTime=t.getUploadTime();
//				if(tSojournTime>tTimeLimit)
//				{
//					tSojournTime=tTimeLimit;
//				}
				
				
				double tUtilityGain=0;
				double tBenefitGain=0;
				for(int j=0;j<this.neighborNodes.size();j++)
				{
					tUtilityGain= tUtilityGain+this.neighborNodes.get(j).calcUtilityGain(tSojournTime);
					//tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.utilityGain=tUtilityGain;
				//this.benefitGain=tBenefitGain;
				this.sojournTime=tSojournTime;
				this.unitUtilityGain=this.utilityGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
	//			if(tSojournTime<=TourDesign.minSojournTime)
	//			{
	//				this.benefit=Double.NEGATIVE_INFINITY;
	//			}
				
				
				GateWay tGateWay=new GateWay(this);
				resultGateWays.add(tGateWay);			
			}
			
			Object[] gSet=resultGateWays.toArray();
			GateWayUnitUtilityGainComparator gCom=new GateWayUnitUtilityGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.utilityGain=result.utilityGain;
				this.unitUtilityGain=result.unitUtilityGain;
				this.sojournTime=result.sojournTime;
//				this.activeNodes.clear();
//				for(int i=0;i<result.activeNodes.size();i++)
//				{
//					this.activeNodes.add(result.activeNodes.get(i));
//				}
			
				//update the chosen result
				if(this.sojournTime>tTimeLimit)
				{
					this.feasible=0;
				}
				else
				{
					if(this.sojournTime<ExperimentSetting.minSojournTime)
					{
						this.feasible=0;
					}
					else
					{
						this.feasible=1;
					}
				}
				
				
				
				
				
			}
			else
			{
				this.feasible=0;
	        	this.utilityGain=0;
	        	this.unitUtilityGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        
        
        
		return this.utilityGain;
	}
	
	
	/*
	 * used for distributed
	 */
	
	
	public void calcTraPriorityWeight(double tUsedTime)
	{

		
		this.priorityWeight=this.movingTime+this.backTime;
	}
	
	
	
	

	public String toString() {
		DecimalFormat df=new DecimalFormat("#.0000");
		
		return "gateway " + id+"-(m)"+df.format(this.movingTime)+"-(s)"+df.format(this.sojournTime)+"-(t)"+this.timeStamp+"-(neighbor)"+this.neighborNodes.toString();
	}
	
	
	
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("test");
	}

}
