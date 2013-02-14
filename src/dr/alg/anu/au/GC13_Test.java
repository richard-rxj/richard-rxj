/**
 * 
 */
package dr.alg.anu.au;

import generate.dr.alg.anu.au.NetworkGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import network.dr.alg.anu.au.BiNetwork;
import network.dr.alg.anu.au.GateWay;
import network.dr.alg.anu.au.LabResult;
import network.dr.alg.anu.au.Node;

/**
 * @author user
 *
 */
public class GC13_Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	
	
	public static void impactPerformance() throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600,700,800,900,1000,2000};
		int[] gatewayLimitSet={50,50,50,50,50,50,50,50,50,50,50};   //50
		int[] transRangeSet={20,20,20,20,20,20,20,20,20,20,20};   //30    20 for distributed
		int[] tourTimeSet={100,200,400,800,3200,6400};    //100,200,400,800,3200,6400
	
			

			String tOutputFileName="test/new/ImpactPerformance/";
			File tf=new File(tOutputFileName);
			if(!tf.exists())
			{
				tf.mkdirs();
			}
			String tFileName="test/new/topology/";
			tf=new File(tFileName);
			if(!tf.exists())
			{
				tf.mkdirs();
			}
			
			
			
			
			
			for(int i=0;i<tourTimeSet.length;i++)
			{
				int tTourTime=tourTimeSet[i];
				ExperimentSetting.tourTime=tTourTime;
				
				
				String tOutputFileName1="test/new/ImpactPerformance/T"+tTourTime+"/";
				tf=new File(tOutputFileName1);
				if(!tf.exists())
				{
					tf.mkdirs();
				}
				
				
				String outputFile=null;
				String outputFile1=null;
				PrintWriter pw=null;
				PrintWriter pw1=null;
				
				
				/*
				 * begin of distributed   section
				 */

				
				
				
				outputFile=tOutputFileName+"dis-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"dis.txt";
				pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile1)));
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					int gatewayLimit=gatewayLimitSet[j];
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
											
						
						
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);
						double iniMinRange=1.75*transRange;
						double iniMaxRange=2.25*transRange;
						double deltaRange=0.05*transRange;
						double iniTimeStamp=20;
						double deltaTimeStamp=5;
						
						
						
						ArrayList<GateWay> solution=new ArrayList<GateWay>();
						LabResult tResult=GC13_Alg.disTraMaxUtilityGainTourDesign(bNet, iniMinRange, iniMaxRange, deltaRange, iniTimeStamp, deltaTimeStamp,solution);

						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet--Dis--<Round>"+k+"-<Node>"+networkSize);
						//end of debug
						
						resultSet.add(tResult);
					}
					
					LabResult gResult=new LabResult();
					int activeNodes=0;
					double totalUtility=0;
					double totalSojournTime=0;
					double totalMovingTime=0;
					for(int k=0;k<resultSet.size();k++)
					{
						LabResult tResult=resultSet.get(k);
						activeNodes=activeNodes+tResult.getActiveNodes();
						totalUtility=totalUtility+tResult.getTotalUtility();
						totalSojournTime=totalSojournTime+tResult.getTotalSojournTime();
						totalMovingTime=totalMovingTime+tResult.getTotalMovingTime();				
					}
					gResult.setNetworkSize(networkSize);
					gResult.setTourTime(ExperimentSetting.tourTime);
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
					pw1.println(networkSize+" "+gResult);
					pw1.flush();
				}
				pw.close();
				pw1.close();
				
				
				
				
				/*
				 * begin of utility
				 */
				
				outputFile=tOutputFileName+"max-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"max.txt";
				pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile1)));
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					int gatewayLimit=gatewayLimitSet[j];
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
											
						
						
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);
					
						
						ArrayList<GateWay> solution=new ArrayList<GateWay>();
						LabResult tResult=GC13_Alg.maxUnitUtilityGainTourDesign(bNet,solution);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet--Max--<Round>"+k+"-<Node>"+networkSize);
						//end of debug
						resultSet.add(tResult);
					}
					
					LabResult gResult=new LabResult();
					int activeNodes=0;
					double totalUtility=0;
					double totalThroughput=0;
					double totalSojournTime=0;
					double totalMovingTime=0;
					for(int k=0;k<resultSet.size();k++)
					{
						LabResult tResult=resultSet.get(k);
						activeNodes=activeNodes+tResult.getActiveNodes();
						totalUtility=totalUtility+tResult.getTotalUtility();
						totalThroughput=totalThroughput+tResult.getTotalThroughput();
						totalSojournTime=totalSojournTime+tResult.getTotalSojournTime();
						totalMovingTime=totalMovingTime+tResult.getTotalMovingTime();				
					}
					gResult.setNetworkSize(networkSize);
					gResult.setTourTime(ExperimentSetting.tourTime);
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
					pw1.println(networkSize+" "+gResult);
					pw1.flush();
				}
				pw.close();
				pw1.close();
				
				
				
				
				
				outputFile=tOutputFileName+"random-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"random.txt";
				pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile1)));
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					int gatewayLimit=gatewayLimitSet[j];
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
											
						
						
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);


						
						ArrayList<GateWay> solution=new ArrayList<GateWay>();
						LabResult tResult=GC13_Alg.randomUtilityGainTourDesign(bNet,solution);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet--Random--<Round>"+k+"-<Node>"+networkSize);
						//end of debug
						
						resultSet.add(tResult);
					}
					
					LabResult gResult=new LabResult();
					int activeNodes=0;
					double totalUtility=0;
					double totalThroughput=0;
					double totalSojournTime=0;
					double totalMovingTime=0;
					for(int k=0;k<resultSet.size();k++)
					{
						LabResult tResult=resultSet.get(k);
						activeNodes=activeNodes+tResult.getActiveNodes();
						totalUtility=totalUtility+tResult.getTotalUtility();
						totalThroughput=totalThroughput+tResult.getTotalThroughput();
						totalSojournTime=totalSojournTime+tResult.getTotalSojournTime();
						totalMovingTime=totalMovingTime+tResult.getTotalMovingTime();				
					}
					gResult.setNetworkSize(networkSize);
					gResult.setTourTime(ExperimentSetting.tourTime);
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
					pw1.println(networkSize+" "+gResult);
					pw1.flush();
				}
				pw.close();
				pw1.close();
				
				/*
				 * end of utility
				 */
			}
	}
	
	
	
	
	
	

			



	
	
	public static void main(String[] args) throws IOException 
	{
			
		GC13_Test.impactPerformance();
	
	}

}
