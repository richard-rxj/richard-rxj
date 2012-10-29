package util.pc.alg.anu.au;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;



public class TopologyFactory {

	
	
	public static void generateTopology(int nSize, String nFile) throws IOException
	{
		DecimalFormat df=new DecimalFormat("#.000000");
		PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(nFile)));
		
		for(int i=0;i<nSize;i++)
		{
			double x=ExperimentSetting.ran.nextDouble()*ExperimentSetting.xRange;
			double y=ExperimentSetting.ran.nextDouble()*ExperimentSetting.yRange;
			double e=ExperimentSetting.harvestRate[0]+ExperimentSetting.ran.nextDouble()*(ExperimentSetting.harvestRate[1]-ExperimentSetting.harvestRate[0]);
				
			pw.println(df.format(x)+" "+df.format(y)+" "+df.format(e));
			pw.flush();
		}
		
		
		
		
		pw.flush();
		pw.close();
		
		
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		int[] networkSizeSet={100,200,300,400,500,600,700,800,900,1000,1500,2000,2500,3000};
		int cishu=ExperimentSetting.cishu;
		
		
		String tFileName="test/topology/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		for(int i=0;i<networkSizeSet.length;i++)
		{
			int tnetworkSize=networkSizeSet[i];
			
			for(int j=0;j<cishu;j++)
			{
				String nFile=tFileName+"node-"+tnetworkSize+"-"+j+".txt";
				TopologyFactory.generateTopology(tnetworkSize, nFile);
				System.out.println("execute "+"[node-"+tnetworkSize+" j-"+j+"]");
			}
		}	
		
	}

}
