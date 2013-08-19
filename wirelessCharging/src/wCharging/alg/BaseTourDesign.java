/**
 * 
 */
package wCharging.alg;

import java.util.ArrayList;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;


/**
 * @author user
 *
 */
public abstract class BaseTourDesign {

	private ChargingRequestQueue  requestQueue;
	private double timeLimit;
	



	/**
	 * @param requestQueue the requestQueue to set
	 */
	public void setRequestQueue(ChargingRequestQueue requestQueue) {
		this.requestQueue = requestQueue;
	}





	public void setTimeLimit(double timeLimit) {
		this.timeLimit = timeLimit;
	}





	public abstract ArrayList<ChargingRequest> design(double currentTime);
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
