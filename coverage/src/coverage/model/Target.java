/**
 * 
 */
package coverage.model;

/**
 * @author u4964526
 *
 */
public class Target extends Node {

	
	
	
	
	public Target(int id, double xValue, double yValue) {
		super(id, xValue, yValue);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public String toString() {
		return String.format("%d %.2f %.2f", this.id, this.x, this.y);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Target test=new Target(1, 0.055, 0.065);
		System.out.println(test);
	}

}
