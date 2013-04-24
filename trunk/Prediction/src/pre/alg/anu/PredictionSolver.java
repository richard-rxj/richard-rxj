/**
 * 
 */
package pre.alg.anu;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author u4964526
 *
 */
public abstract class PredictionSolver {
	
	/**
	 *  @author u4964526
	 *  @param  inFile: String---Filename of input
	 *          outFile:String---Filename of output
	 *          nPast:int--------number of past days
	 *          alpha:int--------weight factor
	 *          nResult:int------number of prediction days required
	 */
	public abstract PredictionResult predict(String inFile, int nPast, double alpha, int nResult) throws IOException;
	
	

}
