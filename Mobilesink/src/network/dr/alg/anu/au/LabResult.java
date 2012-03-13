/**
 * 
 */
package network.dr.alg.anu.au;

import java.text.DecimalFormat;

import dr.alg.anu.au.ExperimentSetting;

/**
 * @author user
 *
 */
public class LabResult {

	int networkSize=0;	
	int activeNodes=0;
	double tourTime=0;
	double totalUtility=0;
	double totalThroughput=0;
	double totalSojournTime=0;
	double totalMovingTime=0;
	double totalThroughputRatio=0;
	
	
	
	public int getNetworkSize() {
		return networkSize;
	}



	public void setNetworkSize(int networkSize) {
		this.networkSize = networkSize;
	}



	public int getActiveNodes() {
		return activeNodes;
	}



	public void setActiveNodes(int activeNodes) {
		this.activeNodes = activeNodes;
	}



	public double getTourTime() {
		return tourTime;
	}



	public void setTourTime(double tourTime) {
		this.tourTime = tourTime;
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
		this.totalThroughputRatio=this.totalThroughput/(this.tourTime*this.networkSize*ExperimentSetting.gRate);
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


	public double getTotalThroughputRatio() {
		return totalThroughputRatio;
	}



	public String toString() {
		DecimalFormat df=new DecimalFormat("#.0000");
		String result="";
		result=result+" "+df.format(this.totalThroughput)+" "+df.format(this.totalUtility)+" "+df.format(this.totalSojournTime)+" "+df.format(this.totalMovingTime)+" "+Double.toString(activeNodes);
		return result;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
