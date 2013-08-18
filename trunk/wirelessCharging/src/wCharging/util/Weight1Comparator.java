/**
 * 
 */
package wCharging.util;

import java.util.Comparator;

import wCharging.model.ChargingRequest;

/**
 * @author user
 *
 */
public class Weight1Comparator implements Comparator<ChargingRequest> {

	private boolean ascendingOrder;
	
	public Weight1Comparator(boolean b)
	{
		this.ascendingOrder=b;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compare(ChargingRequest arg0, ChargingRequest arg1) {
		// TODO Auto-generated method stub
		if((arg0.getWeight1()>arg1.getWeight1()&& this.ascendingOrder)
				|| (arg0.getWeight1()<arg1.getWeight1()&& !this.ascendingOrder))
			{
				return  1;
			}
			else if((arg0.getWeight1()<arg1.getWeight1()&& this.ascendingOrder)
					|| (arg0.getWeight1()>arg1.getWeight1()&& !this.ascendingOrder))
			{
				return  -1;
			}
			else
			{
				return 0;
			}
	}

}
