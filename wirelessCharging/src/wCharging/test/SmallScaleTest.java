/**
 * 
 */
package wCharging.test;

import wCharging.alg.ClusterTourDesignImp;
import wCharging.alg.NearestTourDesignImp;
import wCharging.alg.OfflineApproTourDesignImp;
import wCharging.model.ChargingRequestQueue;

/**
 * @author u4964526
 *
 */
public class SmallScaleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ChargingRequestQueue testQueue1=SimulationSetting.generateRequest(20);
		ChargingRequestQueue testQueue2=(ChargingRequestQueue)testQueue1.clone();
		ChargingRequestQueue testQueue3=(ChargingRequestQueue)testQueue1.clone();
		
		NearestTourDesignImp nearestSolution=new NearestTourDesignImp();
		nearestSolution.setRequestQueue(testQueue1);
		nearestSolution.setTimeLimit(SimulationSetting.timeLimit);
		nearestSolution.setStartX(SimulationSetting.startX);
		nearestSolution.setStartY(SimulationSetting.startY);
		nearestSolution.setCurrentTime(0);
		nearestSolution.design();
		
		
		ClusterTourDesignImp clusterSolution=new ClusterTourDesignImp();
		clusterSolution.setRequestQueue(testQueue2);
		clusterSolution.setTimeLimit(SimulationSetting.timeLimit);
		clusterSolution.setStartX(SimulationSetting.startX);
		clusterSolution.setStartY(SimulationSetting.startY);
		clusterSolution.setCurrentTime(0);
		clusterSolution.setkValue(SimulationSetting.kValue);
		clusterSolution.design();
		
		
		OfflineApproTourDesignImp approSolution=new OfflineApproTourDesignImp();
		approSolution.setRequestQueue(testQueue3);
		approSolution.setTimeLimit(SimulationSetting.timeLimit);
		approSolution.setStartX(SimulationSetting.startX);
		approSolution.setStartY(SimulationSetting.startY);
		approSolution.setCurrentTime(0);
		approSolution.design();
	}

}
