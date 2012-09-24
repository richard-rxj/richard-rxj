package model.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;
import util.pc.alg.anu.au.TopologyFactory;

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

	@Override
	public String toString() {
		return "Network [\r\nsensorSet=" + sensorSet + "\r\n timeSlotSet="
				+ timeSlotSet + "]";
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		String sensorTxt="test/topology/node-100-1.txt";
		double speed=ExperimentSetting.mSpeed[1];
		
		Network tNetwork=CommonFacility.getNetwork(sensorTxt, speed);
		System.out.println(tNetwork);
		
		
		
		
	}

}
