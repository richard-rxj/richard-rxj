package model.pc.alg.anu.au;

import java.util.ArrayList;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;

public class SensorNode extends Node {

	private double batteryCapacity;
	private double energyBudget;
	private double throughput;
	private double utility;
	private double utilityGain;
	private double residualBudget;
	private ArrayList<AllocationPair>  allocation;
	
	
	
	
	
	
	public double getEnergyBudget() {
		return energyBudget;
	}








	public void setEnergyBudget(double energyBudget) {
		this.energyBudget = energyBudget;
		this.residualBudget=this.energyBudget;         //sync residual budget
	}








	public double getBatteryCapacity() {
		return batteryCapacity;
	}




	public double getThroughput() {
		return throughput;
	}








	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}








	public double getUtility() {
		return utility;
	}








	public void setUtility(double utility) {
		this.utility = utility;
	}








	public double getUtilityGain() {
		return utilityGain;
	}








	public void setUtilityGain(double utilityGain) {
		this.utilityGain = utilityGain;
	}








	public void setBatteryCapacity(double batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}




	public double getResidualBudget() {
		return residualBudget;
	}




    








	public void update(int slotID, double slotData, double energyCost)
	{
		AllocationPair temp=new AllocationPair(slotID,slotData,energyCost);
		this.allocation.add(temp);
		
		ExperimentSetting.log.info("add   slot-"+slotID+"  to  sensor-"+this.getId()+ " <"+slotData+"--"+energyCost+">");   //for debug


		this.residualBudget=this.residualBudget-energyCost;
		
		//double add=transRate*ExperimentSetting.unitSlot;
		this.throughput=this.throughput+slotData;
		this.utility=ExperimentSetting.getUtility(this.throughput);
	}


	public void restore(int slotID)
	{
		double slotData=0;
		double energyCost=0;
		for(int i=0; i<this.allocation.size();i++)
		{
			AllocationPair temp=this.allocation.get(i);		
			if(temp.getSlotID()==slotID)
			{
				slotData=temp.getSlotData();
				energyCost=temp.getEnergyCost();
				this.allocation.remove(i);
				break;
			}
		}
		

		ExperimentSetting.log.info("remove   slot-"+slotID+"  from  sensor-"+this.getId()+ " <"+slotData+"--"+energyCost+">");   //for debug
 
		
		
		this.residualBudget=this.residualBudget+energyCost;
		
		this.throughput=this.throughput-slotData;
		this.utility=ExperimentSetting.getUtility(this.throughput);
	}

	

	public void updateUtilityGain(double add)
	{
		this.utilityGain=ExperimentSetting.getUtility(this.throughput+add)-ExperimentSetting.getUtility(this.throughput);
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("sensor  ID-"+this.getId()+"  X-"+this.getX()+"  Y-"+this.getY()+" Budget-"+this.getResidualBudget());
		return sb.toString();
	}

















//	@Override
//	public String toString() {
//		return super.toString1()+"SensorNode [batteryCapacity=" + batteryCapacity
//				+ ", energyBudget=" + energyBudget + ", throughput="
//				+ throughput + ", utility=" + utility + ", utilityGain="
//				+ utilityGain + ", residualBudget="
//				+ residualBudget +"]";
//	}



	public SensorNode() {
		super();
		this.allocation=new ArrayList<AllocationPair>();
		// TODO Auto-generated constructor stub
	}




	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(new SensorNode());
		
		SensorNode s1=new SensorNode();
		AllocationPair a1=new AllocationPair(1,1,3);
		AllocationPair a2=new AllocationPair(2,2,3);
		AllocationPair a3=new AllocationPair(3,3,3);
		s1.allocation.add(a1);
		s1.allocation.add(a2);
		s1.allocation.add(a3);
		System.out.println(s1.allocation);
		AllocationPair a4=new AllocationPair(2,2,3);
        s1.allocation.remove(a4);
        System.out.println(s1.allocation);
	}









}
