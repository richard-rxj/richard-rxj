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
		tempString=in.readLine();
		String[] temp=tempString.split(" ");
		double[] average=new double[temp.length-1];
		double[] refineFactor=new double[temp.length-1];
		double[][] gGrid=new double[nPast+nResult][temp.length-1];
		
		for(int i=0; i<gGrid[0].length;i++ )   //store the first line data
		{
			gGrid[0][i]=Double.parseDouble(temp[i+1]);
			
		}
		
		//get the rest lines 
		while((tempString=in.readLine())!=null)
		{
			tDay++;
			temp=tempString.split(" ");
			
			
			
			
			if(tDay<=nPast)   //history informaton
			{
				for(int i=0; i<gGrid[tDay-1].length;i++ )   //store the rest line data
				{
					gGrid[tDay-1][i]=Double.parseDouble(temp[i+1]);
					
				}
				continue;
			}
			else if(tDay<=(nResult+nPast))   // begin predict
			{
				
				for(int i=0; i<gGrid[tDay-1].length;i++ )   //store the rest line data
				{
					gGrid[tDay-1][i]=Double.parseDouble(temp[i+1]);
					
				}
				
				
				for(int i=0; i<average.length;i++)
				{
					/*
					 * begin:get the average of past nPast days
					 */
					average[i]=0;
					for(int j=tDay-nPast-1;j<tDay-1;j++)
					{
						average[i]+=gGrid[j][i];
					}
					average[i]=average[i]/nPast;
					/*
					 * end: get the average of past nPast days
					 */
					
					/*
					 * begin: calculate the refineFactor;
					 */
					refineFactor[i]=Double.parseDouble(temp[i+1])/average[i];
					double tRefineFactor=1;
					double tGAPDividend=0;
					double tGAPDividor=0;
					for(int j=0;j<i;j++)
					{
						tGAPDividend=tGAPDividend+refineFactor[i]*(j+1)/i;
						tGAPDividor=tGAPDividor+(j+1)/i;
					}
					tRefineFactor=tGAPDividend/tGAPDividor;
					/*
					 * end: calculate the refineFactor;
					 */
					double tEstimate=average[i];
					if(i>0)
					{
						tEstimate=tRefineFactor*average[i]*alpha+(1-alpha)*gGrid[tDay-1][i-1];
					}
					PredictionItem tItem=new PredictionItem();
					tItem.setLabel(temp[0]);
					tItem.setEstimate(tEstimate);   
					tItem.setReal(Double.parseDouble(temp[i+1]));
					tItem.computeError();
					result.addResult(tDay-nPast, tItem);

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
        String outFile="test"+File.separator+"result_vitae2009.txt";
        int nPast=ParameterSetting.nPast;
        double alpha=ParameterSetting.alpha;
        int nResult=ParameterSetting.nPast;
        
        new Vitae2009PredictionSolver().predict(inFile, outFile, nPast, alpha, nResult);
	}

}
