/**
 * 
 */
package wCharging.alg;

import wCharging.model.ChargingRequest;
import wCharging.model.SortedQueue;

/**
 * @author user
 *
 */
public abstract class BaseTourDesign {

	private SortedQueue<ChargingRequest>  requestQueue;
	private double  timeLimit;
	
	



	/**
	 * @param requestQueue the requestQueue to set
	 */
	public void setRequestQueue(SortedQueue<ChargingRequest> requestQueue) {
		this.requestQueue = requestQueue;
	}


	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(double timeLimit) {
		this.timeLimit = timeLimit;
	}


	public abstract void design();
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
