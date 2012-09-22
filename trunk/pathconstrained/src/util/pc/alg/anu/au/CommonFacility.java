package util.pc.alg.anu.au;

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
	
	
	public static Network getNetwork(String sensorTxt, int speed)
	{
		Network result=new Network();
		ArrayList<SensorNode>  sensorSet=result.getSensorSet();
		ArrayList<TimeSlotNode>  timeSlotSet=result.getTimeSlotSet();
		
		
		
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
		
	}

}
