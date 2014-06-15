package coverage.model;

import coverage.util.ExperimentSetting;

public class Sensor extends Node {

	private double transRange;
	private double batteryCapacity;
	private double energyBudget;
	private double residualEnergy;
	
	
	public double getTransRange() {
		return transRange;
	}


	public void setTransRange(double transRange) {
		this.transRange = transRange;
	}


	public double getBatteryCapacity() {
		return batteryCapacity;
	}


	public void setBatteryCapacity(double batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}


	public double getEnergyBudget() {
		return energyBudget;
	}


	public void setEnergyBudget(double energyBudget) {
		energyBudget = energyBudget;
	}


	public double getResidualEnergy() {
		return residualEnergy;
	}


	public void setResidualEnergy(double residualEnergy) {
		this.residualEnergy = residualEnergy;
	}

	
	public void update() {
	// update energy 
		this.residualEnergy-=ExperimentSetting.energyCost;
	}
	

	public Sensor(int id, double xValue, double yValue) {
		super(id, xValue, yValue);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return String.format("%d %.2f %.2f %.2f %.2f %.2f %.2f", this.id, this.x, this.y, this.transRange, this.batteryCapacity, this.energyBudget, this.residualEnergy);
	}

}
