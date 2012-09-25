package util.pc.alg.anu.au;
/**
 * 
 */


import java.util.ArrayList;
import java.util.Random;



/**
 * @author user
 *
 */
public class ExperimentSetting {

	
	public static double transRange=200;        
	public static double xRange=360;
	public static double yRange=18000;
	public static double roadBeginX=0;      
	public static double roadBeginY=180;  
	public static double roadEndX=18000;      
	public static double roadEndY=180;
	public static double roadLength=18000;
	public static double batteryCapacity=10000;
	public static double[] harvestRate={0.0011,0.0022}; // J/s
	//public static double[] mSpeed={5,10,20};   // m/s   
	public static double eCom=0.3;    //  J/s
	public static Random ran=new Random();
	public static int cishu=30;
	public static int unitSlot=1;     //s
	public static int interval=10;
	
	
	public static double getTransRate(double distance)
	{
		if(distance<=20)
			return 250000;
		else if(distance<=50)
			return 19200;
		else if(distance<=120)
			return 9600;
		else if(distance<=200)
			return 4800;
		else 
			return 0;
			
	}
	
    public static double  getUtility(double t)
    {
    	//return Math.log(t+1);
    	return Math.pow(t, 0.5);
    }
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println(Math.log(250000));
	}

}
