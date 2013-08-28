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
public class TimeCostComparator implements Comparator<ChargingRequest> {

private boolean ascendingOrder;
	
	public TimeCostComparator(boolean b)
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
		if(((arg0.getTimeCost()>arg1.getTimeCost())&& this.ascendingOrder)
				|| ((arg0.getTimeCost()<arg1.getTimeCost())&& !this.ascendingOrder))
			{
				return  1;
			}
			else if(((arg0.getTimeCost()<arg1.getTimeCost())&& this.ascendingOrder)
					|| ((arg0.getTimeCost()>arg1.getTimeCost())&& !this.ascendingOrder))
			{
				return  -1;
			}
			else
			{
				return 0;
			}
	}

}
