package coverage.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import coverage.alg.CentralizedSolution;
import coverage.alg.DistributedSolution;
import coverage.alg.DynamicFrameWork;
import coverage.alg.PathBasedCentralizedSolution;
import coverage.alg.Solution;
import coverage.model.Coverage;
import coverage.model.Network;
import coverage.model.TimeSlot;
import coverage.util.ExperimentSetting;
import coverage.util.Func;
import coverage.util.FunctionFactory;

public class TestFrameworkByTuningWeight {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
	public static void test() throws IOException {
		int[] targetSizes={25,50};
		int[] networkSizes={100, 200, 300, 400, 500};
		String[] algs={"Centralized", "Framework-beta-0.8", "Framework-beta-0.5", "Framework-beta-0.2"};
		String[] funcs={"SQR","LOG"};
		int cishu=ExperimentSetting.cishu;
		ExperimentSetting.accuracyThreshold=0.20;
		ExperimentSetting.budgetFactor=0.5;

		
		String outputBase="data"+File.separator+"result";
		File tf=new File(outputBase);
		if(!tf.exists()) {
			tf.mkdirs();
		}
		
		for(int tI=0; tI<targetSizes.length; tI++) {
			int targetSize=targetSizes[tI];
			String outputFile=outputBase+File.separator+"FrameworkTestByTuningWeight-Target-"+targetSize+".txt";
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile, false)), true);
			for(int nI=0; nI<networkSizes.length; nI++) {
				int networkSize=networkSizes[nI];
				pw.print(networkSize);
				double[][] result=new double[funcs.length][algs.length];
				
				for(int cI=0; cI<cishu; cI++) {
					String topologyFile="data"+File.separator+"topology"+File.separator
							 +"topology_"+networkSize+"_"+targetSize+"_"+cI+".txt";
					for(int fI=0; fI<funcs.length; fI++) {
						for(int algI=0; algI<algs.length; algI++) {
							Func func=FunctionFactory.getFunc(funcs[fI]);
							List<TimeSlot> timeslots=ExperimentSetting.getTimeSlots();
							Network network=new Network();
							network.restoreFromFile(topologyFile);
							
							double tResult=0;
							if(algs[algI].equals("Centralized")) {
								Solution solution=new CentralizedSolution();
								solution.setFunc(func);
								solution.setNetwork(network);
								solution.setTimeslots(timeslots);
								Coverage coverage=solution.schedule();
								tResult=coverage.computeCoverage();
								ExperimentSetting.gLog.info(String.format("CoverageGain---<%.2f>", tResult));
								double tResult2=coverage.computeCoverageWithoutAccuracy();
								ExperimentSetting.gLog.info(String.format("CoverageGainWithAccuracy---<%.2f>", tResult2));
							} else if(algs[algI].equals("Framework-beta-0.8")) {
								ExperimentSetting.tuningWeight=0.8;
								DynamicFrameWork solution=new DynamicFrameWork();
								solution.setFunc(func);
								solution.setNetwork(network);
								solution.setTimeslots(timeslots);
								tResult=solution.schedule();
							} else if(algs[algI].equals("Framework-beta-0.5")) {
								ExperimentSetting.tuningWeight=0.5;
								DynamicFrameWork solution=new DynamicFrameWork();
								solution.setFunc(func);
								solution.setNetwork(network);
								solution.setTimeslots(timeslots);
								tResult=solution.schedule();
							} else if(algs[algI].equals("Framework-beta-0.2")) {
								ExperimentSetting.tuningWeight=0.2;
								DynamicFrameWork solution=new DynamicFrameWork();
								solution.setFunc(func);
								solution.setNetwork(network);
								solution.setTimeslots(timeslots);
								tResult=solution.schedule();
							} 
							
							result[fI][algI]+=tResult;
							
							ExperimentSetting.gLog.severe(String.format("Result<%.2f>-Cishu<%d>-%s-%s-finished", result[fI][algI], cI, algs[algI], funcs[fI]));
						}
					}
				}
				
				for(int rI=0; rI<result.length; rI++) {
					for(int rJ=0; rJ<result[rI].length; rJ++) {
						pw.print(String.format(" %.2f", result[rI][rJ]/cishu));
					}
				}
				pw.println();
			}
			
			pw.close();
		}
	}
}
