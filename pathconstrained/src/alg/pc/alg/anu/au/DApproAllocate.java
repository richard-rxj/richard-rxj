/**
 * 
 */
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

/**
 * @author u4964526
 *
 */
public class DApproAllocate extends Allocate {

	/**
	 * @param sensorTxt
	 * @param speed
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public DApproAllocate(String sensorTxt, double speed)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
		/*
		 * reconfigure interval according to speed-----every interval length is R
		 */
		ExperimentSetting.interval=(int)Math.floor(ExperimentSetting.transRange/(speed*ExperimentSetting.unitSlot));
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
				double tDistance=CommonFacility.computeDistance(tSlot, tSensor);
				if(tDistance<=ExperimentSetting.transRange)
				{
					if(tSensor.getResidualBudget()>=(ExperimentSetting.eCom*ExperimentSetting.unitSlot))
					{
						tSensorSet.add(tSensor);
					}
				}
			}
			
			
			/*
			 * initial slotList
			 */
			//int ti=i+1;  //the i slot used for communication
			int ti=i;
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
			this.approAllocate(tSensorSet, tSlotSet);
		}
	}

	
	
	private void approAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
	{
        /*
         * sort sensor according to its X-index
         */
		Collections.sort(sensorSet, new SensorXComparator(true));
		
		/*
		 * initial T vector which indicate timeslot allocation
		 */
		int[] tVector=new int[timeSlotSet.size()];
		for(int i=0; i<tVector.length; i++)
		{
			tVector[i]=-1;
		}
		
		/*
		 * initial rate matrix
		 */
		double[][] rateMatrix=new double[timeSlotSet.size()][sensorSet.size()];
		for(int i=0; i<rateMatrix.length; i++)
		{
			TimeSlotNode  t=timeSlotSet.get(i);			
			for(int j=0; j<rateMatrix[i].length;j++)
			{
				SensorNode v=sensorSet.get(j);
				double tDistance=CommonFacility.computeDistance(v, t);
				rateMatrix[t.getId()][v.getId()]=ExperimentSetting.getTransRate(tDistance);
			}
		}
		
		
		/*
		 * allocate 
		 */
		for(int i=0;i<sensorSet.size();i++)
		{
			SensorNode v=sensorSet.get(i);
			/* 
			 *   construct profit function --- here use all the slots
			 */
			ArrayList<AllocationPair> p=new ArrayList<AllocationPair>();
			for(int j=0; j<timeSlotSet.size();j++)
			{
				TimeSlotNode t=timeSlotSet.get(j);
				if(tVector[t.getId()]>=0)
				{
					AllocationPair tp=new AllocationPair();
					tp.setSlotID(t.getId());
					double tr=rateMatrix[t.getId()][v.getId()]-rateMatrix[t.getId()][tVector[t.getId()]];
					tp.setTransRate(tr);
				}
				else
				{
					AllocationPair tp=new AllocationPair();
					tp.setSlotID(t.getId());
					double tr=rateMatrix[t.getId()][v.getId()];
					tp.setTransRate(tr);
				}
			}
			/*
			 *   sorting and selecting time slots
			 */
			Collections.sort(p,new AllocationPairComparator(false));   //descending order
			
			double energyBudget=v.getResidualBudget();
			int slotBudget=(int)Math.floor(energyBudget/(ExperimentSetting.eCom*ExperimentSetting.unitSlot));
			for(int j=0;j<p.size();j++)
			{
				AllocationPair tp=p.get(j);
				if((tp.getTransRate()>0) && (slotBudget>0))
				{
					slotBudget--;
					/*
					 * update sensors' information according to the final allocation
					 */
					int vPrevious=tVector[tp.getSlotID()];
					if(vPrevious<0)    //first allocate
					{						
						v.update(tp.getSlotID(), tp.getTransRate());
					}
					else               //reallocate
					{
						v.update(tp.getSlotID(), tp.getTransRate());
						SensorNode tPreSensor=sensorSet.get(vPrevious);
						tPreSensor.restore(tp.getSlotID(), rateMatrix[tp.getSlotID()][vPrevious]);
					}
					
					
					tVector[tp.getSlotID()]=v.getId();					
				}
				else
				{
					break;
				}
			}
		}
		
		
		
		/*
		 * update sensors' information according to the final allocation
		 */
		for(int i=0;i<tVector.length;i++)
		{
			int j=tVector[i];
			if(j>=0)
			{
				
			}
		}
		
		
		
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
