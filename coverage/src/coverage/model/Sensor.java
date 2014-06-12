package coverage.model;

public class Sensor extends Node {

	private double transRange;
	private double batteryCapacity;
	private double EnergyBudget;
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
		return EnergyBudget;
	}


	public void setEnergyBudget(double energyBudget) {
		EnergyBudget = energyBudget;
	}


	public double getResidualEnergy() {
		return residualEnergy;
	}


	public void setResidualEnergy(double residualEnergy) {
		this.residualEnergy = residualEnergy;
	}


	public Sensor(double xValue, double yValue) {
		super(xValue, yValue);
		// TODO Auto-generated constructor stub
	}

}
