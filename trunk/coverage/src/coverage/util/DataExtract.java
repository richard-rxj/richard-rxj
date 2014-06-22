/**
 * 
 */
package coverage.util;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @author u4964526
 *
 */
public class DataExtract {

	
	public static void generateData (String inFile, String outFile) throws IOException, ParseException
	{
		BufferedReader  in=new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
		PrintWriter out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile,false)),true);
		
		try
		{
			// pass the first line     
			String tempString=in.readLine();
			
			//initial date=1920
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
			Date gDate=sdf.parse("1/1/1920");
			
			//examine each line
			while((tempString=in.readLine())!=null)
			{
				String[] temp=tempString.split("\\s+");   //split!!!!!!!!!!!!!!!!!
				
				//check date
				Date tDate=sdf.parse(temp[0]);
				if(tDate.compareTo(gDate)==0) //still the same day
				{
					//check the hour between 8am-18pm
					String[] thour=temp[1].split(":");
					//if((Integer.parseInt(thour[0])>=9) && (Integer.parseInt(thour[0])<=17))
					{
						double dayValue=Double.parseDouble(temp[3])*3600*ExperimentSetting.solarPanel;						
						out.print(String.format("%.2f %.2f ", dayValue/2, dayValue/2));
					}
				}
				else   //enter a different day
				{
					out.println();
					out.print(temp[0]+" ");
					gDate=tDate;
					
					//check the hour between 8am-18pm
					String[] thour=temp[1].split(":");
					//if((Integer.parseInt(thour[0])>=8) && (Integer.parseInt(thour[0])<=18))
					{
						double dayValue=Double.parseDouble(temp[3])*3600*ExperimentSetting.solarPanel;						
						out.print(String.format("%.2f %.2f ", dayValue/2, dayValue/2));
					}
				}
			}
			
			
			
			System.out.println("complete!");
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			in.close();
			out.close();
		}
	}
	
	
	
	public static void predict(String inFile, String outFile, int nPast, double alpha,
			int nResult) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
		PrintWriter out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile,false)),true);
		
		try {
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
					out.print(temp[0]+" ");
					for(int i=0; i<average.length;i++)
					{					
						out.print(String.format("%.2f:%.2f ", average[i], Double.parseDouble(temp[i+1])));
						average[i]=alpha*average[i]+(1-alpha)*Double.parseDouble(temp[i+1]);
					}
					out.println();
				}
				else
					break;
			}
			
			
	
			System.out.println("complete");
		
		} finally {
			in.close();
			out.close();
		}
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		//generateData("data/solar_origin.txt","data/solar.txt");
		predict("data/solar.txt", "data/energyBudget.txt", 100, 0.7,
				500);

	}

}
