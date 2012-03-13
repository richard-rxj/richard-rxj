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
public class NewMsTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	
	
	public static void impactPerformance() throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] gatewayLimitSet={50,50,50,50,50,50};
		int[] transRangeSet={30,30,30,30,30,30};
		int[] tourTimeSet={1600,3200,6400};    //100,200,400,800
	
			

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
				
				
				
				
				String outputFile=tOutputFileName+"max-benefitgain-tour-T"+Integer.toString(tTourTime)+".txt";
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				String outputFile1=tOutputFileName1+"max-benefitgain-tour.txt";
				PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile1)));
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
						ArrayList<GateWay> solution=NewTourDesign.maxBenefitGainTourDesign(bNet);
						
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
			
				
				outputFile=tOutputFileName+"unit-benefitgain-tour-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"unit-benefitgain-tour.txt";
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
						ArrayList<GateWay> solution=NewTourDesign.maxUnitBenefitGainTourDesign(bNet);
						
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
				
				
				outputFile=tOutputFileName+"random-benefitgain-tour-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"random-benefitgain-tour.txt";
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
						ArrayList<GateWay> solution=NewTourDesign.randomBenefitGainTourDesign(bNet);
						
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
				
				
				outputFile=tOutputFileName+"max-utilitygain-tour-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"max-utilitygain-tour.txt";
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
						ArrayList<GateWay> solution=NewTourDesign.maxUtilityGainTourDesign(bNet);
						
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
				
				
				outputFile=tOutputFileName+"unit-utilitygain-tour-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"unit-utilitygain-tour.txt";
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
						ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(bNet);
						
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
				
				
				outputFile=tOutputFileName+"random-utilitygain-tour-T"+Integer.toString(tTourTime)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				outputFile1=tOutputFileName1+"random-utilitygain-tour.txt";
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
						ArrayList<GateWay> solution=NewTourDesign.randomUtilityGainTourDesign(bNet);
						
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
			}
	}
	
	
	
	
	
	public static void impactWeightWithUtilityGain() throws IOException
	{

		
		
		
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] gatewayLimitSet={50,50,50,50,50,50};
		int[] transRangeSet={30,30,30,30,30,30};
		int[] tourTimeSet={100,400,1600};
		int[] weightSet={11,20,40,80,160,320,640,1000};   // 0,5,10,20,40,80,100   divide 100
		
			
		for(int tti=0;tti<tourTimeSet.length;tti++)
		{
			int tourTime=tourTimeSet[tti];
			ExperimentSetting.tourTime=tourTime;
			
			
			String tOutputFileName="test/new/ImpactWeight/T"+tourTime+"/";
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
			
			
			for(int i=0;i<weightSet.length;i++)
			{
				
				int tWeight=weightSet[i];			
				ExperimentSetting.utilityA=tWeight/10;
				
				String outputFile=tOutputFileName+"unit-benefit-weight-"+Integer.toString(tWeight)+".txt";
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
				
				
				
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
						ArrayList<GateWay> solution=NewTourDesign.maxUnitBenefitGainTourDesign(bNet);
						
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
					gResult.setNetworkSize(networkSize);
					gResult.setTourTime(ExperimentSetting.tourTime);
					gResult.setActiveNodes(activeNodes/resultSet.size());
					gResult.setTotalUtility(totalUtility/resultSet.size());
					gResult.setTotalThroughput(totalThroughput/resultSet.size());
					gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
					gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
					
					pw.println(networkSize+" "+gResult);
					pw.flush();
				}
				pw.close();
				
				
				
				outputFile=tOutputFileName+"max-utility-weight-"+Integer.toString(tWeight)+".txt";
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
	
				
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
						ArrayList<GateWay> solution=NewTourDesign.maxUtilityGainTourDesign(bNet);
						
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
					gResult.setNetworkSize(networkSize);
					gResult.setTourTime(ExperimentSetting.tourTime);
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
					int gatewayLimit=gatewayLimitSet[j];
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);
						ArrayList<GateWay> solution=NewTourDesign.maxUnitUtilityGainTourDesign(bNet);
						
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
					gResult.setNetworkSize(networkSize);
					gResult.setTourTime(ExperimentSetting.tourTime);
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
	}
	
	

	
	
	
	public static void impactNodeDiff() throws IOException
	{
		int[] networkSizeSet={100};
		int[] gatewayLimitSet={50,50,50,50,50,50};
		int[] transRangeSet={30,30,30,30,30,30};
		int[] tourTimeSet={100,400,1600};
		int[] weightSet={11,20,40,160,640,1000};   // 0,5,10,20,40,80,100   divide 100

		
		for(int tti=0;tti<tourTimeSet.length;tti++)
		{
			int tourTime=tourTimeSet[tti];
			ExperimentSetting.tourTime=tourTime;

			for(int wi=0;wi<weightSet.length;wi++)
			{
				
				int tWeight=weightSet[wi];
				
				ExperimentSetting.utilityA=tWeight/10;
				
				String tOutputFileName="test/new/ImpactDiff-T"+tourTime+"-A"+tWeight+"/";
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
					int gatewayLimit=gatewayLimitSet[j];
					
					
					
					Node[] resultNodeSet=new Node[networkSize];
					for(int i=0;i<resultNodeSet.length;i++)
					{
						resultNodeSet[i]=new Node(i);
					}
					
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);
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
						
						ArrayList<Node> tNodeSet=NewTourDesign.maxUnitBenefitGainTourDesignNode(bNet);
						for(int ti=0;ti<tNodeSet.size();ti++)
						{
							Node tSmallNode=tNodeSet.get(ti);
							System.out.println(tSmallNode.getId()+" "+tSmallNode.getTotalSojournTime()+" "+tSmallNode.gettRate());
							Node tResultNode=resultNodeSet[ti];
							double bbb=tResultNode.getBenefitGain();
							double ttt=tResultNode.getTotalSojournTime();
							double uuu=tResultNode.getUtilityGain();
							tResultNode.setBenefitGain(bbb+tSmallNode.getTotalSojournTime()*tSmallNode.gettRate());
							tResultNode.setTotalSojournTime(ttt+tSmallNode.getTotalSojournTime());
							tResultNode.setUtilityGain(uuu+tSmallNode.getUtilityGain());
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
						double ttt=tNode.getTotalSojournTime()/ExperimentSetting.cishu;
						double bbb=tNode.getBenefitGain()/ExperimentSetting.cishu;
						double uuu=tNode.getUtilityGain()/ExperimentSetting.cishu;
						pw.println(tNode.getId()+" "+ttt+" "+bbb+" "+uuu);
						pw.flush();
					}
					pw.close();
				}
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					int gatewayLimit=gatewayLimitSet[j];
					
					
					Node[] resultNodeSet=new Node[networkSize];
					for(int i=0;i<resultNodeSet.length;i++)
					{
						resultNodeSet[i]=new Node(i);
					}
					
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);
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
						
						ArrayList<Node> tNodeSet=NewTourDesign.maxUnitUtilityGainTourDesignNode(bNet);
						for(int ti=0;ti<tNodeSet.size();ti++)
						{
							Node tSmallNode=tNodeSet.get(ti);
							System.out.println(tSmallNode.getId()+" "+tSmallNode.getTotalSojournTime()+" "+tSmallNode.gettRate());
							Node tResultNode=resultNodeSet[ti];
							double bbb=tResultNode.getBenefitGain();
							double ttt=tResultNode.getTotalSojournTime();
							double uuu=tResultNode.getUtilityGain();
							tResultNode.setBenefitGain(bbb+tSmallNode.getTotalSojournTime()*tSmallNode.gettRate());
							tResultNode.setTotalSojournTime(ttt+tSmallNode.getTotalSojournTime());
							tResultNode.setUtilityGain(uuu+tSmallNode.getUtilityGain());
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
					
					
					String outputFile=tOutputFileName+"max-utility-size-"+Integer.toString(networkSize)+".txt";
					PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
		
					for(int k=0;k<resultNodeSet.length;k++)
					{
						Node tNode=resultNodeSet[k];
						double ttt=tNode.getTotalSojournTime()/ExperimentSetting.cishu;
						double bbb=tNode.getBenefitGain()/ExperimentSetting.cishu;
						double uuu=tNode.getUtilityGain()/ExperimentSetting.cishu;
						pw.println(tNode.getId()+" "+ttt+" "+bbb+" "+uuu);
						pw.flush();
					}
					pw.close();
				}
				
				
				
				for(int j=0;j<networkSizeSet.length;j++)
				{
					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					int gatewayLimit=gatewayLimitSet[j];
					
					
					Node[] resultNodeSet=new Node[networkSize];
					for(int i=0;i<resultNodeSet.length;i++)
					{
						resultNodeSet[i]=new Node(i);
					}
					
					
					for(int k=0;k<ExperimentSetting.cishu;k++)
					{
						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);
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
						
						ArrayList<GateWay> solution=NewTourDesign.maxUtilityGainTourDesign(bNet);
						for(int ti=0;ti<bNet.getnList().size();ti++)
						{
							Node tSmallNode=bNet.getnList().get(ti);
							System.out.println(tSmallNode.getId()+" "+tSmallNode.getTotalSojournTime()+" "+tSmallNode.gettRate());
							Node tResultNode=resultNodeSet[ti];
							double bbb=tResultNode.getBenefitGain();
							double ttt=tResultNode.getTotalSojournTime();
							double uuu=tResultNode.getUtilityGain();
							tResultNode.setBenefitGain(bbb+tSmallNode.getTotalSojournTime()*tSmallNode.gettRate());
							tResultNode.setTotalSojournTime(ttt+tSmallNode.getTotalSojournTime());
							tResultNode.setUtilityGain(uuu+tSmallNode.getUtilityGain());
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
						double ttt=tNode.getTotalSojournTime()/ExperimentSetting.cishu;
						double bbb=tNode.getBenefitGain()/ExperimentSetting.cishu;
						double uuu=tNode.getUtilityGain()/ExperimentSetting.cishu;
						pw.println(tNode.getId()+" "+ttt+" "+bbb+" "+uuu);
						pw.flush();
					}
					pw.close();
				}
				
			}
		}
		
	}
	
	
	

	


			



	
	
	public static void main(String[] args) throws IOException {
		
		
		//NewMsTest.impactPerformance();
		//NewMsTest.impactWeightWithUtilityGain();
		NewMsTest.impactNodeDiff();
		
	}

}
