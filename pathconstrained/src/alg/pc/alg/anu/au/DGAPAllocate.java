package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import model.pc.alg.anu.au.AllocationPair;
import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;
import util.pc.alg.anu.au.AllocationPairComparator;
import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;
import util.pc.alg.anu.au.SensorXComparator;

public class DGAPAllocate extends Allocate {

	public DGAPAllocate(String sensorTxt, double speed, double range)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
		ExperimentSetting.interval=(int)Math.round(range/(speed*ExperimentSetting.unitSlot));
	}

	/* (non-Javadoc)
	 * @see alg.pc.alg.anu.au.Allocate#schedule()
	 */
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
				double tDistance=CommonFacility.computeDistance(tSlot.getX(),tSlot.getY(), tSensor.getX(),tSensor.getY());
				if(tDistance<=ExperimentSetting.transRange)
				{					
					tSensorSet.add(tSensor);
				}
			}
			
			
			/*
			 * initial slotList
			 */
			//int ti=i+1;  //the i slot used for communication
			int ti=i;      //assume communication time is ignore
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
			CGAPAllocate.gapAllocate(tSensorSet, tSlotSet);
		}
	}

	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i=(int)Math.round(2.5);
		System.out.println(i);
		System.out.println(Math.rint(2.5));
	}

}
