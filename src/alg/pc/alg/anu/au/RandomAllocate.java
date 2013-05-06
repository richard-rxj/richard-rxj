package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;

import model.pc.alg.anu.au.AllocationPair;
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
				AllocationPair tp=ExperimentSetting.getSlotPart(tSensor, tSlot);
				
				
				if((tp.getSlotData()>0)&&(tSensor.getResidualBudget()>=tp.getEnergyCost()))
				{
					tList.add(tSensor);
					tSensor.updateUtilityGain(tp.getSlotData());
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
			//double cDistance=CommonFacility.computeDistance(tSlot, cSensor);
			AllocationPair cp=ExperimentSetting.getSlotPart(cSensor, tSlot);
			cSensor.update(tSlot.getId(),cp.getSlotData(),cp.getEnergyCost());
			
		}
	}
	
}
