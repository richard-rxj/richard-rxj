/**
 * 
 */
package coverage.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import coverage.model.TimeSlot;

/**
 * @author u4964526
 *
 */
public class ExperimentSetting {
     
	public static double[] harvestRate={0.0011,0.0022}; // J/s   0.0011--0.0022   only for debug
	public static final double solarPanel=0.01*0.01;
	public static final double energyCost=0.0564*1800;       //pre timeslot(30mins)
	public static final double senseRange=20;
	public static final double transRange=20;
	public static final double batteryCapacity=10000;
	public static final double coverageWeight=0.5;        //0.5;           
	public static final double accuracyThreshold=0.1;
	public static final double tuningWeight=0.8;
	public static final double intervalInitial=0.2;
	public static final int cishu=15;
	public static final int timeSlotSize=48;
	
	
	public static final int xRange=100;
	public static final int yRange=100;
	
	public static final Random random=new Random();
	
	public static double getX() {
		return new Random().nextDouble()*xRange;
	}
	
	public static double getY() {
		return new Random().nextDouble()*yRange;
	}
	
	
	public static final Logger gLog=Logger.getLogger(ExperimentSetting.class.getName());  
    static
    {
    	gLog.setLevel(Level.ALL);
    	//gLog.addHandler(new ConsoleHandler());
    	try {
   		FileHandler htmlHandler=new FileHandler(ExperimentSetting.class.getName()+".html");
   		htmlHandler.setFormatter(new HtmlLogFormatter());			
   		gLog.addHandler(htmlHandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
	public static double[][] actualEnergy=new double[500][48];
	public static double[][] predictEnergy=new double[500][48];
	
	
	//initial actual energy
	static {
		try {
			BufferedReader  in=new BufferedReader(new InputStreamReader(new FileInputStream("data/energy/energyBudget.txt")));
			
			try{
				int row=0;
				String line=null;
				//System.out.println(in.readLine());
				while((line=in.readLine())!=null) {
					String[] sSet=line.split("\\s+");
					for(int i=1; i<sSet.length; i++) {
						String[] values=sSet[i].split(":");
						predictEnergy[row][i-1]=Double.parseDouble(values[0]);
						actualEnergy[row][i-1]=Double.parseDouble(values[1]);
						//predictEnergy[row][i-1]= ExperimentSetting.harvestRate[0]+new Random().nextDouble()
					    //        *(ExperimentSetting.harvestRate[1]-ExperimentSetting.harvestRate[0])
					    //        *3600*24;
						//actualEnergy[row][i-1]=predictEnergy[row][i-1];
					}
					row++;
				}
			}finally {
				in.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	
	
	//get total ActualBudget 
	public static double getActualBudget(int id) {
		double sum=0;
		for(int i=0;i<48; i++) {
			sum+=actualEnergy[id-1][i];
		}
		return sum;
	}
	
	//get ActualBudget of an Interval 
	public static double getActualBudget(int id, int start, int end) {
		double sum=0;
		for(int i=start;i<=end; i++) {
			sum+=actualEnergy[id-1][i];
		}
		return sum;
	}
	
	//get total PredictBudget
	public static double getPredictBudget(int id) {
		
		double sum=0;
		for(int i=0;i<48; i++) {
			sum+=predictEnergy[id-1][i];
		}
		return sum;
	}
	
	//get total PredictBudget of an Interval
	public static double getPredictBudget(int id, int start, int end) {
		double sum=0;
		for(int i=start;i<=end; i++) {
			sum+=predictEnergy[id-1][i];
		}
		return sum;
	}
	
	
	public static List<TimeSlot> getTimeSlots() {
		List<TimeSlot> timeslots=new ArrayList<TimeSlot>();
		
		
		for(int id=1; id<=ExperimentSetting.timeSlotSize; id++) {
			timeslots.add(new TimeSlot(id));
		}
		
		return timeslots;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i=0; i<48; i++)
		System.out.println(actualEnergy[0][i]);
	}

}
