/**
 * 
 */
package coverage.model;

/**
 * @author u4964526
 *
 */
public abstract class Node {
	final int id;
	final double x;
	final double y;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	public Node (int idValue, double xValue, double yValue) {
		id=idValue;
		x=xValue;
		y=yValue;
	}
	
	@Override
	public String toString() {
		return String.format("%d %.2f %.2f", id, x, y);
	}

	
	public double getDistance (Node b) {
		return Math.sqrt(Math.pow(this.x-b.x, 2)+Math.pow(this.y-b.y, 2));
	}
}
