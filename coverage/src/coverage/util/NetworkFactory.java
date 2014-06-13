/**
 * 
 */
package coverage.util;

import coverage.model.Network;

/**
 * @author u4964526
 *
 */
public class NetworkFactory {

    private int sensorSize;
    private int targetSize;
    
	
	


	public void setSensorSize(int sensorSize) {
		this.sensorSize = sensorSize;
	}


	


	public void setTargetSize(int targetSize) {
		this.targetSize = targetSize;
	}


	public static Network getNetwork() {
		Network result=new Network();
		
		//TBD
		
		return result;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
