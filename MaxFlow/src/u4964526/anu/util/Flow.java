/**
 * 
 */
package u4964526.anu.util;

import java.util.ArrayList;

/**
 * @author richard.ren
 *
 */
public class Flow {

	/**
	 * 
	 */
	private Vertex start;
	private Vertex end;
	private double rate;
	private double maxRate;
	private ArrayList<Path> pathList;
	
	
	public Flow() {
		// TODO Auto-generated constructor stub
		pathList=new ArrayList<Path>();
		
	}


	public Vertex getStart() {
		return start;
	}


	public void setStart(Vertex start) {
		this.start = start;
	}


	public Vertex getEnd() {
		return end;
	}


	public void setEnd(Vertex end) {
		this.end = end;
	}


	public Double getRate() {
		return rate;
	}


	public void setRate(double rate) {
		this.rate = rate;
		this.getStart().setRate(rate);
	}
	
	public double getMaxRate() {
		return maxRate;
	}


	public void setMaxRate(double maxRate) {
		this.maxRate = maxRate;
	}


	public ArrayList<Path> getPathList() {
		return pathList;
	}


	public void addPath(Path e)
	{
		pathList.add(e);
	}

	public String toString()
	{
		String result="Flow Information:\n";
		result=result+"------start:"+this.getStart()+"\n";
		result=result+"------end:"+this.getEnd()+"\n";
		result=result+"------rate:"+this.getRate()+"\n";
		for (int i=0;i<this.getPathList().size();i++)
		{
			result=result+"------Path"+i+":"+this.getPathList().get(i)+"\n";
		}
		
		return result;
	}
}
