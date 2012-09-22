package model.pc.alg.anu.au;

import java.util.ArrayList;

public class Network {

	private ArrayList<SensorNode>  sensorSet;
	private ArrayList<TimeSlotNode> timeSlotSet;
	
	
	
	public ArrayList<SensorNode> getSensorSet() {
		return sensorSet;
	}

	public ArrayList<TimeSlotNode> getTimeSlotSet() {
		return timeSlotSet;
	}

	public Network() {
		// TODO Auto-generated constructor stub
		sensorSet=new ArrayList<SensorNode>();
		timeSlotSet=new ArrayList<TimeSlotNode>();
				
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
