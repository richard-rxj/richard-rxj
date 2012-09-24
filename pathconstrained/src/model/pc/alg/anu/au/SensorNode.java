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




    








	public void update(int slotID, double transRate)
	{
		AllocationPair temp=new AllocationPair(slotID,transRate);
		this.allocation.add(temp);
		
		double add=transRate*ExperimentSetting.unitSlot;		
		double eCom=ExperimentSetting.eCom*ExperimentSetting.unitSlot;
		this.residualBudget=this.residualBudget-eCom;
		this.throughput=this.throughput+add;
		this.utility=this.utility+ExperimentSetting.getUtility(add);
	}



	

	public void updateUtilityGain(double add)
	{
		this.utilityGain=ExperimentSetting.getUtility(this.throughput+add)-ExperimentSetting.getUtility(this.throughput);
	}
	
	


















	@Override
	public String toString() {
		return super.toString()+"SensorNode [batteryCapacity=" + batteryCapacity
				+ ", energyBudget=" + energyBudget + ", throughput="
				+ throughput + ", utility=" + utility + ", utilityGain="
				+ utilityGain + ", residualBudget="
				+ residualBudget +"]";
	}



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
		System.out.println(new SensorNode());

	}









}
