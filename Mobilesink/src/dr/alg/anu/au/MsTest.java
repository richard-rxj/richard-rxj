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
public class MsTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void impactPerformance() throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] transRangeSet={23,16,14,12,10,10};
		  
	
			

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
			
			
			
			
			

			
			String outputFile=tOutputFileName+"max-benefitgain-tour.txt";
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxBenefitGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--MaxBenefitGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
		
		
			
			outputFile=tOutputFileName+"unit-benefitgain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxUnitBenefitGainTourDesign(nFile, gFile,null);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--UnitBenefitGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			outputFile=tOutputFileName+"random-benefitgain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.randomBenefitGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--RandomBenefitGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			
			outputFile=tOutputFileName+"max-utilitygain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxUtilityGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--MaxUtilityGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			
			outputFile=tOutputFileName+"unit-utilitygain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(nFile, gFile,null);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--UnitUtilityGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			
			outputFile=tOutputFileName+"random-utilitygain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.randomUtilityGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--RandomUtilityGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
	}
	
	
	public static void impactPerformance50() throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] transRangeSet={23,16,14,12,10,10};

	
			

			String tOutputFileName="test/new/ImpactPerformance50/";
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
			
			
			
			
			

			
			String outputFile=tOutputFileName+"max-benefitgain-tour.txt";
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				ExperimentSetting.gatewayLimit=50;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxBenefitGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--MaxBenefitGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
		
		
			
			outputFile=tOutputFileName+"unit-benefitgain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				//ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxUnitBenefitGainTourDesign(nFile, gFile,null);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--UnitBenefitGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			outputFile=tOutputFileName+"random-benefitgain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				//ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.randomBenefitGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--RandomBenefitGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			
			outputFile=tOutputFileName+"max-utilitygain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				//ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxUtilityGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--MaxUtilityGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			
			outputFile=tOutputFileName+"unit-utilitygain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				//ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(nFile, gFile,null);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--UnitUtilityGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
			
			outputFile=tOutputFileName+"random-utilitygain-tour.txt";
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(int j=0;j<networkSizeSet.length;j++)
			{
				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
				int networkSize=networkSizeSet[j];
				int transRange=transRangeSet[j];
				ExperimentSetting.transmissionRange=transRange;
				//ExperimentSetting.gatewayLimit=networkSize;
				
				for(int k=0;k<ExperimentSetting.cishu;k++)
				{
										
					
					
					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
					ArrayList<GateWay> solution=NewTourDesign.randomUtilityGainTourDesign(nFile, gFile);
					
					//begin of debug
					for(int ti=0;ti<solution.size();ti++)
					{
							System.out.println(solution.get(ti));
					}
					System.out.println("!!!!!Completet--RandomUtilityGain--<Round>"+k+"-<Node>"+networkSize);
					//end of debug
					
					
					LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
				gResult.setActiveNodes(activeNodes/resultSet.size());
				gResult.setTotalUtility(totalUtility/resultSet.size());
				gResult.setTotalThroughput(totalThroughput/resultSet.size());
				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
				
				pw.println(networkSize+" "+gResult);
				pw.flush();
			}
			pw.close();
			
			
	}
	
	
	public static void impactWeightWithUtilityGain() throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] transRangeSet={23,16,14,12,10,10};
		int[] weightSet={12,20,40,80,160};   // 0,5,10,20,40,80,100   divide 100
		//int[] speedSet={5,10,30,60,180};
		
			
			for(int i=0;i<weightSet.length;i++)
			{
				String tOutputFileName="test/new/ImpactWeight/";
				File tf=new File(tOutputFileName);
				if(!tf.exists())
				{
					tf.mkdirs();
				}
				int tWeight=weightSet[i];
				
				ExperimentSetting.utilityA=tWeight/10;
				
				
				String tFileName="test/new/topology/";
				tf=new File(tFileName);
				if(!tf.exists())
				{
					tf.mkdirs();
				}
				
				
				String outputFile=tOutputFileName+"max-utility-weight-"+Integer.toString(tWeight)+".txt";
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
					ExperimentSetting.gatewayLimit=networkSize;
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
						ArrayList<GateWay> solution=NewTourDesign.maxUtilityGainTourDesign(nFile, gFile);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet-Utility-Weight-<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
						//end of debug
						
						
						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
				}
				pw.close();
				
				
				outputFile=tOutputFileName+"unit-utility-weight-"+Integer.toString(tWeight)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
					ExperimentSetting.gatewayLimit=networkSize;
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
						ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(nFile, gFile,null);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet-UnitUtility-Weight--<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
						//end of debug
						
						
						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
				}
				pw.close();
			}
		
	}
	
	
	public static void impactWeightWithUtilityGain50() throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] transRangeSet={23,16,14,12,10,10};
		int[] weightSet={11,20,40,80,160,320,640,1000,2000,10000};   // 0,5,10,20,40,80,100   divide 100
		//int[] speedSet={5,10,30,60,180};
		
			
			for(int i=0;i<weightSet.length;i++)
			{
				String tOutputFileName="test/new/ImpactWeight50/";
				File tf=new File(tOutputFileName);
				if(!tf.exists())
				{
					tf.mkdirs();
				}
				int tWeight=weightSet[i];
				
				ExperimentSetting.utilityA=tWeight/10;
				
				
				String tFileName="test/new/topology/";
				tf=new File(tFileName);
				if(!tf.exists())
				{
					tf.mkdirs();
				}
				
				
				String outputFile=tOutputFileName+"unit-benefit-weight-"+Integer.toString(tWeight)+".txt";
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
					//ExperimentSetting.gatewayLimit=networkSize;
					ExperimentSetting.gatewayLimit=50;
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
						ArrayList<GateWay> solution=NewTourDesign.maxUnitBenefitGainTourDesign(nFile, gFile,null);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet-Utility-Weight-<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
						//end of debug
						
						
						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
				}
				pw.close();
				
				
				outputFile=tOutputFileName+"unit-utility-weight-"+Integer.toString(tWeight)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
					//ExperimentSetting.gatewayLimit=networkSize;
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
						ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(nFile, gFile,null);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet-UnitUtility-Weight--<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
						//end of debug
						
						
						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
				}
				pw.close();
			}
		
	}
	
	
	public static void impactLocation() throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] transRangeSet={23,16,14,12,10,10};
		int[] weightSet={20,40,80,100};   // 0,5,10,20,40,80,100   divide 100
		//int[] speedSet={5,10,30,60,180};
		
			
			for(int i=0;i<weightSet.length;i++)
			{
				String tOutputFileName="test/new/ImpactLocation/";
				File tf=new File(tOutputFileName);
				if(!tf.exists())
				{
					tf.mkdirs();
				}
				int tWeight=weightSet[i];
				
				ExperimentSetting.gatewayLimit=tWeight;
				
				
				String tFileName="test/new/topology/";
				tf=new File(tFileName);
				if(!tf.exists())
				{
					tf.mkdirs();
				}
				
				
				String outputFile=tOutputFileName+"unit-benefit-location-"+Integer.toString(tWeight)+".txt";
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
			
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
				//ExperimentSetting.gatewayLimit=networkSize;

					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
						ArrayList<GateWay> solution=NewTourDesign.maxUnitBenefitGainTourDesign(nFile, gFile,null);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet-Utility-Weight-<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
						//end of debug
						
						
						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
				}
				pw.close();
				
				
				outputFile=tOutputFileName+"unit-utility-location-"+Integer.toString(tWeight)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
					//ExperimentSetting.gatewayLimit=networkSize;
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
						ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(nFile, gFile,null);
						
						//begin of debug
						for(int ti=0;ti<solution.size();ti++)
						{
								System.out.println(solution.get(ti));
						}
						System.out.println("!!!!!Completet-UnitUtility-Location--<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
						//end of debug
						
						
						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
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
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
				}
				pw.close();
			}
		
	}
	
	
	
	
	public static void impactNodeDiff() throws IOException
	{
		int[] networkSizeSet={100};
		int[] transRangeSet={23,16,14,12,10,10};
		int[] weightSet={11,20,40,80,160,320,640,1000,2000,10000};   // 0,5,10,20,40,80,100   divide 100
		//int[] speedSet={5,10,30,60,180};
		
		ExperimentSetting.gatewayLimit=50;

			for(int wi=0;wi<weightSet.length;wi++)
			{
				
				int tWeight=weightSet[wi];
				
				ExperimentSetting.utilityA=tWeight/10;
				
				String tOutputFileName="test/new/ImpactDiff"+ExperimentSetting.gatewayLimit+"-"+tWeight+"/";
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
				
				
	
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
					//ExperimentSetting.gatewayLimit=networkSize;
					
					
					
					Node[] resultNodeSet=new Node[networkSize];
					for(int i=0;i<resultNodeSet.length;i++)
					{
						resultNodeSet[i]=new Node(i);
					}
					
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=new BiNetwork();;
						//ArrayList<GateWay> solution=NewTourDesign.maxUnitBenefitGainTourDesign(nFile, gFile,bNet);
						
//						//begin of debug
//						for(int ti=0;ti<solution.size();ti++)
//						{
//								System.out.println(solution.get(ti));
//						}
//						System.out.println("!!!!!Completet-UnitBenefit-Diff-<Round>"+k+"-<Node>"+networkSize);
//						//end of debug
//						
//						
//						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
//						resultSet.add(tResult);
						
						ArrayList<Node> tNodeSet=NewTourDesign.maxUnitBenefitGainTourDesignNode(nFile, gFile);
						for(int ti=0;ti<tNodeSet.size();ti++)
						{
							Node tSmallNode=tNodeSet.get(ti);
							System.out.println(tSmallNode.getId()+" "+tSmallNode.getTotalSojournTime()+" "+tSmallNode.gettRate());
							Node tResultNode=resultNodeSet[ti];
							double ttt=tResultNode.getBenefitGain();
							tResultNode.setBenefitGain(ttt+tSmallNode.getTotalSojournTime()*tSmallNode.gettRate());
						}
						
						
					}
					
//					LabResult gResult=new LabResult();
//					int activeNodes=0;
//					double totalUtility=0;
//					double totalThroughput=0;
//					double totalSojournTime=0;
//					double totalMovingTime=0;
//					for(int k=0;k<resultSet.size();k++)
//					{
//						LabResult tResult=resultSet.get(k);
//						activeNodes=activeNodes+tResult.getActiveNodes();
//						totalUtility=totalUtility+tResult.getTotalUtility();
//						totalThroughput=totalThroughput+tResult.getTotalThroughput();
//						totalSojournTime=totalSojournTime+tResult.getTotalSojournTime();
//						totalMovingTime=totalMovingTime+tResult.getTotalMovingTime();				
//					}
//					gResult.setActiveNodes(activeNodes/resultSet.size());
//					gResult.setTotalUtility(totalUtility/resultSet.size());
//					gResult.setTotalThroughput(totalThroughput/resultSet.size());
//					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
//					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					

					String outputFile=tOutputFileName+"unit-benefit-size-"+Integer.toString(networkSize)+".txt";
					PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
					
					for(int k=0;k<resultNodeSet.length;k++)
					{
						Node tNode=resultNodeSet[k];
						double ttt=tNode.getBenefitGain()/ExperimentSetting.cishu;
						pw.println(tNode.getId()+" "+ttt);
						pw.flush();
					}
					pw.close();
				}
				
				
				
				
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					ExperimentSetting.transmissionRange=transRange;
					//ExperimentSetting.gatewayLimit=networkSize;
					
					
					Node[] resultNodeSet=new Node[networkSize];
					for(int i=0;i<resultNodeSet.length;i++)
					{
						resultNodeSet[i]=new Node(i);
					}
					
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
//						ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(nFile, gFile,bNet);
//						
//						//begin of debug
//						for(int ti=0;ti<solution.size();ti++)
//						{
//								System.out.println(solution.get(ti));
//						}
//						System.out.println("!!!!!Completet-UnitUtility-Diff--<Round>"+k+"-<Node>"+networkSize);
//						//end of debug
//						
//						
//						LabResult tResult=NewTourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
//						resultSet.add(tResult);
						
						ArrayList<Node> tNodeSet=NewTourDesign.maxUnitUtilityGainTourDesignNode(nFile, gFile);
						for(int ti=0;ti<tNodeSet.size();ti++)
						{
							Node tSmallNode=tNodeSet.get(ti);
							System.out.println(tSmallNode.getId()+" "+tSmallNode.getTotalSojournTime()+" "+tSmallNode.gettRate());
							Node tResultNode=resultNodeSet[ti];
							double ttt=tResultNode.getBenefitGain();
							tResultNode.setBenefitGain(ttt+tSmallNode.getTotalSojournTime()*tSmallNode.gettRate());
						}
						
						
					}
					
