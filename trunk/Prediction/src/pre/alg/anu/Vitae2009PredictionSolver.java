/**
 * 
 */
package pre.alg.anu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author u4964526
 *
 */
public class Vitae2009PredictionSolver extends PredictionSolver {

	/* (non-Javadoc)
	 * @see pre.alg.anu.PredictionSolver#predict(java.lang.String, java.lang.String, int, double, int)
	 */
	@Override
	public PredictionResult predict(String inFile, String outFile, int nPast,
			double alpha, int nResult) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
		PredictionResult result=new PredictionResult(nResult);
		
		//pass the first line
		String tempString=in.readLine();
		//get the first line
		int tDay=1;
		double refineFactor=1;
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
					tItem.setEstimate(refineFactor*average[i]);   //refineFactor apply here !!!!!!
					tItem.setReal(Double.parseDouble(temp[i+1]));
					tItem.computeError();
					result.addResult(tDay-nPast, tItem);
					refineFactor=tItem.getReal()/average[i];
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
        String outFile="test"+File.separator+"result_dcoss2009.txt";
        int nPast=ParameterSetting.nPast;
        double alpha=ParameterSetting.alpha;
        int nResult=ParameterSetting.nPast;
        
        new Vitae2009PredictionSolver().predict(inFile, outFile, nPast, alpha, nResult);
	}

}
