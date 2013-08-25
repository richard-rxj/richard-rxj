/**
 * 
 */
package wCharging.test;

/**
 * @author user
 *
 */
public class SimulationSetting {
	
	//k-mean
	public static final int kValue=10;
	public static final double kMeanThreshold=0.05;     //used to stop k-mean
	
	//charger
	public static final double timeLimit=0;             
    public static final double stepWaitingConstant=5;   //if no feasible then keep still and wait
	public static final double startX=0;                
	public static final double startY=0;
	
	//network topology
	public static final double xRange=500;
	public static final double yRange=500;
	public static final double travelSpeed=5;          //m/s
    public static final double chargingConstant=20;    //s

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
