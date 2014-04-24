package seqi;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * StationDateRangeFilter filters catalog events between 
 * the start and end dates of the deployment period 
 * of a set of stations, i.e. 
 * between the first start date and last end date of  
 * any station in the collection. 
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Mar 4, 2010
 */
public class StationDateFilter extends EventFilter {
	
	/** The set of all stations used in the filter. */
	private Set<StationInfo> allStations = new HashSet<StationInfo>();
	
	/** Set the list of stations used for the filter */
	public void setAllStations(Set<StationInfo> stations) {
		allStations.clear();
		allStations.addAll(stations);
	}
	
	/** Clear the list of stations used for the filter */
	public void clearAllStations() {
		allStations.clear();
	}
	
	/** Add the set of stations specified in the parameter 
	 * to the existing list of stations */
	public void addStationCollection(Set<StationInfo> stations) {
		allStations.addAll(stations);
	}
	
	/** Add a station to the existing list of stations */
	public void addStation(StationInfo station) {
		allStations.add(station);
	}
	
	/** Remove the station given by the parameter from 
	 * the existing list of stations */
	public void removeStation(StationInfo station) {
		allStations.remove(station);
	}
	

	/**
	 * Retain any events happening between the first start date 
	 * and the last end date.
	 * Note: The pre-condition of this method is that the list of station
	 * must not be empty.
	 * 
	 * @see EventFilter#filter(EventList)
	 */
	@Override
	public EventList filter(EventList inputList) {
		// Find the first start date and the last end date
		GregorianCalendar firstStartDate = null, lastEndDate = null;
		for (StationInfo station : allStations) {
			GregorianCalendar start = station.startDate;
			GregorianCalendar end = station.endDate;
			
			if (firstStartDate == null)
				firstStartDate = start;
			else if (start.compareTo(firstStartDate) < 0)
				firstStartDate = start;
			
			if (lastEndDate == null)
				lastEndDate = end;
			else if (end.compareTo(lastEndDate) > 0)
				lastEndDate = end;
		}
		
		Date firstStart =  firstStartDate.getTime();
		Date lastEnd =  lastEndDate.getTime();
		
		
		// Retrieve any events between the first start date and last end date.
		
		List<CatalogEvent> events = inputList.getEvents();
		List<CatalogEvent> output = new ArrayList<CatalogEvent>();
		
		for (CatalogEvent e : events) {			
			Date eventDate = e.startDate.getTime();					
			if ((eventDate.compareTo(firstStart) >=0) && (eventDate.compareTo(lastEnd) <=0))
				output.add(e);
		}
		
		return new EventList(output);
	}

	@Override
	protected void drawFilterInfo(Graphics2D g2d, MapView map) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Check if the filter has received valid input to perform filtering.
	 * 
	 * @return true if sufficient, false otherwise.
	 * 
	 */
	public boolean hasValidInput() {
		return !allStations.isEmpty();
	}

}