//					LabResult gResult=new LabResult();
//					int activeNodes=0;
//					double totalUtility=0;
//					double totalThroughput=0;
//					double totalSojournTime=0;
//					double totalMovingTime=0;
//					for(int k=0;k<resultSet.size();k++)
//					{
//						LabResult tResult=resultSet.get(k);
//						activeNodes=activeNodes+tResult.getActiveNodes();
//						totalUtility=totalUtility+tResult.getTotalUtility();
//						totalThroughput=totalThroughput+tResult.getTotalThroughput();
//						totalSojournTime=totalSojournTime+tResult.getTotalSojournTime();
//						totalMovingTime=totalMovingTime+tResult.getTotalMovingTime();				
//					}
//					gResult.setActiveNodes(activeNodes/resultSet.size());
//					gResult.setTotalUtility(totalUtility/resultSet.size());
//					gResult.setTotalThroughput(totalThroughput/resultSet.size());
//					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
//					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					
					String outputFile=tOutputFileName+"unit-utility-size-"+Integer.toString(networkSize)+".txt";
					PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
		
					for(int k=0;k<resultNodeSet.length;k++)
					{
						Node tNode=resultNodeSet[k];
						double ttt=tNode.getBenefitGain()/ExperimentSetting.cishu;
						pw.println(tNode.getId()+" "+ttt);
						pw.flush();
					}
					pw.close();
				}
				
			}
		
	}
	
	
	
