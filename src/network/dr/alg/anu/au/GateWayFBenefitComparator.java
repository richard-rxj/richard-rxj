package network.dr.alg.anu.au;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GateWayFBenefitComparator<V> implements Comparator<V> {

	/**
	 * The sort order for node uploadtime. <code>true</code> for ascending
	 * degree order (smaller degrees first), <code>false</code> for descending.
	 */
	private boolean ascendingOrder;

	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Creates a comparator for comparing the uploadtime of nodes in the
	 * specified graph. The comparator compares in ascending order of degrees
	 * (lowest first).
	 * 
	 * @param g
	 *            graph with respect to which the degree is calculated.
	 */
	public GateWayFBenefitComparator(boolean a) {
		this.ascendingOrder = a;
	}

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * Compare the uploadtime of <code>v1</code> and <code>v2</code>,
	 * taking into account whether ascending or descending order is used.
	 * 
	 * @param v1
	 *            the first vertex to be compared.
	 * @param v2
	 *            the second vertex to be compared.
	 * 
	 * @return -1 if <code>v1</code> comes before <code>v2</code>, +1 if <code>
	 * v1</code> comes after <code>v2</code>, 0 if equal.
	 */
	
	
	@Override
	public int compare(V v1, V v2) {
		// TODO Auto-generated method stub
		double fBenefit1 = 0;
		double fBenefit2 = 0;
		
		try {
			fBenefit1 = (Double) v1.getClass().getMethod("getBenefit")
					.invoke(v1, null);
			fBenefit2 = (Double) v2.getClass().getMethod("getBenefit")
					.invoke(v2, null);

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		if (((fBenefit1 < fBenefit2) && ascendingOrder)
				|| ((fBenefit1 > fBenefit2) && !ascendingOrder)) {
			return -1;
		} else if (((fBenefit1 > fBenefit2) && ascendingOrder)
				|| ((fBenefit1 < fBenefit2) && !ascendingOrder)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<GateWay> a=new ArrayList<GateWay>();
		GateWay n1=new GateWay(10);
		n1.setBenefit(100);
		a.add(n1);
		
		GateWay n2=new GateWay(2);
		n2.setBenefit(80);
		a.add(n2);
		
		GateWay n3=new GateWay(3);
		n3.setBenefit(200);
		a.add(n3);
		
		Object[] gSet=a.toArray();
		GateWayFBenefitComparator gLCom=new GateWayFBenefitComparator(false);
		Arrays.sort(gSet, gLCom);
		
		for(int i=0;i<gSet.length;i++)
		{
			GateWay nt=(GateWay)gSet[i];
			nt.setId(6);
			System.out.println(nt);
		}
		
		for(int i=0;i<a.size();i++)
		{
			System.out.println(a.get(i));
		}
	}

}
