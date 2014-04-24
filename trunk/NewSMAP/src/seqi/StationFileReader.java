package seqi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**
 * StationFileReader is a class that loads station information
 * from station coordinate files in text format.
 *  
 * An input file consists of several records, each stored in a line,
 * including the following fields (from left to right): 
 * network_code	station_code latitude longitude depth 
 * start_date start_time end_date end_time network_full_name network_location  
 * 
 * For example, a line of this file should look like this
 * 7J CP01 -20.2625 120.2075 108 18/06/2006 08:23:53.000 25/05/2007 02:02:00.000 CAPRA North_West_Australia
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 11, 2009
 */
public class StationFileReader {
	public StationFileReader() {
		
	}
	
	/**
	 * Parse a catalogue file in the short form prepared for SDC. 
	 */
	public Set<StationInfo> parseFile(File file) {
		try {
			
			Scanner scanner = new Scanner(file);			
			Set<StationInfo> stations = new HashSet<StationInfo>();
			
			 // 7J CP01 -20.2625 120.2075 108 18/06/2006 08:23:53.000 25/05/2007 02:02:00.000 CAPRA North_West_Australia
			
			//int lineNumber = 0; // for debugging
			while (scanner.hasNext()) {
				//lineNumber++;
				//System.out.println("Line " + lineNumber);
				
				String networkCode = scanner.next(); 
				String stationCode = scanner.next();
				
				float latitude = scanner.nextFloat();
				float longitude = scanner.nextFloat();
				float depth = scanner.nextFloat();

				String sdate = scanner.next();
				String stime = scanner.next();
				String edate = scanner.next();
				String etime = scanner.next();
				
				String netFullName = scanner.next();
				String netLoc = scanner.nextLine();// skip to the end of line
				
				GregorianCalendar sd, ed;
				// start date
				String[] sDateElems = sdate.split("/");
				// start time
				String[] sTimeElems = stime.split(":");				
				// end date
				String[] eDateElems = edate.split("/");
				// end time
				String[] eTimeElems = etime.split(":");
				
				sd = new GregorianCalendar(Integer.parseInt(sDateElems[2]), 
						Integer.parseInt(sDateElems[1]) - 1, 
						Integer.parseInt(sDateElems[0]),						
						Integer.parseInt(sTimeElems[0]), 
						Integer.parseInt(sTimeElems[1]), 
						(int)Float.parseFloat(sTimeElems[2]));
			
				ed = new GregorianCalendar(Integer.parseInt(eDateElems[2]), 
						Integer.parseInt(eDateElems[1]) - 1, 
						Integer.parseInt(eDateElems[0]),						
						Integer.parseInt(eTimeElems[0]), 
						Integer.parseInt(eTimeElems[1]), 
						(int)Float.parseFloat(eTimeElems[2]));
				
				StationInfo station = new StationInfo(networkCode, stationCode, latitude, longitude, depth,
						sd, ed, netFullName, netLoc);
				
				stations.add(station);
			}
			
			return stations;			
		} catch (FileNotFoundException e) {
			System.err.println("File "+ file + " not found!");
			return null;
		}
	}
}
