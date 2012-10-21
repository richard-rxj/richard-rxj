/**
 * 
 */
package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import util.pc.alg.anu.au.AllocationPairComparator;
import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;

import model.pc.alg.anu.au.AllocationPair;
import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

/**
 * @author u4964526
 *
 */
public class CApproAllocate extends Allocate {

	/**
	 * @param sensorTxt
	 * @param speed
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public CApproAllocate(String sensorTxt, double speed)
			throws RuntimeException, IOException {
		super(sensorTxt, speed);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see alg.pc.alg.anu.au.Allocate#schedule()
	 */
	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		approAllocate(super.getgNet().getSensorSet(), super.getgNet().getTimeSlotSet());
	}

	
	private void approAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
	{
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
				SensorNode v=sensorSet.get(j);
				v.update(i, rateMatrix[i][j]);
			}
		}
		
		
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        double[][] t= new double[2][3];
        System.out.println(t.length);
        System.out.println(t[1].length);
        
        double[] s={2, 3,1};
        Arrays.sort(s);
        for(int i=0; i<s.length;i++)
        		System.out.println(s[i]);
        
        int k=(int)Math.floor(2.1);
        System.out.println(k);
	}

}
