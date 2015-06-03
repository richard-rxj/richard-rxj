/**
 * 
 */
package wCharging.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import wCharging.alg.ClusterTourDesignImp;
import wCharging.alg.LeastLifetimeTourDesignImp;
import wCharging.alg.NearestTourDesignImp;
import wCharging.alg.OfflineApproTourDesignImp;
import wCharging.alg.OfflineOptimalTourDesignImp;
import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;
import wCharging.util.HtmlLogFormatter;

/**
 * @author u4964526
 * 
 */
public class SmallScaleTest {

	public static final Logger gLog = Logger.getLogger(SimulationSetting.class
			.getName());
	static {
		gLog.setLevel(Level.ALL);
		// gLog.addHandler(new ConsoleHandler());
		try {
			FileHandler htmlHandler = new FileHandler(
					SmallScaleTest.class.getName() + ".html");
			htmlHandler.setFormatter(new HtmlLogFormatter());
			gLog.addHandler(htmlHandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test() throws FileNotFoundException {
		SimulationSetting.xRange = 50;
		SimulationSetting.yRange = 50;
		SimulationSetting.kValue = 3;
		SimulationSetting.timeLimit = 30;
		SimulationSetting.ciShu = 3;
		SimulationSetting.chargingConstant = 2;
		SimulationSetting.travelSpeed = 8;

		int[] requestNumSet = { 10, 15, 20, 25, 30 };
		int cishu = SimulationSetting.ciShu;
		String[] algSet = { "Nearest", "Cluster", "Appro", "Optimal",
				"LeastLifetime" };

		String tFileName = "test/smallscale";
		File tf = new File(tFileName);
		if (!tf.exists()) {
			tf.mkdirs();
		}

		PrintWriter pwAlg = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream("small.txt", true)));

		for (int requestNumIndex = 0; requestNumIndex < requestNumSet.length; requestNumIndex++) {
			int requestNum = requestNumSet[requestNumIndex];
			pwAlg.print(requestNum + " ");
			pwAlg.flush();

			int nearestResult = 0;
			int clusterResult = 0;
			int approResult = 0;
			int optimalResult = 0;
			int leastResult = 0;
			for (int i = 0; i < cishu; i++) {
				gLog.warning("requestNum: " + requestNum + "   Iteration: " + i);

				ChargingRequestQueue testQueue1 = SimulationSetting
						.generateRequest(requestNum);
				ChargingRequestQueue testQueue2 = (ChargingRequestQueue) testQueue1
						.clone();
				ChargingRequestQueue testQueue3 = (ChargingRequestQueue) testQueue1
						.clone();
				ChargingRequestQueue testQueue4 = (ChargingRequestQueue) testQueue1
						.clone();
				ChargingRequestQueue testQueue5 = (ChargingRequestQueue) testQueue1
						.clone();

				NearestTourDesignImp nearestSolution = new NearestTourDesignImp();
				nearestSolution.setRequestQueue(testQueue1);
				nearestSolution.setTimeLimit(SimulationSetting.timeLimit);
				nearestSolution.setStartX(SimulationSetting.startX);
				nearestSolution.setStartY(SimulationSetting.startY);
				nearestSolution.setCurrentTime(0);
				ArrayList<ChargingRequest> tList = nearestSolution.design();
				nearestResult = nearestResult + tList.size();

				gLog.warning("nearestSolution: " + tList.size());
				for (ChargingRequest c : tList) {
					gLog.info(c.toString());
				}

				ClusterTourDesignImp clusterSolution = new ClusterTourDesignImp();
				clusterSolution.setRequestQueue(testQueue2);
				clusterSolution.setTimeLimit(SimulationSetting.timeLimit);
				clusterSolution.setStartX(SimulationSetting.startX);
				clusterSolution.setStartY(SimulationSetting.startY);
				clusterSolution.setCurrentTime(0);
				clusterSolution.setkValue(SimulationSetting.kValue);
				tList = clusterSolution.design();
				clusterResult = clusterResult + tList.size();

				gLog.warning("clusterSolution: " + tList.size());
				for (ChargingRequest c : tList) {
					gLog.info(c.toString());
				}

				OfflineApproTourDesignImp approSolution = new OfflineApproTourDesignImp();
				approSolution.setRequestQueue(testQueue3);
				approSolution.setTimeLimit(SimulationSetting.timeLimit);
				approSolution.setStartX(SimulationSetting.startX);
				approSolution.setStartY(SimulationSetting.startY);
				approSolution.setCurrentTime(0);
				tList = approSolution.design();
				approResult = approResult + tList.size();

				gLog.warning("approSolution: " + tList.size());
				for (ChargingRequest c : tList) {
					gLog.info(c.toString());
				}

				OfflineOptimalTourDesignImp optimalSolution = new OfflineOptimalTourDesignImp();
				optimalSolution.setRequestQueue(testQueue4);
				optimalSolution.setTimeLimit(SimulationSetting.timeLimit);
				optimalSolution.setStartX(SimulationSetting.startX);
				optimalSolution.setStartY(SimulationSetting.startY);
				optimalSolution.setCurrentTime(0);
				tList = optimalSolution.design();
				optimalResult = optimalResult + tList.size();

				gLog.warning("optimalSolution: " + tList.size());
				for (ChargingRequest c : tList) {
					gLog.info(c.toString());
				}

				LeastLifetimeTourDesignImp leastSolution = new LeastLifetimeTourDesignImp();
				leastSolution.setRequestQueue(testQueue5);
				leastSolution.setTimeLimit(SimulationSetting.timeLimit);
				leastSolution.setStartX(SimulationSetting.startX);
				leastSolution.setStartY(SimulationSetting.startY);
				leastSolution.setCurrentTime(0);
				tList = leastSolution.design();
				leastResult = leastResult + tList.size();

				gLog.warning("leastSolution: " + tList.size());
				for (ChargingRequest c : tList) {
					gLog.info(c.toString());
				}
			}
			nearestResult = nearestResult / cishu;
			clusterResult = clusterResult / cishu;
			approResult = approResult / cishu;
			optimalResult = optimalResult / cishu;
			leastResult = leastResult / cishu;
			pwAlg.println(nearestResult + " " + clusterResult + " "
					+ approResult + " " + optimalResult + " " + leastResult);
			pwAlg.flush();

			gLog.severe("average: <nearestSolution: " + nearestResult + ">"
					+ " <clusterSolution: " + clusterResult + ">"
					+ " <approSolution: " + approResult + ">"
					+ " <optimalSolution: " + optimalResult + ">"
					+ " <leastSolution: " + leastResult + ">");
		}
		pwAlg.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
