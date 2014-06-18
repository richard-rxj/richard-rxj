package coverage.model;

import coverage.util.ExperimentSetting;

public class Sensor extends Node {

	private double transRange;
	private double senseRange;
	private double batteryCapacity;
	private double actualBudget;
	private double predictBudget;
	private double residualEnergy;
	
	
	public double getTransRange() {
		return transRange;
	}


	public void setTransRange(double transRange) {
		this.transRange = transRange;
	}

	
	/**
	 * @return the senseRange
	 */
	public double getSenseRange() {
		return senseRange;
	}


	/**
	 * @param senseRange the senseRange to set
	 */
	public void setSenseRange(double senseRange) {
		this.senseRange = senseRange;
	}


	public double getBatteryCapacity() {
		return batteryCapacity;
	}


	public void setBatteryCapacity(double batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}


	public double getActualBudget() {
		return actualBudget;
	}


	public void setActualBudget(double energyBudget) {
		this.actualBudget = energyBudget;
	}
	
	public double getPredictBudget() {
		return predictBudget;
	}


	public void setPredictBudget(double energyBudget) {
		this.predictBudget = energyBudget;
	}


	public double getResidualEnergy() {
		return residualEnergy;
	}


	public void setResidualEnergy(double residualEnergy) {
		this.residualEnergy = residualEnergy;
	}

	
	
	public Sensor(int id, double xValue, double yValue) {
		super(id, xValue, yValue);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return String.format("%d %.2f %.2f %.2f %.2f %.2f %.2f %.2f %.2f", this.id, this.x, this.y, this.transRange, this.senseRange, this.batteryCapacity, this.actualBudget, this.predictBudget, this.residualEnergy);
	}

}
