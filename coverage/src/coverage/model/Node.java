/**
 * 
 */
package coverage.model;

/**
 * @author u4964526
 *
 */
public abstract class Node {
	public final int id;
	public final double x;
	public final double y;
	
	public Node (int idValue, double xValue, double yValue) {
		id=idValue;
		x=xValue;
		y=yValue;
	}
	
	@Override
	public String toString() {
		return String.format("%d %.2f %.2f", id, x, y);
	}

}
