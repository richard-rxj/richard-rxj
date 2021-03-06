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
import alg.pc.alg.anu.au.DApproAllocate;

public class JTestWork_Slot_N {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws RuntimeException 
	 */
	public static void main(String[] args) throws RuntimeException, IOException {
		// TODO Auto-generated method stub
        DecimalFormat df=new DecimalFormat("#.0000");
        
        
        int[] networkSizeSet={100,200,300,400,500,600};
		int speed=30;    //5   10
		int[] tauSet={1,2,4,6,8,10};    //1,2,4,8,16
        int cishu=ExperimentSetting.cishu;
		String[] algSet={"CAppro","DAppro"};
		
		String tFileName="test/journal-data/";
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
			pwAlgSet[i]=new PrintWriter(new OutputStreamWriter(new FileOutputStream(tFileName+algSet[i]+"-Slot-N-Speed"+speed+".txt",true)));
		}
		
		
		for(int tN=0;tN<networkSizeSet.length;tN++)
		{
			int tNetworkSize=networkSizeSet[tN];
			
			for(int i=0;i<algSet.length;i++)
			{
				pwAlgSet[i].print(tNetworkSize);
				pwAlgSet[i].flush();
			}
			
			
			
			
			for(int tS=0;tS<tauSet.length;tS++)
			{
                ExperimentSetting.unitSlot=tauSet[tS];

			
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

		for(int i=0;i<algSet.length;i++)
		{
			pwAlgSet[i].close();
		}
	}

}
