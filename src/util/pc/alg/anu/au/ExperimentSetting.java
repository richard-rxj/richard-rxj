package util.pc.alg.anu.au;
/**
 * 
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import model.pc.alg.anu.au.AllocationPair;
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
	public static double roadEndX=10000;   //10000      
	public static double yRange=360;
	public static double batteryCapacity=10000;
	public static double[] harvestRate={0.0011,0.0022}; // J/s   0.0011--0.0022
	//public static double[] mSpeed={5,10,20};   // m/s   
	public static double[] transPowerSet={0.17,0.22,0.3,0.33};    // 0.3 J/s    //transmission power set
	public static double[] transRateSet={250000,19200,9600,4800};     //bps
	public static Random ran=new Random();
	public static int cishu=50;    //50
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
	
	public static AllocationPair getSlotPart(SensorNode s, TimeSlotNode t)
	{
		AllocationPair result=new AllocationPair();
		double slotData=0;
		double energyCost=0;
		double start=t.getX();
		double end=t.getX2();
		for(double i=start;i<end;i=i+ExperimentSetting.rateFactor)
		{
			double x=i+0.5*ExperimentSetting.rateFactor;
			double td=CommonFacility.computeDistance(s.getX(), s.getY(), x, t.getY());
			if(td<=20)
		    {
				slotData=slotData+ExperimentSetting.transRateSet[0]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
				energyCost=energyCost+ExperimentSetting.transPowerSet[0]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
		    }
			else if(td<=50)
			{
				slotData=slotData+ExperimentSetting.transRateSet[1]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
				energyCost=energyCost+ExperimentSetting.transPowerSet[1]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
		    }
			else if(td<=120)
			{
				slotData=slotData+ExperimentSetting.transRateSet[2]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
				energyCost=energyCost+ExperimentSetting.transPowerSet[2]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
		    }
			else if(td<=200)
			{
				slotData=slotData+ExperimentSetting.transRateSet[3]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
				energyCost=energyCost+ExperimentSetting.transPowerSet[3]*ExperimentSetting.rateFactor/(end-start)*ExperimentSetting.unitSlot;
		    }
			else 
			{
				slotData=slotData+0;
				energyCost=energyCost+0;
		    }
			
			
		}
		
		
		result.setSlotID(t.getId());
		result.setSlotData(slotData);
		result.setEnergyCost(energyCost);
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
