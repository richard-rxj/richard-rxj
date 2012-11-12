package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import model.pc.alg.anu.au.AllocationPair;
import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;
import util.pc.alg.anu.au.UtilityGainComparator;

public class CentralAllocate extends Allocate {
	/*
	 * heuristic
	 */

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
	public static void maxGainAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
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
			 * choose a slot with maxGain
			 */
			Object[] tSet=tList.toArray();
			UtilityGainComparator tCom=new UtilityGainComparator(false);
			Arrays.sort(tSet, tCom);
			SensorNode cSensor=(SensorNode)tSet[0];
			
			
			/*
			 * allocate and update
			 */
			AllocationPair cp=ExperimentSetting.getSlotPart(cSensor, tSlot);
			cSensor.update(tSlot.getId(),cp.getSlotData(),cp.getEnergyCost());
			
		}
	}
	
	
}
