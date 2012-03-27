package network.dr.alg.anu.au;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import dr.alg.anu.au.ExperimentSetting;
import dr.alg.anu.au.NewTourDesign;
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
	private double backTime=0;
	private double benefitGain=0;     //new
	private double unitBenefitGain=0; //new
	private double utilityGain=0;     //new
	private double unitUtilityGain=0; //new
	private int    feasible=0;        // 0-non feasible 1-feasible
	private double timeStamp=0;
	private double priorityWeight=0;
	private double distance=0;
	private double similarity=0;
	
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
		this.backTime=g.backTime;
		this.benefitGain=g.benefitGain;
		this.unitBenefitGain=g.unitBenefitGain;
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


	public double getBackTime() {
		return backTime;
	}


	public void setBackTime(double backTime) {
		this.backTime = backTime;
	}


	public double getBenefitGain() {
		return benefitGain;
	}


	public void setBenefitGain(double benefitGain) {
		this.benefitGain = benefitGain;
	}


	public double getUnitBenefitGain() {
		return unitBenefitGain;
	}


	public void setUnitBenefitGain(double unitBenefitGain) {
		this.unitBenefitGain = unitBenefitGain;
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


	public double getSimilarity() {
		return similarity;
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
			if(tSojournTime<=ExperimentSetting.minSojournTime)
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
			if(tSojournTime<=ExperimentSetting.minSojournTime)
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
	
	
	public double calcBenefitGain(double lastBackTime, double timeLimit, boolean lastLocation)                   //new
	{
		
		
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
		double tTimeLimit=timeLimit-this.movingTime-this.backTime;     //back to origin spot
        if(tTimeLimit<=0)
        {
        	this.feasible=0;
        	this.benefitGain=Double.NEGATIVE_INFINITY;
        	this.unitBenefitGain=Double.NEGATIVE_INFINITY;
        	this.sojournTime=0;
        	this.activeNodes.clear();
        	return this.benefitGain;
        }
		
        if(lastLocation)
        {
        		
			for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
				if(tSojournTime>tTimeLimit)
				{
					tSojournTime=tTimeLimit;
				}
				
				
				double tBenefitGain=0;
				double tUnitBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.benefitGain=tBenefitGain;
				this.sojournTime=tSojournTime;
				this.unitBenefitGain=this.benefitGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
	//			if(tSojournTime<=TourDesign.minSojournTime)
	//			{
	//				this.benefit=Double.NEGATIVE_INFINITY;
	//			}
				
				
				GateWay tGateWay=new GateWay(this);
				resultGateWays.add(tGateWay);			
			}
			
			Object[] gSet=resultGateWays.toArray();
			GateWayBenefitGainComparator gCom=new GateWayBenefitGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.benefitGain=result.benefitGain;
				this.unitBenefitGain=result.unitBenefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.benefitGain=Double.NEGATIVE_INFINITY;
	        	this.unitBenefitGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        else
        {
        	for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
//				if(tSojournTime>tTimeLimit)
//				{
//					tSojournTime=tTimeLimit;
//				}
				
				
				double tBenefitGain=0;
				double tUnitBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.benefitGain=tBenefitGain;
				this.sojournTime=tSojournTime;
				this.unitBenefitGain=this.benefitGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
	//			if(tSojournTime<=TourDesign.minSojournTime)
	//			{
	//				this.benefit=Double.NEGATIVE_INFINITY;
	//			}
				
				
				GateWay tGateWay=new GateWay(this);
				resultGateWays.add(tGateWay);			
			}
			
			Object[] gSet=resultGateWays.toArray();
			GateWayBenefitGainComparator gCom=new GateWayBenefitGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.benefitGain=result.benefitGain;
				this.unitBenefitGain=result.unitBenefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.benefitGain=Double.NEGATIVE_INFINITY;
	        	this.unitBenefitGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        
        
        
		return this.benefitGain;
	}
	
	
	
	public double calcUnitBenefitGain(double lastBackTime, double timeLimit, boolean lastLocation)                   //new
	{
		
		
		
		ArrayList<GateWay> resultGateWays =new ArrayList<GateWay>();
		double tTimeLimit=timeLimit-this.movingTime-this.backTime;     //back to origin spot
        if(tTimeLimit<=0)
        {
        	this.feasible=0;
        	this.benefitGain=Double.NEGATIVE_INFINITY;
        	this.unitBenefitGain=Double.NEGATIVE_INFINITY;
        	this.sojournTime=0;
        	this.activeNodes.clear();
        	return this.benefitGain;
        }
		
        if(lastLocation)
        {
        		
			for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
				if(tSojournTime>tTimeLimit)
				{
					tSojournTime=tTimeLimit;
				}
				
				
				double tBenefitGain=0;
				double tUnitBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.benefitGain=tBenefitGain;
				this.sojournTime=tSojournTime;
				this.unitBenefitGain=this.benefitGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
	//			if(tSojournTime<=TourDesign.minSojournTime)
	//			{
	//				this.benefit=Double.NEGATIVE_INFINITY;
	//			}
				
				
				GateWay tGateWay=new GateWay(this);
				resultGateWays.add(tGateWay);			
			}
			
			Object[] gSet=resultGateWays.toArray();
			GateWayUnitBenefitGainComparator gCom=new GateWayUnitBenefitGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.benefitGain=result.benefitGain;
				this.unitBenefitGain=result.unitBenefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.benefitGain=Double.NEGATIVE_INFINITY;
	        	this.unitBenefitGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        else
        {
        	for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
//				if(tSojournTime>tTimeLimit)
//				{
//					tSojournTime=tTimeLimit;
//				}
				
				
				double tBenefitGain=0;
				double tUnitBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.benefitGain=tBenefitGain;
				this.sojournTime=tSojournTime;
				this.unitBenefitGain=this.benefitGain/(this.movingTime+this.sojournTime+this.backTime-lastBackTime);
	//			if(tSojournTime<=TourDesign.minSojournTime)
	//			{
	//				this.benefit=Double.NEGATIVE_INFINITY;
	//			}
				
				
				GateWay tGateWay=new GateWay(this);
				resultGateWays.add(tGateWay);			
			}
			
			Object[] gSet=resultGateWays.toArray();
			GateWayUnitBenefitGainComparator gCom=new GateWayUnitBenefitGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.benefitGain=result.benefitGain;
				this.unitBenefitGain=result.unitBenefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.benefitGain=Double.NEGATIVE_INFINITY;
	        	this.unitBenefitGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        
        
        
		return this.benefitGain;
	}
	
	
	
	public double calcUtilityGain(double lastBackTime, double timeLimit, boolean lastLocation)                   //new
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
        {
        		
			for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
				if(tSojournTime>tTimeLimit)
				{
					tSojournTime=tTimeLimit;
				}
				
				
				double tUtilityGain=0;
				double tBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tUtilityGain= tUtilityGain+this.activeNodes.get(i).calcUtilityGain(tSojournTime);
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.utilityGain=tUtilityGain;
				this.benefitGain=tBenefitGain;
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
			GateWayUtilityGainComparator gCom=new GateWayUtilityGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.utilityGain=result.utilityGain;
				this.unitUtilityGain=result.unitUtilityGain;
				this.benefitGain=result.benefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.utilityGain=Double.NEGATIVE_INFINITY;
	        	this.unitUtilityGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        else
        {
        	for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
//				if(tSojournTime>tTimeLimit)
//				{
//					tSojournTime=tTimeLimit;
//				}
				
				
				double tUtilityGain=0;
				double tBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tUtilityGain= tUtilityGain+this.activeNodes.get(i).calcUtilityGain(tSojournTime);
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.utilityGain=tUtilityGain;
				this.benefitGain=tBenefitGain;
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
			GateWayUtilityGainComparator gCom=new GateWayUtilityGainComparator(false);
			Arrays.sort(gSet,gCom);
		
			
			
			if(gSet.length>0)
			{
				GateWay result=(GateWay)gSet[0];
				this.utilityGain=result.utilityGain;
				this.unitUtilityGain=result.unitUtilityGain;
				this.benefitGain=result.benefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.utilityGain=Double.NEGATIVE_INFINITY;
	        	this.unitUtilityGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
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
        {
        		
			for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
				if(tSojournTime>tTimeLimit)
				{
					tSojournTime=tTimeLimit;
				}
				
				
				double tUtilityGain=0;
				double tBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tUtilityGain= tUtilityGain+this.activeNodes.get(i).calcUtilityGain(tSojournTime);
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.utilityGain=tUtilityGain;
				this.benefitGain=tBenefitGain;
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
				this.benefitGain=result.benefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.utilityGain=Double.NEGATIVE_INFINITY;
	        	this.unitUtilityGain=Double.NEGATIVE_INFINITY;
	        	this.sojournTime=0;
	        	this.activeNodes.clear();
			}
        }
        else
        {
        	for (int i=0;i<this.neighborNodes.size();i++)
			{
				this.neighborNodes.get(i).calcSurvivalTime(this.movingTime,this.eConSet.get(i));
			}
			Object[] nSet=this.neighborNodes.toArray();
			NodeSurvivalTimeComparator nCom=new NodeSurvivalTimeComparator(false);
			Arrays.sort(nSet, nCom);
			this.activeNodes.clear();
			
			for(int i=0;i<nSet.length;i++)
			{
				Node t=(Node)nSet[i];
				this.activeNodes.add(t);
				double tSojournTime=t.getSurvivalTime();
//				if(tSojournTime>tTimeLimit)
//				{
//					tSojournTime=tTimeLimit;
//				}
				
				
				double tUtilityGain=0;
				double tBenefitGain=0;
				for(int j=0;j<this.activeNodes.size();j++)
				{
					tUtilityGain= tUtilityGain+this.activeNodes.get(i).calcUtilityGain(tSojournTime);
					tBenefitGain= tBenefitGain+this.activeNodes.get(i).gettRate()*tSojournTime;
				}
				
				
				this.utilityGain=tUtilityGain;
				this.benefitGain=tBenefitGain;
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
				this.benefitGain=result.benefitGain;
				this.sojournTime=result.sojournTime;
				this.activeNodes.clear();
				for(int i=0;i<result.activeNodes.size();i++)
				{
					this.activeNodes.add(result.activeNodes.get(i));
				}
			
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
	        	this.utilityGain=Double.NEGATIVE_INFINITY;
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
	public void calcSimilarity(ArrayList<Node> tList)
	{
		ArrayList<Node> l1=new ArrayList<Node>();
		l1.addAll(this.neighborNodes);
		l1.retainAll(tList);
		this.similarity=l1.size()*1.0/this.neighborNodes.size();
	}
	
	
	public void calcPriorityWeight(double tUsedTime)
	{

//		this.priorityWeight=0;
		/*
		 * detail of calculate the priority weight
		 */
		//this.priorityWeight=tUsedTime-this.timeStamp-this.movingTime-this.backTime;
		this.priorityWeight=(1-this.similarity)*(tUsedTime-this.timeStamp-this.movingTime-this.backTime)*this.neighborNodes.size();
		//this.priorityWeight=this.movingTime+this.backTime;
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
