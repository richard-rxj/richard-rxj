/**
 * 
 */
package pre.alg.anu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author u4964526
 *
 */
public class TestTogether {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String baseDir="test"+File.separator+"NREL";
		String inFile=baseDir+File.separator+"solar_NREL.txt";
        String outFileVitae2009=baseDir+File.separator+"result_vitae2009.txt";
        String outFileDcoss2009=baseDir+File.separator+"result_dcoss2009.txt";
        String outFileBasic=baseDir+File.separator+"result_basic.txt";
        String outFile=baseDir+File.separator+"result.txt";
        int nPast=ParameterSetting.nPast;
        double alpha=ParameterSetting.alpha;
        int nResult=ParameterSetting.nPast;
        
        PredictionResult resultVitae2009=new Vitae2009PredictionSolver().predict(inFile, outFileVitae2009, nPast, alpha, nResult);
        PredictionResult resultDcoss2009=new Dcoss2009PredictionSolver().predict(inFile, outFileDcoss2009, nPast, alpha, nResult);
        PredictionResult resultBasic=new BasicPredictionSolver().predict(inFile, outFileBasic, nPast, alpha, nResult);
        
        
        /*
         * begin: output result
         */
        PrintWriter out=null;
		
		if(outFile!=null)
		{
			out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile,false)),true);
		}
		else
		{
			out=new PrintWriter(new OutputStreamWriter(System.out));
		}
		
		int gHour=0;
		for(int gI=0;gI<resultBasic.getResultSet().size();gI++)
		{

			for(int i=0;i<8;i++)
			{
				out.println(gHour+" "+0+" "+0+" "+0+" "+0+" "+0+" "+0);
				gHour++;
			}
			
			for(int i=0;i<resultBasic.getResultSet().get(gI).size();i++)
			{
				PredictionItem itemBasic=resultBasic.getResultSet().get(gI).get(i);
				PredictionItem itemDcoss2009=resultDcoss2009.getResultSet().get(gI).get(i);
				PredictionItem itemVitae2009=resultVitae2009.getResultSet().get(gI).get(i);
				out.println(gHour+" "+itemBasic.getReal()+" "+itemBasic.getEstimate()+" "+itemDcoss2009.getReal()+" "
						+itemDcoss2009.getEstimate()+" "+itemVitae2009.getReal()+" "+itemVitae2009.getEstimate());
				gHour++;
			}
			
			
			for(int i=0;i<5;i++)
			{
				out.println(gHour+" "+0+" "+0+" "+0+" "+0+" "+0+" "+0);
				gHour++;
			}
		}
		/*
		 * end: output result
		 */
	}


}
