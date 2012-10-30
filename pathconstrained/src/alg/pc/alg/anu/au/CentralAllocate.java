package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;
import util.pc.alg.anu.au.UtilityGainComparator;

public class CentralAllocate extends Allocate {

	public CentralAllocate(String sensorTxt, double speed)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		maxGainAllocate(super.getgNet().getSensorSet(), super.getgNet().getTimeSlotSet());
	}

	
	/*
	 * allocate a set of time slot to a set of sensors with maximise utility
	 */
	private void maxGainAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
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
				double tDistance=CommonFacility.computeDistance(tSlot.getX(),tSlot.getY(), tSensor.getX(),tSensor.getY());
				if(tDistance<=ExperimentSetting.transRange)
				{
					if(tSensor.getResidualBudget()>=(ExperimentSetting.eCom*ExperimentSetting.unitSlot))
					{
						tList.add(tSensor);
						double tSlotData=ExperimentSetting.getSlotData(tSensor, tSlot);
						tSensor.updateUtilityGain(tSlotData);
					}
				}
			}
			
			if(tList.size()==0)
			{
				continue;
			}
			/*
			 * choose a slot with maxGain
			 */
			Object[] tSet=tList.toArray();
			UtilityGainComparator tCom=new UtilityGainComparator(false);
			Arrays.sort(tSet, tCom);
			SensorNode cSensor=(SensorNode)tSet[0];
			
			
			/*
			 * allocate and update
			 */
			//double cDistance=CommonFacility.computeDistance(tSlot, cSensor);
			double cSlotData=ExperimentSetting.getSlotData(cSensor, tSlot);
			cSensor.update(tSlot.getId(), cSlotData);
			
		}
	}
	
	
}
