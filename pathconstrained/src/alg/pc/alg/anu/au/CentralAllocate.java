package alg.pc.alg.anu.au;

import java.io.IOException;

import util.pc.alg.anu.au.CommonFacility;

public class CentralAllocate extends Allocate {

	public CentralAllocate(String sensorTxt, double speed)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		CommonFacility.maxGainAllocate(super.getgNet().getSensorSet(), super.getgNet().getTimeSlotSet());
	}

}
