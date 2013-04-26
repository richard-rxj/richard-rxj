/**
 * 
 */
package pre.alg.anu;

/**
 * @author u4964526
 *
 */
public class PredictionItem {

	private double real;
	private double estimate;
	private String label;
	private double error;
	
	
	
	public double getReal() {
		return real;
	}



	public void setReal(double real) {
		this.real = real;
	}



	public double getEstimate() {
		return estimate;
	}



	public void setEstimate(double estimate) {
		this.estimate = estimate;
	}


	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}



	public double getError() {
		return error;
	}



	



	/**
	 * error is used for evaluation
	 */
	public double computeError()
	{
		this.error=Math.abs(1-this.real/this.estimate);
		return error;
	}

	@Override
	public String toString() {
		return "[error=" + error + ", estimate=" + estimate
				+ ", real=" + real + ", label=" + label + "]";
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
