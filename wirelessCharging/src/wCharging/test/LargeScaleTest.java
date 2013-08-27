package wCharging.test;

import wCharging.alg.ClusterTourDesignImp;
import wCharging.alg.NearestTourDesignImp;
import wCharging.alg.OfflineApproTourDesignImp;
import wCharging.model.ChargingRequestQueue;

public class LargeScaleTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SimulationSetting.xRange=500;
		SimulationSetting.yRange=500;
		SimulationSetting.kValue=5;
		SimulationSetting.timeLimit=1800;
		
		
		
		ChargingRequestQueue testQueue1=SimulationSetting.generateRequest(200);
		ChargingRequestQueue testQueue2=(ChargingRequestQueue)testQueue1.clone();

		
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
		
		

	}

}
