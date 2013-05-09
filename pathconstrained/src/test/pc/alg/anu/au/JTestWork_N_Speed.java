package test.pc.alg.anu.au;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import util.pc.alg.anu.au.ExperimentSetting;
import alg.pc.alg.anu.au.Allocate;
import alg.pc.alg.anu.au.CApproAllocate;
import alg.pc.alg.anu.au.CGAPAllocate;
import alg.pc.alg.anu.au.CentralAllocate;
import alg.pc.alg.anu.au.DApproAllocate;
import alg.pc.alg.anu.au.DGAPAllocate;
import alg.pc.alg.anu.au.DistributeAllocate;
import alg.pc.alg.anu.au.RandomAllocate;

public class JTestWork_N_Speed {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws RuntimeException 
	 */
	public static void main(String[] args) throws RuntimeException, IOException {
		// TODO Auto-generated method stub

		
		DecimalFormat df=new DecimalFormat("#.0000");
        
        
        int[] networkSizeSet={100,200,300,400,500,600};
		double[] speedSet={5,10,30};
		ExperimentSetting.unitSlot=1;
		int tau=ExperimentSetting.unitSlot;
        //int cishu=1;
		int cishu=ExperimentSetting.cishu;
		String[] algSet={"CGAP","DGAP"};
		
		String tFileName="test/journal-data/GAP/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		System.out.println("begin...");
		
		/*
		 * initial writers
		 */
		PrintWriter[]  pwSpeedSet=new PrintWriter[speedSet.length];
		for(int i=0;i<speedSet.length;i++)
		{
			pwSpeedSet[i]=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tFileName+"Speed-"+speedSet[i]+"-slot"+tau+".txt",true)));
		}
		
		PrintWriter[]  pwAlgSet=new PrintWriter[algSet.length];
		for(int i=0;i<algSet.length;i++)
		{
			pwAlgSet[i]=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tFileName+algSet[i]+"-R-slot"+tau+".txt",true)));
		}
		
		
		for(int tN=0;tN<networkSizeSet.length;tN++)
		{
			int tNetworkSize=networkSizeSet[tN];
			
			for(int i=0;i<speedSet.length;i++)
			{
				pwSpeedSet[i].print(tNetworkSize);
				pwSpeedSet[i].flush();
			}
			for(int i=0;i<algSet.length;i++)
			{
				pwAlgSet[i].print(tNetworkSize);
				pwAlgSet[i].flush();
			}
			
			
			
			
			for(int tS=0;tS<speedSet.length;tS++)
			{
				double tSpeed=speedSet[tS];

			
				for(int tA=0;tA<algSet.length;tA++)
				{
					Allocate tAllo=null;
					double tUtility=0;
					
					for(int tC=0;tC<cishu;tC++)
					{
						String tSensorTxt="test/topology/node-"+tNetworkSize+"-"+tC+".txt";
						
						switch(tA)
						{
						   case 0:
							      tAllo=new CGAPAllocate(tSensorTxt,tSpeed);
							      break;
						   case 1:
							      tAllo=new DGAPAllocate(tSensorTxt,tSpeed,ExperimentSetting.transRange);
							      break;
						   case 2:
							      tAllo=new CentralAllocate(tSensorTxt,tSpeed);
							      break;
						   case 3:
							      tAllo=new DistributeAllocate(tSensorTxt,tSpeed,ExperimentSetting.transRange);
							      break;
						   case 4:
							      tAllo=new RandomAllocate(tSensorTxt,tSpeed);
							      break;
						}
						
						tAllo.schedule();
					    	
						tUtility=tUtility+tAllo.getNetworkUtility();
					}
					tUtility=tUtility/cishu;
	
					/*
					 * output to files
					 */
					pwSpeedSet[tS].print(" "+df.format(tUtility));
					pwSpeedSet[tS].flush();
					
					pwAlgSet[tA].print(" "+df.format(tUtility));
					pwAlgSet[tA].flush();
					
				}
			}
			
			
			for(int i=0;i<speedSet.length;i++)
			{
				pwSpeedSet[i].println();
				pwSpeedSet[i].flush();
			}
			for(int i=0;i<algSet.length;i++)
			{
				pwAlgSet[i].println();
				pwAlgSet[i].flush();
			}
			
		}	
		
		
		/*
		 * close printwriters
		 */
		for(int i=0;i<speedSet.length;i++)
		{
			pwSpeedSet[i].close();
		}
		for(int i=0;i<algSet.length;i++)
		{
			pwAlgSet[i].close();
		}
		
	}

}
