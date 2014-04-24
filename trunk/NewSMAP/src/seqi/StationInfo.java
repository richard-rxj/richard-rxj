package seqi;

import java.util.GregorianCalendar;


/**
 * StationInfo contains the information on each deployment site,
 * including the network name, the station name, the starting 
 * and ending dates and times, the longitude, latitude,
 * depth and network fullname and full name of location.  
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 11, 2009
 */
public class StationInfo implements Comparable<StationInfo> {
	
	public final String network; // network code
	public final String station; // station code
	public final float lat;	   // latitude in degrees
	public final float lon;	   // longitude in degrees
	public final float depth;	   // depth in km
	public final GregorianCalendar startDate, endDate; // start, end dates
	public final String networkFullName; // full name
	public final String networkLocation; // location of the network
	// whether the current station is currently selected by the user or not.
	public boolean isSelected;

	
	/**
	 * Constructor of StationInfo
	 * void
	 * @param n network code
	 * @param s station code
	 * @param lat latitude
	 * @param lon longitude
	 * @param depth depth
	 * @param sd starting date
	 * @param ed ending date
	 * @param netFullName full name of network
	 * @param location location of network
	 */
	public StationInfo(String n, String s, float lat, float lon, float depth,
			GregorianCalendar sd, GregorianCalendar ed, String netFullName, String location) {
		network = n;
		station = s;
		this.lat = lat;
		this.lon = lon;
		this.depth = depth;
		startDate = sd;
		endDate = ed;
		networkFullName = netFullName;
		networkLocation = location;
	}
	
	/**
	 * @return the station code.
	 */
	public String toString() {
		return station;
	}

	
	/**
	 * Set the selection status of this StationInfo.  
	 * 
	 * @param isSelected The selection status.
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int compareTo(StationInfo o) {
		return this.station.compareTo(o.station);
		
	} 	
	
	
}
