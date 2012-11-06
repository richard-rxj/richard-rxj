package util.pc.alg.anu.au;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;



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
     * A method to randomize a value based on a Poisson distribution.
     * @param lambda The starting value.
     * @return The integer which forms part of the distribution
     * See pp 65 of Simulation and the Monte Carlo Method by
     * Rubinstein and Kroese
     */
    private static int poissonDistribute(int lambda)
    {
        int n = 1;
        double a = 1.0;
        Random r = new Random();

        while(true)
        {
            a *= r.nextDouble();
            if(a < Math.exp((double)-lambda)) break;
            n += 1;
        }
        return n - 1;
    }
	
	
	
	
	private static void generatePoissonTopology(int nSize, String nFile) throws IOException
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
	
	
	private static void generateSquareGridTopology(int nSize, String nFile) throws IOException
	{
		DecimalFormat df=new DecimalFormat("#.000000");
		PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(nFile)));
		
		
		double tInterval=Math.sqrt(ExperimentSetting.xRange*ExperimentSetting.yRange/nSize);
		

		for(double  x=ExperimentSetting.roadBeginX+0.5*tInterval;x<ExperimentSetting.roadEndX;x=x+tInterval)
		{
			for(double y=0+0.5*tInterval;y<ExperimentSetting.yRange;y=y+tInterval)
			{
				double e=ExperimentSetting.harvestRate[0]+ExperimentSetting.ran.nextDouble()*(ExperimentSetting.harvestRate[1]-ExperimentSetting.harvestRate[0]);
				
				pw.println(df.format(x)+" "+df.format(y)+" "+df.format(e));
				pw.flush();
			}
		}
		
		
		
		
		pw.flush();
		pw.close();
		
		
	}
	
	private static void generateLinearTopology(int nSize, String nFile, double distance) throws IOException
	{
		DecimalFormat df=new DecimalFormat("#.000000");
		PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(nFile)));
		
		double tInterval=ExperimentSetting.xRange/((nSize/2)-1);
		
		/*
		 * fill one side
		 */
		double y=ExperimentSetting.roadBeginY+distance;
		double x=ExperimentSetting.roadBeginX;
		for(int i=0;i<nSize/2;i++)
		{
			if(x>ExperimentSetting.roadEndX)
			{
				x=ExperimentSetting.roadEndX;
			}
			double e=ExperimentSetting.harvestRate[0]+ExperimentSetting.ran.nextDouble()*(ExperimentSetting.harvestRate[1]-ExperimentSetting.harvestRate[0]);				
			pw.println(df.format(x)+" "+df.format(y)+" "+df.format(e));
			pw.flush();
			x=x+tInterval;
		}
		
		/*
		 * fill the other side;
		 */
		y=ExperimentSetting.roadBeginY-distance;
		x=ExperimentSetting.roadBeginX;
		for(int i=0;i<nSize/2;i++)
		{
			if(x>ExperimentSetting.roadEndX)
			{
				x=ExperimentSetting.roadEndX;
			}
			double e=ExperimentSetting.harvestRate[0]+ExperimentSetting.ran.nextDouble()*(ExperimentSetting.harvestRate[1]-ExperimentSetting.harvestRate[0]);				
			pw.println(df.format(x)+" "+df.format(y)+" "+df.format(e));
			pw.flush();
			x=x+tInterval;
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

		ExperimentSetting.roadEndX=2000;
		ExperimentSetting.xRange=2000;
		ExperimentSetting.cishu=10;
		int[] networkSizeSet={20,30,40,50,60,70,80,90,100};    //for matlab
		
		//int[] networkSizeSet={100,200,300,400,500,600};   
		int cishu=ExperimentSetting.cishu;
		
		
		String tFileName="test/matlab/topology/";
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
				TopologyFactory.generateTopology(tnetworkSize, nFile);    //Random
				System.out.println("execute "+"[node-"+tnetworkSize+" j-"+j+"]");
			}
		}	
		

		
	}

}
