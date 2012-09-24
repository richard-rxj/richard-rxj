package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;

import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

import util.pc.alg.anu.au.CommonFacility;

public class RandomAllocate extends Allocate {

	public RandomAllocate(String sensorTxt, double speed)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		randomAllocate(super.getgNet().getSensorSet(), super.getgNet().getTimeSlotSet());
	}

	
	/*
	 * allocate a set of time slot to a set of sensors randomly
	 */
	private void randomAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
	{
		
	}
	
}
