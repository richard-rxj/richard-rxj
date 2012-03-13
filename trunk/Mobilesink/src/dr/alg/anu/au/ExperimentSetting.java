/**
 * 
 */
package dr.alg.anu.au;

import java.util.Random;

/**
 * @author user
 *
 */
public class ExperimentSetting {

	
	//public static double transmissionRange=10;
	//public static int  gatewayLimit=50;        
	public static double minSojournTime=0.2;
	public static double minLeftTime=0.001;
	public static double xRange=100;
	public static double yRange=100;
	public static double initSinkX=50;
	public static double initSinkY=50;
	public static double gRate= 1000; //  bps
	public static double tRate= 1000; //  bps    old 20000
	public static double tourTime=100;  //  s    -----------------------varible
	public static double[] harvestRate={0.0004,0.0009}; // J/s
	public static double mSpeed=2;   // m/s   
	public static double lossWeight=0.4;  //--------------------------------varible
	public static double beta=0.000000015; //J/b/m^2    0.0000000006;J/b/m^3     £¡£¡£¡£¡£¡need to reset
	public static double eComM=2;
	public static double minEConsumption=0.002;   //J/s   
	public static double utilityA=2;
	public static Random ran=new Random();
	public static int cishu=1;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
