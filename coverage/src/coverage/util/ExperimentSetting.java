/**
 * 
 */
package coverage.util;

import java.util.Random;

/**
 * @author u4964526
 *
 */
public class ExperimentSetting {
     
	public static final double energyCost=0.00006*1800;       //pre timeslot(30mins)
	public static final double senseRange=10;
	public static final double transRange=20;
	public static final double batteryCapacity=10000;
	public static final double coverageWeight=0.5;           
	public static final double accuracyThreshold=0.1;
	public static final double tuningWeight=0.8;
	
	public static final int xRange=100;
	public static final int yRange=100;
	
	public static final Random random=new Random();
	
	public static double getX() {
		return new Random().nextDouble()*xRange;
	}
	
	public static double getY() {
		return new Random().nextDouble()*yRange;
	}
	
	public static double getActualBudget() {
		//TBD
	}
	
	public static double getPredictBudget() {
		//TBD
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
