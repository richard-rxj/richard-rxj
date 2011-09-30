package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

public class NodeDataGenerator {

	private double dThreshold = 0.9;
	private double seed = 50;
	private double step = 5;
	private int nodeSum=100;
	private int dataSum=100;
	private int stepGroup=10;
	
	public double getdThreshold() {
		return dThreshold;
	}

	public void setdThreshold(double dThreshold) {
		this.dThreshold = dThreshold;
	}

	public double getSeed() {
		return seed;
	}

	public void setSeed(double seed) {
		this.seed = seed;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public int getNodeSum() {
		return nodeSum;
	}

	public void setNodeSum(int vertexSum) {
		this.nodeSum = vertexSum;
	}

	public int getDataSum() {
		return dataSum;
	}

	public void setDataSum(int dataSum) {
		this.dataSum = dataSum;
	}

	public int getStepGroup() {
		return stepGroup;
	}

	public void setStepGroup(int stepGroup) {
		this.stepGroup = stepGroup;
	}

	private double[] subData(double[] master)
	{
		double[] result=new double[dataSum];
		
		if(master==null)
		{
			double tSeed=Math.random()*this.seed;
			int tGroup=0;
			for(int i=0;i<result.length;i++)
			{
				tGroup++;
				if(tGroup>this.stepGroup)
				{
					tGroup=0;
					tSeed=tSeed+this.step;
				}
				result[i]=tSeed+Math.random()*this.step;
			}
		}
		else
		{
			for(int i=0;i<result.length;i++)
			{
				result[i]=master[i]*(this.dThreshold+Math.random()*(1-this.dThreshold));
			}
		}
		return result;
	}
	
	public double[][] dataGenerator(String weightFile,String dataFile)
	{
		double[][] result=new double[nodeSum][dataSum];
		try
		{
			
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(weightFile)));
			String tempString;
			int lineNum=0;
			
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<dataSum;j++)
				{
					result[i][j]=-1;
				}
			}
			
			while((tempString=bf.readLine())!=null)
			{
				String[] temp=tempString.split(" ");
				if(Double.valueOf(temp[1])>0.95)
				{
					if(result[Integer.valueOf(temp[0])-1][0]==-1)
					{
						result[Integer.valueOf(temp[0])-1]=subData(null);
				
					}
				}
				else
				{
					
						if(result[Integer.valueOf(temp[2])-1][0]==-1)
						{
							result[Integer.valueOf(temp[2])-1]=subData(null);
							
						}
						if(result[Integer.valueOf(temp[0])-1][0]==-1)
						{
							result[Integer.valueOf(temp[0])-1]=subData(result[Integer.valueOf(temp[2])-1]);
						}
						//Begin of Debug
						//System.out.println(result[Integer.valueOf(temp[0])-1][0]+" : "+result[Integer.valueOf(temp[2])-1][0]);
						
						//End of Debug
						
						
				}
				lineNum++;
			}
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile)));
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<dataSum;j++)
				{
					pw.print(result[i][j]+" ");
				}
				pw.println();
			}
			pw.flush();
			pw.close();
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NodeDataGenerator dGenerator= new NodeDataGenerator();
		dGenerator.setDataSum(100);
		dGenerator.setNodeSum(99);
		String fData="test/real/data.txt";
		String fWeight="test/real/weight.txt";
		dGenerator.dataGenerator(fWeight, fData);
	}

}
