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
    private double startX;
    private double startY;



	/**
	 * @param requestQueue the requestQueue to set
	 */
	public void setRequestQueue(ChargingRequestQueue requestQueue) {
		this.requestQueue = requestQueue;
	}





	public void setTimeLimit(double timeLimit) {
		this.timeLimit = timeLimit;
	}





	/**
	 * @param startX the startX to set
	 */
	public void setStartX(double startX) {
		this.startX = startX;
	}





	/**
	 * @param startY the startY to set
	 */
	public void setStartY(double startY) {
		this.startY = startY;
	}





	public abstract ArrayList<ChargingRequest> design();
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
