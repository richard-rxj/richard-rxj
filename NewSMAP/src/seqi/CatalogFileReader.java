package seqi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

/**
 * CatalogFileReader is a class that loads events
 * from input catalog files in text format.
 *  
 * An input file consists of several records, each stored in a line,
 * including the following fields (from left to right): 
 * 	year month date hour min sec latitude longitude depth mb ms
 * 
 * For example, a line of this file should look like this
 * 	1980   2   5  10  33  11.22   -30.288  -177.764   44.6  4.8  0.0
 * 
 * @author Cong Phuoc Huynh
 * 
 * Date: Apr 8, 2009
 * 
 */
public class CatalogFileReader {
	
	/** Input file to be read */
	// private File file;
	
	public CatalogFileReader() {
				
	}
	
	/**
	 * Parse a catalogue file in the short form prepared for SDC. 
	 */
	public EventList parseFile(File file) {
		
		//int lineNumber = 0; // for debugging
		try {
			
			Scanner scanner = new Scanner(file);
			
			// 1960   1   1  23  12  38.43    56.039   163.381   35.0  0.0  0.0
			
			List<CatalogEvent> events = new ArrayList<CatalogEvent>();
			while (scanner.hasNext()) {
				//lineNumber++;
				// System.out.println("Line " + lineNumber);
				
				int year = scanner.nextInt(); 
				int month = scanner.nextInt() - 1; // month field in GregorianCalendar starts from 0.
				int date = scanner.nextInt();
				int hour = scanner.nextInt();
				int min = scanner.nextInt();
				int sec = (int)scanner.nextFloat();
				
				float latitude = scanner.nextFloat();
				float longitude = scanner.nextFloat();
				float depth = scanner.nextFloat();
				float mb = scanner.nextFloat();
				float ms = scanner.nextFloat();
				float mw = 0; // set to 0 for the current format
				
				// skip to the end of line
				scanner.nextLine();				
				
				GregorianCalendar startDate = new GregorianCalendar(year, month, date, hour, min, sec);
				
				CatalogEvent event = new CatalogEvent(startDate, longitude, latitude,
						depth, mb, ms, mw);
				
				events.add(event);
			}
			
			EventList catList = new EventList(events);
			
			return catList;			
		} catch (Exception e) {
			if (e instanceof FileNotFoundException)
				System.err.println("File "+ file + " not found!");
			//else 
			//	System.out.println("Exception ocurred at line " + lineNumber + " of file "+ file);
			
			e.printStackTrace();
			
			return null;
		}
	}
}



