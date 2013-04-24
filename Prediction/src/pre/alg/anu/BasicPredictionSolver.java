/**
 * 
 */
package pre.alg.anu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author u4964526
 *
 */
public class BasicPredictionSolver extends PredictionSolver {

	/**
	 *      only expotential moving average
	 */
	public BasicPredictionSolver() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pre.alg.anu.PredictionSolver#predict(java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	public PredictionResult predict(String inFile, int nPast, double alpha,
			int nResult) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
		PredictionResult result=new PredictionResult();
		
		
		
		
		
		double[][] gGrid=new double[nPast+nResult][11]; //double[Days][Hours]
		//pass the first line
		String tempString=in.readLine();
		//fill gGrid using data from inFile
		for(int i=0;i<gGrid.length;i++)
		{
			tempString=in.readLine();
			String[] temp=tempString.split(" ");
			for(int j=0; j<gGrid[0].length; j++)
			{
				gGrid[i][j]=Double.parseDouble(temp[j+1]);
			}
			System.out.println(gGrid[i][0]);   //for debug
		}
		
		
		
		
		in.close();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
        String inFile="test/solar.txt";
        String outFile="test/result_basic.txt";
        int nPast=10;
        double alpha=0.7;
        int nResult=10;
        new BasicPredictionSolver().predict(inFile, outFile, nPast, alpha, nResult);
	}

}