//	public static void impactTime() throws IOException
//	{
//		int[] networkSizeSet={100,200,300,400,500,600};
//		int[] transRangeSet={23,16,14,12,10,10};
//		int[] weightSet={300,600,1200,2400,4800,36000,72000,144000};   
//		int  cishu=15;   
//		for(int i=0;i<weightSet.length;i++)
//		{
//			String tFileName="test/new/ImpactTime/";
//			File tf=new File(tFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//			int tWeight=weightSet[i];
//			
//			String outputFile=tFileName+"-time-"+Integer.toString(tWeight)+".txt";
//			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
//
//			
//			ExperimentSetting.tourTime=tWeight;
//			
//						
//			tFileName="test/new/topology/";
//			tf=new File(tFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//			
//			for(int j=0;j<networkSizeSet.length;j++)
//			{
//				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
//				int networkSize=networkSizeSet[j];
//				int transRange=transRangeSet[j];
//				ExperimentSetting.transmissionRange=transRange;
//				
//				for(int k=0;k<cishu;k++)
//				{
//					String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
//					String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
//					BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
//					ArrayList<GateWay> solution=TourDesign.linearTourDesign(nFile, gFile);
//					
//					//begin of debug
//					for(int ti=0;ti<solution.size();ti++)
//					{
//							System.out.println(solution.get(ti));
//					}
//					System.out.println("!!!!!Completet--Linear--<Round>"+k+"-<Node>"+networkSize+"-<Time>"+tWeight);
//					//end of debug
//					
//					
//					LabResult tResult=TourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
//					resultSet.add(tResult);
//				}
//				
//				LabResult gResult=new LabResult();
//				int activeNodes=0;
//				double totalUtility=0;
//				double totalThroughput=0;
//				double totalSojournTime=0;
//				double totalMovingTime=0;
//				for(int k=0;k<resultSet.size();k++)
//				{
//					LabResult tResult=resultSet.get(k);
//					activeNodes=activeNodes+tResult.getActiveNodes();
//					totalUtility=totalUtility+tResult.getTotalUtility();
//					totalThroughput=totalThroughput+tResult.getTotalThroughput();
//					totalSojournTime=totalSojournTime+tResult.getTotalSojournTime();
//					totalMovingTime=totalMovingTime+tResult.getTotalMovingTime();				
//				}
//				gResult.setActiveNodes(activeNodes/resultSet.size());
//				gResult.setTotalUtility(totalUtility/resultSet.size());
//				gResult.setTotalThroughput(totalThroughput/resultSet.size());
//				gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
//				gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
//				
//				pw.println(networkSize+" "+gResult);
//				pw.flush();
//			}
//			pw.close();
//		}
//		
//	}
	


			



	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Logger logger=Logger.getLogger("MobileSink");
		//FileHandler fh=new FileHandler("m.log");
		//fh.setFormatter(new SimpleFormatter());
		//fh.setLevel(Level.WARNING);
		//logger.addHandler(fh);
		
		
		
		
