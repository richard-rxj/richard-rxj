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

	protected ChargingRequestQueue  requestQueue;
	protected double timeLimit;
	protected double currentTime;
    protected double startX;
    protected double startY;



	/**
	 * @param requestQueue the requestQueue to set
	 */
	public void setRequestQueue(ChargingRequestQueue requestQueue) {
		this.requestQueue = requestQueue;
	}





	public void setTimeLimit(double timeLimit) {
		this.timeLimit = timeLimit;
	}





	public double getCurrentTime() {
		return currentTime;
	}





	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
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
