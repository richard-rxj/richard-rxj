/**
 * 
 */
package pre.alg.anu;

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
				String[] temp=tempString.split(",");   //split!!!!!!!!!!!!!!!!!
				
				//check date
				Date tDate=sdf.parse(temp[0]);
				if(tDate.compareTo(gDate)==0) //still the same day
				{
					//check the hour between 8am-18pm
					String[] thour=temp[1].split(":");
					if((Integer.parseInt(thour[0])>=9) && (Integer.parseInt(thour[0])<=17))
					{
						out.print(temp[3]+" ");
						//out.print(temp[2]+" ");     //for nrel!!!!!!!!!!!!!!!!!!!!
					}
				}
				else   //enter a different day
				{
					out.println();
					out.print(temp[0]+" ");
					gDate=tDate;
					
					//check the hour between 8am-18pm
					String[] thour=temp[1].split(":");
					if((Integer.parseInt(thour[0])>=8) && (Integer.parseInt(thour[0])<=18))
					{
						out.print(temp[3]+" ");
						//out.print(temp[2]+" ");     //for nrel!!!!!!!!!!!!!!!!!!!!
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
	/**
	 * @param args
	 * @throws IOException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		//generateData("test/NSRDB/origin/solar_origin.txt","test/NSRDB/solar.txt");
		generateData("test/NSRDB/origin/solar_origin1.csv","test/NSRDB/solar1.txt");
		generateData("test/NSRDB/origin/solar_origin2.csv","test/NSRDB/solar2.txt");
		generateData("test/NSRDB/origin/solar_origin3.csv","test/NSRDB/solar3.txt");
		generateData("test/NSRDB/origin/solar_origin4.csv","test/NSRDB/solar4.txt");
		generateData("test/NSRDB/origin/solar_origin5.csv","test/NSRDB/solar5.txt");
	}

}
