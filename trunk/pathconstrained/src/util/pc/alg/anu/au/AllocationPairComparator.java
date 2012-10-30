/**
 * 
 */
package util.pc.alg.anu.au;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.pc.alg.anu.au.AllocationPair;

/**
 * @author user
 *
 */
public class AllocationPairComparator implements Comparator<AllocationPair> {

	private boolean ascendingOrder;
	/**
	 * 
	 */
	public AllocationPairComparator(boolean b) {
		// TODO Auto-generated constructor stub
		this.ascendingOrder=b;
	}

	

	@Override
	public int compare(AllocationPair arg0, AllocationPair arg1) {
		// TODO Auto-generated method stub
		if((arg0.getSlotData()>arg1.getSlotData()&& this.ascendingOrder)
			|| (arg0.getSlotData()<arg1.getSlotData()&& !this.ascendingOrder))
		{
			return  1;
		}
		else if((arg0.getSlotData()<arg1.getSlotData()&& this.ascendingOrder)
				|| (arg0.getSlotData()>arg1.getSlotData()&& !this.ascendingOrder))
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
        ArrayList<AllocationPair> p=new ArrayList<AllocationPair>();
        p.add(new AllocationPair(1,2.0));
        p.add(new AllocationPair(2,3.0));
        p.add(new AllocationPair(3,1.0));
        Collections.sort(p, new AllocationPairComparator(false));
        System.out.println(p);
	}
	
}
