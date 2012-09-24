package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;

import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;

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
		for(int i=0;i<timeSlotSet.size();i++)
		{
			TimeSlotNode tSlot=timeSlotSet.get(i);
			ArrayList<SensorNode> tList=new ArrayList<SensorNode>();
			
			/*
			 * fill tList
			 */
			for(int j=0;j<sensorSet.size();j++)
			{
				SensorNode tSensor=sensorSet.get(j);
				double tDistance=CommonFacility.computeDistance(tSlot, tSensor);
				if(tDistance<=ExperimentSetting.transRange)
				{
					if(tSensor.getResidualBudget()>=(ExperimentSetting.eCom*ExperimentSetting.unitSlot))
					{
						tList.add(tSensor);
						double tTransRate=ExperimentSetting.getTransRate(tDistance);
						tSensor.updateUtilityGain(ExperimentSetting.unitSlot*tTransRate);
					}
				}
			}
			
			
			if(tList.size()==0)
			{
				continue;
			}
			
			/*
			 * choose a slot randomly
			 */
			Object[] tSet=tList.toArray();
			SensorNode cSensor=(SensorNode)tSet[ExperimentSetting.ran.nextInt(tSet.length)];
			
			
			/*
			 * allocate and update
			 */
			double cDistance=CommonFacility.computeDistance(tSlot, cSensor);
			double cTransRate=ExperimentSetting.getTransRate(cDistance);
			cSensor.update(tSlot.getId(), cTransRate);
			
		}
	}
	
}
