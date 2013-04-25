/**
 * 
 */
package pre.alg.anu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author u4964526
 *
 */
public class PredictionResult {

	private ArrayList<ArrayList<PredictionItem>> resultSet;
	private int totalSum;
	
	public PredictionResult(int nDays) {
		super();
		resultSet=new ArrayList<ArrayList<PredictionItem>>();
		totalSum=0;
		// TODO Auto-generated constructor stub
		for(int i=0;i<nDays;i++)
		{
			resultSet.add(new ArrayList<PredictionItem>());
		}
	}
	
	public void addResult(int day, PredictionItem resultItem)
	{
		resultSet.get(day-1).add(resultItem);
		totalSum++;
	}
	
	public void output(String outFile) throws IOException
	{
		PrintWriter out=null;
		
		if(outFile!=null)
		{
			out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile,false)),true);
		}
		else
		{
			out=new PrintWriter(new OutputStreamWriter(System.out));
		}
		
		double totalError=0;
		for(ArrayList<PredictionItem> r:resultSet)
		{
			for(PredictionItem p:r)
			{
				out.print(p+" ");
				totalError+=p.getError();
			}
			out.println();
		}
		out.println("average error="+totalError/this.totalSum);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
