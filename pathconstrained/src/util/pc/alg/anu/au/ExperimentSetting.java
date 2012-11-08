package util.pc.alg.anu.au;
/**
 * 
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;



/**
 * @author user
 *
 */
public class ExperimentSetting {

	
	public static double transRange=200;        
	public static double roadBeginX=0;      
	public static double roadY=180;  
	public static double roadEndX=3000;   //10000      
	public static double yRange=360;
	public static double batteryCapacity=10000;
	public static double[] harvestRate={0.0011,0.0066}; // J/s   0.0011--0.0022
	//public static double[] mSpeed={5,10,20};   // m/s   
	public static double eCom=0.3;    //  J/s
	public static Random ran=new Random();
	public static int cishu=10;    //50
	public static int unitSlot=1;     //s
	public static int interval=10;    //fixed interval for distributed
	public static int rateFactor=1;   //used for eastimate the profit of a time slot
	public static double epsilon=0.2;     //1+alpha---->alpha=0.25
	
	
	private static double getTransRateByDistance(double distance)
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
	
	public static double getSlotData(SensorNode s, TimeSlotNode t)
	{
		double result=0;
		double start=t.getX();
		double end=t.getX2();
		for(double i=start;i<end;i=i+ExperimentSetting.rateFactor)
		{
			double x=i+0.5*ExperimentSetting.rateFactor;
			double td=CommonFacility.computeDistance(s.getX(), s.getY(), x, t.getY());
			result=result+ExperimentSetting.getTransRateByDistance(td)*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
		}
		
		return result;
	}
	
	
    public static double  getUtility(double t)
    {
    
    	//return Math.pow(t, 0.5);   // non-linear
    	return t;                  // linear 
    }
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println(Math.log(250000));
        int[] a={1,2,3};
        System.out.print(Arrays.toString(a));
	}

}
