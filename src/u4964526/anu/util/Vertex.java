/**
 * 
 */
package u4964526.anu.util;

/**
 * @author richard.ren
 *
 */
public class Vertex implements Comparable<Vertex> {

	private String name;
    private double minKey = Double.POSITIVE_INFINITY;
    private double capacity=Double.POSITIVE_INFINITY;//etc: Energy Budget
    private double weight=1;
    private double value=0;
    private int verValue=0;
    private double maxRate=Double.POSITIVE_INFINITY;
    private double tMaxRate=Double.POSITIVE_INFINITY;
    private double rate=0;
    private boolean wasVisited=false;
    private double xLabel=0;
    private double yLabel=0;
    private boolean wasSource=false;
    private boolean wasSink=false;
    private boolean wasConnected=true;
    private double budgetEnergy=1000;
    
    public Vertex(String name) 
    {
    	this.name=name; 
    }
    
    public String toString() { return name; }
	
	
	@Override
	public int compareTo(Vertex arg0) {
		return Double.compare(minKey, arg0.getMinKey());
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setMinKey(double minKey) {
		this.minKey = minKey;
	}
	public double getMinKey() {
		return minKey;
	}
	

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getWeight() {
		return weight;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getValue() {
		return value;
	}
	public double getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(double maxRate) {
		this.maxRate = maxRate;
	}

	public double gettMaxRate() {
		return tMaxRate;
	}

	public void settMaxRate(double tMaxRate) {
		this.tMaxRate = tMaxRate;
	}

	public boolean isWasSource() {
		return wasSource;
	}

	public void setWasSource(boolean wasSource) {
		this.wasSource = wasSource;
	}

	public boolean isWasSink() {
		return wasSink;
	}

	public void setWasSink(boolean wasSink) {
		this.wasSink = wasSink;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getRate() {
		return rate;
	}
	public void setWasVisited(boolean wasVisited) {
		this.wasVisited = wasVisited;
	}
	public boolean isWasVisited() {
		return wasVisited;
	}
	public void setVerValue(int verValue) {
		this.verValue = verValue;
	}
	public int getVerValue() {
		return verValue;
	}

	public double getxLabel() {
		return xLabel;
	}

	public void setxLabel(double xLabel) {
		this.xLabel = xLabel;
	}

	public double getyLabel() {
		return yLabel;
	}

	public void setyLabel(double yLabel) {
		this.yLabel = yLabel;
	}

	public double getBudgetEnergy() {
		return budgetEnergy;
	}

	public void setBudgetEnergy(double budgetEnergy) {
		this.budgetEnergy = budgetEnergy;
	}

	public boolean isWasConnected() {
		return wasConnected;
	}

	public void setWasConnected(boolean wasConnected) {
		this.wasConnected = wasConnected;
	}
	
}
