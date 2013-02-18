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
import java.lang.reflect.InvocationTargetException;
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
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	
	
	
	public static void impactPerformance() throws IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		int[] networkSizeSet={100,200,300,400,500,600,700,800,900,1000};                     //100,200,300,400,500,600,700,800,900,1000,2000
		int[] gatewayLimitSet={50,50,50,50,50,50,50,50,50,50,50};   //50
		int[] transRangeSet={20,20,20,20,20,20,20,20,20,20,20};   //30    20 for distributed
		int[] tourTimeSet={100,200,400,800,1600,3200,6400};    //100,200,400,800,1600,3200,6400
	    String[] algSet={"maxUnit","max","randomUnit","random","dis2-3","disCombine","disCombineMoving"};
	    DecimalFormat df=new DecimalFormat("#.0");	

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
				
				
			
				String outputFileTime=null;
				PrintWriter pwTime=null;
				outputFileTime=tOutputFileName+"T"+tTourTime+".txt";
				pwTime=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFileTime)));
				
				
				String[] outputAlgSet=new String[algSet.length];
			    PrintWriter[] pwAlgSet=new PrintWriter[algSet.length];                        
				        
				for(int algI=0;algI<algSet.length;algI++)
				{
				 
					outputAlgSet[algI]=tOutputFileName+algSet[algI]+"-T"+tTourTime+".txt";
					pwAlgSet[algI]=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputAlgSet[algI])));
				}
				
				

				for(int j=0;j<networkSizeSet.length;j++)
				{
					
					int networkSize=networkSizeSet[j];
					int transRange=transRangeSet[j];
					int gatewayLimit=gatewayLimitSet[j];
					pwTime.print(networkSize+" ");
					
					for(int algI=0;algI<algSet.length;algI++)
					{
						ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
						
						pwAlgSet[algI].print(networkSize+" ");
						
						for(int k=0;k<ExperimentSetting.cishu;k++)
						{
												
							
							
							String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
							String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
							BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile, gatewayLimit, transRange);
						
							
							
						
							
							ArrayList<GateWay> solution=new ArrayList<GateWay>();
							LabResult tResult=null;
								
							switch(algI)
							{
							 case 0:
								 tResult=GC13_Alg.maxUnitUtilityGainTourDesign(bNet,solution); break;
							 case 1:
								 tResult=GC13_Alg.maxUtilityGainTourDesign(bNet,solution); break;
							 case 2:
								 tResult=GC13_Alg.randomUnitUtilityGainTourDesign(bNet,solution); break;
							 case 3:
								 tResult=GC13_Alg.randomUtilityGainTourDesign(bNet,solution); break;
							 case 4:
							 	{
							 		double iniMinRange=2*transRange;
									double iniMaxRange=3*transRange;
									double deltaRange=0.05*transRange;
									double iniTimeStamp=50;
									double deltaTimeStamp=5;
									tResult=GC13_Alg.disTraMaxUtilityGainTourDesign(bNet, iniMinRange, iniMaxRange, deltaRange, iniTimeStamp, deltaTimeStamp, solution); 
									break;
							 	}
							 case 5:
							 	{
							 		double iniMinRange=2*transRange;
									double iniMaxRange=10*transRange;
									double deltaRange=0.05*transRange;
									double iniTimeStamp=0;    // means no timestamp constraint
									double deltaTimeStamp=5;
									tResult=GC13_Alg.dis2TraMaxUtilityGainTourDesign(bNet, iniMinRange, iniMaxRange, deltaRange, iniTimeStamp, deltaTimeStamp, solution, "calcPriorityWeight"); 
									break;
							 	}		
							 case 6:
							 	{
							 		double iniMinRange=2*transRange;
									double iniMaxRange=10*transRange;
									double deltaRange=0.05*transRange;
									double iniTimeStamp=0;    // means no timestamp constraint
									double deltaTimeStamp=5;
									tResult=GC13_Alg.dis2TraMaxUtilityGainTourDesign(bNet, iniMinRange, iniMaxRange, deltaRange, iniTimeStamp, deltaTimeStamp, solution,"calcPriorityWeightPlusMoving"); 
									break;
							 	}							 
							}
							//begin of debug
//							for(int ti=0;ti<solution.size();ti++)
//							{
//									System.out.println(solution.get(ti));
//							}
							System.out.println("T--"+tTourTime+"!!!!!Completet--"+algSet[algI]+"("+tResult.getTotalUtility()+")--<Round>"+k+"-<Node>"+networkSize);
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
						gResult.setActiveNodes(activeNodes/resultSet.size());
						gResult.setTotalUtility(totalUtility/resultSet.size());
						gResult.setTotalSojournTime(totalSojournTime/resultSet.size());
						gResult.setTotalMovingTime(totalMovingTime/resultSet.size());
						
						
						pwAlgSet[algI].println("utility:"+df.format(gResult.getTotalUtility())+" movingtime:"+df.format(gResult.getTotalMovingTime())+" sojourntime"+df.format(gResult.getTotalSojournTime()));
						pwAlgSet[algI].flush();
						
						
						pwTime.print(df.format(gResult.getTotalUtility())+" ");
					}
					
					pwTime.println();
					pwTime.flush();

				}

				
				pwTime.flush();
				pwTime.close();
				
				
				for(int algI=0;algI<algSet.length;algI++)
				{
				 
					pwAlgSet[algI].flush();
					pwAlgSet[algI].close();
				}
				

				
		}
	}
	
	
	
	
	
	

			



	
	
	public static void main(String[] args) throws IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException 
	{
			
		GC13_Test.impactPerformance();
	
	}

}
