/**
 * 
 */
package coverage.model;

/**
 * @author u4964526
 *
 */
public class BaseStation extends Node {

	private double transRange;
	
	/**
	 * @return the transRange
	 */
	public double getTransRange() {
		return transRange;
	}


	/**
	 * @param transRange the transRange to set
	 */
	public void setTransRange(double transRange) {
		this.transRange = transRange;
	}


	public BaseStation(int id, double xValue, double yValue) {
		super(id, xValue, yValue);
	}

	
	@Override
	public String toString() {
		return String.format("%d %.2f %.2f %.2f", this.id, this.x, this.y, this.transRange);
	}
}
