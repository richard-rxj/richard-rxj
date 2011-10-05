package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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
		double[] result=new double[2*dataSum];
		
		if(master==null)
		{
			double tSeed=Math.random()*this.seed;
			double tStep=Math.random()*this.step;
			int tGroup=0;
			for(int i=0;i<result.length;i++)
			{
				tGroup++;
				if(tGroup>this.stepGroup)
				{
					tGroup=0;
					tSeed=tSeed+tStep;
				}
				result[i]=tSeed+Math.random()*tStep;
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
	
	private double computeDataCorrelation(double[] u, double[] v)
	{
		double result=0;
		for(int i=1;i<u.length;i++)
		{
			double temp=Math.min(u[i], v[i])/Math.max(u[i], v[i]);
			result=result+temp;
		}
		
		
		return result/u.length;
	}
	
	public double[][] dataWeightGenerator(String weightFile,String dataFile,String fWeight)
	{
		double[][] result=new double[nodeSum][2*dataSum];
		try
		{
			
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(weightFile)));
			PrintWriter pwWeight=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fWeight)));
			String tempString;
			int lineNum=0;
			
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<2*dataSum;j++)
				{
					result[i][j]=-1;
				}
			}
			
			while((tempString=bf.readLine())!=null)
			{
				String[] temp=tempString.split(" ");
				pwWeight.print(temp[0]+" ");
				if(Double.valueOf(temp[1])>0.95)
				{
					if(result[Integer.valueOf(temp[0])-1][0]==-1)
					{
						result[Integer.valueOf(temp[0])-1]=subData(null);
				
					}
					pwWeight.print(temp[1]+" ");
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
						double tWeight=this.computeDataCorrelation(result[Integer.valueOf(temp[0])-1], result[Integer.valueOf(temp[2])-1]);
						DecimalFormat df=new DecimalFormat("#.00");
						pwWeight.print(df.format(1-tWeight)+" "+temp[2]+" ");
						
						
				}
				lineNum++;
				pwWeight.println();
			}
			PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-1.txt")));
			PrintWriter pw2=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-2.txt")));
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<2*dataSum;j++)
				{
					if(j<dataSum)
						{
							pw1.print(result[i][j]+" ");
						}
					else
					{
						pw2.print(result[i][j]+" ");
					}
				}
				pw1.println();
				pw2.println();
			}
			pw1.flush();
			pw1.close();
			pw2.flush();
			pw2.close();
			pwWeight.flush();
			pwWeight.close();
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
	}

}
