package model.pc.alg.anu.au;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;

public class SensorNode extends Node {

	private double batteryCapacity;
	private double energyBudget;
	private double throughput;
	private double utility;
	private double utilityGain;
	
	
	
	
	
	
	
	public double getEnergyBudget() {
		return energyBudget;
	}








	public void setEnergyBudget(double energyBudget) {
		this.energyBudget = energyBudget;
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








	public void updateEnergyBudget(double eCom, double eHarvest)
	{
		this.energyBudget=this.energyBudget+eHarvest-eCom;
		if(this.energyBudget>this.batteryCapacity)
			this.energyBudget=this.batteryCapacity;
	}
	

	public void updateThroughput(double add)
	{
		this.throughput=this.throughput+add;
	}
	
	public void updateUtility(double add)
	{
		this.utility=this.utility+add;
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
				+ utilityGain + "]";
	}








	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new SensorNode());

	}
}
