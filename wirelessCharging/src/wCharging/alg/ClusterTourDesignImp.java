/**
 * 
 */
package wCharging.alg;

import java.util.ArrayList;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;

/**
 * @author u4964526
 *
 */
public class ClusterTourDesignImp extends BaseTourDesign {

	private int kValue=0;
	
	
	
	/* (non-Javadoc)
	 * @see wCharging.alg.BaseTourDesign#design()
	 */
	public int getkValue() {
		return kValue;
	}


	public void setkValue(int kValue) {
		this.kValue = kValue;
	}


	@Override
	public ArrayList<ChargingRequest> design() {
		// TODO Auto-generated method stub
		ArrayList<ChargingRequest> result=new ArrayList<ChargingRequest>();
		
		double currentX=this.startX;
		double currentY=this.startY;
		
		while(this.currentTime<=this.timeLimit)
		{
			ArrayList<ChargingRequest> tList=this.subClusterDesign(currentX, currentY, this.currentTime, this.requestQueue.getSubQueueByReleaseTime(currentTime));
			if(tList.size()==0)
			{
				break;
			}
			else
			{
				ChargingRequest c=null;
				for(int i=0;i<tList.size();i++)
				{
					c=tList.get(i);
					c.setProcessTime(c.getTravelTime());
					result.add(c);		
					this.requestQueue.removeById(c.getId());     //update the queue
					this.currentTime=this.currentTime+c.getProcessTime();  //update the time
				}
				currentX=c.getxAxis();
				currentY=c.getyAxis();
			}
		}
		
		// for the residual requests, apply nearest rule
		{
			NearestTourDesignImp nImp=new NearestTourDesignImp();
			nImp.setRequestQueue(this.requestQueue);
			nImp.setTimeLimit(this.timeLimit);
			nImp.setCurrentTime(this.currentTime);
			nImp.setStartX(currentX);
			nImp.setStartY(currentY);
			ArrayList<ChargingRequest> tList=nImp.design();
			for(ChargingRequest c:tList)
			{
				result.add(c);
			}
		}
		
		return result;
		
		
	}

	
	private ArrayList<ChargingRequest> subClusterDesign(double currentX, double currenY, double currentTime, ChargingRequestQueue currentQueue)
	{
		ArrayList<ChargingRequest> result=new ArrayList<ChargingRequest>();
		
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
