/**
 * 
 */
package wCharging.alg;

import java.util.ArrayList;
import java.util.Collections;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;
import wCharging.test.SimulationSetting;
import wCharging.util.TravelTimeComparator;

/**
 * @author u4964526
 *
 */
public class NearestTourDesignImp extends BaseTourDesign {

	/* (non-Javadoc)
	 * @see wCharging.alg.BaseTourDesign#design()
	 */
	@Override
	public ArrayList<ChargingRequest> design() {
		// TODO Auto-generated method stub
		
		
		ArrayList<ChargingRequest> result=new ArrayList<ChargingRequest>();
		
		double currentX=this.startX;
		double currentY=this.startY;
		
		while(currentTime<=this.timeLimit)
		{
			ChargingRequest target=this.subDesign(currentX, currentY, this.currentTime, this.requestQueue.getSubQueueByReleaseTime(currentTime));
			if(target==null)
			{
				target=new ChargingRequest();
				target.setId(-1);
				target.setProcessTime(SimulationSetting.stepWaitingConstant);
			}
			else
			{
				target.setProcessTime(target.getTravelTime());
				this.requestQueue.removeById(target.getId());      //update the queue
				currentX=target.getxAxis();
				currentY=target.getyAxis();
			}
			this.currentTime=this.currentTime+target.getProcessTime();       //update time
			result.add(target);
		}
		
		return result;
	}

	
	private ChargingRequest subDesign(double currentX, double currentY, double currentTime, ChargingRequestQueue currentQueue)
	{
		ChargingRequest result=null;
		
		for(ChargingRequest c:currentQueue)
		{
			c.ComputeBothTime(currentX, currentY, this.startX, this.startY);
		}
		
		ChargingRequestQueue  feasibleQueue=currentQueue.getSubQueueByTimeLimit(this.timeLimit-currentTime);
		
		Collections.sort(feasibleQueue, new TravelTimeComparator(true));
		
		result=currentQueue.peek();
		
		return result;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
