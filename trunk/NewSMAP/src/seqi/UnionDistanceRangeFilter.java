package seqi;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * DistanceRangeFilterUnion collects the union of the filtering results 
 * produced by a set of DistanceRangeFilters.
 * 
 * Note that the minimum and maximum angular distances are set 
 * to be common to all the stations.
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 2, 2010
 */
public class UnionDistanceRangeFilter extends AbstractDistanceRangeFilter {
	
	/** 
	 * A list of stations selected as reference points 
	 * for distance range filtering. 
	 */
	private List<StationInfo> stations = new ArrayList<StationInfo>();
	
	/**
	 * A list of AtomicDistanceRangeFilter, each created for a 
	 * Station added to the list above. 
	 */
	// private List<AtomicDistanceRangeFilter> distFilters = new ArrayList<AtomicDistanceRangeFilter>();
	
	
	public void addStation(StationInfo s) {
		if (!stations.contains(s))
			stations.add(s);
	}
	
	
	public void addStations(StationInfo[] ss) {
		if (ss == null)
			return;
		
		for (StationInfo s : ss) {
			if (!stations.contains(s))
				stations.add(s);
		}
	}
	
	public void removeStation(StationInfo s) {
		stations.remove(s);
	}
	
	public void removeAllStations(){
		stations.clear();
	}
	
	public int getNumStations() {
		if (stations!= null)
			return stations.size();
		return 0;
	}
	
	public List<StationInfo> getStations() {
		return Collections.unmodifiableList(stations);
	}

	@Override
	protected void drawFilterInfo(Graphics2D g2d, MapView map) {
		// If there is only one filter in the set, then draw 
		// its graphic information
		if (stations.size() == 1) {			
			AtomicDistanceRangeFilter f = new AtomicDistanceRangeFilter();			
			StationInfo s = stations.get(0);		
			
			f.setMinAngle(getMinAngle());
			f.setMaxAngle(getMaxAngle());
			f.setRefLat(s.lat);
			f.setRefLon(s.lon);			
			f.drawFilterInfo(g2d, map);
		}
		// Otherwise do nothing 
	}

	/**
	 * 
	 * @return The Union of the resulting events of 
	 * 		the component DistanceRangeFilters. 
	 */
	public EventList filter(EventList inputList) {
		EventList allEvents = new EventList();
		
		AtomicDistanceRangeFilter f = new AtomicDistanceRangeFilter();
		
		for (StationInfo s : stations) {
			f.setMinAngle(getMinAngle());
			f.setMaxAngle(getMaxAngle());
			f.setRefLat(s.lat);
			f.setRefLon(s.lon);
			
			EventList fEvents = f.filter(inputList);
			allEvents.addEvents(fEvents);	
		}		
		
		return allEvents;
	}

	@Override
	public boolean hasValidInput() {		
		AtomicDistanceRangeFilter f = new AtomicDistanceRangeFilter();
		
		for (StationInfo s : stations) {
			f.setMinAngle(getMinAngle());
			f.setMaxAngle(getMaxAngle());
			f.setRefLat(s.lat);
			f.setRefLon(s.lon);
			
			if (!f.hasValidInput())
				return false;
		}
		
		return true;
	}
}
