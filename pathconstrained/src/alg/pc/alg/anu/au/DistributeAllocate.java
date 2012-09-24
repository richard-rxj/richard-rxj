package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;

import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

public class DistributeAllocate extends Allocate {

	public DistributeAllocate(String sensorTxt, double speed)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		maxGainAllocate(super.getgNet().getSensorSet(),super.getgNet().getTimeSlotSet());
	}

	
	/*
	 * allocate a set of time slot to a set of sensors with maximise utility
	 */
	public void maxGainAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
	{
		
	}
	
}
