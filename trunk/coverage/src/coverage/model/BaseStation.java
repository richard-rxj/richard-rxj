/**
 * 
 */
package coverage.model;

/**
 * @author u4964526
 *
 */
public class BaseStation extends Node {

	private final double transRange;
	
	public BaseStation(int id, double xValue, double yValue, double range) {
		super(id, xValue, yValue);
		this.transRange=range;
	}

	
	@Override
	public String toString() {
		return String.format("%d %.2f %.2f %.2f", this.id, this.x, this.y, this.transRange);
	}
}
