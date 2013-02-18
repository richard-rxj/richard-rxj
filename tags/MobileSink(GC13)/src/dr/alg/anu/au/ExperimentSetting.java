/**
 * 
 */
package dr.alg.anu.au;

import java.util.ArrayList;
import java.util.Random;

import network.dr.alg.anu.au.Node;

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
	public static double initSinkX=0;   //50    0 for distributed
	public static double initSinkY=0;   //50    0 for distributed
	public static double gRate= 1000; //  bps
	public static double tRate= 5000; //  bps    old 20000
	public static double tourTime=100;  //  s    -----------------------varible
	public static double[] harvestRate={0.0004,0.0009}; // J/s
	public static double mSpeed=1;   // 2m/s   
	public static double lossWeight=0.4;  //--------------------------------varible
	public static double beta=0.000000015; //J/b/m^2    0.0000000006;J/b/m^3     need to reset
	public static double eComM=2;
	public static double minEConsumption=0.002;   //J/s   
	public static double utilityA=2;
	public static Random ran=new Random();
	public static int cishu=20;    //20
	
	
	public static double calcVariance(ArrayList<Node> nList)
	{
		double mean=0;
		double variance=0;
		for(int i=0;i<nList.size();i++)
		{
			mean=mean+nList.get(i).getTotalSojournTime();
		}
		mean=mean/nList.size();
		for(int i=0;i<nList.size();i++)
		{
			variance=variance+Math.pow((nList.get(i).getTotalSojournTime()-mean), 2);
		}
		variance=variance/nList.size();
		return Math.sqrt(variance);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
