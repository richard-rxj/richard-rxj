package util.pc.alg.anu.au;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.pc.alg.anu.au.Network;
import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

public class CommonFacility {

	public static int uniqueID=0;
	
	
	
	public static int getID()
	{
		uniqueID++;
		return uniqueID;
	}
	
	
	public static Network getNetwork(String sensorTxt, double speed) throws NumberFormatException, IOException
	{
		Network result=new Network();
		ArrayList<SensorNode>  sensorSet=result.getSensorSet();
		ArrayList<TimeSlotNode>  timeSlotSet=result.getTimeSlotSet();
		
		/*
		 * initial sensorSet
		 */
		String tempString=null;
		BufferedReader nReader=new BufferedReader(new InputStreamReader(new FileInputStream(sensorTxt)));
	
		while((tempString=nReader.readLine())!=null)
		{
			String[] b=tempString.split(" ");
			SensorNode tSensor=new SensorNode();
			tSensor.setX(Double.parseDouble(b[0]));
			tSensor.setY(Double.parseDouble(b[1]));
			tSensor.setBatteryCapacity(ExperimentSetting.batteryCapacity);
			tSensor.setEnergyBudget(Double.parseDouble(b[2])*ExperimentSetting.roadLength/speed);
			sensorSet.add(tSensor);
		}
		
		
		nReader.close();
		
		
		
		/*
		 * initial timeSlotSet
		 */
		double x=ExperimentSetting.roadBeginX;
		double y=ExperimentSetting.roadBeginY;
		x=x+0.5*speed*ExperimentSetting.unitSlot;
		TimeSlotNode tSlot=new TimeSlotNode();
		tSlot.setX(x);
		tSlot.setY(y);
		timeSlotSet.add(tSlot);
		while((x+speed*ExperimentSetting.unitSlot)<=ExperimentSetting.roadEndX)
		{
			x=x+speed*ExperimentSetting.unitSlot;
			tSlot=new TimeSlotNode();
			tSlot.setX(x);
			tSlot.setY(y);
			timeSlotSet.add(tSlot);
		}
		
		
		
		
		
		return result;
	}
	
	/*
	 * allocate a set of time slot to a set of sensors with maxmize utility
	 */
	public static void maxGainAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
	{
		
	}
	
	
	/*
	 * allocate a set of time slot to a set of sensors randomly
	 */
	public static void randomAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
	{
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(CommonFacility.getID());
		System.out.println(CommonFacility.getID());
		System.out.println(CommonFacility.getID());
		
	}

}
