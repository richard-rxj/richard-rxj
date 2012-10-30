package util.pc.alg.anu.au;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.pc.alg.anu.au.Network;
import model.pc.alg.anu.au.Node;
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
	    
		int i=0;
		while((tempString=nReader.readLine())!=null)
		{
			String[] b=tempString.split(" ");
			SensorNode tSensor=new SensorNode();
			tSensor.setId(i);
			tSensor.setX(Double.parseDouble(b[0]));
			tSensor.setY(Double.parseDouble(b[1]));
			tSensor.setBatteryCapacity(ExperimentSetting.batteryCapacity);
			tSensor.setEnergyBudget(Double.parseDouble(b[2])*ExperimentSetting.roadLength/speed);
			sensorSet.add(tSensor);
			i++;
		}
		
		
		nReader.close();
		
		
		
		/*
		 * initial timeSlotSet
		 */
		i=0;
		double x=ExperimentSetting.roadBeginX;
		double y=ExperimentSetting.roadBeginY;
		double x2=0;
		x2=x+1*speed*ExperimentSetting.unitSlot;
		TimeSlotNode tSlot=new TimeSlotNode();
		tSlot.setId(i);
		tSlot.setX(x);
		tSlot.setX2(x2);
		tSlot.setY(y);
		timeSlotSet.add(tSlot);
		while((x+speed*ExperimentSetting.unitSlot)<ExperimentSetting.roadEndX)
		{
			i++;
			x=x+speed*ExperimentSetting.unitSlot;
			x2=x+1*speed*ExperimentSetting.unitSlot;
			if(x2>ExperimentSetting.roadEndX)
			{
				x2=ExperimentSetting.roadEndX;
			}
			tSlot=new TimeSlotNode();
			tSlot.setId(i);
			tSlot.setX(x);
			tSlot.setX2(x2);
			tSlot.setY(y);
			timeSlotSet.add(tSlot);
		}
		
		
		
		
		
		return result;
	}
	

	public static double computeDistance(double x1, double y1, double x2, double y2)
	{
		double result=Math.sqrt(Math.pow((x1-x2), 2)+Math.pow((y1-y2), 2));
		return result;
	}
	
	

	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(CommonFacility.getID());
		System.out.println(CommonFacility.getID());
		System.out.println(CommonFacility.getID());
		
		
		SensorNode a=new SensorNode();
		TimeSlotNode b=new TimeSlotNode();
		a.setX(0);
		b.setX(3);
		a.setY(0);
		b.setY(4);

	}

}
