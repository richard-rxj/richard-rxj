/**
 * 
 */
package test.pc.alg.anu.au;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;
import util.pc.alg.anu.au.ExperimentSetting;
import alg.pc.alg.anu.au.Allocate;
import alg.pc.alg.anu.au.CGAPAllocate;
import alg.pc.alg.anu.au.CentralAllocate;
import alg.pc.alg.anu.au.DGAPAllocate;
import alg.pc.alg.anu.au.DistributeAllocate;
import alg.pc.alg.anu.au.RandomAllocate;

/**
 * @author u4964526
 *
 */
public class SimpleCase {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
        DecimalFormat df=new DecimalFormat("#.0000");
        
        

		double speed=10;
		ExperimentSetting.unitSlot=1;
		int tau=ExperimentSetting.unitSlot;
        //int cishu=1;
		int cishu=1;
		String[] algSet={"CGAP","CGreedy"};
		
		String tFileName="test/journal-data/simpleCase/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		System.out.println("begin...");
		
		/*
		 * initial network data
		 */
		ArrayList<SensorNode>  sensorSet1=new ArrayList<SensorNode>();
		
		SensorNode s1=new SensorNode();
		s1.setId(100);
		s1.setX(20);
		s1.setY(298);
		s1.setEnergyBudget(0.66);
		sensorSet1.add(s1);
		
		s1=new SensorNode();
		s1.setId(101);
		s1.setX(30);
		s1.setY(198);
		s1.setEnergyBudget(0.55);
		sensorSet1.add(s1);
		
		
		ArrayList<TimeSlotNode> timeSlotSet1=new ArrayList<TimeSlotNode>();
		
		
		double x=0;
		double x2=x+speed*tau;
		for(int i=1;i<=6;i++)  {
			TimeSlotNode t=new TimeSlotNode();
			t.setId(i);
			t.setX(x);
			t.setX2(x2);
			t.setY(180);
			timeSlotSet1.add(t);
			
			x=x2;
			x2=x+speed*tau;
		}
		
		
		
		
		
		
		ArrayList<SensorNode>  sensorSet2=new ArrayList<SensorNode>();
		s1=new SensorNode();
		s1.setId(100);
		s1.setX(20);
		s1.setY(298);
		s1.setEnergyBudget(0.66);
		sensorSet2.add(s1);
		
		s1=new SensorNode();
		s1.setId(101);
		s1.setX(30);
		s1.setY(198);
		s1.setEnergyBudget(0.55);
		sensorSet2.add(s1);
		
		ArrayList<TimeSlotNode> timeSlotSet2=new ArrayList<TimeSlotNode>(timeSlotSet1);
		
		
		/*
		 * initial writers
		 */


			
				for(int tA=0;tA<algSet.length;tA++)
				{

					double tUtility=0;
					
					for(int tC=0;tC<cishu;tC++)
					{

						
						switch(tA)
						{
						   case 0:
							      CGAPAllocate.gapAllocate(sensorSet1, timeSlotSet1);
							      tUtility=tUtility+Allocate.getNetworkUtility(sensorSet1);
							      break;

						   case 1:
							      CentralAllocate.maxGainAllocate(sensorSet2, timeSlotSet2);
							      tUtility=tUtility+Allocate.getNetworkUtility(sensorSet2);
							      break;

						}
						

					    	
						
					}
					tUtility=tUtility/cishu;
	
					/*
					 * output to files
					 */
					ExperimentSetting.log.info("!!!!!!!!!!"+algSet[tA]+" utility------------"+tUtility);
					
				}
			
			

	}

}