//		String tFileName="test/";
//		File tf=new File(tFileName);
//		if(!tf.exists())
//		{
//			tf.mkdirs();
//		}
//		
//		String nFile=tFileName+"n-100.txt";
//		String gFile=tFileName+"g-100.txt";
//		
//		//BiNetwork bNet=NetworkGenerator.generateNetwork(100, 50);
//		//bNet.saveToFile(nFile, gFile);
//		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
//		//bNet.saveToFile(tFileName+"n-100-2.txt", tFileName+"g-100-2.txt");
//		
//		//ArrayList<GateWay> solution=TourDesign.linearTourDesign(nFile, gFile);
//		ArrayList<GateWay> solution=TourDesign.fairTourDesign(nFile, gFile);
//		for(int i=0;i<solution.size();i++)
//		{
//			System.out.println(solution.get(i));
//		}
		//NewMobileSinkTest.impactWeightWithLinear();
		//MobileSinkTest.impactWeightWithFair();
		//MobileSinkTest.impactTimeWithLinear();
		//MobileSinkTest.impactTimeWithFair();
		
		
//		NewMobileSinkTest.impactPerformance();
//		NewMobileSinkTest.impactWeightWithUtilityGain();
		
		MsTest.impactPerformance50();
		ExperimentSetting.gatewayLimit=50;
		MsTest.impactNodeDiff();	
		MsTest.impactWeightWithUtilityGain50();
		ExperimentSetting.utilityA=2;
		MsTest.impactLocation();
	}

}
