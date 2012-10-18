package network.dr.alg.anu.au;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class NodeUploadTimeComparator<V> implements Comparator<V> {

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
	public NodeUploadTimeComparator(boolean a) {
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
		double uploadTime1 = 0;
		double uploadTime2 = 0;
		
		try {
			uploadTime1 = (Double) v1.getClass().getMethod("getUploadTime")
					.invoke(v1, null);
			uploadTime2 = (Double) v2.getClass().getMethod("getUploadTime")
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
		

		if (((uploadTime1 < uploadTime2) && ascendingOrder)
				|| ((uploadTime1 > uploadTime2) && !ascendingOrder)) {
			return -1;
		} else if (((uploadTime1 > uploadTime2) && ascendingOrder)
				|| ((uploadTime1 < uploadTime2) && !ascendingOrder)) {
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
		ArrayList<Node> a=new ArrayList<Node>();
		Node n1=new Node(1);
		n1.setUploadTime(100);
		a.add(n1);
		
		Node n2=new Node(2);
		n2.setUploadTime(80);
		a.add(n2);
		
		Node n3=new Node(3);
		n3.setUploadTime(200);
		a.add(n3);
		
		Node n4=new Node(4);
		
		
		Object[] nSet=a.toArray();
		NodeUploadTimeComparator nCom=new NodeUploadTimeComparator(false);
		Arrays.sort(nSet, nCom);
		
		for(int i=0;i<nSet.length;i++)
		{
			Node nt=(Node)nSet[i];
			nt.setId(10);
			System.out.println(nt);
		}
		
		for(int i=0;i<a.size();i++)
		{
			System.out.println(a.get(i));
			System.out.println(a.contains(n4));
			System.out.println(a.contains(n1));
			System.out.println(a.contains(n2));
		}
		
	}

}
