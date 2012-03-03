/**
 * 
 */
package network.dr.alg.anu.au;

import java.text.DecimalFormat;

/**
 * @author user
 *
 */
public class LabResult {

	int activeNodes=0;
	double totalUtility=0;
	double totalThroughput=0;
	double totalSojournTime=0;
	double totalMovingTime=0;
	
	
	
	public int getActiveNodes() {
		return activeNodes;
	}



	public void setActiveNodes(int activeNodes) {
		this.activeNodes = activeNodes;
	}



	public double getTotalUtility() {
		return totalUtility;
	}



	public void setTotalUtility(double totalUtility) {
		this.totalUtility = totalUtility;
	}



	public double getTotalThroughput() {
		return totalThroughput;
	}



	public void setTotalThroughput(double totalThroughput) {
		this.totalThroughput = totalThroughput;
	}



	public double getTotalSojournTime() {
		return totalSojournTime;
	}



	public void setTotalSojournTime(double totalSojournTime) {
		this.totalSojournTime = totalSojournTime;
	}



	public double getTotalMovingTime() {
		return totalMovingTime;
	}



	public void setTotalMovingTime(double totalMovingTime) {
		this.totalMovingTime = totalMovingTime;
	}


	public String toString() {
		DecimalFormat df=new DecimalFormat("#.0000");
		String result="";
		result=result+" "+df.format(Double.toString(totalUtility))+" "+df.format(Double.toString(totalThroughput))+" "+df.format(Double.toString(totalSojournTime))+" "+df.format(Double.toString(totalMovingTime))+" "+Double.toString(activeNodes);
		return result;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
