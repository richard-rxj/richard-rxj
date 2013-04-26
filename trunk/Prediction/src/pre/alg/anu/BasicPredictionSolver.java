/**
 * 
 */
package pre.alg.anu;

import java.io.BufferedReader;
import java.io.File;
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
	public PredictionResult predict(String inFile, String outFile, int nPast, double alpha,
			int nResult) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
		PredictionResult result=new PredictionResult(nResult);
		
		//pass the first line
		String tempString=in.readLine();
		//get the first line
		int tDay=1;
		tempString=in.readLine();
		String[] temp=tempString.split(" ");
		double[] average=new double[temp.length-1];
		for(int i=0; i<average.length;i++ )
		{
			average[i]=Double.parseDouble(temp[i+1]);
		}
		
		//get the rest lines 
		while((tempString=in.readLine())!=null)
		{
			tDay++;
			temp=tempString.split(" ");
			if(tDay<=nPast)   //history informaton
			{
				for(int i=0; i<average.length;i++)
				{
					average[i]=alpha*average[i]+(1-alpha)*Double.parseDouble(temp[i+1]);
				}
			}
			else if(tDay<=(nResult+nPast))   // begin predict
			{
				for(int i=0; i<average.length;i++)
				{
					PredictionItem tItem=new PredictionItem();
					tItem.setLabel(temp[0]);
					tItem.setEstimate(average[i]);
					tItem.setReal(Double.parseDouble(temp[i+1]));
					tItem.computeError();
					result.addResult(tDay-nPast, tItem);
					average[i]=alpha*average[i]+(1-alpha)*Double.parseDouble(temp[i+1]);
				}
			}
			else
				break;
		}
		
		
		result.output(outFile);

		
		

		in.close();
		return result;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
        String inFile="test/solar.txt";
        String outFile="test"+File.separator+"result_basic.txt";
        int nPast=ParameterSetting.nPast;
        double alpha=ParameterSetting.alpha;
        int nResult=ParameterSetting.nPast;
        
        new BasicPredictionSolver().predict(inFile, outFile, nPast, alpha, nResult);
	}

}
