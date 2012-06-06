package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	private double getSubUtility(double ratio)
	{
		return 1-Math.pow((1-ratio),2);   //a=2
	}
	
	
	private double[] computeSubMSE(int dOption,double[] slaveBase, double slaveRate, double[] masterBase, double masterRate,double[] saveData, double slaveMaxRate, double masterMaxRate)
	{
		double[] result=new double[2];

		
		int tSlaveRate=(int) Math.floor(slaveRate);
		int tMasterRate=(int) Math.floor(masterRate);
		int bothRate=0;
		
		if (tSlaveRate>slaveBase.length)
		{
			tSlaveRate=slaveBase.length;
		}
		if(tMasterRate>slaveBase.length)
		{
			tMasterRate=slaveBase.length;
		}
		
		double[] tSlave=new double[slaveBase.length];
		if(masterBase==null)
		{
			
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
					tTemp=(tSlave[tiPre]+tSlave[ti])/2;
				}
				else
				{
					tTemp=tSlave[tiPre];                 
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
			
			
			result[1]=this.getSubUtility(tSlaveRate*1.0/slaveMaxRate);
			
		}
		else
		{
			ArrayList<String> a=new ArrayList<String>();

			int ti=0;
			int tStep=0;
			int tiPre=0;
			
			
			
			int tMStep=(int) Math.floor(slaveBase.length*1.0/tMasterRate);
			int tMi=0;
			
			if(tSlaveRate==0)
			{
				for(int i=0;i<tSlave.length;i++)
				{
					if(i==tMi)
					{
						tSlave[i]=masterBase[i];
						a.add(String.valueOf(i));
						tMi=tMi+tMStep;
					}
				}
			}
			else
			{
				tStep=(int) Math.floor(slaveBase.length*1.0/tSlaveRate);
				
				for(int i=0;i<tSlave.length;i++)
				{

				    if((i==ti)&&(i==tMi))
				    {
				    	bothRate++;
				    }
					
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
					if(i>ti)
					{
						ti=ti+tStep;
					}
					if(i>tMi)
					{
						tMi=tMi+tMStep;
					}
				}
			}
			
			
			tiPre=Integer.parseInt(a.get(0));
			for(int i=1;i<a.size();i++)
			{
				ti=Integer.parseInt(a.get(i));
				double tTemp=(tSlave[tiPre]+tSlave[ti])/2;   
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
			
			
			double u1=tSlaveRate*1.0/slaveMaxRate;
			double u2=(tSlaveRate+tMasterRate-bothRate)*1.0/(slaveMaxRate+masterMaxRate);
			result[1]=this.getSubUtility(Math.max(u1, u2));
			
			
		}
		
		
		for(int t=0;t<tSlave.length;t++)
		{
			result[0]=result[0]+Math.pow((tSlave[t]-slaveBase[t]), 2);
			saveData[t]=tSlave[t];
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
			//tempString=bf.readLine();
			while((tempString=bf.readLine())!=null)
			{
				
				String[] tData=tempString.split(" ");
				for(int i=0;i<dataSum;i++)
				{
					result[lineNum][i]=Double.parseDouble(tData[i]);
				}
				lineNum++;
				if(lineNum>=nodeSum)
					break;
			}
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	private double[][] loadRate(String rateFile)
	{
		double[][] result=new double[nodeSum][2];
		try
		{
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(rateFile)));
			String tempString;
			int lineNum=0;
			while((tempString=bf.readLine())!=null)
			{
				String[] tData=tempString.split(" ");
				result[lineNum][0]=Double.parseDouble(tData[1]);
				result[lineNum][1]=Double.parseDouble(tData[4]);
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
	
	/*
	 * 
	 *
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
	/*
	 * 
	 */
	
//	public double computeMSE2(String dataFile, String rateFile, int wOption,String weightFile,int dOption,String saveFile)
//	{
//		double result=0;
//		try
//		{
//			double[][] sData=new double[this.nodeSum][this.dataSum];
//			for(int i=0;i<this.nodeSum;i++)
//				for(int j=0;j<this.dataSum;j++)
//					sData[i][j]=0;
//			
//			
//			double[][] gData=loadData(dataFile);
//			double[] gRate=loadRate(rateFile);
//			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(weightFile)));			
//			String tempString;
//			int lineNum=0;
//			while((tempString=bf.readLine())!=null)
//			{
//				String[] temp=tempString.split(" ");
//				
//				
//				if((Double.parseDouble(temp[1])<1)&&(wOption<1))
//				{
//					int tSlaveId = (int)Double.parseDouble(temp[0]);
//					int tMasterId = (int)Double.parseDouble(temp[2]);
//					result=result+this.computeSubMSE(dOption,gData[tSlaveId], gRate[tSlaveId-1], gData[tMasterId], gRate[tMasterId-1],sData[tSlaveId]);
//
//				}
//				else
//				{
//					int tSlaveId = (int)Double.parseDouble(temp[0]);
//					result=result+this.computeSubMSE(dOption,gData[tSlaveId], gRate[tSlaveId-1], null, 0,sData[tSlaveId]);
//				}
//				lineNum++;
//			}
//			bf.close();
//			
//			if(saveFile!=null)
//			{
//				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveFile)));
//				for(int i=0;i<this.nodeSum;i++)
//				{
//					for(int j=0;j<this.dataSum;j++)
//					{
//						pw.print(sData[i][j]+" ");
//					}
//					pw.println();
//				}
//				pw.flush();
//				pw.close();
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/*
	 * begin utility calculation + data saving
	 */
	
	public double[] computeUtility(String dataFile, String rateFile, int wOption,String weightFile,int dOption,String saveFile, String utilityFile)
	{
		double result[]=new double[2];
		
		try
		{
			double[][] sUtility=new double[this.nodeSum][3];
			double[][] sData=new double[this.nodeSum][this.dataSum];
			for(int i=0;i<this.nodeSum;i++)
				for(int j=0;j<this.dataSum;j++)
					sData[i][j]=0;
			
			
			double[][] gData=loadData(dataFile);
			double[][] gRate=loadRate(rateFile);
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(weightFile)));			
			String tempString;
			int lineNum=0;
			while((tempString=bf.readLine())!=null)
			{
				String[] temp=tempString.split(" ");
				
				
				if((Double.parseDouble(temp[1])<1)&&(wOption<1))
				{
					int tSlaveId = (int)Double.parseDouble(temp[0]);
					int tMasterId = (int)Double.parseDouble(temp[2]);
					double tSlaveMaxRate=Double.parseDouble(temp[3]);
					double tMasterMaxRate=Double.parseDouble(temp[4]);
					double[] tResult=this.computeSubMSE(dOption,gData[tSlaveId], gRate[tSlaveId-1][0], gData[tMasterId], gRate[tMasterId-1][0],sData[tSlaveId],gRate[tSlaveId-1][1],gRate[tMasterId-1][1]);
					result[0]=result[0]+tResult[0];
					result[1]=result[1]+tResult[1];
					sUtility[tSlaveId][0]=gRate[tSlaveId-1][0];
					sUtility[tSlaveId][1]=gRate[tSlaveId-1][1];
					sUtility[tSlaveId][2]=tResult[1];
				}
				else
				{
					int tSlaveId = (int)Double.parseDouble(temp[0]);
					double tSlaveMaxRate=Double.parseDouble(temp[3]);
					double[] tResult=this.computeSubMSE(dOption,gData[tSlaveId], gRate[tSlaveId-1][0], null, 0,sData[tSlaveId],gRate[tSlaveId-1][1],0);
					result[0]=result[0]+tResult[0];
					result[1]=result[1]+tResult[1];
					sUtility[tSlaveId][0]=gRate[tSlaveId-1][0];
					sUtility[tSlaveId][1]=gRate[tSlaveId-1][1];
					sUtility[tSlaveId][2]=tResult[1];
				}
				lineNum++;
			}
			bf.close();
			result[0]=result[0]/(this.nodeSum*this.dataSum);
			
			if(saveFile!=null)
			{
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveFile)));
				for(int i=0;i<this.nodeSum;i++)
				{
					for(int j=0;j<this.dataSum;j++)
					{
						pw.print(sData[i][j]+" ");
					}
					pw.println();
				}
				pw.flush();
				pw.close();
			}
			
			if(utilityFile!=null)
			{
				double t1=0;
				double t2=0;
				double t3=0;
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(utilityFile)));
				for(int i=0;i<this.nodeSum;i++)
				{
					
				    pw.println(i+" "+sUtility[i][0]+" "+sUtility[i][1]+" "+sUtility[i][2]+" ");
					t1=t1+sUtility[i][0];
					t2=t2+sUtility[i][1];
					t3=t3+sUtility[i][2];
				}
				pw.println("sum "+t1+" "+t2+" "+t3+" ");
				pw.flush();
				pw.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 *  end of utility calculation + data saving
	 */
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tFileName="test/real/100/9/";
		String fData="test/real/labData/50/data/data-1-2.txt";
		String fWeight="test/real/labData/50/9/weight/weight-1.txt";
		String fRate="test/real/labData/50/9/rate/rate-1-0.txt";
		//DataQuality dq=new DataQuality();
		//dq.setDataSum(100);
		//dq.setNodeSum(50);
		//dq.computeMSE(fData, fRate, 0, fWeight,0);
		ArrayList<String> a=new ArrayList<String>();
		a.add("1");
		a.add("3");
		a.add("2");
		System.out.println(a);
		System.out.println(a.contains("1"));
		Arrays.sort(a.toArray());
		System.out.println(a);
		
	}

}
