/**
 * 
 */
package test.pc.alg.anu.au;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import alg.pc.alg.anu.au.Allocate;
import alg.pc.alg.anu.au.CApproAllocate;
import alg.pc.alg.anu.au.DApproAllocate;

import util.pc.alg.anu.au.CommonFacility;
import util.pc.alg.anu.au.ExperimentSetting;

import model.pc.alg.anu.au.Network;
import model.pc.alg.anu.au.SensorNode;
import model.pc.alg.anu.au.TimeSlotNode;

/**
 * @author user
 *
 */
public class JTestWork_Matlab {

	private static void preIntegerProgram(ArrayList<SensorNode> sensorSet, ArrayList<TimeSlotNode> slotSet, String fFile, String aFile, String bFile, String aEqFile, String bEqFile) throws IOException
	{
		
		
		PrintWriter pwF=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fFile,true)));
		PrintWriter pwA=new PrintWriter(new OutputStreamWriter(new FileOutputStream(aFile,true)));
		PrintWriter pwB=new PrintWriter(new OutputStreamWriter(new FileOutputStream(bFile,true)));
		PrintWriter pwAEq=new PrintWriter(new OutputStreamWriter(new FileOutputStream(aEqFile,true)));
		PrintWriter pwBEq=new PrintWriter(new OutputStreamWriter(new FileOutputStream(bEqFile,true)));
        
		int gDim=sensorSet.size()*slotSet.size();
		
		
		/*
		 * constraint for sensors
		 */
		for(int i=0; i<sensorSet.size();i++)
		{
			SensorNode tSensor=sensorSet.get(i);
			
			pwB.println(Math.floor(tSensor.getEnergyBudget()/ExperimentSetting.eCom));   //  capacity for each sensor
			pwBEq.println(0);                                                             // non avaible slots
			
			int[] tA=new int[gDim];
			int[] tAEq=new int[gDim];
			Arrays.fill(tA, 0);
			Arrays.fill(tAEq, 0);
			
			for(int j=0; j<slotSet.size();j++)
			{
				TimeSlotNode tSlot=slotSet.get(j);
				double tSlotData=ExperimentSetting.getSlotData(tSensor, tSlot);
				
				pwF.println((-1)*Math.floor(tSlotData));

				tA[i*slotSet.size()+j]=1;
				if(tSlotData<=0)
				{
					tAEq[i*slotSet.size()+j]=1;
				}
			}

			for(int j=0;j<tA.length;j++)
			{
				pwA.print(tA[j]+" ");
			}
			pwA.println();
			
			
			for(int j=0;j<tAEq.length;j++)
			{
				pwAEq.print(tAEq[j]+" ");
			}
			pwAEq.println();
			
			
		}
		
		
		/*
		 * constraint for timeslots
		 */
		for(int j=0;j<slotSet.size();j++)
		{
			pwB.println(1);
			
			int[] tA=new int[gDim];
			Arrays.fill(tA, 0);
			
			for(int i=0;i<sensorSet.size();i++)
			{
				tA[i*slotSet.size()+j]=1;
			}
			
			for(int i=0;i<tA.length;i++)
			{
				pwA.print(tA[i]+" ");
			}
			pwA.println();
		}
		
		
		pwF.flush();
		pwF.close();
		pwA.flush();
		pwA.close();
		pwB.flush();
		pwB.close();
		pwAEq.flush();
		pwAEq.close();
		pwBEq.flush();
		pwBEq.close();
		
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws RuntimeException 
	 */
	public static void main(String[] args) throws RuntimeException, IOException {
		// TODO Auto-generated method stub

		DecimalFormat df=new DecimalFormat("#.0000");
        
        
        int[] networkSizeSet={20,30,40,50,60};
		double speed=2;
        int cishu=ExperimentSetting.cishu;
		String[] algSet={"CAppro","DAppro"};
		
		String tFileName="test/matlab/journal-data/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		System.out.println("begin...");
		
		/*
		 * initial writers
		 */
		
		PrintWriter[]  pwAlgSet=new PrintWriter[algSet.length];
		for(int i=0;i<algSet.length;i++)
		{
			pwAlgSet[i]=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tFileName+algSet[i]+"-Matlab.txt",true)));
		}
		
		
		for(int tN=0;tN<networkSizeSet.length;tN++)
		{
			int tNetworkSize=networkSizeSet[tN];
			
			/*
			 * Appro algorithms
			 */
			for(int i=0;i<algSet.length;i++)
			{
				pwAlgSet[i].print(tNetworkSize);
				pwAlgSet[i].flush();
			}
			
				for(int tA=0;tA<algSet.length;tA++)
				{
					Allocate tAllo=null;
					double tUtility=0;
					
					for(int tC=0;tC<cishu;tC++)
					{
						String tSensorTxt="test/matlab/topology/node-"+tNetworkSize+"-"+tC+".txt";
						
						switch(tA)
						{
						   case 0:
							      tAllo=new CApproAllocate(tSensorTxt,speed);break;
						   case 1:
							      tAllo=new DApproAllocate(tSensorTxt,speed,ExperimentSetting.transRange);break;

						}
						
						tAllo.schedule();
					    	
						tUtility=tUtility+tAllo.getNetworkUtility();
					}
					tUtility=tUtility/cishu;
	
					/*
					 * output to files
					 */
					
					pwAlgSet[tA].print(" "+df.format(tUtility));
					pwAlgSet[tA].flush();
					
				}
			
			
			for(int i=0;i<algSet.length;i++)
			{
				pwAlgSet[i].println();
				pwAlgSet[i].flush();
			}
			
			
			
			/*
			 * matlab file
			 */
			for(int tC=0;tC<cishu;tC++)
			{
				String tSensorTxt="test/matlab/topology/node-"+tNetworkSize+"-"+tC+".txt";
				Network tNet=CommonFacility.getNetwork(tSensorTxt, speed);
				
				String tmFileName="test/matlab/mfiles/";
				File tmf=new File(tmFileName);
				if(!tmf.exists())
				{
					tmf.mkdirs();
				}
				String tFFile=tmFileName+"f-"+tNetworkSize+"-"+tC+".txt";
				String tBFile=tmFileName+"b-"+tNetworkSize+"-"+tC+".txt";
				String tAFile=tmFileName+"a-"+tNetworkSize+"-"+tC+".txt";
				String tBEqFile=tmFileName+"beq-"+tNetworkSize+"-"+tC+".txt";
				String tAEqFile=tmFileName+"aeq-"+tNetworkSize+"-"+tC+".txt";
				JTestWork_Matlab.preIntegerProgram(tNet.getSensorSet(), tNet.getTimeSlotSet(), tFFile, tAFile, tBFile, tAEqFile, tBEqFile);
			}
			
			
		}	
		
		
		/*
		 * close printwriters
		 */

		for(int i=0;i<algSet.length;i++)
		{
			pwAlgSet[i].close();
		}
	}

}
