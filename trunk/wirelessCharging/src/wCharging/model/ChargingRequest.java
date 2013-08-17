/**
 * 
 */
package wCharging.model;

/**
 * @author user
 *
 */
public class ChargingRequest implements Comparable<ChargingRequest> {

	private double xAxis;
	private double yAxis;
	private double releaseTime;
	private double dueTime;
	private double residualEnergy;
	private double residualLifetime;
	private double maxEnergy;
	
	/**
	 * @return the xAxis
	 */
	public double getxAxis() {
		return xAxis;
	}


	/**
	 * @param xAxis the xAxis to set
	 */
	public void setxAxis(double xAxis) {
		this.xAxis = xAxis;
	}


	/**
	 * @return the yAxis
	 */
	public double getyAxis() {
		return yAxis;
	}


	/**
	 * @param yAxis the yAxis to set
	 */
	public void setyAxis(double yAxis) {
		this.yAxis = yAxis;
	}


	/**
	 * @return the releaseTime
	 */
	public double getReleaseTime() {
		return releaseTime;
	}


	/**
	 * @param releaseTime the releaseTime to set
	 */
	public void setReleaseTime(double releaseTime) {
		this.releaseTime = releaseTime;
	}


	/**
	 * @return the dueTime
	 */
	public double getDueTime() {
		return dueTime;
	}


	/**
	 * @param dueTime the dueTime to set
	 */
	public void setDueTime(double dueTime) {
		this.dueTime = dueTime;
	}


	/**
	 * @return the residualEnergy
	 */
	public double getResidualEnergy() {
		return residualEnergy;
	}


	/**
	 * @param residualEnergy the residualEnergy to set
	 */
	public void setResidualEnergy(double residualEnergy) {
		this.residualEnergy = residualEnergy;
	}


	/**
	 * @return the residualLifetime
	 */
	public double getResidualLifetime() {
		return residualLifetime;
	}


	/**
	 * @param residualLifetime the residualLifetime to set
	 */
	public void setResidualLifetime(double residualLifetime) {
		this.residualLifetime = residualLifetime;
	}


	/**
	 * @return the maxEnergy
	 */
	public double getMaxEnergy() {
		return maxEnergy;
	}


	/**
	 * @param maxEnergy the maxEnergy to set
	 */
	public void setMaxEnergy(double maxEnergy) {
		this.maxEnergy = maxEnergy;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	@Override
	public int compareTo(ChargingRequest arg0) {
		// TODO Auto-generated method stub
		final int BEFORE =-1;
		final int EQUAL=0;
		final int AFTER=1;
		
		if(this.releaseTime == arg0.releaseTime)
			return EQUAL;
		if(this.releaseTime < arg0.releaseTime)
			return BEFORE;
		if(this.releaseTime > arg0.releaseTime)
			return AFTER;
		
		return EQUAL;
	}

}
