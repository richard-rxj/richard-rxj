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
import coverage.alg.ICDCSSolution;
import coverage.alg.Solution;
import coverage.model.Coverage;
import coverage.model.Network;
import coverage.model.TimeSlot;
import coverage.util.ExperimentSetting;
import coverage.util.Func;
import coverage.util.FunctionFactory;

public class TestCentralized {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int[] targetSizes={25};//{25,50};
		int[] networkSizes={100};//{100, 200, 300, 400, 500};
		String[] algs={"Centralized", "Distributed","ICDCS"};
		String[] funcs={"Linear"};//{"SQR","LOG"};
		int cishu=1;//ExperimentSetting.cishu;

		
		String outputBase="data"+File.separator+"result";
		File tf=new File(outputBase);
		if(!tf.exists()) {
			tf.mkdirs();
		}
		
		for(int tI=0; tI<targetSizes.length; tI++) {
			int targetSize=targetSizes[tI];
			String outputFile=outputBase+File.separator+"CentralizedTest-Target-"+targetSize+".txt";
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
							
							Coverage coverage=null;
							if(algs[algI].equals("Centralized")) {
								Solution solution=new CentralizedSolution();
								solution.setFunc(func);
								solution.setNetwork(network);
								solution.setTimeslots(timeslots);
								coverage=solution.schedule();
							} else if(algs[algI].equals("ICDCS")) {
								Solution solution=new ICDCSSolution();
								solution.setFunc(func);
								solution.setNetwork(network);
								solution.setTimeslots(timeslots);
								coverage=solution.schedule();
							} else if (algs[algI].equals("Distributed")) {
								Solution solution=new DistributedSolution();
								solution.setFunc(func);
								solution.setNetwork(network);
								solution.setTimeslots(timeslots);
								coverage=solution.schedule();
							}
							
							result[fI][algI]+=coverage.computeCoverage();
							
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
