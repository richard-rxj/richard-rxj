package alg.pc.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import model.pc.alg.anu.au.AllocationPair;
import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;
import util.pc.alg.anu.au.AllocationPairComparator;
import util.pc.alg.anu.au.ExperimentSetting;
import util.pc.alg.anu.au.SensorXComparator;

public class CGAPAllocate extends Allocate {

	public CGAPAllocate(String sensorTxt, double speed)
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
		gapAllocate(super.getgNet().getSensorSet(), super.getgNet().getTimeSlotSet());
	}

	
	public static void gapAllocate(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> timeSlotSet)
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
				
				/*
				 * only consider time slots with positive profit
				 */
				if(tp.getSlotData()>0)
				{
					p.add(tp);
				}
			}
			
			/*
			 *   using knapsack algorithm to allocate time slots to this sensor
			 */			
			ArrayList<AllocationPair> tSelected=CGAPAllocate.approKnapsack(v, p, ExperimentSetting.epsilon);
		    
			
			
			for(int j=0;j<tSelected.size();j++)
			{
				AllocationPair tp=tSelected.get(j);
				int vPrevious=tVector[tp.getSlotID()];    // the slotID is Tid
				TimeSlotNode cSlot=timeSlotSet.get(tp.getSlotID());
					
					if(vPrevious<0)    //first allocate
					{						
						v.update(cSlot.getId(), tp.getSlotData(), tp.getEnergyCost());
					}
					else               //reallocate
					{
						v.update(cSlot.getId(), tp.getSlotData(), tp.getEnergyCost());
						SensorNode tPreSensor=sensorSet.get(vPrevious);
						tPreSensor.restore(cSlot.getId());
					}
					
					
					tVector[tp.getSlotID()]=v.getTid();					
				
				
			}
		}
				
	}
	
	
	/*
	 * FPTAS algorithm with a parameter epsilon---------Z*-z =< epsilon * (Z*) 
	 */
	private static ArrayList<AllocationPair> approKnapsack(SensorNode tSensor, ArrayList<AllocationPair> tSlotPairSet, double epsilon)
	
	{
		  ArrayList<AllocationPair> resultSet=new ArrayList<AllocationPair>();
		  if(tSlotPairSet.size()==0)   //exception:  no slots
		  {
			  return resultSet;
		  }
		  
		  /*
		   * find critical item s
		   */
		  int s=0;
		  Collections.sort(tSlotPairSet, new AllocationPairComparator(false));
		  double tBinSize=tSensor.getResidualBudget();
		  for(int i=0; i<tSlotPairSet.size();i++)
		  {
			  AllocationPair tp=tSlotPairSet.get(i);
			  resultSet.add(tp);
			  tBinSize=tBinSize-tp.getEnergyCost();
			  if(tBinSize==0)
			  {
				  return resultSet;
			  }
			  if(tBinSize<0)
			  {
				  s=i;
				  break;
			  }
		  }
		  if (s==0)
		  {
			  return resultSet;
		  }
		  
		  /*
		   * allocate based on epsilon
		   */
		  resultSet.clear();	  
			  
			  
			  
		 
		  
		  
		  
		  
		  return resultSet;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
