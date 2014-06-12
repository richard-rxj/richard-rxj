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
	
	public BaseStation(double xValue, double yValue, double range) {
		super(xValue, yValue);
		this.transRange=range;
	}

}
