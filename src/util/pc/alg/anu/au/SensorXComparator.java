/**
 * 
 */
package util.pc.alg.anu.au;

import java.util.Comparator;

import model.pc.alg.anu.au.SensorNode;

/**
 * @author u4964526
 *
 */
public class SensorXComparator implements Comparator<SensorNode> {

	private boolean ascendingOrder;
	/**
	 * 
	 */
	public SensorXComparator(boolean b) {
		// TODO Auto-generated constructor stub
		this.ascendingOrder=b;
	}

	@Override
	public int compare(SensorNode o1, SensorNode o2) {
		if((o1.getX()>o2.getX()&& this.ascendingOrder)
				|| (o1.getX()<o2.getX()&& !this.ascendingOrder))
			{
				return  1;
			}
			else if((o1.getX()<o2.getX()&& this.ascendingOrder)
					|| (o1.getX()>o2.getX()&& !this.ascendingOrder))
			{
				return  -1;
			}
			else
			{
				return 0;
			}
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
