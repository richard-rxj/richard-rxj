/**
 * 
 */
package u4964526.anu.util;

import java.util.ArrayList;

/**
 * @author richard.ren
 *
 */
public class Edge {

	/**
	 * 
	 */
	private Vertex source;
	private Vertex target;
	private double capacity=Double.POSITIVE_INFINITY;
	private double length=1;
	private double realCap=0;
	private ArrayList<Vertex> wfNodeSet;
	private double wfFactor=0;
	private boolean wasTreed=false;
	private boolean wasFaked=false;
	
	
	
	public Edge(Vertex source, Vertex target, final double capacity) {
		// TODO Auto-generated constructor stub
		this.source=source;
		this.target=target;
		this.capacity=capacity;
		this.wfNodeSet=new ArrayList<Vertex>();
	}





	public void setSource(Vertex source) {
		this.source = source;
	}





	public Vertex getSource() {
		return source;
	}





	public void setTarget(Vertex target) {
		this.target = target;
	}


	public Vertex getTarget() {
		return target;
	}


	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}


	public double getCapacity() {
		return capacity;
	}


	public void setLength(double length) {
		this.length = length;
	}


	public double getLength() {
		return length;
	}

	public void setRealCap(double realCap) {
		this.realCap = realCap;
	}





	public double getRealCap() {
		return realCap;
	}





	public ArrayList<Vertex> getWfNodeSet() {
		return wfNodeSet;
	}







	public double getWfFactor() {
		return wfFactor;
	}





	public void setWfFactor(double wfFactor) {
		this.wfFactor = wfFactor;
	}





	public boolean isWasTreed() {
		return wasTreed;
	}





	public void setWasTreed(boolean wasTreed) {
		this.wasTreed = wasTreed;
	}





	public boolean isWasFaked() {
		return wasFaked;
	}





	public void setWasFaked(boolean wasFaked) {
		this.wasFaked = wasFaked;
	}


    public void addWfNode(Vertex e)
    {
    	wfNodeSet.add(e);
    }


	public String toString()
	{
		String result="E<"+this.source+"-"+this.target+"-c"+this.capacity+"-realC"+this.realCap+">";
		return result;
	}
	
}
