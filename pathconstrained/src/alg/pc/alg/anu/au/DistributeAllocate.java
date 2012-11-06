package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;
import util.pc.alg.anu.au.UtilityGainComparator;

import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

public class DistributeAllocate extends Allocate {

	
	
	public DistributeAllocate(String sensorTxt, double speed,double interRange)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
		
		/*
		 * reconfigure interval according to speed-----every interval length is R
		 */
		ExperimentSetting.interval=(int)Math.floor(interRange/(speed*ExperimentSetting.unitSlot));
	}

	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		ArrayList<SensorNode> gSensorSet=super.getgNet().getSensorSet();
		ArrayList<TimeSlotNode>  gSlotSet=super.getgNet().getTimeSlotSet();
		
		for(int i=0;i<gSlotSet.size();i=i+ExperimentSetting.interval)
		{
			TimeSlotNode tSlot=gSlotSet.get(i);
			
			/*
			 * initial neighbourList
			 */
			ArrayList<SensorNode>  tSensorSet=new ArrayList<SensorNode>();
			for(int j=0;j<gSensorSet.size();j++)
			{
				SensorNode tSensor=gSensorSet.get(j);
				double tDistance=CommonFacility.computeDistance(tSlot.getX(),tSlot.getY(), tSensor.getX(), tSensor.getY());
				if(tDistance<=ExperimentSetting.transRange)
				{
					tSensorSet.add(tSensor);
				}
			}
			
			
			/*
			 * initial slotList
			 */
			//int ti=i+1;  //the i slot used for communication
			int ti=i;      // ignore communication time
			int tEnd=i+ExperimentSetting.interval;
			ArrayList<TimeSlotNode>  tSlotSet=new ArrayList<TimeSlotNode>();
			while((ti<tEnd)&&(ti<gSlotSet.size()))
			{
				tSlotSet.add(gSlotSet.get(ti));
				ti++;
			}
			
			/*
			 * allocate for this interval
			 */
			this.maxGainAllocate(tSensorSet, tSlotSet);
		}
		
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