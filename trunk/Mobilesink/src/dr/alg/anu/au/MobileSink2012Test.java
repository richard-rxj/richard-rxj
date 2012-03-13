///**
// * 
// */
//package dr.alg.anu.au;
//
//import generate.dr.alg.anu.au.NetworkGenerator;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.logging.FileHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;
//
//import network.dr.alg.anu.au.BiNetwork;
//import network.dr.alg.anu.au.GateWay;
//import network.dr.alg.anu.au.LabResult;
//
///**
// * @author user
// *
// */
//public class MobileSink2012Test {
//
//	/**
//	 * @param args
//	 * @throws IOException 
//	 */
//	public static void impactPerformanceWithLinear() throws IOException
//	{
//		int[] networkSizeSet={100,200,300,400,500,600};
//		int[] transRangeSet={23,16,14,12,10,10};
//		int  cishu=15;   
//	
//			
//
//			String tOutputFileName="test/ImpactPerformance/";
//			File tf=new File(tOutputFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//			String tFileName="test/Topology/";
//			tf=new File(tFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//			
//			
//			
//			
//			
//
//			
//			String outputFile=tOutputFileName+"linear-tour.txt";
//			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
//			for(int j=0;j<networkSizeSet.length;j++)
//			{
//				ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
//				int networkSize=networkSizeSet[j];
//				int transRange=transRangeSet[j];
//				ExperimentSetting.transmissionRange=transRange;
//				
//				for(int k=0;k<cishu;k++)
//				{
//										
//					
//					
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
//					System.out.println("!!!!!Completet--Linear--<Round>"+k+"-<Node>"+networkSize+"-<TourDesign>");
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
//		
//		
//			
//			
//			outputFile=tOutputFileName+"linear-random.txt";
//			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
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
//					ArrayList<GateWay> solution=TourDesign.randomLinearTourDesign(nFile, gFile);
//					
//					//begin of debug
//					for(int ti=0;ti<solution.size();ti++)
//					{
//							System.out.println(solution.get(ti));
//					}
//					System.out.println("!!!!!Completet--Linear--<Round>"+k+"-<Node>"+networkSize+"-<Random>");
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
//			
//			
//			outputFile=tOutputFileName+"linear-nearest.txt";
//			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
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
//					ArrayList<GateWay> solution=TourDesign.nearestLinearTourDesign(nFile, gFile);
//					
//					//begin of debug
//					for(int ti=0;ti<solution.size();ti++)
//					{
//							System.out.println(solution.get(ti));
//					}
//					System.out.println("!!!!!Completet--Linear--<Round>"+k+"-<Node>"+networkSize+"-<Nearest>");
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
//			
//			
//	}
//	
//	
//	
//	public static void impactWeightWithLinear() throws IOException
//	{
//		int[] networkSizeSet={100,200,300,400,500,600};
//		int[] transRangeSet={23,16,14,12,10,10};
//		int[] weightSet={0,20,40,60,80,100};   // 0,5,10,20,40,80,100   divide 100
//		int[] speedSet={5,10,30,60,180};
//		int  cishu=15;   
//		for(int s=0;s<speedSet.length;s++)
//		{
//			int speed=speedSet[s];
//			ExperimentSetting.mSpeed=speed*1.0/10;
//			
//			for(int i=0;i<weightSet.length;i++)
//			{
//				String tFileName="test/ImpactWeight-rg-"+speed+"/";
//				File tf=new File(tFileName);
//				if(!tf.exists())
//				{
//					tf.mkdirs();
//				}
//				int tWeight=weightSet[i];
//				
//				String outputFile=tFileName+"linear-weight-"+Integer.toString(tWeight)+".txt";
//				PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
//	
//				
//				ExperimentSetting.lossWeight=1.0*tWeight/100;
//				
//							
//				tFileName="test/Topology/";
//				tf=new File(tFileName);
//				if(!tf.exists())
//				{
//					tf.mkdirs();
//				}
//				
//				for(int j=0;j<networkSizeSet.length;j++)
//				{
//					ArrayList<LabResult> resultSet=new ArrayList<LabResult>();
//					int networkSize=networkSizeSet[j];
//					int transRange=transRangeSet[j];
//					ExperimentSetting.transmissionRange=transRange;
//					
//					for(int k=0;k<cishu;k++)
//					{
//						String nFile=tFileName+"node-"+networkSize+"-"+k+".txt";
//						String gFile=tFileName+"gateway-"+networkSize+"-"+k+".txt";
//						BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
//						ArrayList<GateWay> solution=TourDesign.linearTourDesign(nFile, gFile);
//						
//						//begin of debug
//						for(int ti=0;ti<solution.size();ti++)
//						{
//								System.out.println(solution.get(ti));
//						}
//						System.out.println("!!!!!Completet--Linear--<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
//						//end of debug
//						
//						
//						LabResult tResult=TourDesign.getSimInfo(solution, bNet, ExperimentSetting.tourTime);
//						resultSet.add(tResult);
//					}
//					
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
//					
//					pw.println(networkSize+" "+gResult);
//					pw.flush();
//				}
//				pw.close();
//			}
//		}
//	}
//	
//	
//	public static void impactWeightWithFair() throws IOException
//	{
//		int[] networkSizeSet={100,200,300,400,500,600};
//		int[] transRangeSet={23,16,14,12,10,10};
//		int[] weightSet={0,5,10,20,40,80,100};   // 0,5,10,20,40,80,100   divide 100
//		int  cishu=15;   
//		for(int i=0;i<weightSet.length;i++)
//		{
//			String tFileName="test/ImpactWeight/";
//			File tf=new File(tFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//			int tWeight=weightSet[i];
//			
//			String outputFile=tFileName+"fair-weight-"+Integer.toString(tWeight)+".txt";
//			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
//
//			
//			ExperimentSetting.lossWeight=1.0*tWeight/100;
//			
//						
//			tFileName="test/Topology/";
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
//					ArrayList<GateWay> solution=TourDesign.fairTourDesign(nFile, gFile);
//					
//					//begin of debug
//					for(int ti=0;ti<solution.size();ti++)
//					{
//							System.out.println(solution.get(ti));
//					}
//					System.out.println("!!!!!Completet--Fair--<Round>"+k+"-<Node>"+networkSize+"-<Weight>"+tWeight);
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
//	
//	
//	
//	public static void impactTimeWithLinear() throws IOException
//	{
//		int[] networkSizeSet={100,200,300,400,500,600};
//		int[] transRangeSet={23,16,14,12,10,10};
//		int[] weightSet={300,600,1200,2400,4800,36000,72000,144000};   
//		int  cishu=15;   
//		for(int i=0;i<weightSet.length;i++)
//		{
//			String tFileName="test/ImpactTime/";
//			File tf=new File(tFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//			int tWeight=weightSet[i];
//			
//			String outputFile=tFileName+"linear-time-"+Integer.toString(tWeight)+".txt";
//			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
//
//			
//			ExperimentSetting.tourTime=tWeight;
//			
//						
//			tFileName="test/Topology/";
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
//	
//	public static void impactTimeWithFair() throws IOException
//	{
//		int[] networkSizeSet={100,200,300,400,500,600};
//		int[] transRangeSet={23,16,14,12,10,10};
//		int[] weightSet={300,600,1200,2400,4800};   // 0,5,10,20,40,80,100   divide 100
//		int  cishu=15;   
//		for(int i=0;i<weightSet.length;i++)
//		{
//			String tFileName="test/ImpactTime/";
//			File tf=new File(tFileName);
//			if(!tf.exists())
//			{
//				tf.mkdirs();
//			}
//			int tWeight=weightSet[i];
//			
//			String outputFile=tFileName+"fair-time-"+Integer.toString(tWeight)+".txt";
//			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
//
//			
//			ExperimentSetting.tourTime=tWeight;
//			
//						
//			tFileName="test/Topology/";
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
//					ArrayList<GateWay> solution=TourDesign.fairTourDesign(nFile, gFile);
//					
//					//begin of debug
//					for(int ti=0;ti<solution.size();ti++)
//					{
//							System.out.println(solution.get(ti));
//					}
//					System.out.println("!!!!!Completet---Fair---<Round>"+k+"-<Node>"+networkSize+"-<Time>"+tWeight);
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
//	
//	
//	public static void main(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//		//Logger logger=Logger.getLogger("MobileSink");
//		//FileHandler fh=new FileHandler("m.log");
//		//fh.setFormatter(new SimpleFormatter());
//		//fh.setLevel(Level.WARNING);
//		//logger.addHandler(fh);
//		
//		
//		
//		
////		String tFileName="test/";
////		File tf=new File(tFileName);
////		if(!tf.exists())
////		{
////			tf.mkdirs();
////		}
////		
////		String nFile=tFileName+"n-100.txt";
////		String gFile=tFileName+"g-100.txt";
////		
////		//BiNetwork bNet=NetworkGenerator.generateNetwork(100, 50);
////		//bNet.saveToFile(nFile, gFile);
////		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
////		//bNet.saveToFile(tFileName+"n-100-2.txt", tFileName+"g-100-2.txt");
////		
////		//ArrayList<GateWay> solution=TourDesign.linearTourDesign(nFile, gFile);
////		ArrayList<GateWay> solution=TourDesign.fairTourDesign(nFile, gFile);
////		for(int i=0;i<solution.size();i++)
////		{
////			System.out.println(solution.get(i));
////		}
//		MobileSink2012Test.impactWeightWithLinear();
//		//MobileSinkTest.impactWeightWithFair();
//		//MobileSinkTest.impactTimeWithLinear();
//		//MobileSinkTest.impactTimeWithFair();
//		//MobileSinkTest.impactPerformanceWithLinear();
//	}
//
//}
