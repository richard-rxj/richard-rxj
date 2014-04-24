package seqi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;

import edu.sc.seis.TauP.PhaseName;
import edu.sc.seis.TauP.TauModel;
import util.*;


/**
 * EventWriter write a list of events to an output file 
 * in a format compatible for inputting into SDC.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 4, 2009
 */
public class EventWriter {
	
	// OUTPUT FORMAT PRODUCED BY this writer (for SDC)
	// The output file produced by SEISMOMAP (info4sdc.txt) describes the events filtered by SEISMOMAP. 
	// Each of them is stored in a separate line. 
	// Each line contains the following fields (in the order shown). Note that there is no leading spaces in each line.
	// startYear startMonth startDate startHour startMin startSecond startMiliseconds 
	// endYear   endMonth     endDate   endHour   endMin   endSecond   endMiliseconds 
	// latitude longitude depth(in kms) mb ms
	
	/**
	 * Write the input event list into the given file.
	 * 
	 * @param events The filter output event list.
	 * @param file The output file.
	 * 
	 * The start and end points of the time interval of 
	 * extracted data are specified as the number of hours, mins, secs
	 * before and 
	 * after the earthquake event's origin time, respectively. 
	 * The parameters to specify these are as follows.
	 * 
	 * @param startBeforeHour The number of hours of the start time before the origin time. 
	 * @param startBeforeMin  The number of minutes of the start time before the origin time.
	 * @param startBeforeSec  The number of seconds of the start time before the origin time.
	 * 
	 * @param endAfterHour 	 The number of hours of the end time after the origin time.
	 * @param endAfterMin    The number of minutes of the end time after the origin time.
	 * @param endAfterSec	 The number of seconds of the end time after the origin time.
	 * 
	 */
	public static void writeSegmentTimeWrtOriginTime(EventList eventList, File file,
			int startBeforeHour, int startBeforeMin, int startBeforeSec,
			int endAfterHour, int endAfterMin, int endAfterSec) {
		
		List<CatalogEvent> events = eventList.getEvents();
		
		try {
			FileWriter fw = new FileWriter(file);
			
			for (CatalogEvent event : events) {
				
				// Start date
				GregorianCalendar start = new GregorianCalendar(
						event.startDate.get(GregorianCalendar.YEAR), 
						event.startDate.get(GregorianCalendar.MONTH), 
						event.startDate.get(GregorianCalendar.DAY_OF_MONTH),
						event.startDate.get(GregorianCalendar.HOUR_OF_DAY),
						event.startDate.get(GregorianCalendar.MINUTE),
						event.startDate.get(GregorianCalendar.SECOND));
				
				start.add(GregorianCalendar.HOUR_OF_DAY, -startBeforeHour);
				start.add(GregorianCalendar.MINUTE, -startBeforeMin);
				start.add(GregorianCalendar.SECOND, -startBeforeSec);
				
//				int startYear = start.get(GregorianCalendar.YEAR);
//				int startMonth = start.get(GregorianCalendar.MONTH) + 1;
//				int startDate = start.get(GregorianCalendar.DAY_OF_MONTH);
//				int startHour = start.get(GregorianCalendar.HOUR_OF_DAY);
//				int startMin = start.get(GregorianCalendar.MINUTE);
//				int startSec = start.get(GregorianCalendar.SECOND);
//				int startMillisec = start.get(GregorianCalendar.MILLISECOND);
				
				// End date
				GregorianCalendar end = new GregorianCalendar(
						event.startDate.get(GregorianCalendar.YEAR), 
						event.startDate.get(GregorianCalendar.MONTH), 
						event.startDate.get(GregorianCalendar.DAY_OF_MONTH),
						event.startDate.get(GregorianCalendar.HOUR_OF_DAY),
						event.startDate.get(GregorianCalendar.MINUTE),
						event.startDate.get(GregorianCalendar.SECOND));
				end.add(GregorianCalendar.HOUR_OF_DAY, endAfterHour);
				end.add(GregorianCalendar.MINUTE, endAfterMin);
				end.add(GregorianCalendar.SECOND, endAfterSec);
				
//				int endYear = end.get(GregorianCalendar.YEAR);
//				int endMonth = end.get(GregorianCalendar.MONTH) + 1;
//				int endDate = end.get(GregorianCalendar.DAY_OF_MONTH);
//				int endHour = end.get(GregorianCalendar.HOUR_OF_DAY);
//				int endMin = end.get(GregorianCalendar.MINUTE);
//				int endSec = end.get(GregorianCalendar.SECOND);
//				int endMillisec = end.get(GregorianCalendar.MILLISECOND);
				
				// Note: in SDC.ExtractionManager.readMultipleEventFile(String multipleEventFile)
				// the last column in its input event file is the source of the events.
				// However, here the output event list has ms magnitude as the last field.
				//String str = startYear + " " +startMonth + " " + startDate + " " 
				//	+ startHour + " " + startMin + " " + startSec + " " +  startMillisec + " " +  
				//	endYear + " " +endMonth + " " + endDate + " " 
				//	+ endHour + " " + endMin + " " + endSec + " " +  endMillisec + " "
				//	+ event.latitude + " " + event.longitude + " " 
				//	+ event.depth + " " + event.mb + " " + event.ms + "\n";
				
				String str = String.format(
						"%1$tY %1$tm %1$2te %1$2tk %1$2tM %1$2tS %1$2tL " +
						"%2$tY %2$tm %2$2te %2$2tk %2$2tM %2$2tS %2$2tL " +
						"%3$6.2f %4$7.2f %5$6.1f %6$5.1f %7$5.1f\n", 
						start, end, event.latitude, event.longitude, 
						event.depth, event.mb, event.ms);
				
				fw.write(str);
			} 
			
			fw.close();			
		} catch (IOException e) {
			if (e instanceof FileNotFoundException)
				System.err.println("File "+ file + " not found!");			
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Write the input event list into the given file.
	 * 
	 * @param eventList The filter output event list.
	 * @param file The output file.
	 * 
	 * @param lon The longitude of the destination location.
	 * @param lat The latitude  of the destination location.
	 *  
	 * @param phaseName The phase name of the wave. 
	 * @param modelName The name of the wave propagation model. 
	 * 
	 * The start and end of the time interval of extracted data 
	 * are specified as the number of hours, mins, secs
	 * before and after the ARRIVAL time of the earthquake 
	 * at a given station, respectively.
	 *  
	 * The parameters to specify these are as follows.
	 * 
	 * @param startBeforeHour The number of hours of the start time before the arrival time. 
	 * @param startBeforeMin  The number of minutes of the start time before the arrival time.
	 * @param startBeforeSec  The number of seconds of the start time before the arrival time.
	 * 
	 * @param endAfterHour 	  The number of hours of the end time after the arrival time.
	 * @param endAfterMin     The number of minutes of the end time after the arrival time.
	 * @param endAfterSec	  The number of seconds of the end time after the arrival time.
	 * 
	 */
	public static void writeSegmentTimeWrtArrivalTime(EventList eventList,
			File file, 
			float lon, float lat, String phaseName, String modelName,			
			int startBeforeHour, int startBeforeMin, int startBeforeSec,
			int endAfterHour, int endAfterMin, int endAfterSec) {
		
		
		try {
			FileWriter fw = new FileWriter(file);
			
			List<CatalogEvent> events = eventList.getEvents();
			
			
			
			
			for (CatalogEvent event : events) {
				
				// Compute how long it takes for the wave to propagate 
				// from the quake's origin to the destination station
				double travelTime = Double.MAX_VALUE;				
				
				// if the Earliest phase name is selected as input.
				String[] allPhaseNames = EventOutputPanel.PHASE_NAMES;		
				if (phaseName == allPhaseNames[0]) {				
					
					// Go through all the other phase names
					for (int i = 1; i < allPhaseNames.length; i++) {						
						String eachPhase = allPhaseNames[i];
						
						// System.out.println("Event longitude "  + event.longitude + " latitude " +  event.latitude + " depth "+ event.depth +  " Phase " + phaseName);						
						double travelTimeForCurPhase = TravelTime.travelTime(event, lon, lat, eachPhase, modelName);
						if (travelTime > travelTimeForCurPhase && travelTimeForCurPhase != -1)
							travelTime = travelTimeForCurPhase;
					}
				} else { // otherwise compute the travel time for a phase rather than the earliest one. 
					travelTime = TravelTime.travelTime(event, lon, lat, phaseName, modelName);
				}
				
				// If the phase can reach the destination
				if (travelTime != -1) {
					// Start date
					GregorianCalendar start = new GregorianCalendar(
							event.startDate.get(GregorianCalendar.YEAR), 
							event.startDate.get(GregorianCalendar.MONTH), 
							event.startDate.get(GregorianCalendar.DAY_OF_MONTH),
							event.startDate.get(GregorianCalendar.HOUR_OF_DAY),
							event.startDate.get(GregorianCalendar.MINUTE),
							event.startDate.get(GregorianCalendar.SECOND));
					
					start.add(GregorianCalendar.HOUR_OF_DAY, -startBeforeHour);
					start.add(GregorianCalendar.MINUTE, -startBeforeMin);
					start.add(GregorianCalendar.SECOND, -startBeforeSec + (int)(Math.floor(travelTime)));
					start.add(GregorianCalendar.MILLISECOND, (int)((travelTime - Math.floor(travelTime)) * 1000)) ;
					
					
//					int startYear = start.get(GregorianCa6lendar.YEAR);
//					int startMonth = start.get(GregorianCalendar.MONTH) + 1;
//					int startDate = start.get(GregorianCalendar.DAY_OF_MONTH);
//					int startHour = start.get(GregorianCalendar.HOUR_OF_DAY);
//					int startMin = start.get(GregorianCalendar.MINUTE);
//					int startSec = start.get(GregorianCalendar.SECOND);
//					int startMillisec = start.get(GregorianCalendar.MILLISECOND);
					
					// End date
					GregorianCalendar end = new GregorianCalendar(
							event.startDate.get(GregorianCalendar.YEAR), 
							event.startDate.get(GregorianCalendar.MONTH), 
							event.startDate.get(GregorianCalendar.DAY_OF_MONTH),
							event.startDate.get(GregorianCalendar.HOUR_OF_DAY),
							event.startDate.get(GregorianCalendar.MINUTE),
							event.startDate.get(GregorianCalendar.SECOND));
					end.add(GregorianCalendar.HOUR_OF_DAY, endAfterHour);
					end.add(GregorianCalendar.MINUTE, endAfterMin);
					end.add(GregorianCalendar.SECOND, endAfterSec + (int)(Math.floor(travelTime)));
					end.add(GregorianCalendar.MILLISECOND, endAfterSec + (int)((travelTime - Math.floor(travelTime)) * 1000)) ; 
					
//					int endYear = end.get(GregorianCalendar.YEAR);
//					int endMonth = end.get(GregorianCalendar.MONTH) + 1;
//					int endDate = end.get(GregorianCalendar.DAY_OF_MONTH);
//					int endHour = end.get(GregorianCalendar.HOUR_OF_DAY);
//					int endMin = end.get(GregorianCalendar.MINUTE);
//					int endSec = end.get(GregorianCalendar.SECOND);
//					int endMillisec = end.get(GregorianCalendar.MILLISECOND);
					
					// Note: in SDC.ExtractionManager.readMultipleEventFile(String multipleEventFile)
					// the last column in its input event file is the source of the events.
					// However, here the output event list has ms magnitude as the last field.
					//String str = startYear + " " +startMonth + " " + startDate + " " 
					//	+ startHour + " " + startMin + " " + startSec + " " +  startMillisec + " " +  
					//	endYear + " " +endMonth + " " + endDate + " " 
					//	+ endHour + " " + endMin + " " + endSec + " " +  endMillisec + " "
					//	+ event.latitude + " " + event.longitude + " " 
					//	+ event.depth + " " + event.mb + " " + event.ms + "\n";
					
					// Format String using String.format  
					String str = String.format(
						"%1$tY %1$tm %1$2te %1$2tk %1$2tM %1$2tS %1$2tL " +
						"%2$tY %2$tm %2$2te %2$2tk %2$2tM %2$2tS %2$2tL " +
						"%3$6.2f %4$7.2f %5$6.1f %6$5.1f %7$5.1f\n", 
						start, end, event.latitude, event.longitude, 
						event.depth, event.mb, event.ms);					
					fw.write(str);				
				}
			} 
			
			fw.close();			
		} catch (IOException e) {
			if (e instanceof FileNotFoundException)
				System.err.println("File "+ file + " not found!");			
			e.printStackTrace();
		}
	}
	
}
