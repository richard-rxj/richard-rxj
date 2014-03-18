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

	/*
	 * distributed  heuristic 
	 */
	
	public DistributeAllocate(String sensorTxt, double speed,double interRange)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
		
		/*
		 * reconfigure interval according to speed-----every interval length is R
		 */
		ExperimentSetting.interval=(int)Math.round(interRange/(speed*ExperimentSetting.unitSlot));
	}

	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		ExperimentSetting.log.info("DistributedAllocate  start---------------------------------------");
		
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
			CentralAllocate.maxGainAllocate(tSensorSet, tSlotSet);
			
			ExperimentSetting.log.info("DistributedAllocate  end---------------------------------------");
		}
		
	}

	
	
	
}
