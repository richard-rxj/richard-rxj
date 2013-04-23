/**
 * 
 */
package pre.alg.anu;

import java.io.IOException;

/**
 * @author u4964526
 *
 */
public abstract class PredictionSolver {
	public abstract void predict(String inFile, String outFile, int nPast, double alpha, int nResult) throws IOException;
	/**
	 *  @author u4964526
	 *  @param  inFile: String---Filename of input
	 *          outFile:String---Filename of output
	 *          nPast:int--------number of past days
	 *          alpha:int--------weight factor
	 *          nResult:int------number of prediction days required
	 */
}
