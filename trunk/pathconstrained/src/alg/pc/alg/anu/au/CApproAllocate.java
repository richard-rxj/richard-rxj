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
import util.pc.alg.anu.au.SensorXComparator;

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

	
	public static void approAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
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
			timeSlotSet.get(i).setTid(i);    //reindex
		}
		
		for(int i=0; i<sensorSet.size();i++)
		{
			sensorSet.get(i).setTid(i);      //reindex
		}
		
		/*
		 * initial rate matrix
		 */
		AllocationPair[][] rateMatrix=new AllocationPair[timeSlotSet.size()][sensorSet.size()];
		for(int i=0; i<rateMatrix.length; i++)
		{
			TimeSlotNode  t=timeSlotSet.get(i);			
			for(int j=0; j<rateMatrix[i].length;j++)
			{
				SensorNode v=sensorSet.get(j);
				//double tDistance=CommonFacility.computeDistance(v, t);
				rateMatrix[t.getTid()][v.getTid()]=ExperimentSetting.getSlotPart(v, t);
			}
		}

		
		
		/*
		 * allocate 
		 */
		for(int i=0;i<sensorSet.size();i++)
		{
			SensorNode v=sensorSet.get(i);
			
			//System.out.println(v);   //debug
			/* 
			 *   construct profit function --- here use all the slots
			 */
			ArrayList<AllocationPair> p=new ArrayList<AllocationPair>();
			for(int j=0; j<timeSlotSet.size();j++)
			{
				TimeSlotNode t=timeSlotSet.get(j);
				AllocationPair tp=new AllocationPair();
				tp.setSlotID(t.getTid());
				tp.setEnergyCost(rateMatrix[t.getTid()][v.getTid()].getEnergyCost());
				if(tVector[t.getTid()]>=0)
				{					
					double tr=rateMatrix[t.getTid()][v.getTid()].getSlotData()-rateMatrix[t.getTid()][tVector[t.getTid()]].getSlotData();
					tp.setSlotData(tr);
					
				}
				else
				{
					double tr=rateMatrix[t.getTid()][v.getTid()].getSlotData();
					tp.setSlotData(tr);
				}
				p.add(tp);
			}
			/*
			 *   sorting and selecting time slots
			 */
			Collections.sort(p,new AllocationPairComparator(false));   //descending order
			
			//double energyBudget=v.getResidualBudget();
			//int slotBudget=(int)Math.floor(energyBudget/(ExperimentSetting.eCom*ExperimentSetting.unitSlot));
		    /*
		     *   tuning the slot budget. For example, if the slotbudget is 2.3, it will be 3 with 30% probability
		     */
//			double tSlotBudgetVar=energyBudget/(ExperimentSetting.eCom*ExperimentSetting.unitSlot)-slotBudget;
//			if(Math.random()<tSlotBudgetVar)
//			{
//				slotBudget++;
//			}
			
			
			for(int j=0;j<p.size();j++)
			{
				AllocationPair tp=p.get(j);
				if((tp.getSlotData()>0) && (v.getResidualBudget()>tp.getEnergyCost()))
				{
					//slotBudget--;
					/*
					 * update sensors' information according to the final allocation
					 */
					int vPrevious=tVector[tp.getSlotID()];
					TimeSlotNode cSlot=timeSlotSet.get(tp.getSlotID());
					
					if(vPrevious<0)    //first allocate
					{						
						v.update(cSlot.getId(), tp.getSlotData(),tp.getEnergyCost());
					}
					else               //reallocate
					{
						v.update(cSlot.getId(), tp.getSlotData(),tp.getEnergyCost());
						SensorNode tPreSensor=sensorSet.get(vPrevious);
						tPreSensor.restore(cSlot.getId());
					}
					
					
					tVector[tp.getSlotID()]=v.getTid();					
				}
				else
				{
					break;
				}
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
