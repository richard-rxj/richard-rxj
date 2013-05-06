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
public class EvaluateWeight {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String baseDir="test"+File.separator+"NSRDB";
		String inFile0=baseDir+File.separator+"solar.txt";
		String inFile1=baseDir+File.separator+"solar1.txt";
		String inFile2=baseDir+File.separator+"solar2.txt";
		String inFile3=baseDir+File.separator+"solar3.txt";
		String inFile4=baseDir+File.separator+"solar4.txt";
		String inFile5=baseDir+File.separator+"solar5.txt";
        String outFile=baseDir+File.separator+"error.txt";
        int nPast=ParameterSetting.nPast;
        double alpha=ParameterSetting.alpha;
        int nResult=ParameterSetting.nPast;
        
        //String[] inFileSet={inFile0,inFile1,inFile2,inFile3,inFile4,inFile5};
        String[] inFileSet={inFile0, inFile1, inFile2,inFile3, inFile4};
        double[] wSet={0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,0.95,0.99,1};

        PrintWriter out=null;
        if(outFile!=null)
    	{
    		out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile,false)),true);
    	}
    	else
    	{
    		out=new PrintWriter(new OutputStreamWriter(System.out));
    	}
        
        for(int wI=0;wI<wSet.length;wI++)
        {
        	alpha=wSet[wI];
        	out.print(alpha);
        	
        	for(int fileI=0;fileI<inFileSet.length;fileI++)
        	{
        		String inFile=inFileSet[fileI];
        		String outFileDcoss2009=baseDir+File.separator+"error"+File.separator+"result_dcoss2009_"+alpha+"_inFile"+fileI+".txt";
        		PredictionResult resultDcoss2009=new Dcoss2009PredictionSolver().predict(inFile, outFileDcoss2009, nPast, alpha, nResult);
        		out.print(" "+resultDcoss2009.getAverageError());
        	}
        	out.println();
        }
        
        
//        /*
//         * begin: output result
//         */
//        PrintWriter out=null;
//		
//		if(outFile!=null)
//		{
//			out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile,false)),true);
//		}
//		else
//		{
//			out=new PrintWriter(new OutputStreamWriter(System.out));
//		}
//		
//		int gHour=0;
//		for(int gI=0;gI<resultSet.get(0).getResultSet().size();gI++)
//		{
//
//			for(int i=0;i<8;i++)
//			{
//				out.print(gHour);
//				for(int ti=0;ti<wSet.length;ti++)
//				{
//					out.print(" "+0);
//				}
//				out.println();
//				gHour++;
//			}
//			
//			for(int i=0;i<resultSet.get(0).getResultSet().get(gI).size();i++)
//			{
//				out.print(gHour);
//				for(int ti=0;ti<wSet.length;ti++)
//				{
//					out.print(" "+resultSet.get(ti).getResultSet().get(gI).get(i).getError());
//				}
//				out.println();
//				gHour++;
//				
//			}
//			
//			
//			for(int i=0;i<5;i++)
//			{
//				out.print(gHour);
//				for(int ti=0;ti<wSet.length;ti++)
//				{
//					out.print(" "+0);
//				}
//				out.println();
//				gHour++;
//			}
//		}
//		/*
//		 * end: output result
//		 */
        
        out.close();
	}

}
