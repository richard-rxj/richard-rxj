package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataQuality {

	private int nodeSum=100;
	private int dataSum=100;
	
	public int getNodeSum() {
		return nodeSum;
	}
	public void setNodeSum(int nodeSum) {
		this.nodeSum = nodeSum;
	}
	public int getDataSum() {
		return dataSum;
	}
	public void setDataSum(int dataSum) {
		this.dataSum = dataSum;
	}
	private double computeSubMSE(int dOption,double[] slaveBase, double slaveRate, double[] masterBase, double masterRate)
	{
		double result=0;
		if (slaveRate>slaveBase.length)
		{
			slaveRate=slaveBase.length;
		}
		if(masterRate>slaveBase.length)
		{
			masterRate=slaveBase.length;
		}
		double[] tSlave=new double[slaveBase.length];
		if(masterBase==null)
		{
			
			int tSlaveRate=(int) Math.floor(slaveRate);
			int tStep=(int) Math.floor(slaveBase.length*1.0/tSlaveRate);
			int ti=0;
			tSlave[ti]=slaveBase[ti];
			int tiPre=0;
			for(int i=1;i<tSlaveRate;i++)
			{
				tiPre=ti;
				ti=ti+tStep;
				tSlave[ti]=slaveBase[ti];
				double tTemp;
				if(dOption<1)
				{
					tTemp=(tSlave[tiPre]+tSlave[ti])/2;//中位数填充
				}
				else
				{
					tTemp=tSlave[tiPre];                 //前一位填充
				}	
				
				for(int j=tiPre+1;j<ti;j++)
				{
					tSlave[j]=tTemp;
				}
			}
			for(int i=ti+1;i<tSlave.length;i++)
			{
				tSlave[i]=tSlave[ti];			
			}
		}
		else
		{
			ArrayList<String> a=new ArrayList<String>();
			int tSlaveRate=(int) Math.floor(slaveRate);
			int ti=0;
			int tStep=0;
			int tiPre=0;
			if(tSlaveRate>0)
			{
				tStep=(int) Math.floor(slaveBase.length*1.0/tSlaveRate);
				ti=0;
				a.add(String.valueOf(ti));
				tSlave[ti]=slaveBase[ti];
				tiPre=0;
			}
			else
			{
				ti=-1;
			}
			
			
			int tMasterRate=(int) Math.floor(masterRate);
			int tMStep=(int) Math.floor(slaveBase.length*1.0/tMasterRate);
			int tMi=0+tMStep;
			ti=ti+tStep;
			for(int i=1;i<tSlave.length;i++)
			{
				if(i==ti)
				{
					tSlave[i]=slaveBase[i];
					a.add(String.valueOf(i));
					ti=ti+tStep;
				}
				else if(i==tMi)
				{
					tSlave[i]=masterBase[i];
					a.add(String.valueOf(i));
					tMi=tMi+tMStep;
				}
			}
			
			
			
			tiPre=Integer.parseInt(a.get(0));
			for(int i=1;i<a.size();i++)
			{
				ti=Integer.parseInt(a.get(i));
				double tTemp=(tSlave[tiPre]+tSlave[ti])/2;   //中位数填充
				for(int j=tiPre+1;j<ti;j++)
				{
					tSlave[j]=tTemp;
				}
				tiPre=ti;
			}
			for(int i=ti+1;i<tSlave.length;i++)
			{
				tSlave[i]=tSlave[ti];			
			}
		}
		
		
		for(int t=0;t<tSlave.length;t++)
		{
			result=result+Math.pow((tSlave[t]-slaveBase[t]), 2);
		}
		
		return result;
	}
	
	private double[][] loadData(String dataFile)
	{
		double[][] result=new double[nodeSum][dataSum];
		try
		{
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
			String tempString;
			int lineNum=0;
			while((tempString=bf.readLine())!=null)
			{
				String[] tData=tempString.split(" ");
				for(int i=0;i<dataSum;i++)
				{
					result[lineNum][i]=Double.parseDouble(tData[i]);
				}
				lineNum++;
			}
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	private double[] loadRate(String rateFile)
	{
		double[] result=new double[nodeSum];
		try
		{
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(rateFile)));
			String tempString;
			int lineNum=0;
			while((tempString=bf.readLine())!=null)
			{
				String[] tData=tempString.split(" ");
				result[lineNum]=Double.parseDouble(tData[1]);
				lineNum++;
			}
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public double computeMSE(String dataFile, String rateFile, int wOption,String weightFile,int dOption,int begin,int end)
	{
		double result=0;
		try
		{
			double[][] gData=RealDataHandler.loadData(dataFile, begin, end);
			double[] gRate=loadRate(rateFile);
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(weightFile)));			
			String tempString;
			int lineNum=0;
			while((tempString=bf.readLine())!=null)
			{
				String[] temp=tempString.split(" ");
				
				
				if((Double.parseDouble(temp[1])<0.95)&&(wOption<1))
				{
					int tSlaveId = (int)Double.parseDouble(temp[0]);
					int tMasterId = (int)Double.parseDouble(temp[2]);
					result=result+this.computeSubMSE(dOption,gData[tSlaveId-1], gRate[tSlaveId-1], gData[tMasterId-1], gRate[tMasterId-1]);

				}
				else
				{
					int tSlaveId = (int)Double.parseDouble(temp[0]);
					result=result+this.computeSubMSE(dOption,gData[tSlaveId-1], gRate[tSlaveId-1], null, 0);
				}
				lineNum++;
			}
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tFileName="test/real/100/9/";
		String fData="test/real/labData/50/data/data-1-2.txt";
		String fWeight="test/real/labData/50/9/weight/weight-1.txt";
		String fRate="test/real/labData/50/9/rate/rate-1-0.txt";
		DataQuality dq=new DataQuality();
		dq.setDataSum(100);
		dq.setNodeSum(50);
		//dq.computeMSE(fData, fRate, 0, fWeight,0);
		
		
	}

}
